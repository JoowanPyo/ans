package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.breakingNewsFtpInfo.dto.BreakingNewsFtpInfoDTO;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileService;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.fileFtpInfo.FileFtpInfo;
import com.gemiso.zodiac.app.fileFtpInfo.FileFtpInfoRepository;
import com.gemiso.zodiac.app.fileFtpInfo.FileFtpInfoService;
import com.gemiso.zodiac.app.fileFtpInfo.dto.FileFtpInfoDTO;
import com.gemiso.zodiac.app.symbol.dto.*;
import com.gemiso.zodiac.app.symbol.mapper.SymbolCreateMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolUpdateMapper;
import com.gemiso.zodiac.core.enumeration.CodeEnum;
import com.gemiso.zodiac.core.service.FTPconnectService;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SymbolService {

    private final SymbolRepository symbolRepository;
    private final FileFtpInfoRepository fileFtpInfoRepository;
    //private final AttachFileRepository attachFileRepository;

    private final SymbolMapper symbolMapper;
    private final SymbolCreateMapper symbolCreateMapper;
    private final SymbolUpdateMapper symbolUpdateMapper;
    //private final AttachFileMapper attachFileMapper;

    private final AttachFileService attachFileService;
    //private final FileFtpInfoService fileFtpInfoService;

    //private final UserAuthService userAuthService;

    @Value("${files.url-key}")
    private String fileUrl;

    public List<SymbolDTO> findAll(String useYn, String symbolNm) {

        BooleanBuilder booleanBuilder = getSearch(useYn, symbolNm);

        List<Symbol> symbolList = (List<Symbol>) symbolRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "symbolOrd"));

        List<SymbolDTO> symbolDTOS = symbolMapper.toDtoList(symbolList);

        //방송아이콘 이미지가 있는 서버Url Set
        List<SymbolDTO> returnSymbolDTOList = new ArrayList<>();

        for (SymbolDTO symbolDTO : symbolDTOS) {

            if (ObjectUtils.isEmpty(symbolDTO.getAttachFile()) == false) {
                String fileLoc = symbolDTO.getAttachFile().getFileLoc();
                String url = fileUrl + fileLoc;
                symbolDTO.setUrl(url);
            }
            returnSymbolDTOList.add(symbolDTO);
        }


        return returnSymbolDTOList;
    }


    public SymbolDTO find(String symbolId) { //방송 아이콘 상세(단건) 조회

        //수정! 1:1관계에서 조인이 잘 되지 않음.
        Symbol symbol = symbolFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);

        //방송아이콘 이미지가 있는 서버Url Set
        if (ObjectUtils.isEmpty(symbolDTO.getAttachFile()) == false) {
            String fileLoc = symbolDTO.getAttachFile().getFileLoc();
            String url = fileUrl + fileLoc;
            symbolDTO.setUrl(url);
        }

        return symbolDTO;
    }

    public String create(SymbolCreateDTO symbolCreateDTO, String userId) { //방송 아이콘 등록 서비스

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        symbolCreateDTO.setInputrId(userId); //등록자 추가.

        Symbol symbol = symbolCreateMapper.toEntity(symbolCreateDTO); //articleDTO -> Entity로 변환

        symbolRepository.save(symbol); //등록

        return symbol.getSymbolId(); //articleDTO -> Entity변환시 자동생성된 ID return

    }

    public void update(SymbolUpdateDTO symbolUpdateDTO, String symbolId, String userId) {

        Symbol symbol = symbolFindOrFail(symbolId);

        //String userId = userAuthService.authUser.getUserId();
        symbolUpdateDTO.setUpdtrId(userId); // 수정자 추가.

        //수정.
        //파일이 바뀐경우 엔티티에서 파일을 지우고 새로운 파일로 업데이트,이유=update가 안댐[file_id가 필수값.]
        AttachFileDTO attachFileDTO = symbolUpdateDTO.getAttachFile();
        Long newFileId = Optional.ofNullable(attachFileDTO.getFileId()).orElse(0L);
        Long orgFileId = Optional.ofNullable(symbol.getAttachFile().getFileId()).orElse(0L);
        if (newFileId.equals(orgFileId) == false) {
            AttachFile attachFile = AttachFile.builder().build();
            symbol.setAttachFile(attachFile);
        }

        symbolUpdateMapper.updateFromDto(symbolUpdateDTO, symbol);
        symbolRepository.save(symbol);

    }

    public void delete(String symbolId, String userId) {

        Symbol symbol = symbolFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);


        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        symbolDTO.setDelrId(userId);
        symbolDTO.setDelYn("Y");
        symbolDTO.setDelDtm(new Date());

        symbolMapper.updateFromDto(symbolDTO, symbol);

        symbolRepository.save(symbol);


    }

    public void ordupdate(SymbolOrdUpdateDTO symbolOrdUpdateDTO, String symbolId) {

        Symbol symbol = symbolFindOrFail(symbolId);

        Integer symbolOrd = symbolOrdUpdateDTO.getSymbolOrd();

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);
        symbolDTO.setSymbolOrd(symbolOrd);

        symbolMapper.updateFromDto(symbolDTO, symbol);
        symbolRepository.save(symbol);

        String typCd = symbolOrdUpdateDTO.getTypCd();

        List<Symbol> symbolList = symbolRepository.findSymbolList(typCd);

        for (int i = symbolList.size() - 1; i >= 0; i--) {

            String id = symbolList.get(i).getSymbolId();

            if (id.equals(symbolId)) {
                symbolList.remove(i);
            }
        }

        symbolList.add(symbolOrd, symbol);

        //조회된 방송아이콘 리스트 Ord 업데이트
        int index = 0;
        for (Symbol symbolEntity : symbolList) {

            SymbolDTO updateSymbolDTO = symbolMapper.toDto(symbolEntity);
            updateSymbolDTO.setSymbolOrd(index);

            Symbol symbolSave = symbolMapper.toEntity(updateSymbolDTO);

            symbolRepository.save(symbolSave);

            index++;
        }

    }

    public Symbol symbolFindOrFail(String symbolId) { //방송 아이콘 Id로 등록된 방송아이콘 유무 검증.

        Optional<Symbol> symbol = symbolRepository.findBySymbolId(symbolId);

        if (symbol.isPresent() == false) {
            throw new ResourceNotFoundException("방송아이콘을 찾을 수 없습니다. 방송아이콘 아이디 : " + symbolId);
        }

        return symbol.get();

    }

    private BooleanBuilder getSearch(String useYn, String symbolNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSymbol qSymbol = QSymbol.symbol;

        booleanBuilder.and(qSymbol.delYn.eq("N"));

        if (useYn != null && useYn.trim().isEmpty() == false) {
            booleanBuilder.and(qSymbol.useYn.eq(useYn));
        }
        if (symbolNm != null && symbolNm.trim().isEmpty() == false) {
            booleanBuilder.and(qSymbol.symbolNm.contains(symbolNm));
        }

        return booleanBuilder;
    }

    public void sendFileToPrompter(MultipartFile file, String id) {

       /* //방송아이콘 확인
        Symbol symbol = symbolFindOrFail(symbolId);

        *//*****방송아이콘 이미지 파일 확인*****//*
        AttachFile SymbolAttachFile = symbol.getAttachFile();

        Long fileId = SymbolAttachFile.getFileId();

        AttachFileDTO attachFile = attachFileService.strFilefind(fileId);

        String fileDivCd = attachFile.getFileDivCd();

        PropertyUtil xu = new PropertyUtil();
        UploadFileBean ub = new UploadFileBean();
        ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);

        String fileLoc = attachFile.getFileLoc();
        String realpath = ub.getDest() + fileLoc + File.separator + attachFile.getFileNm();

        File file = null;
        // file에 파일경로 및 파일네임.확장자
        file=new File(realpath);*/

        //파일 존재 여부 확인
        if (file.getSize() == 0) {
            String errorMessage = "send file to Prompter - file does not exist";
            log.info(errorMessage);
            throw new ResourceNotFoundException("전송 하려는 방송아이콘 이미지 파일이 존재하지 않습니다. 방송아이콘 아이디 : " + file.getOriginalFilename());
        }

        /***** A 부조 전송 *****/
        getPrompterInpoAndSend(id, 1L, file);
       /* Optional<FileFtpInfo> AfileFtpInfoEntity = fileFtpInfoRepository.findFtpInfo(1L);

        if (AfileFtpInfoEntity.isPresent()){

            FileFtpInfo AfileFtpInfo = AfileFtpInfoEntity.get();

            ftpSend(file, AfileFtpInfo);

        }*/

        /***** B 부조 전송 *****/
        getPrompterInpoAndSend(id, 4L, file);
       /* Optional<FileFtpInfo> BfileFtpInfoEntity = fileFtpInfoRepository.findFtpInfo(4L);

        if (BfileFtpInfoEntity.isPresent()){

            FileFtpInfo BfileFtpInfo = BfileFtpInfoEntity.get();

            ftpSend(file, BfileFtpInfo);

        }*/

    }

    public void sendFileToTaker(MultipartFile file, String id, String fileDivCd) {

        /*//방송아이콘 확인
        Symbol symbol = symbolFindOrFail(symbolId);

        *//*****방송아이콘 이미지 파일 확인*****//*
        AttachFile SymbolAttachFile = symbol.getAttachFile();
        String typCd = symbol.getTypCd();

        Long fileId = SymbolAttachFile.getFileId();

        AttachFileDTO attachFile = attachFileService.strFilefind(fileId);

        String fileDivCd = attachFile.getFileDivCd();

        PropertyUtil xu = new PropertyUtil();
        UploadFileBean ub = new UploadFileBean();
        ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);

        String fileLoc = attachFile.getFileLoc();
        String realpath = ub.getDest() + fileLoc + File.separator + attachFile.getFileNm();

        File file = null;
        // file에 파일경로 및 파일네임.확장자
        file=new File(realpath);*/

        //파일 존재 여부 확인
        if (file.getSize() == 0) {
            String errorMessage = "send file to taker - file does not exist";
            log.info(errorMessage);
            throw new ResourceNotFoundException("전송 하려는 방송아이콘 이미지 파일이 존재하지 않습니다. 방송아이콘 아이디 : " + file.getOriginalFilename());
        }

        /***** A 부조 전송 *****/

        getTakerInpoAndSend(id, fileDivCd, 2L, 3L, file);

        /***** B 부조 전송 *****/
        /*Optional<FileFtpInfo> BfileFtpInfoEntity = fileFtpInfoRepository.findFtpInfo( 4L);

        if (BfileFtpInfoEntity.isPresent()){

            FileFtpInfo BfileFtpInfo = BfileFtpInfoEntity.get();

            ftpSend(file, BfileFtpInfo);

        }*/
        getTakerInpoAndSend(id, fileDivCd, 5L, 6L, file);

    }

    public void getPrompterInpoAndSend(String id, Long prompterId, MultipartFile file) {

        //A부조 프롬프터
        Optional<FileFtpInfo> AfileFtpInfoEntity = fileFtpInfoRepository.findFtpInfo(prompterId);

        if (AfileFtpInfoEntity.isPresent()) {

            FileFtpInfo AfileFtpInfo = AfileFtpInfoEntity.get();

            ftpSend(id, file, AfileFtpInfo);

        }
    }

    public void getTakerInpoAndSend(String id, String typCd, Long audioId, Long videoId, MultipartFile file) {


        if (typCd.equals(CodeEnum.AUDIO.getCode(CodeEnum.AUDIO))) {

            //A부조 테이커 AUDIO
            Optional<FileFtpInfo> ATakerAudioFtpEntity = fileFtpInfoRepository.findFtpInfo(audioId);

            if (ATakerAudioFtpEntity.isPresent()) {

                FileFtpInfo AfileFtpInfo = ATakerAudioFtpEntity.get();

                ftpSend(id, file, AfileFtpInfo);

            }

        } else if (typCd.equals(CodeEnum.VIDEO.getCode(CodeEnum.VIDEO))) {

            //A부조 테이커 AUDIO
            Optional<FileFtpInfo> ATakerVideoFtpEntity = fileFtpInfoRepository.findFtpInfo(videoId);

            if (ATakerVideoFtpEntity.isPresent()) {

                FileFtpInfo AfileFtpInfo = ATakerVideoFtpEntity.get();

                ftpSend(id, file, AfileFtpInfo);

            }

        }

    }

    public void ftpSend(String id, MultipartFile file, FileFtpInfo fileFtpInfo) {

        File transfFile = null;

        String ftpIp = fileFtpInfo.getFtpIp();
        Integer ftpPort = fileFtpInfo.getFtpPort();
        String ftpId = fileFtpInfo.getFtpId();
        String ftpPw = fileFtpInfo.getFtpPw();
        String ftpPath = fileFtpInfo.getFtpPath();
        String ftpType = fileFtpInfo.getFtpType();

        //FTP코드 생성자
        FTPconnectService ftp = new FTPconnectService();
        FileInputStream fis = null;

        String fileNm = null;
        String rname = file.getOriginalFilename();

        if (rname != null && rname.trim().isEmpty() == false) {

            if (rname.endsWith(".png") || rname.endsWith(".bmp")) {

                //확장자 파싱
                String ext = cutExtension(rname);
                fileNm = id + "." + ext;
            } else {
                throw new ResourceNotFoundException(" 업로드할 수 없는 확장자입니다. 현재 확장자 : " + rname);
            }
        }

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            } else if ("passive".equals(ftpType)) {
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            try {
                if (file.getSize() != 0) {
                    transfFile = new File("/data/storage/temp", file.getOriginalFilename());
                    file.transferTo(Paths.get("/data/storage/temp/" + transfFile.getName()));
                    fis = new FileInputStream(transfFile);
                    ftp.storeFile(fileNm, fis); //파일명

                    log.info("속보 뉴스 FTP1 파일명 : " + file.getName() + " fis : " + fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("속보 뉴스 FTP file handle exception : " + "FileNotFoundException");
                // TODO: handle exception
            } catch (IOException e) {
                log.error("속보 뉴스 FTP file handle exception : " + "IOException");
                // TODO: handle exception
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            //ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("속보 뉴스 FTP file NumberFormatException : " + "NumberFormatException");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("속보 뉴스 FTP file Exception : " + "Exception - " + e.getMessage());
        } finally {
            if (ftp != null) {
                ftp.disconnect();
            }

            if (transfFile.exists()) {
                if (transfFile.delete()) {
                    log.info("속보 뉴스 File Delete ");
                } else {
                    log.error("속보 뉴스 file delete failed");
                }
            }

        }

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


}
