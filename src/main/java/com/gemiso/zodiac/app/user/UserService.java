package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.UserCreateDTO;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.app.user.mapper.UserCreateMapper;
import com.gemiso.zodiac.app.user.mapper.UserGroupUserMapper;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.app.user.mapper.UserUpdateMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupRepository;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService {

    private final UserRepository userRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupRepository userGroupRepository;

    private final UserMapper userMapper;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserGroupUserMapper userGroupUserMapper;

    private final PasswordEncoder passwordEncoder;

  /*  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }*/


    public List<UserDTO> findAll(String userId, String userNm, String delYn) {

        BooleanBuilder booleanBuilder = getSearch(userId, userNm, delYn);

        List<User> userEntity = (List<User>) userRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "userNm"));

        List<UserDTO> userDtos = userMapper.toDtoList(userEntity);

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserDTO userDTO : userDtos) {
            String findUserId = userDTO.getUserId();
            List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(findUserId);

            if (!CollectionUtils.isEmpty(userGroupUserList)) {
                List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.dtoList(userGroupUserList);

                List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();

                for (UserGroupUserDTO getUserGroupUserList : userGroupUserDTOs) {
                    UserGroupUserDTO userGroupUserDto = new UserGroupUserDTO();
                    userGroupUserDto.setUserGroup(getUserGroupUserList.getUserGroup());
                    userGroupUserDTO.add(userGroupUserDto);
                }
                userDTO.setUserGroupUserDTO(userGroupUserDTO);
            }
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    public UserDTO find(String userId){

        User userEntity = userRepository.findByUserId(userId);

        UserDTO userDTO = userMapper.toDto(userEntity);

        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        //Set<UserGroupUser> userGroupUserSet = userEntity.getUserGroupUser();

        if (!CollectionUtils.isEmpty(userGroupUserList)){
            List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.dtoList(userGroupUserList);

            List<UserGroupUserDTO> userGroupUserDTO = new ArrayList<>();

            for (UserGroupUserDTO getUserGroupUserList : userGroupUserDTOs){
                UserGroupUserDTO userGroupUserDto = new UserGroupUserDTO();
                userGroupUserDto.setUserGroup(getUserGroupUserList.getUserGroup());
                userGroupUserDTO.add(userGroupUserDto);
            }

            userDTO.setUserGroupUserDTO(userGroupUserDTO);
        }

        return userDTO;
    }

    public void create(UserCreateDTO userCreateDTO) {

        String password = encodePassword(userCreateDTO.getPwd()); //password encoding type 어떻게 할지
        userCreateDTO.setPwd(password);

        User userEntity = userCreateMapper.toEntity(userCreateDTO);

        userRepository.save(userEntity);

        List<UserGroupUserDTO> userGroupUserDTO = userCreateDTO.getUserGroupUserDTO();
        List<UserGroupUser> userGroupUserList = userGroupUserMapper.toEntityList(userGroupUserDTO);

        for (UserGroupUser userGroupUsers : userGroupUserList) {

            Long userGrpId = userGroupUsers.getUserGroup().getUserGrpId();

            UserGroup userGroup = userGroupFindOrFail(userGrpId);

            userGroupUsers.setUser(userEntity);
            userGroupUsers.setUserGroup(userGroup);

            userGroupUserRepository.save(userGroupUsers);

        }


    }


    /*@Override
    public UserDTO getUser(String userId) {
        User userEntity = userRepository.getUserWithGroup(userId);

        //List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);
        List<UserGroupUser> userGroupUserList = userEntity.getUserGroupUsers();

        List<UserGroupUserDTO> userGroupUserDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userGroupUserList)) {
            for (UserGroupUser userGroupUser : userGroupUserList) {

                UserGroupUser Entity = userGroupUser;

                UserGroupUserDTO dto = userGroupUserMapper.toDto(Entity);

                userGroupUserDTOList.add(dto);
            }
        }
        UserDTO userDto = userMapper.toDto(userEntity);

        userDto.setUserGroupUserDTOS(userGroupUserDTOList);

        return userDto;
    }*/

    public UserDTO update(UserUpdateDTO userUpdateDTO, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. userId : " + userId));

        userUpdateDTO.setUserId(userId);

        User userEntity = userUpdateMapper.toEntity(userUpdateDTO);

        userEntity.setPwd(user.getPwd());

        userRepository.save(userEntity);

        UserDTO userDTO = userFindOrFail(userId);

        return userDTO;
    }


    public void delete(String userId) {

        UserDTO userDTO = userFindOrFail(userId);

        userDTO.setDelYn("Y");

        User userEntity = userMapper.toEntity(userDTO);

        userRepository.save(userEntity);
    }



    public UserDTO userFindOrFail(String userId) {
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. userId : " + userId));

        UserDTO userDto = userMapper.toDto(userEntity);

        return userDto;
    }


    private BooleanBuilder getSearch(String userId, String userNm, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUser quser = QUser.user;

        if(!StringUtils.isEmpty(delYn)){
            booleanBuilder.and(quser.delYn.eq(delYn));
        }
        if(!StringUtils.isEmpty(userNm)){
            booleanBuilder.and(quser.userNm.contains(userNm));
        }
        if(!StringUtils.isEmpty(userId)){
            booleanBuilder.and(quser.userId.eq(userId));
        }
        return booleanBuilder;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkUser(String userId){

        Optional<User> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()){
            return true;
        }else {
            return false;
        }
    }

    public UserGroup userGroupFindOrFail(Long userGrpId){

       return  userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));
    }



}
