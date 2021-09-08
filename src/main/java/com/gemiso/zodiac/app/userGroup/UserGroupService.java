package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupCreateMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupMapper;
import com.gemiso.zodiac.app.userGroup.mapper.UserGroupUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    private final UserGroupMapper userGroupMapper;
    private final UserGroupCreateMapper userGroupCreateMapper;
    private final UserGroupUpdateMapper userGroupUpdateMapper;


    public List<UserGroupDTO> findAll(String userGrpNm,String useYn){

        BooleanBuilder booleanBuilder = getSearch(userGrpNm, useYn);

        List<UserGroup> userGroupList = (List<UserGroup>) userGroupRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "ord"));

        List<UserGroupDTO> userGroupDTO = userGroupMapper.toDtoList(userGroupList);

        return userGroupDTO;
    }


    public UserGroupDTO create(UserGroupCreateDTO userGroupCreateDTO) {

        UserGroup userGroup = userGroupCreateMapper.toEntity(userGroupCreateDTO);

        userGroupRepository.save(userGroup);

        return userGroupMapper.toDto(userGroup);
    }

    public UserGroupDTO find(Long userGrpId){

        UserGroup userGroupEntity = userGroupRepository.findByUserGroupId(userGrpId);

        //System.out.println(userGroupEntity.getUserGroupUsers());

        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

        return userGroupDTO;
    }

    public void update(UserGroupUpdateDTO userGroupUpdateDTO, Long userGrpId){

        userGroupUpdateDTO.setUserGrpId(userGrpId);
        userGroupUpdateDTO.setUpdtrId("userGrpId");

        UserGroup userGroupEntity = userGroupUpdateMapper.toEntity(userGroupUpdateDTO);

        userGroupRepository.save(userGroupEntity);

    }

    public void delete(Long userGrpId){

        UserGroup userGroupEntity = userGrouupFindOrFail(userGrpId);

        UserGroupDTO userGroupDTO = userGroupMapper.toDto(userGroupEntity);

        userGroupDTO.setDelYn("Y");

        UserGroup userGroup = userGroupMapper.toEntity(userGroupDTO);

        userGroupRepository.save(userGroup);
    }

    public UserGroup userGrouupFindOrFail(Long userGrpId){

        return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId));

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
}
