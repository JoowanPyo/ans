package com.gemiso.zodiac.app.dept;

import com.gemiso.zodiac.app.article.QArticle;
import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.app.dept.mapper.DeptsMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DeptsService {

    private final DeptsRepository deptsRepository;

    private final DeptsMapper deptsMapper;


    public List<DeptsDTO> findAll(String name, String isEnabled, String parentCode){

        BooleanBuilder booleanBuilder = getSearch(name, isEnabled, parentCode);

        List<Depts> deptsList = (List<Depts>) deptsRepository.findAll(booleanBuilder);

        List<DeptsDTO> deptsDTOList = deptsMapper.toDtoList(deptsList);

        return deptsDTOList;
    }

    public DeptsDTO find(Long id){

        Depts depts = findDept(id);

        DeptsDTO deptsDTO = deptsMapper.toDto(depts);

        return deptsDTO;

    }

    public Depts findDept(Long id){

        Optional<Depts> dept = deptsRepository.findDept(id);

        if (dept.isPresent() == false){
            throw new ResourceNotFoundException("부서 정보를 찾을 수 없습니다. Id : "+id);
        }

        return dept.get();
    }

    public BooleanBuilder getSearch(String name, String isEnabled, String parentCode){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QDepts qDepts = QDepts.depts;

        if (name != null && name.trim().isEmpty() == false){
            booleanBuilder.and(qDepts.name.eq(name));
        }

        if (isEnabled != null && isEnabled.trim().isEmpty() == false){
            booleanBuilder.and(qDepts.isEnabled.eq(isEnabled));
        }

        if (parentCode != null && parentCode.trim().isEmpty() == false){
            booleanBuilder.and(qDepts.parentCode.eq(parentCode));
        }

        return booleanBuilder;
    }
}
