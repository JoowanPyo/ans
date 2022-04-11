package com.gemiso.zodiac.core.mis;

import com.gemiso.zodiac.app.dept.Depts;
import com.gemiso.zodiac.app.dept.DeptsRepository;
import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.app.dept.mapper.DeptsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MisService {


    private final MisDeptRepository misDeptRepository;
    private final MisUserRepository misUserRepository;

    private final DeptsRepository deptsRepository;

    private final DeptsMapper deptsMapper;

    public void findMisDept(){

        List<MisDept> misDepts = misDeptRepository.findAll();

        List<Depts> ansDepts = deptsRepository.findAll();


        for (MisDept misDept : misDepts){

            String misDeptCode = misDept.getDeptCode(); //mis부서코드
            String misDeptName = misDept.getDeptName(); //mis부서이름
            String misDeptUsexYsno = misDept.getUsexYsno(); //mis사용여부
            //String

            for (Depts dept : ansDepts){

                String ansDeptCode = dept.getCode(); //ANS부서코드
                String ansDeptName = dept.getName(); //ANS부서이름
                String ansUsexYsno = dept.getIsEnabled(); //ANS사용여부

                if (misDeptCode.equals(ansDeptCode)){

                    //사용여부 & 부서이름 변경시 정보수정
                    if (misDeptName.equals(ansDeptName) == false  || misDeptUsexYsno.equals(ansUsexYsno)){

                        //만약 사용여부가 false로 들어온경우 삭제처리??
                        // 부서 사용여부가 1인경우 ANS부서 사용여부 Y
                        if (misDeptUsexYsno == "1") {
                            DeptsDTO deptsDTO = deptsMapper.toDto(dept);
                            deptsDTO.setName(misDeptName); //변경된 부서이름 수정
                            deptsDTO.setIsEnabled("Y");

                            deptsMapper.updateFromDto(deptsDTO, dept);

                            deptsRepository.save(dept);

                            continue;

                        }else { // 부서 사용여부가 0인경우 ANS부서 사용여부 N
                            DeptsDTO deptsDTO = deptsMapper.toDto(dept);
                            deptsDTO.setName(misDeptName); //변경된 부서이름 수정
                            deptsDTO.setIsEnabled("N");

                            deptsMapper.updateFromDto(deptsDTO, dept);

                            deptsRepository.save(dept);

                            continue;
                        }

                    }
                }
            }

            createDept(misDept);

        }

    }

    public void createDept(MisDept misDept){

        String useYn = misDept.getUsexYsno();

        if (useYn == "1") {
            Depts depts = Depts.builder()
                    .name(misDept.getDeptName())
                    .code(misDept.getDeptCode())
                    .isEnabled("Y")
                    .build();
        }else {

        }
    }

}
