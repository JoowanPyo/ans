package com.gemiso.zodiac.app.file;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;

    private final AttachFileMapper attachFileMapper;

    //private final UserAuthService userAuthService;


    //파일 업로드
    public StatusCodeFileDTO create(MultipartFile file, String fileDivCd, String userId){

        int code = 200;

        //AttachFileDTO attachFileDTO = new AttachFileDTO();

        String ext = "";
        String msg = "";
        Long fileId = null;
        String upDir = "";
        String rname = "";
        String realpath = "";
        String orgFileNm = "";

        try {
            PropertyUtil xu = new PropertyUtil();
            UploadFileBean ub = new UploadFileBean();

            ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);
            int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));


            SimpleDateFormat fromat1 = new SimpleDateFormat("yyyy");
            SimpleDateFormat fromat2 = new SimpleDateFormat("MMdd");

            Date time = new Date();

            String year = fromat1.format(time);
            String day = fromat2.format(time);

            //업로드 디렉토리 path
            upDir = ub.getUpdir() +"/"+ year + "/" + day;
            realpath = ub.getDest() + upDir;
            log.info("file size : " + uploadsize+ ", Dest : " + ub.getDest());
            //디렉토리 생성
            if (isMakeDir(realpath)) {
                log.info("Directory create: " + realpath);
            }else {
                throw new IOException();
            }

            rname = file.getOriginalFilename();

            log.info("original name: " + rname);

            //파일 아이디 생성(날짜+""+시퀀스)
            //file_id = attachFileDAO.getFileId();

            //DB 등록
            AttachFileDTO fb = new AttachFileDTO();

            //원본파일 아이디 저장
            orgFileNm = file.getOriginalFilename();

            //fb.setFileId(file_id);
            fb.setOrgFileNm(orgFileNm);
            //fb.setFileNm(rname);
            fb.setFileDivCd(fileDivCd);
            fb.setFileSize((int) file.getSize());
            fb.setFileLoc(upDir);
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            //String userId = userAuthService.authUser.getUserId();
            fb.setInputrId(userId);
            fb.setFileUpldDtm(new Date());
            fb.setInputDtm(new Date());
            //로그인 아이디로 바꿔야 함?
            //	fb.setFile_upldr_id("system");

            //DB에 insert
            AttachFile attachFileEntity = attachFileMapper.toEntity(fb);
            attachFileRepository.save(attachFileEntity);

            fileId = attachFileEntity.getFileId();

            //DB에 insert
            //attachFileDAO.insertStorageFile(fb);

            if (log.isDebugEnabled()) {
                log.debug("attach file insert ok: " + fileId);
            }

            //오리지널 파일네임 여부
            /*if (ub.getRname_yn().equals("N") == false) {
                //확장자 파싱
                ext = cutExtension(rname);

                rname = fileId + "." + ext;
            }*/

            File uploadFile = null;

            if (rname != null && rname.trim().isEmpty() == false){

                if( rname.endsWith(".doc") || rname.endsWith(".hwp") || rname.endsWith(".pdf") || rname.endsWith(".xls") ||
                        rname.endsWith(".png") || rname.endsWith(".svg") || rname.endsWith(".txt") || rname.endsWith(".zip") ||
                        rname.endsWith(".jpg") || rname.endsWith(".xml")){

                    //String fileName = file.getOriginalFilename();
                    //오리지널 파일네임 여부
                    if (ub.getRname_yn().equals("N") == false) {
                        //확장자 파싱
                        ext = cutExtension(rname);

                        rname = fileId + "." + ext;
                    }
                    uploadFile = new File(realpath + File.separator + rname);
                }
            }

            byte[] bytes = file.getBytes();

            BufferedOutputStream buffStream = null;
            FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);
            try {

                //파일을 버퍼링을 이용하여 저장할 경로
                // YYYYMMDD+FI+seq 요런식으로 들어감...
                buffStream = new BufferedOutputStream(fileOutputStream);

                //파일 복사
                buffStream.write(bytes);
            }
            catch (IOException e)
            {
                //System.out.println("BufferedReader 파일복사 중 에러 발생");
                log.error("BufferedReader 파일복사 중 에러 발생");
            }
            finally {
                try {
                    if (buffStream != null) {
                        buffStream.close();
                    }
                    if (fileOutputStream != null){
                        fileOutputStream.close();
                    }
                }catch (IOException e){
                    log.error("IOException : buffStream.close()");
                }

            }

            AttachFile attachFile = attachFileRepository.findById(fileId)
                  .orElseThrow(() -> new ResourceNotFoundException("File not found. FileId :"));

            AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile);

            attachFileDTO.setFileNm(rname);
            AttachFile aveAttachFile = attachFileMapper.toEntity(attachFileDTO);
            attachFileRepository.save(aveAttachFile);

            msg = "/store/"+upDir+"/"+rname;
            log.info("msg: "+msg);

            /*buffStream.close();*/
            msg += " - Uploaded (" + file.getOriginalFilename() + ")";
            code = 200;

            log.info("attach file start : " + fileId);

            //return attachFileDTO;

        } catch (IOException e) {
            code = 500;
            msg = "You failed to upload " + rname + ": "+ "<br/>";
            /*e.printStackTrace();*/
            log.error("IOException Occured "+msg);
        }
        //return new StatusCodeFileDTO(codeDTO, msg, file_id, org_file_nm);
        return new StatusCodeFileDTO(code, msg, fileId, orgFileNm);
    }

    //파일 업로드
    public StatusCodeFileDTO createFile(MultipartFile file, String fileDivCd, String fileNm, String userId){

        int code = 200;

        //AttachFileDTO attachFileDTO = new AttachFileDTO();

        String ext = "";
        String msg = "";
        Long fileId = null;
        String upDir = "";
        String rname = "";
        String realpath = "";
        String orgFileNm = "";

        try {
            PropertyUtil xu = new PropertyUtil();
            UploadFileBean ub = new UploadFileBean();

            ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);
            int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));


            SimpleDateFormat fromat1 = new SimpleDateFormat("yyyy");
            SimpleDateFormat fromat2 = new SimpleDateFormat("MMdd");

            Date time = new Date();

            String year = fromat1.format(time);
            String day = fromat2.format(time);

            //업로드 디렉토리 path
            upDir = ub.getUpdir() +"/"+ year + "/" + day;
            realpath = ub.getDest() + upDir;
            log.info("file size : " + uploadsize+ ", Dest : " + ub.getDest());
            //디렉토리 생성
            if (isMakeDir(realpath)) {
                log.info("Directory create: " + realpath);
            }else {
                throw new IOException();
            }

            rname = fileNm;

            log.info("original name: " + rname);

            //파일 아이디 생성(날짜+""+시퀀스)
            //file_id = attachFileDAO.getFileId();

            //DB 등록
            AttachFileDTO fb = new AttachFileDTO();

            //원본파일 아이디 저장
            orgFileNm = file.getOriginalFilename();

            //fb.setFileId(file_id);
            fb.setOrgFileNm(orgFileNm);
            //fb.setFileNm(rname);
            fb.setFileDivCd(fileDivCd);
            fb.setFileSize((int) file.getSize());
            fb.setFileLoc(upDir);
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            //String userId = userAuthService.authUser.getUserId();
            fb.setInputrId(userId);
            fb.setFileUpldDtm(new Date());
            fb.setInputDtm(new Date());
            //로그인 아이디로 바꿔야 함?
            //	fb.setFile_upldr_id("system");


            //DB에 insert
            AttachFile attachFileEntity = attachFileMapper.toEntity(fb);
            attachFileRepository.save(attachFileEntity);

            fileId = attachFileEntity.getFileId();

            //DB에 insert
            //attachFileDAO.insertStorageFile(fb);

            if (log.isDebugEnabled()) {
                log.debug("attach file insert ok: " + fileId);
            }

            //오리지널 파일네임 여부
          /*  if (ub.getRname_yn().equals("N") == false) {
                //확장자 파싱
                ext = cutExtension(rname);

                rname = fileId + "." + ext;
            }*/

            File uploadFile = null;

            if (rname != null && rname.trim().isEmpty() == false){

                if( rname.endsWith(".doc") || rname.endsWith(".hwp") || rname.endsWith(".pdf") || rname.endsWith(".xls") ||
                        rname.endsWith(".png") || rname.endsWith(".svg") || rname.endsWith(".txt") || rname.endsWith(".zip") ||
                        rname.endsWith(".jpg") || rname.endsWith(".xml")){

                    //String fileName = file.getOriginalFilename();
                    //오리지널 파일네임 여부
                    if (ub.getRname_yn().equals("N") == false) {
                        //확장자 파싱
                        ext = cutExtension(rname);

                        rname = fileId + "." + ext;
                    }
                    uploadFile = new File(realpath + File.separator + rname);
                }
            }

            byte[] bytes = file.getBytes();

            BufferedOutputStream buffStream = null;
            FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);

            try {

                //파일을 버퍼링을 이용하여 저장할 경로
                // YYYYMMDD+FI+seq 요런식으로 들어감...
                buffStream = new BufferedOutputStream(fileOutputStream);

                //파일 복사
                buffStream.write(bytes);
            }
            catch (IOException e)
            {
                //System.out.println("BufferedReader 파일복사 중 에러 발생");
                log.error("BufferedReader 파일복사 중 에러 발생");
            }
            finally {
                try {
                    if (buffStream != null) {
                        buffStream.close();
                    }
                    if (fileOutputStream != null){
                        fileOutputStream.close();
                    }
                }catch (IOException e){
                    log.error("IOException - close");
                }

            }

            AttachFile attachFile = attachFileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found. FileId :"));

            AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile);

            attachFileDTO.setFileNm(rname);
            AttachFile aveAttachFile = attachFileMapper.toEntity(attachFileDTO);
            attachFileRepository.save(aveAttachFile);


            msg = "/store/"+upDir+"/"+rname;
            log.info("msg: "+msg);

            /*buffStream.close();*/
            msg += " - Uploaded (" + file.getOriginalFilename() + ")";
            code = 200;

            log.info("attach file start : " + fileId);


            //return attachFileDTO;

        } catch (IOException e) {
            code = 500;
            msg = "You failed to upload " + rname + ": " + "<br/>";
            /*e.printStackTrace();*/
            log.error("IOException Occured "+msg);
        }
        //return new StatusCodeFileDTO(codeDTO, msg, file_id, org_file_nm);
        return new StatusCodeFileDTO(code, msg, fileId, orgFileNm);
    }

    public AttachFileDTO strFilefind(Long fileId){

        AttachFile attachFile = attachFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found. FileId : " + fileId));

        AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile);

        return attachFileDTO;

    }

    public ResponseEntity find(HttpServletRequest request, HttpServletResponse response, AttachFileDTO attachFileDTO
                                , String fileDivCd) throws Exception {
        UploadFileBean ub = new UploadFileBean();
        PropertyUtil xu = new PropertyUtil();

        String _rname="";
        String dispositionPrefix = "attachment; filename=";

        if(log.isDebugEnabled()){
            log.debug("file id : " +attachFileDTO.getFileId());
        }

        //xml파일에서 파일구분코드로 파일경로추가
        ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);
        //dest(파일경로)/file_loc(divcd:04일 경우)jebo/2021/0415/file_name.확장자
        _rname=ub.getDest()+File.separator+attachFileDTO.getFileLoc()+File.separator+attachFileDTO.getFileNm();

        if(log.isDebugEnabled()){

            log.debug("download file name: " +_rname);
        }

        File file = null;
        // file에 파일경로 및 파일네임.확장자
        file=new File(_rname);

        //파일 존재 여부 확인
        if (!file.exists()) {
            String errorMessage = "File does not exist";
            log.info(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        //데이터 파일로부터 파일명 맵 (mimetable)을 로드합니다.
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file.getName());

        if (mimeType == null) {
            log.info("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        //encoding Type
        response.setContentType(mimeType);

        //다운로드시 한글깨짐 방지처리, msie타입 지정
        String browser = getBrowser(request);

        //원본파일 아이디(원래값)
        String _org_file_nm = attachFileDTO.getOrgFileNm();
        String encodedFilename = "";

        //다운로드시 한글깨짐 방지처리
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(_org_file_nm, "UTF-8").replaceAll("\\+", "%20");
            log.info(encodedFilename + " 최종 받아질 파일 이름 MSIE에서 테스트  ."  );
        } else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
            encodedFilename = URLEncoder.encode(_org_file_nm, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(_org_file_nm.getBytes("UTF-8"), "8859_1") + "\"";
            encodedFilename = URLDecoder.decode(encodedFilename);
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(_org_file_nm.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
			/*StringBuffer sb = new StringBuffer();
			for (int i = 0; i < _org_file_nm.length(); i++) {
				char c = _org_file_nm.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			/*
			 * 정규식 변환.
			 * 한글, 영어, 숫자, 한자 외 문자 _(언더바)로 replace 했습니다.
			 */
//			encodedFilename = new String(_org_file_nm.getBytes("UTF-8"),"ISO-8859-1");
            encodedFilename = _org_file_nm;

            encodedFilename = removeExtension(encodedFilename);
            String _file_ext = cutExtension(_org_file_nm);

            String RegularExpression =  "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9一-龥\\s]*$";
            StringBuffer stringbuffer = new StringBuffer();
            for(int i = 0; i < encodedFilename.length() ; i++ ) {
                char c = encodedFilename.charAt(i);
                if(Character.toString(c).matches(RegularExpression)) {
                    stringbuffer = stringbuffer.append(c);
                } else {
                    stringbuffer = stringbuffer.append('_');
                }
            }

            //encodedFilename.replace(".", "_");

            encodedFilename = stringbuffer.toString()+"."+_file_ext;
        } else if (browser.equals("Safari")) {
            encodedFilename = "\"" + new String(_org_file_nm.getBytes("UTF-8"), "8859_1") + "\"";
            encodedFilename = URLDecoder.decode(encodedFilename);
        }
        else {
            encodedFilename = "\"" + new String(_org_file_nm.getBytes("UTF-8"), "8859_1") + "\"";
        }

        log.info("[browser: " + browser + "][org_file_nm: " + _org_file_nm + "][encodedFilename: " + encodedFilename + "][mimeType: " + mimeType + "]");
        log.info("lencodedFilename : " + encodedFilename + " : regularExpressioned ");

        //log.info("[browser: " + browser + "][org_file_nm: " + _org_file_nm + "][encodedFilename: " + encodedFilename + "][mimeType: " + mimeType + "]");

        //수정중중
       log.info("lencodedFilename : " + encodedFilename + " : regularExpressioned ");

        //String fileName = URLEncoder.encode(_org_file_nm, "UTF-8");


        String fileName = URLEncoder.encode(_org_file_nm, "UTF-8");
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }

    public static String removeExtension(String s) {
        String returnValue = null;
        if (s != null) {
            int index = s.lastIndexOf('.');
            if (index != -1) {
                returnValue = s.substring(0, index);
            } else {
                returnValue = s;
            }
        }
        return returnValue;
    }

    public boolean isMakeDir(String sdir)
    {
        File dir = new File(sdir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                if (log.isDebugEnabled()) {
                    log.info("isMakeDir Fail: " + sdir);
                }
            }
            else if (log.isDebugEnabled()) {
                log.info("isMakeDir success: " + sdir);
            }
        }

        return true;
    }

    public static String cutExtension(String s) {
        String returnValue = null;
        if (s != null) {
            int index = s.lastIndexOf('.');
            if (index != -1) {
                returnValue = s.substring(index + 1);
            }
        }
        return returnValue;
    }

    public String getBrowser(HttpServletRequest request) {

        String header = request.getHeader("user-agent");

        log.info("MisUser-Agent: " + header);

        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) {
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Safari") > -1) {
            return "Safari";
        }
        return "Firefox";

    }
}
