package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.user.dto.*;
import com.gemiso.zodiac.app.user.mapper.UserCreateMapper;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.app.user.mapper.UserUpdateMapper;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupRepository;
import com.gemiso.zodiac.app.userGroupUser.QUserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUserRepository;
import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDTO;
import com.gemiso.zodiac.app.userGroupUser.dto.UserToGroupUdateDTO;
import com.gemiso.zodiac.app.userGroupUser.mapper.UserGroupUserMapper;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.EncodingHelper;
import com.gemiso.zodiac.exception.PasswordFailedException;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    private final DateChangeHelper dateChangeHelper;

    //private final UserAuthService userAuthService;

    @Value("${password.salt.key:saltKey}")
    private String saltKey;

  /*  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }*/


    public List<UserDTO> findAll(String userId, String userNm, String searchWord, String email, String delYn) {

        BooleanBuilder booleanBuilder = getSearch(userId, userNm, searchWord, email, delYn);

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

    public void create(UserCreateDTO userCreateDTO, String tokenUserId) throws NoSuchAlgorithmException {

        //들어온 패스워드 해싱[sha256]
        String getPwd = userCreateDTO.getPwd();
        EncodingHelper encodingHelper = new EncodingHelper(getPwd, saltKey);
        String hexPwd = encodingHelper.getHex();

        //password encoding
        String password = encodePassword(hexPwd);
        userCreateDTO.setPwd(password);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String tokenUserId = userAuthService.authUser.getUserId();
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
        MisUser userEntity = userRepository.getUserWithGroup(userId);

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

    public void update(UserUpdateDTO userUpdateDTO, String userId, String tokenUserId) throws NoSuchAlgorithmException {

        User user = userFindOrFail(userId);

        //패스워드 업데이트 시
        String getPwd = userUpdateDTO.getPwd();

        if (getPwd != null && getPwd.trim().isEmpty() == false) {
            //들어온 패스워드 해싱[sha256]
            EncodingHelper encodingHelper = new EncodingHelper(getPwd, saltKey);
            String hexPwd = encodingHelper.getHex();

            //password encoding
            String password = encodePassword(hexPwd);
            userUpdateDTO.setPwd(password);
        }

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String tokenUserId = userAuthService.authUser.getUserId();
        userUpdateDTO.setUpdtrId(tokenUserId);
        userUpdateDTO.setUserId(userId);
       // MisUser userEntity = userUpdateMapper.toEntity(userUpdateDTO);
       // userEntity.setPwd(user.getPwd());
        userUpdateMapper.updateFromDto(userUpdateDTO, user);
        userRepository.save(user);


        List<UserToGroupUdateDTO> userGroupUsers = userUpdateDTO.getUserGroupUser();
        //부서 업데이트시,
        updateDepts(userGroupUsers, userId, user);

    }

    public BooleanBuilder getSearchUserGroupUser(String userId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QUserGroupUser qUserGroupUser = QUserGroupUser.userGroupUser;

        if (userId != null && userId.trim().isEmpty() == false){
            booleanBuilder.and(qUserGroupUser.user.userId.eq(userId));
        }

        return booleanBuilder;
    }

    //부서정보 업데이트 [ 기존저장되있던 부터 삭재후 새로들어온 정보 재등록]
    public void updateDepts(List<UserToGroupUdateDTO> userGroupUsers, String userId, User user){

        if (CollectionUtils.isEmpty(userGroupUsers) == false){

            //기존에 등록되어 있던 유저그룹 삭제
            BooleanBuilder booleanBuilder = getSearchUserGroupUser(userId);
            List<UserGroupUser> groupUserList = (List<UserGroupUser>) userGroupUserRepository.findAll(booleanBuilder);
            if (CollectionUtils.isEmpty(groupUserList) == false){

                for (UserGroupUser userGroupUser : groupUserList){

                    Long id = userGroupUser.getId();

                    userGroupUserRepository.deleteById(id);
                }

            }

            //유저그룹 재등록
            for (UserToGroupUdateDTO userToGroupUdateDTO : userGroupUsers){

                Long userGrpId = userToGroupUdateDTO.getUserGrpId();
                UserGroup userGroup = userGroupFindOrFail(userGrpId);
                //UserGroup userGroup = UserGroup.builder().userGrpId(groupId).build();

                UserGroupUser userGroupUser = UserGroupUser.builder()
                        .user(user)
                        .userGroup(userGroup)
                        .build();

                userGroupUserRepository.save(userGroupUser);
            }

        }

    }

    public void delete(String userId, String tokenUserId) {

        User user = userFindOrFail(userId);

        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setDelYn("Y");
        userDTO.setDelDtm(new Date());
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String tokenUserId = userAuthService.authUser.getUserId();
        userDTO.setDelrId(tokenUserId);

        userMapper.updateFromDto(userDTO, user);

        userRepository.save(user);
    }

    public void passwordConfirm(UserConfirmDTO userConfirmDTO, String tokenUserId) throws NoSuchAlgorithmException {

        String comfirm = userConfirmDTO.getComfirm();

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String tokenUserId = userAuthService.authUser.getUserId();

        User user = userFindOrFail(tokenUserId);

        String password = user.getPwd();

        //아리랑 pwd sha256해싱 [ pwd + salt ]
        EncodingHelper encodingHelper = new EncodingHelper(comfirm, saltKey);
        String hexPwd = encodingHelper.getHex();
        //String encodePassword = encodePassword(hexPwd); //패스워드 비크립트

        //log.info(" 패스워드 확인 : "+ hexPwd.toString());

        if (!passwordEncoder.matches(hexPwd, password)) {
            throw new PasswordFailedException("Password failed.");
        }

    }

    public void deleteUserUpdate(UserDeleteUpdateDTO userDeleteUpdateDTO, String userId){

        String delYn = userDeleteUpdateDTO.getDelYn();

        User user = deleteUserFind(userId);

        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setDelYn(delYn);

        userMapper.updateFromDto(userDTO, user);

        userRepository.save(user);

    }

    public User deleteUserFind(String userId){

        Optional<User> userEntity = userRepository.findDeleteUser(userId);

        if (!userEntity.isPresent()){
            throw new ResourceNotFoundException("삭제된 사용자 아이디를 찾을 수 없습니다. 사용자 아이디 : " + userId);
        }

        return userEntity.get();
    }

    //삭제처리된 사용자인지 확인.
    public User deleteChkUserFind(String userId){

        User user = null;

        Optional<User> userEntity = userRepository.findChkDeleteUser(userId);

        if (userEntity.isPresent() == false){
            return user;
        }

        return userEntity.get();
    }

    public User userFindOrFail(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);

        if (userEntity.isPresent() == false){
            throw new ResourceNotFoundException("사용자 정보를 찾을 수 없습니다. 사용자 아이디 : " + userId);
        }

        return userEntity.get();
    }

    //패스워드 비크립트
    public String encodePassword(String password) {

        return passwordEncoder.encode(password);
    }


    private BooleanBuilder getSearch(String userId, String userNm, String searchWord, String email, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QUser quser = QUser.user;

        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(quser.userNm.contains(searchWord).or(quser.userId.contains(searchWord)));
        }
        if(delYn != null && delYn.trim().isEmpty() == false){
            booleanBuilder.and(quser.delYn.eq(delYn));
        }else {
            booleanBuilder.and(quser.delYn.eq("N"));
        }
        if (email != null && email.trim().isEmpty() == false){
            booleanBuilder.and(quser.email.contains(email));
        }
        if(userNm != null && userNm.trim().isEmpty() == false){
            booleanBuilder.and(quser.userNm.contains(userNm));
        }
        if(userId != null && userId.trim().isEmpty() == false){
            booleanBuilder.and(quser.userId.eq(userId));
        }
        return booleanBuilder;
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
            throw new ResourceNotFoundException("유저 그룹을 찾을 수 없습니다. 유저 그룹 아이디 : " + userGrpId);
        }

        return userGroup.get();
    }

    public void excelDownload(HttpServletResponse response, List<UserDTO> userDTOList) throws IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("ANS 사용자 정보");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 10000);
        //sheet.setColumnWidth(6, 4000);
        Row row = null;
        Cell cell = null;
        int rowNum = 0;


        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("사용자 아이디");
        cell = row.createCell(1);
        cell.setCellValue("사용자 명");
        cell = row.createCell(2);
        cell.setCellValue("사원 번호");
        cell = row.createCell(3);
        cell.setCellValue("최종 로그인 일시");
        cell = row.createCell(4);
        cell.setCellValue("부서");
        cell = row.createCell(5);
        cell.setCellValue("이메일");
        /*cell = row.createCell(6);
        cell.setCellValue("전화번호");*/
        /*cell = row.createCell(7);
        cell.setCellValue("등록 일시");
        cell = row.createCell(8);
        cell.setCellValue("사내 번호");*/


        // Body
        for (UserDTO userDTO : userDTOList) {

            Date lastLoginDtm = userDTO.getLastLoginDtm();
            String dtm = dateChangeHelper.dateToStringNormal(lastLoginDtm);
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(userDTO.getUserId());
            cell = row.createCell(1);
            cell.setCellValue(userDTO.getUserNm());
            cell = row.createCell(2);
            cell.setCellValue(userDTO.getEmplNo());
            cell = row.createCell(3);
            cell.setCellValue(dtm);
            cell = row.createCell(4);
            cell.setCellValue(userDTO.getDeptNm());
            cell = row.createCell(5);
            cell.setCellValue(userDTO.getEmail());
            /*cell = row.createCell(6);
            cell.setCellValue(userDTO.getTel());*/
           /* cell = row.createCell(7);
            cell.setCellValue(userDTO.getInputDtm());
            cell = row.createCell(8);
            cell.setCellValue(userDTO.getInphonNo());*/
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();


    }


}
