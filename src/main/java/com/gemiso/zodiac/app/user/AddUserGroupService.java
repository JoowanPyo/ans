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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AddUserGroupService {

    private final UserRepository userRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupRepository userGroupRepository;

    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final UserGroupUserMapper userGroupUserMapper;


    public List<UserDTO> find(Long userGrpId){

        List<User> userEntity = userRepository.findByUser(userGrpId);

        List<UserDTO> userDTOList = userMapper.toDtoList(userEntity);

        return userDTOList;
    }

    public UserDTO findUser(String userId) {

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. userId : " + userId));

        UserDTO userDTO = userMapper.toDto(userEntity);

        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        //Set<UserGroupUser> userGroupUserSet = userEntity.getUserGroupUser();

        if (!CollectionUtils.isEmpty(userGroupUserList)) {
            List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.dtoList(userGroupUserList);

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

    public void create(String userId, Long userGrpId) {

        UserDTO userDTO = userFindOrFail(userId);

        UserGroup userGroupEntity = userGroupFindOrFail(userGrpId);
        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

        UserGroupUserDTO userGroupUserDTO = new UserGroupUserDTO();
        userGroupUserDTO.setUser(userDTO);
        userGroupUserDTO.setUserGroup(userGroupDTO);

        UserGroupUser userGroupUserEntity = userGroupUserMapper.toEntity(userGroupUserDTO);
        userGroupUserRepository.save(userGroupUserEntity);

    }

    public void update(List<UserToGroupUdateDTO> userToGroupUdateDTO, String userId) {

        UserDTO userDTO = userFindOrFail(userId);

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

    public void delete(List<UserGroupUserDeleteDTO> userGroupUserDeleteDTOS, Long userGrpId){

        for (UserGroupUserDeleteDTO userDeleteDTO : userGroupUserDeleteDTOS){

            String userId = userDeleteDTO.getUserId();

            userRepository.deleteById(userId);

        }

    }

    public UserDTO userFindOrFail(String userId) {
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. userId : " + userId));

        UserDTO userDto = userMapper.toDto(userEntity);

        return userDto;
    }

    public UserGroup userGroupFindOrFail(Long userGrpId) {

        return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));
    }
}
