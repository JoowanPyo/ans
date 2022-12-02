package com.gemiso.zodiac.app.auth;

import java.util.List;

public interface AuthRepositoryCustorm {

    List<Auth> findAllUserType(String userId, String userLoginTyp);
}
