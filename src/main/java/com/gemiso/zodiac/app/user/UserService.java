package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.*;
import com.gemiso.zodiac.app.user.mapper.UserCreateMapper;
import com.gemiso.zodiac.app.user.mapper.UserGroupUserMapper;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.app.user.mapper.UserUpdateMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupRepository;
import com.gemiso.zodiac.core.service.UserAuthService;
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
import java.util.Date;
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

    private final UserAuthService userAuthService;

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
                List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.toDtoList(userGroupUserList);

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

        User userEntity = userFindOrFail(userId);

        UserDTO userDTO = userMapper.toDto(userEntity);

        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        //Set<UserGroupUser> userGroupUserSet = userEntity.getUserGroupUser();

        if (!CollectionUtils.isEmpty(userGroupUserList)){
            List<UserGroupUserDTO> userGroupUserDTOs = userGroupUserMapper.toDtoList(userGroupUserList);

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

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String tokenUserId = userAuthService.authUser.getUserId();
        userCreateDTO.setInputrId(tokenUserId);

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

                UserGroupUserDTO CueSheet = userGroupUserMapper.toDto(Entity);

                userGroupUserDTOList.add(CueSheet);
            }
        }
        UserDTO userDto = userMapper.toDto(userEntity);

        userDto.setUserGroupUserDTOS(userGroupUserDTOList);

        return userDto;
    }*/

    public void update(UserUpdateDTO userUpdateDTO, String userId) {

        User user = userFindOrFail(userId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String tokenUserId = userAuthService.authUser.getUserId();
        userUpdateDTO.setUpdtrId(tokenUserId);
        userUpdateDTO.setUserId(userId);
       // User userEntity = userUpdateMapper.toEntity(userUpdateDTO);
       // userEntity.setPwd(user.getPwd());
        userUpdateMapper.updateFromDto(userUpdateDTO, user);
        userRepository.save(user);

    }


    public void delete(String userId) {

        User user = userFindOrFail(userId);

        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setDelYn("Y");
        userDTO.setDelDtm(new Date());
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String tokenUserId = userAuthService.authUser.getUserId();
        userDTO.setDelrId(tokenUserId);

        userMapper.updateFromDto(userDTO, user);

        userRepository.save(user);
    }



    public User userFindOrFail(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);

        if (!userEntity.isPresent()){
            throw new ResourceNotFoundException("User not found. userId : " + userId);
        }

        return userEntity.get();
    }


    private BooleanBuilder getSearch(String userId, String userNm, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUser quser = QUser.user;

        if(!StringUtils.isEmpty(delYn)){
            booleanBuilder.and(quser.delYn.eq(delYn));
        }else {
            booleanBuilder.and(quser.delYn.eq("N"));
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

       /*return  userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId));*/
        Optional<UserGroup> userGroup = userGroupRepository.findByUserGroupId(userGrpId);

        if (!userGroup.isPresent()){
            throw new ResourceNotFoundException("UserGroupId not found. userGroupId : " + userGrpId);
        }

        return userGroup.get();
    }



}
