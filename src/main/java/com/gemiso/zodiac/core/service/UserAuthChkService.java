package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUserRepository;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthChkService {

    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;

    private final UserAuthService userAuthService;


    public Boolean authChks(String auth1, String auth2) {

        //사용자 토큰 정보에서 사용자 아이디를 get
        String userId = userAuthService.authUser.getUserId();

        log.info("User Auth Check : UserId : " + userId);

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        List<String> appAuthList = new ArrayList<>();//리턴할 권한 List
        Long[] appAuthArr = new Long[userGroupUserList.size()]; //inquery로 조회할 유저그룹아이디 LongArray

        for (int i = 0; i < userGroupUserList.size(); i++) { //등록된 사용자 그룹에 포함한 권한을 모두 불러온다.
            Long groupId = userGroupUserList.get(i).getUserGroup().getUserGrpId();
            appAuthArr[i] = groupId;
        }
        List<UserGroupAuth> findUserGroupAuthList = userGroupAuthRepository.findByUserGrpIdArr(appAuthArr);

        for (UserGroupAuth userGroupAuth : findUserGroupAuthList) {

            String appAuthCD = userGroupAuth.getAppAuth().getAppAuthCd();
            if (appAuthList.contains(appAuthCD) == false) {
                appAuthList.add(appAuthCD);
            }
        }

        if (appAuthList.contains(auth1) || appAuthList.contains(auth2)) {
            return false; //조회된 사용자 권한에 해당 api에 맞는 권한이 있을 경우 false를 return해 예외처리를 빠져나간다.
        }

        //권한 리스트 리턴
        return true;//조회된 사용자 권한에 해당 api에 맞는 권한이 없을경우 true리턴 exception 403 FORBIDDEN 발생.
    }

    //권한체크
    public Boolean authChk(String auth1) {

        //사용자 토큰 정보에서 사용자 아이디를 get
        String userId = userAuthService.authUser.getUserId();

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);


        List<String> appAuthList = new ArrayList<>();//리턴할 권한 List
        Long[] appAuthArr = new Long[userGroupUserList.size()]; //inquery로 조회할 유저그룹아이디 LongArray

        for (int i = 0; i < userGroupUserList.size(); i++) { //등록된 사용자 그룹에 포함한 권한을 모두 불러온다.
            Long groupId = userGroupUserList.get(i).getUserGroup().getUserGrpId();
            appAuthArr[i] = groupId;
        }


        List<UserGroupAuth> findUserGroupAuthList = userGroupAuthRepository.findByUserGrpIdArr(appAuthArr);

        for (UserGroupAuth userGroupAuth : findUserGroupAuthList) {

            String appAuthCD = userGroupAuth.getAppAuth().getAppAuthCd();
            if (appAuthList.contains(appAuthCD) == false) {
                appAuthList.add(appAuthCD);
            }
        }
        log.info("User Auth Check : UserId : " + userId + " User Auths : " + appAuthList.toString());

        if (appAuthList.contains(auth1)) {
            return false; //조회된 사용자 권한에 해당 api에 맞는 권한이 있을 경우 false를 return해 예외처리를 빠져나간다.
        }

        //권한 리스트 리턴
        return true;//조회된 사용자 권한에 해당 api에 맞는 권한이 없을경우 true리턴 exception 403 FORBIDDEN 발생.
    }

}
