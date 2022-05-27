package com.gemiso.zodiac.core.mis;

import com.gemiso.zodiac.app.dept.Depts;
import com.gemiso.zodiac.app.dept.DeptsRepository;
import com.gemiso.zodiac.app.dept.dto.DeptsDTO;
import com.gemiso.zodiac.app.dept.mapper.DeptsMapper;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserRepository;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUserRepository;
import com.gemiso.zodiac.core.helper.EncodingHelper;
import com.gemiso.zodiac.exception.PasswordFailedException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MisService {

    @Value("${password.salt.key:saltKey}")
    private String saltKey;

    private final MisDeptRepository misDeptRepository;
    private final MisUserRepository misUserRepository;

    private final DeptsRepository deptsRepository;
    private final UserRepository userRepository;
    private final UserGroupUserRepository userGroupUserRepository;

    private final DeptsMapper deptsMapper;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    /*@Value("${password.salt.key:saltKey}")
    private String saltKey;*/

    //Mis 부서정보 수정 및 등록
    public void findMisDept() {

        List<MisDept> misDepts = misDeptRepository.findAll(); //mis에서 부서정보를 가져온다.

        List<Depts> ansDepts = deptsRepository.findAll(); //ans에서 부서정보를 가져온다.


        loof1 :
        //수정되었거나 추가된 부서정보 수정 및 등록
        for (MisDept misDept : misDepts) { //Mis부서에서 Ans부서정보를 비교한다.

            String misDeptCode = misDept.getId().getDeptCode(); //mis부서코드
            String misDeptName = misDept.getId().getDeptName(); //mis부서이름
            String misDeptUsexYsno = misDept.getUsexYsno(); //mis사용여부
            String misUpprDtcd = misDept.getUpprDtcd(); //부모코드
            String misDeptLevl = misDept.getDeptLevl();//레벨

            //사용여부 ANS형식으로 변환
            if ("1".equals(misDeptUsexYsno)){
                misDeptUsexYsno = "Y";
            }else {
                misDeptUsexYsno = "N";
            }

            loof2:
            for (Depts dept : ansDepts) { //ans에서 조회한 부서정보

                String ansDeptCode = dept.getCode(); //ANS부서코드
                String ansDeptName = dept.getName(); //ANS부서이름
                String ansUsexYsno = dept.getIsEnabled(); //ANS사용여부
                String ansUpprDtcd = dept.getParentCode(); //ANS부모코드

                //같은코드 일시, 같은코드명 일시
                if (misDeptCode.equals(ansDeptCode)) {
                    //최상의 부모일 경우.
                    if ("1".equals(misDeptLevl)) {
                        //사용여부 & 부서이름 변경시 정보수정 & 코드 변경시 & 코드명 변경시
                        if (misDeptName.equals(ansDeptName) == false || misDeptUsexYsno.equals(ansUsexYsno) == false
                                || misDeptCode.equals(ansDeptCode) == false) {

                            // 부서 사용여부가 1인경우 ANS부서 사용여부 Y
                            if ("Y".equals(misDeptUsexYsno)) {
                                //부모부서일때 부서정보 변경시 업데이트
                                updateUse(dept, misDeptCode, misDeptName, null, null);
                                continue loof1;
                            } else { // 부서 사용여부가 0인경우 ANS부서 사용여부 N
                                updateDelete(dept);//부서정보 삭제 업데이트
                                continue loof1;
                            }

                        }
                    } else { //하위 코드일 경우

                        //사용여부 & 부서이름 변경시 정보수정 & 부모코드 변경시 & 코드 변경시 & 코드명 변경시
                        if (misDeptName.equals(ansDeptName) == false || misDeptUsexYsno.equals(ansUsexYsno) == false
                                || misUpprDtcd.equals(ansUpprDtcd) == false || misDeptCode.equals(ansDeptCode) == false) {

                            // 부서 사용여부가 1인경우 ANS부서 사용여부 Y
                            if ("Y".equals(misDeptUsexYsno)) {
                                //부모코드로 부모아이디 find
                                Long parentId = findParentId(misUpprDtcd);
                                //부모부서일때 부서정보 변경시 업데이트
                                updateUse(dept, misDeptCode, misDeptName, misUpprDtcd, parentId);
                                continue loof1;
                            } else { // 부서 사용여부가 0인경우 ANS부서 사용여부 N
                                //부서정보 삭제 업데이트
                                updateDelete(dept);
                                continue loof1;
                            }
                        }
                    }
                    continue loof1;
                }
            }
            createDept(misDept); //부서정보 신규생성
        }

    }
    
    //Mis 사용자 정보 수정 및 등록
    public void findUser() throws NoSuchAlgorithmException {

        //mis에서 ans사용가능한 사용자만 찾기위해 조건 빌드 ANSX_YSNO ="1"
        BooleanBuilder misBooleanBuilder = getBuilder();
        List<MisUser> misUserList = (List<MisUser>) misUserRepository.findAll(misBooleanBuilder);

        List<User> userList = userRepository.findAll();

        //mis조회정보에서 ans조회정보 검사
        loof1:
        for (MisUser misUser : misUserList){

            MisUserId misUserIdDTO = misUser.getId();
            String misUserId = misUserIdDTO.getUserIdxx(); //Mis사용자 아이디
            String misPassword = misUser.getScrtNumb(); //Mis사용자 비밀번호
            String misDeptCode = Optional.ofNullable(misUser.getDeptCode()).orElse(""); //Mis사용자 부서코트
            
            loof2:
            for (User user : userList){
                
                String ansUserId = user.getUserId(); //Ans사용자 아이디
                String ansPassword = user.getPwd(); // Ans사용자 비밀번호
                String ansDeptCode = Optional.ofNullable(user.getDeptCd()).orElse(""); //Ans사용자 부서코드
                
                if (misUserId.equals(ansUserId)){ //사용자 아디디가 같으면

                    // 패스워드 변경시
                    if (passwordEncoder.matches(misPassword, ansPassword) == false) {
                        updateUserPwd(user, misPassword);

                        log.info("Mis User Password Update : userId - "+ ansUserId);
                    }
                    // 부서코드 변경시
                    if (misDeptCode.equals(ansDeptCode) == false){
                        updateUserDept(user, misDeptCode);

                        log.info("Mis User Dept Code Update : userId - "+ ansUserId+" Dept Code - "+misDeptCode);
                    }

                  continue loof1;  
                }
            }

            createUser(misUser); //사용자 신규 등록
        }
        
    }

    //사용자 등록
    public void createUser(MisUser misUser){

        log.info("Mis User Create : Mis User Model - "+misUser.toString());

        //조회된 misUser에 등록된 부서코드를 가져와 부서 아이디 조회
        String misDeptCode = misUser.getDeptCode();
        Depts dept = findDept(misDeptCode);
        Long deptId = null;
        if (ObjectUtils.isEmpty(dept) ==false){
            deptId = dept.getId();
        }

        String misPassword = misUser.getScrtNumb(); //조회된 misUser에서 패스워드 get
        String password = encodePassword(misPassword); //패스워드 비크립트

        MisUserId misUserId = misUser.getId(); //Mis유저 아디디DTO get

        User user = User.builder()
                .userId(misUserId.getUserIdxx()) //사용자 아이디
                .userNm(misUser.getEmplName()) //사용자 이름
                .pwd(password) //비밀번호
                .emplNo(misUser.getEmplNumb()) //사원번호
                //.freeYn() //프리렌서 여부
                .deptId(deptId) //부서 아이디
                //.dutyCd() //직종코드
                //.chiefYn() //부서장 여부
                .email(misUser.getEltrMlad()) //이메일
                //.tel() //전화번호
                //.telPubYn() //전화번호 공개 여부
                .userDivCd(misUserId.getUsdnCode()) //사용자 구분 코드
                //.memo() //메모
                //.rmk() //비고
                //.userStCd() //사용자 상태 코드
                .useStartDtm(new Date()) //사용시작 일시
                //.useEndDtm() //사용 종료 일시
                //.useYn() //사용 여부
                //.inputrId() //등록자
                //.inphonNo() //사내번호
                .deptCd(misDeptCode) //부서코드
                //.userGroupUser(userGroup) //기본그룹설정
                .build();

        userRepository.save(user);

        //Mis에서 불러온 사용자 최초 저장시 일반그룹 초기화.
        UserGroup userGroup = UserGroup.builder().userGrpId(2L).build();

        UserGroupUser userGroupUser = UserGroupUser.builder().user(user).userGroup(userGroup).build();

        userGroupUserRepository.save(userGroupUser);


    }

    //사용자 부서정보 업데이트
    public void updateUserDept(User user, String misDeptCode){

        Depts dept = findDept(misDeptCode);
        Long deptId = null;
        if (ObjectUtils.isEmpty(dept) ==false){
            deptId = dept.getId();
        }

        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setDeptCd(misDeptCode);
        userDTO.setDeptId(deptId);

        userMapper.updateFromDto(userDTO, user);

        userRepository.save(user);

    }

    //사용자 비밀번호 업데이트
    public void updateUserPwd(User user, String misPassword){

        String password = encodePassword(misPassword); //패스워드 비크립트

        UserDTO userDTO = userMapper.toDto(user);
        user.setPwd(password);
        userDTO.setPwdChgDtm(new Date());

        userMapper.updateFromDto(userDTO, user);

        userRepository.save(user);

    }

    //패스워드 비크립트
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    //사용자 부서조회
    public Depts findDept(String misDeptCode){

        Optional<Depts> depts = deptsRepository.findByCode(misDeptCode);

        if (depts.isPresent() == false){
            return null;
        }

        return depts.get();
    }

    //mis에서 ans사용가능한 사용자만 찾기위해 조건 빌드 ANSX_YSNO ="1"
    public BooleanBuilder getBuilder(){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMisUser qMisUser = QMisUser.misUser;

        booleanBuilder.and(qMisUser.ansxYsno.eq("1"));

        return booleanBuilder;
    }

    //부서정보 삭제 업데이트
    public void updateDelete(Depts dept) {

        DeptsDTO deptsDTO = deptsMapper.toDto(dept);
        deptsDTO.setIsEnabled("N"); // "0"일경우 사용안함
        deptsDTO.setDeletedAt(new Date()); //삭제된 시점 셋팃

        deptsMapper.updateFromDto(deptsDTO, dept); //원본에서 수정된값 업데이트

        deptsRepository.save(dept); //수정

        log.info("Update Dept Info : Dept Id : "+dept.getId() +" IsEnabled : "+"N");

    }

    //부서정보 업데이트
    public void updateUse(Depts dept, String misDeptCode, String misDeptName, String parentCode, Long parentId) {

        DeptsDTO deptsDTO = deptsMapper.toDto(dept);
        deptsDTO.setCode(misDeptCode);//코드변경
        deptsDTO.setName(misDeptName); //변경된 부서이름 수정
        deptsDTO.setIsEnabled("Y"); // "1"일 경우 사용
        deptsDTO.setParentCode(parentCode);//혹시나 하위에서 부모코드로 변경됫으경우를 대비 빈값 셋팅
        deptsDTO.setId2(parentId); //혹시나 하위에서 부모코드로 변경됫으경우를 대비 빈값 셋팅

        deptsMapper.updateFromDto(deptsDTO, dept); //원본에서 수정된값 업데이트

        deptsRepository.save(dept); //수정

        log.info("Update Dept Info : Dept Id : "+dept.getId() +" CODE : "+misDeptCode+" DEPT NAME : "
                +misDeptName +" IsEnabled : "+"Y" +" ParentCode : "+parentCode+" Parent Id :"+parentId);
    }

    //부모코드로 부모아이디 find
    public Long findParentId(String misUpprDtcd) {

        Optional<Depts> depts = deptsRepository.findDeptByParentCode(misUpprDtcd);

        if (depts.isPresent() == false) {
            return 0L;
        }

        return depts.get().getId(); //아이디 리턴

    }

    //신규 부서 등록
    public void createDept(MisDept misDept) {

        String useYn = misDept.getUsexYsno();

        if ("1".equals(useYn)) { //사용중

            Long parentId = null;
            String misUpprDtcd = misDept.getUpprDtcd(); //부모코드
            if (misUpprDtcd != null && misUpprDtcd.trim().isEmpty() == false) { //부모코드가 있을경우
                parentId = findParentId(misUpprDtcd); //부모코드로 부모아이디 find
            }
            Depts depts = Depts.builder()
                    .name(misDept.getId().getDeptName())
                    .code(misDept.getId().getDeptCode())
                    .isEnabled("Y")
                    .parentCode(misUpprDtcd)
                    .id2(parentId)
                    .build();

            deptsRepository.save(depts);

        } else { //일단 사용하지 않은 데이터로 만들어놓은 경우.

            Long parentId = null;
            String misUpprDtcd = misDept.getUpprDtcd(); //부모코드
            if (misUpprDtcd != null && misUpprDtcd.trim().isEmpty() == false) { //부모코드가 있을경우
                parentId = findParentId(misUpprDtcd); //부모코드로 부모아이디 find
            }

            Depts depts = Depts.builder()
                    .name(misDept.getId().getDeptName())
                    .code(misDept.getId().getDeptCode())
                    .isEnabled("N")
                    .deletedAt(new Date())
                    .parentCode(misUpprDtcd)
                    .id2(parentId)
                    .build();

            deptsRepository.save(depts);
        }
    }

}
