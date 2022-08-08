package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.user.dto.UserDTO;

public class UserTokenService extends UserDTO {

    private UserDTO authUser;

    public UserTokenService(UserDTO user){
        this.authUser = user;
    }

    public String getuserId(){
        return authUser.getUserId();
    }
}
