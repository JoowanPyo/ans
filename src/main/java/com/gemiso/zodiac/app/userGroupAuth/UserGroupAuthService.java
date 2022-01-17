package com.gemiso.zodiac.app.userGroupAuth;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.appAuth.AppAuthRepository;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.appAuth.mapper.AppAuthMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupRepository;
import com.gemiso.zodiac.app.userGroupAuth.dto.UserGroupAuthCreateDTO;
import com.gemiso.zodiac.app.userGroupAuth.dto.UserGroupAuthDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroupAuth.mapper.UserGroupAuthMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserGroupAuthService {

    private final UserGroupRepository userGroupRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;
    private final AppAuthRepository appAuthRepository;

    private final UserGroupMapper userGroupMapper;
    private final AppAuthMapper appAuthMapper;
    private final UserGroupAuthMapper userGroupAuthMapper;

    public UserGroupDTO find(Long userGrpId) {

        UserGroup userGroup = userGrouupFindOrFail(userGrpId);

        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroup);

        List<UserGroupAuth> userGroupAuthList = userGroupAuthRepository.findByUserGrpId(userGrpId);

        if (!CollectionUtils.isEmpty(userGroupAuthList)) {

            List<UserGroupAuthDTO> userGroupAuthDTOList = userGroupAuthMapper.toDtoList(userGroupAuthList);

            //맵핑테이블에 그룹권한 정보만 빼서 보여주기 위함
            List<UserGroupAuthDTO> returnUserGroupAuthDTOS = new ArrayList<>();

            for (UserGroupAuthDTO userGroupAuthDTO : userGroupAuthDTOList) {
                UserGroupAuthDTO userGroupAuthDto = new UserGroupAuthDTO();
                userGroupAuthDto.setAppAuth(userGroupAuthDTO.getAppAuth());
                returnUserGroupAuthDTOS.add(userGroupAuthDto);
            }
            userGroupDTO.setUserGroupAuthDTO(returnUserGroupAuthDTOS);
        }

        return userGroupDTO;
    }

    public void create(List<UserGroupAuthCreateDTO> userGroupAuthCreateDTO,
                       Long UserGrpId) {

        UserGroup userGroup = userGrouupFindOrFail(UserGrpId);
        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroup);

        List<UserGroupAuth> userGroupAuthList = userGroupAuthRepository.findByUserGrpId(UserGrpId);

        if (!CollectionUtils.isEmpty(userGroupAuthList)) {
            for (UserGroupAuth userGroupAuths : userGroupAuthList) {
                Long userGroupAuthId = userGroupAuths.getId();

                userGroupAuthRepository.deleteById(userGroupAuthId);
            }
        }

        if (!CollectionUtils.isEmpty(userGroupAuthCreateDTO)) {

            UserGroupAuthDTO userGroupAuthDTO = new UserGroupAuthDTO();

            for (UserGroupAuthCreateDTO userGroupAuthCreateDTOs : userGroupAuthCreateDTO) {
                Long userGroupAuthId = userGroupAuthCreateDTOs.getAppAuthId();

                AppAuth appAuthEntity = appAuthFindOrFail(userGroupAuthId);
                AppAuthDTO appAuthDTO = appAuthMapper.toDto(appAuthEntity);

                userGroupAuthDTO.setUserGroup(userGroupDTO);
                userGroupAuthDTO.setAppAuth(appAuthDTO);

                UserGroupAuth userGroupAuth = userGroupAuthMapper.toEntity(userGroupAuthDTO);
                userGroupAuthRepository.save(userGroupAuth);
            }
        }
    }

    public UserGroup userGrouupFindOrFail(Long userGrpId) {

       /* return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId));*/

        Optional<UserGroup> userGroup = userGroupRepository.findByUserGroupId(userGrpId);

        if (userGroup.isPresent()==false){
            throw new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId);
        }
        return userGroup.get();
    }

    public AppAuth appAuthFindOrFail(Long userGroupAuthId) {

       /* return appAuthRepository.findById(userGroupAuthId)
                .orElseThrow(() -> new ResourceNotFoundException("AppAuthId not found. userGroupAuthId : " + userGroupAuthId));*/

        Optional<AppAuth> appAuth = appAuthRepository.findByAppAuthId(userGroupAuthId);

        if (!appAuth.isPresent()){
            throw new ResourceNotFoundException("AppAuthId not found. AppAuthId : " + userGroupAuthId);
        }
        return appAuth.get();
    }
}
