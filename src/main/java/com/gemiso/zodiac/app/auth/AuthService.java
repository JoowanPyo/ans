package com.gemiso.zodiac.app.auth;

import com.gemiso.zodiac.app.auth.dto.AuthDTO;
import com.gemiso.zodiac.app.auth.dto.AuthRequestDTO;
import com.gemiso.zodiac.app.auth.dto.JwtDTO;
import com.gemiso.zodiac.app.auth.dto.UserTokenDTO;
import com.gemiso.zodiac.app.auth.mapper.AuthMapper;
import com.gemiso.zodiac.app.auth.mapper.UserTokenMapper;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserRepository;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.mapper.UserMapper;
import com.gemiso.zodiac.core.helper.EncodingHelper;
import com.gemiso.zodiac.core.helper.JWTBuilder;
import com.gemiso.zodiac.core.helper.JWTParser;
import com.gemiso.zodiac.exception.PasswordFailedException;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    @Value("${password.salt.key:saltKey}")
    private String saltKey;

    private final UserService userService;

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final AuthRepository authRepository;

    private final UserMapper userMapper;
    private final UserTokenMapper userTokenMapper;
    private final AuthMapper authMapper;

    private final PasswordEncoder passwordEncoder;
    private final JWTBuilder jwtBuilder;
    private final JWTParser jwtParser;

    public JwtDTO login(AuthRequestDTO authRequestDTO) throws Exception {

        JwtDTO jwtDTO = new JwtDTO(); //returnDTO

        String userId = authRequestDTO.getUserId();
        String password = authRequestDTO.getPassword();

        /*MisUser userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("MisUser not found. userId : " + userId));*/
        User userEntity = userService.userFindOrFail(userId);

        UserDTO userDTO = userMapper.toDto(userEntity); //tb_user_login 테이블에 정보를 저장하기위한 DTO생성

        //아리랑 pwd sha256해싱 [ pwd + salt ]
        EncodingHelper encodingHelper = new EncodingHelper(password, saltKey);
        String hexPwd = encodingHelper.getHex();
        //String encodePassword = encodePassword(hexPwd); //패스워드 비크립트

        //log.info(" 패스워드 확인 : "+ hexPwd.toString());

        if (!passwordEncoder.matches(hexPwd, userEntity.getPwd())) {
            throw new PasswordFailedException("Password failed.");
        }

        UserToken userTokenEntity = userTokenRepository.findUserToken(userId);

        if (ObjectUtils.isEmpty(userTokenEntity)) {

            String refreshToken = jwtBuilder.createRefreshToken(""); //리플레시 토큰 생성 [토큰 유효시간 24시간 유저정보 X]

            UserTokenDTO userTokenDTO = new UserTokenDTO();

            Date expirationDtm = jwtParser.refreshTokenParser(refreshToken); //생성한 리플레시 토큰 파싱하여 만료시간 저장.

            userTokenDTO.setRefreshToken(refreshToken); //tb_user_token에 정보 Set
            userTokenDTO.setUserId(userId);
            userTokenDTO.setExpirationDtm(expirationDtm);

            UserToken userToken = userTokenMapper.toEntity(userTokenDTO); //userToken 정보 Entity변환
            userTokenRepository.save(userToken);//tb_user_token 테이블에 사용자 리플레시토큰 정보 저장

            int returnExpirationDtm = jwtParser.verityJwt(refreshToken); //client로 만료시간 초단위(int)로 변환하여 보내준다

            jwtDTO.setExpirationIn(returnExpirationDtm); //client로 보내줄 리플레시 토큰 만료시간
            jwtDTO.setRefreshToken(refreshToken);   // client로 보내줄 리플레시 토큰

        } else {
            //리플레시 토큰이 있으면, 리플레시토큰 시간이 만료되었는지 검증.
            String refreshToken = jwtParser.refreshTokenVerification(userTokenEntity.getRefreshToken());
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                refreshToken = jwtBuilder.createRefreshToken(""); //만료된 토큰 재생성.

                Date expirationDtm = jwtParser.refreshTokenParser(refreshToken); //생성한 리플레시 토큰 파싱하여 만료시간 저장.

                UserTokenDTO userTokenDTO = userTokenMapper.toDto(userTokenEntity);
                userTokenDTO.setUserId(userId);
                userTokenDTO.setRefreshToken(refreshToken);
                userTokenDTO.setExpirationDtm(expirationDtm);

                userTokenMapper.updateFromDto(userTokenDTO, userTokenEntity);
                userTokenRepository.save(userTokenEntity);

            }
            int returnExpirationDtm = jwtParser.verityJwt(refreshToken); //client로 만료시간 초단위(int)로 변환하여 보내준다

            jwtDTO.setExpirationIn(returnExpirationDtm); //client로 보내줄 리플레시 토큰 만료시간
            jwtDTO.setRefreshToken(refreshToken);   // client로 보내줄 리플레시 토큰
        }

        String accessToken = jwtBuilder.createAccessToken(userId);

        Auth auth = authRepository.findByLogin(userId);

        if (ObjectUtils.isEmpty(auth)) {
            //사용자 로그인 정보 신규 저장
            AuthDTO authDTO = new AuthDTO(); //DTO생성
            authDTO.setToken(accessToken);
            authDTO.setUserNm(userDTO.getUserNm());
            authDTO.setDeptId(userDTO.getDeptId());
            authDTO.setLoginDtm(new Date());
            authDTO.setStCd(userDTO.getUserStCd());
            authDTO.setUserId(userDTO.getUserId());

            Auth authEntity = authMapper.toEntity(authDTO);
            authRepository.save(authEntity);

        } else {
            AuthDTO authDTO = authMapper.toDto(auth); //기존 로그인 정보가 있을경우 업데이트
            //사용자 로그인 정보 업데이트
            authDTO.setToken(accessToken);
            authDTO.setUserNm(userDTO.getUserNm());
            authDTO.setDeptId(userDTO.getDeptId());
            authDTO.setLoginDtm(new Date());
            authDTO.setStCd(userDTO.getUserStCd());
            authDTO.setUserId(userDTO.getUserId());

            Auth authEntity = authMapper.toEntity(authDTO);
            authRepository.save(authEntity);
        }

        jwtDTO.setAccessToken(accessToken);

        return jwtDTO;

    }

    //패스워드 비크립트
    public String encodePassword(String password) {

        return passwordEncoder.encode(password);
    }

    public JwtDTO reissuance(String authorization) throws Exception {

        String userId = jwtParser.acTokenParser(authorization);

        UserToken userToken = userTokenRepository.findUserToken(userId);

        if (ObjectUtils.isEmpty(userToken)){ //토큰정보가 없을시 인증실패
            throw new ResourceNotFoundException("인증할 수 있는 토큰 정보가 없습니다 토큰 정보를 확인해 주세요.");
            //throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String refreshToken = jwtParser.refreshTokenVerification(userToken.getRefreshToken()); //리플레시 토큰이 만료되었는지 검증
        if (StringUtils.isEmpty(refreshToken)) { //만료되었으면 재생성
            //throw new TokenFailedException("리플레시 토큰 기간이 만료되었습니다 다시 로그인 해주세요" );
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        int returnExpirationDtm = jwtParser.verityJwt(refreshToken); //client로 만료시간 초단위(int)로 변환하여 보내준다

        String accessToken = jwtBuilder.createAccessToken(userId); //엑세스토큰 재발급

        Auth auth = authRepository.findByLogin(userId);

        AuthDTO authDTO = authMapper.toDto(auth); //기존 로그인 정보가 있을경우 업데이트
        authDTO.setToken(accessToken);
        //authDTO.setLoginDtm(new Date());
        Auth authEntity = authMapper.toEntity(authDTO);
        authRepository.save(authEntity);

        JwtDTO jwtDTO = new JwtDTO(); //returnDTO
        jwtDTO.setAccessToken(accessToken);
        jwtDTO.setRefreshToken(refreshToken);
        jwtDTO.setExpirationIn(returnExpirationDtm);

        return jwtDTO;

    }

    public void logout(String authorization) throws Exception {

       /* String userId = jwtParser.acTokenParser(authorization);*/
        String userId = jwtParser.acTokenParser(authorization);

        Auth auth = authRepository.findByLogin(userId);//로그인정보를 불러온다.
        String token = auth.getToken();//저장된 사용자 토큰 get
        if (StringUtils.isEmpty(token)){ //저장된 사용자 토큰이 없는경우 이미 로그아웃되어 있기때문에 에러없이 처리. return;
            return;
        }

        //에러없이 처리 [로그아웃 되어있는대 로그아웃 할 경우]

        UserToken userToken = userTokenRepository.findUserToken(userId);

        String refreshToken = jwtParser.refreshTokenVerification(userToken.getRefreshToken()); //리플레시 토큰이 만료되었는지 검증
        if (StringUtils.isEmpty(refreshToken)) { //만료되었으면 재생성
            userTokenRepository.deleteById(userToken.getId());
        }

        //사용자 로그인 정보를 조회 -> 로그아웃 기록추가(logoutDtm 로그아웃 시간 등록)

        AuthDTO authDTO = authMapper.toDto(auth);
        authDTO.setLogoutDtm(new Date());
        authDTO.setToken(null);
        Auth authEntity = authMapper.toEntity(authDTO);
        authRepository.save(authEntity);

    }

    public AuthDTO find(String authorization) throws Exception {

        String userId = jwtParser.acTokenParser(authorization);

        Auth auth = authRepository.findByLogin(userId);

        AuthDTO authDTO = authMapper.toDto(auth);

        return authDTO;

    }

}
