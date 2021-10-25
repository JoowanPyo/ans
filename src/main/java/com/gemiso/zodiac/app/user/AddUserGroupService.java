package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDTO;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDeleteDTO;
import com.gemiso.zodiac.app.user.dto.UserToGroupUdateDTO;
import com.gemiso.zodiac.app.user.mapper.UserCreateMapper;
import com.gemiso.zodiac.app.user.mapper.UserGroupUserMapper;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.app.user.mapper.UserUpdateMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupRepository;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AddUserGroupService {

    private final UserService userService;

    private final UserRepository userRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupRepository userGroupRepository;

    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final UserGroupUserMapper userGroupUserMapper;


    public List<UserGroupUserDTO> findAll(Long userGrpId){

        BooleanBuilder booleanBuilder = getSearch(userGrpId);

        List<UserGroupUser> userGroupUsers = (List<UserGroupUser>) userGroupUserRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "id"));

        List<UserGroupUserDTO> userDTOList = userGroupUserMapper.toDtoList(userGroupUsers);

        return userDTOList;

    }

    public List<UserDTO> find(Long userGrpId) {

        List<User> userEntity = userRepository.findByUser(userGrpId);

        List<UserDTO> userDTOList = userMapper.toDtoList(userEntity);

        return userDTOList;
    }

    public UserDTO findUser(String userId) {

        User userEntity = userService.userFindOrFail(userId);
        /*User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. userId : " + userId));*/

        UserDTO userDTO = userMapper.toDto(userEntity);

        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        //Set<UserGroupUser> userGroupUserSet = userEntity.getUserGroupUser();

        if (!CollectionUtils.isEmpty(userGroupUserList)) {
            List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.toDtoList(userGroupUserList);

            List<UserGroupUserDTO> returnUserGroupUserDTO = new ArrayList<>();

            for (UserGroupUserDTO getUserGroupUserList : userGroupUserDTOs) {
                UserGroupUserDTO userGroupUserDto = new UserGroupUserDTO();
                userGroupUserDto.setUserGroup(getUserGroupUserList.getUserGroup());
                returnUserGroupUserDTO.add(userGroupUserDto);
            }

            userDTO.setUserGroupUserDTO(returnUserGroupUserDTO);
        }

        return userDTO;
    }

    public void create(List<String> userIdList, Long userGrpId) {

        for (String userId : userIdList) {
            User user = userFindOrFail(userId);
            UserDTO userDTO = userMapper.toDto(user);

            UserGroup userGroupEntity = userGroupFindOrFail(userGrpId);
            UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

            UserGroupUserDTO userGroupUserDTO = new UserGroupUserDTO();
            userGroupUserDTO.setUser(userDTO);
            userGroupUserDTO.setUserGroup(userGroupDTO);

            UserGroupUser userGroupUserEntity = userGroupUserMapper.toEntity(userGroupUserDTO);
            userGroupUserRepository.save(userGroupUserEntity);
        }
    }

    public void update(List<UserToGroupUdateDTO> userToGroupUdateDTO, String userId) {

        User user = userFindOrFail(userId);

        UserDTO userDTO = userMapper.toDto(user);

        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        if (!CollectionUtils.isEmpty(userGroupUserList)) {
            for (UserGroupUser userGroupUser : userGroupUserList) {

                Long UserGroupUserId = userGroupUser.getId();

                userGroupUserRepository.deleteById(UserGroupUserId);
            }
        }

        if (!CollectionUtils.isEmpty(userToGroupUdateDTO)) {
            UserGroupUserDTO userGroupUserDTO = new UserGroupUserDTO();

            for (UserToGroupUdateDTO userGrpIds : userToGroupUdateDTO) {
                Long userGrpId = userGrpIds.getUserGrpId();

                UserGroup userGroupEntity = userGroupFindOrFail(userGrpId);
                UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

                userGroupUserDTO.setUser(userDTO);
                userGroupUserDTO.setUserGroup(userGroupDTO);

                UserGroupUser userGroupUserEntity = userGroupUserMapper.toEntity(userGroupUserDTO);
                userGroupUserRepository.save(userGroupUserEntity);
            }
        }
    }

    public void delete(List<UserGroupUserDeleteDTO> userGroupUserDeleteDTOS, Long userGrpId) {

        for (UserGroupUserDeleteDTO userDeleteDTO : userGroupUserDeleteDTOS) {

            String userId = userDeleteDTO.getUserId();

            Optional<UserGroupUser> userGroupUser = userGroupUserRepository.findAllByUserId(userId, userGrpId);

            if (!userGroupUser.isPresent()) {
                throw new ResourceNotFoundException("그룹에 등록된 사용자를 찾을 수 없습니다. UserId : " + userId);
            }

            Long userGrpUserId = userGroupUser.get().getId();

            userGroupUserRepository.deleteById(userGrpUserId);

        }

    }

    public User userFindOrFail(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);

        if (!userEntity.isPresent()) {
            throw new ResourceNotFoundException("User not found. userId : " + userId);
        }

        return userEntity.get();
    }

    public UserGroup userGroupFindOrFail(Long userGrpId) {

        return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));
    }

    private BooleanBuilder getSearch(Long userGrpId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUserGroupUser qUserGroupUser = QUserGroupUser.userGroupUser;

        if(!StringUtils.isEmpty(userGrpId)){
            booleanBuilder.and(qUserGroupUser.userGroup.userGrpId.eq(userGrpId));
        }

        return booleanBuilder;
    }

}
