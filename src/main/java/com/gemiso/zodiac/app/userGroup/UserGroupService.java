package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.userGroup.dto.UserGroupAuthDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupAuthMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupCreateMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    private final UserGroupMapper userGroupMapper;
    private final UserGroupCreateMapper userGroupCreateMapper;
    private final UserGroupUpdateMapper userGroupUpdateMapper;
    private final UserGroupAuthMapper userGroupAuthMapper;

    private final UserAuthService userAuthService;


    public List<UserGroupDTO> findAll(String userGrpNm,String useYn){

        BooleanBuilder booleanBuilder = getSearch(userGrpNm, useYn);

        List<UserGroup> userGroupList = (List<UserGroup>) userGroupRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "ord"));

        List<UserGroupDTO> userGroupDTO = userGroupMapper.toDtoList(userGroupList);

        return userGroupDTO;
    }


    public UserGroupDTO create(UserGroupCreateDTO userGroupCreateDTO) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        userGroupCreateDTO.setInputrId(userId);

        UserGroup userGroup = userGroupCreateMapper.toEntity(userGroupCreateDTO);

        userGroupRepository.save(userGroup);

        return userGroupMapper.toDto(userGroup);
    }

    public UserGroupDTO find(Long userGrpId){

        UserGroup userGroupEntity = userGroupFindOrFail(userGrpId);

        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

        //조회된 권한 List get :: 맵핑테이블 안에 유저그룹이 있기 때문에 맵퍼변환시 무한루프[스텍오버플로우]
        List<UserGroupAuth> userGroupAuth = userGroupEntity.getUserGroupAuths();

        List<UserGroupAuthDTO> userGroupAuthDTOList = userGroupAuthMapper.toDtoList(userGroupAuth);

        userGroupDTO.setUserGroupAuthDTO(userGroupAuthDTOList);//권한 set

        return userGroupDTO;
    }

    public void update(UserGroupUpdateDTO userGroupUpdateDTO, Long userGrpId){

        UserGroup userGroup = userGrouupFindOrFail(userGrpId);

        userGroupUpdateDTO.setUserGrpId(userGrpId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        userGroupUpdateDTO.setUpdtrId(userId);

        userGroupUpdateMapper.updateFromDto(userGroupUpdateDTO, userGroup);

        userGroupRepository.save(userGroup);

    }

    public void delete(Long userGrpId){

        UserGroup userGroupEntity = userGrouupFindOrFail(userGrpId);

        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        userGroupDTO.setDelrId(userId);
        userGroupDTO.setDelDtm(new Date());
        userGroupDTO.setDelYn("Y");

        UserGroup userGroup = userGroupMapper.toEntity(userGroupDTO);

        userGroupRepository.save(userGroup);
    }

    public UserGroup userGrouupFindOrFail(Long userGrpId){

        /*return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId));*/

        Optional<UserGroup> userGroup = userGroupRepository.findByUserGroupId(userGrpId);

        if (!userGroup.isPresent()){
            throw new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId);
        }

        return userGroup.get();
    }

    private BooleanBuilder getSearch(String userGrpNm, String useYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUserGroup qUserGroup = QUserGroup.userGroup;

        if(!StringUtils.isEmpty(useYn)){
            booleanBuilder.and(qUserGroup.useYn.eq(useYn));
        }
        if(!StringUtils.isEmpty(userGrpNm)){
            booleanBuilder.and(qUserGroup.userGrpNm.contains(userGrpNm));
        }

        return booleanBuilder;
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
