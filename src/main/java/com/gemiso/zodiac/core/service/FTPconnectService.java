package com.gemiso.zodiac.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Service
@Slf4j
public class FTPconnectService {

    private FTPClient ftpClient;
    public static final int MAX_ATTEMPTS = 10;

    public FTPconnectService() {
        this.ftpClient = new FTPClient();
    }

    // FTP 연결 및 설정
    public void connectActive(String ip, int port, String id, String pw, String dir) throws Exception{
        try {
            boolean result = false;
            ftpClient.connect(ip, port);			//FTP 연결
            ftpClient.setControlEncoding("UTF-8");	//FTP 인코딩 설정
            int reply = ftpClient.getReplyCode();	//응답코드 받기

            if (!FTPReply.isPositiveCompletion(reply)) {	//응답 False인 경우 연결 해제
                ftpClient.disconnect();
                log.info("FTP서버 연결실패 - ip : "+ip);
                throw new Exception("FTP서버 연결실패");
            }

            /********시큐어 코딩 인증 가이드 **********/
            String ok = "FAIL";
            int count = 0;

            while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)){
                ok = verifyUser(id, pw);
                count++;
            }

            if(!ftpClient.login(id, pw)) {
                ftpClient.logout();
                log.info("FTP서버 로그인실패");
                throw new Exception("FTP서버 로그인실패 - ip : "+ip);
            }

            ftpClient.setSoTimeout(1000 * 10);		//Timeout 설정
            ftpClient.login(id, pw);				//FTP 로그인

            ftpClient.setBufferSize(65536);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);	//파일타입설정
            //파일 전송 타입 설정
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.enterLocalActiveMode();			//Active 모드 설정
            result = ftpClient.changeWorkingDirectory(dir);	//저장파일경로

            if(!result){	// result = False 는 저장파일경로가 존재하지 않음
                ftpClient.makeDirectory(dir);	//저장파일경로 생성
                ftpClient.changeWorkingDirectory(dir);
            }
        } catch (IOException e) {
            //if(e.getMessage().indexOf("refused") != -1) {
                throw new Exception("FTP서버 연결실패 - ip : "+ip +" mesagge - "+"IOException");
            //}
            //throw e;
        }
    }

    // FTP 연결 및 설정
    public void connectPassive(String ip, int port, String id, String pw, String dir) throws Exception{
        try {
            boolean result = false;
            ftpClient.connect(ip, port);			//FTP 연결
            ftpClient.setControlEncoding("UTF-8");	//FTP 인코딩 설정
            int reply = ftpClient.getReplyCode();	//응답코드 받기

            if (!FTPReply.isPositiveCompletion(reply)) {	//응답 False인 경우 연결 해제
                ftpClient.disconnect();
                log.info("FTP서버 연결실패 - ip : "+ip);
                throw new Exception("FTP서버 연결실패");
            }
            /********시큐어 코딩 인증 가이드 **********/
            String ok = "FAIL";
            int count = 0;

            while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)){
                ok = verifyUser(id, pw);
                count++;
            }

            if(!ftpClient.login(id, pw)) {
                ftpClient.logout();
                log.info("FTP서버 로그인실패");
                throw new Exception("FTP서버 로그인실패 - ip : "+ip);
            }

            ftpClient.setSoTimeout(1000 * 10);		//Timeout 설정
            ftpClient.login(id, pw);				//FTP 로그인

            ftpClient.setBufferSize(65536);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);	//파일타입설정
            //파일 전송 타입 설정
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.enterLocalPassiveMode();			//Passive 모드 설정
            result = ftpClient.changeWorkingDirectory(dir);	//저장파일경로

            if(!result){	// result = False 는 저장파일경로가 존재하지 않음
                ftpClient.makeDirectory(dir);	//저장파일경로 생성
                ftpClient.changeWorkingDirectory(dir);
            }
        } catch (IOException e) {
            //if(e.getMessage().indexOf("refused") != -1) {
                throw new Exception("FTP서버 연결실패 - ip : "+ip +" mesagge - "+"IOException");
            //}
            //throw e;
        }
    }

    // FTP 연결 및 설정
    public void connectActiveEucKr(String ip, int port, String id, String pw, String dir) throws Exception{
        try {
            boolean result = false;
            ftpClient.setControlEncoding("EUC-KR");	//FTP 인코딩 설정
            ftpClient.connect(ip, port);			//FTP 연결
            int reply = ftpClient.getReplyCode();	//응답코드 받기

            if (!FTPReply.isPositiveCompletion(reply)) {	//응답 False인 경우 연결 해제
                ftpClient.disconnect();
                log.info("FTP서버 연결실패 - ip : "+ip);
                throw new Exception("FTP서버 연결실패");
            }

            /********시큐어 코딩 인증 가이드 **********/
            String ok = "FAIL";
            int count = 0;

            while ("FAIL".equals(ok) && (count < MAX_ATTEMPTS)){
                ok = verifyUser(id, pw);
                count++;
            }

            if(!ftpClient.login(id, pw)) {
                ftpClient.logout();
                log.info("FTP서버 로그인실패");
                throw new Exception("FTP서버 로그인실패 - ip : "+ip);
            }

            ftpClient.setSoTimeout(1000 * 10);		//Timeout 설정
            ftpClient.login(id, pw);				//FTP 로그인

            ftpClient.setBufferSize(65536);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);	//파일타입설정
            //파일 전송 타입 설정
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.enterLocalActiveMode();			//Active 모드 설정
            result = ftpClient.changeWorkingDirectory(dir);	//저장파일경로

            if(!result){	// result = False 는 저장파일경로가 존재하지 않음
                ftpClient.makeDirectory(dir);	//저장파일경로 생성
                ftpClient.changeWorkingDirectory(dir);
            }
        } catch (IOException e) {
            //if(e.getMessage().indexOf("refused") != -1) {
            throw new Exception("FTP서버 연결실패 - ip : "+ip +" mesagge - "+"IOException");
            //}
            //throw e;
        }
    }

    // FTP 연결해제
    public void disconnect(){
        try {
            if(ftpClient.isConnected()){
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            int a = 0;
            int b = 0;
            a = b;
            b = a;
        }
    }

    // FTP 파일 업로드
    public void storeFile(String saveFileNm, InputStream inputStream) throws Exception {
        try {
            if(!ftpClient.storeFile(saveFileNm, inputStream)) {
                log.info("FTP서버 업로드실패 : fileNm - "+saveFileNm);
                throw new Exception("FTP서버 업로드실패");
            }
        } catch (IOException | RuntimeException e) {
            //if(e.getMessage().indexOf("not open") != -1) {
                log.info("FTP서버 연결실패 : fileNm - "+saveFileNm);
                throw new Exception("FTP서버 연결실패");
            //}
            //throw e;
        }
    }

    public String verifyUser(String id, String pw){

        if( id.length() > 0 && pw.length() > 0 )
            return "OK";

        return "FAIL";
    }
}

