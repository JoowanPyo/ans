package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.app.user.UserGroupUserRepository;
import com.gemiso.zodiac.app.userGroup.UserGroupAuth;
import com.gemiso.zodiac.app.userGroup.UserGroupAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthChkService {

    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;

    private final UserAuthService userAuthService;


    public List<String> authChk() {

        //사용자 토큰 정보에서 사용자 아이디를 get
        String userId = userAuthService.authUser.getUserId();

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        List<String> appAuthList = new ArrayList<>(); //리턴할 권한 List

        for (UserGroupUser userGroupUser : userGroupUserList) { //등록된 사용자 그룹에 포함한 권한을 모두 불러온다.

            Long groupId = userGroupUser.getUserGroup().getUserGrpId();
            // 나중에 코드 개선 필요 ->> In query 를 이용하여 한번오 호출하도록 개선
            List<UserGroupAuth> findUserGroupAuthList = userGroupAuthRepository.findByUserGrpId(groupId);

            for (UserGroupAuth userGroupAuth : findUserGroupAuthList) {

                String appAuthCD = userGroupAuth.getAppAuth().getAppAuthCd();
                if (appAuthList.contains(appAuthCD) == false) {
                    appAuthList.add(appAuthCD);
                }
            }
        }
        //권한 리스트 리턴
        return appAuthList;
    }
}
