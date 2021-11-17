package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.appInterface.programDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.cueFindAllDTO.TakerCueProgramDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.program.Program;

import java.util.ArrayList;
import java.util.List;

public class AnsToTaker extends AnsCueSheet{

    public AnsToTaker() {

    }

    public List<ParentProgramDTO> ToStringXmlList() {

        List<ParentProgramDTO> cueSheetDTOList = new ArrayList<ParentProgramDTO>();

        for (CueSheet cueSheet : GetCuesheet()){

            ParentProgramDTO pcsDto = ParentProgramDTO.builder()
                    .cueId(cueSheet.getCueId())
                    //.cueDivCd(cueSheet.getCueDivCd())
                    .chDivCd(cueSheet.getChDivCd())
                    .brdcDt(cueSheet.getBrdcDt())
                    .brdcStartTime(cueSheet.getBrdcStartTime())
                    .brdcEndTime(cueSheet.getBrdcEndTime())
                    //.brdcSchTime(cueSheet.getBrdcSchTime())
                    .brdcPgmNm(cueSheet.getBrdcPgmNm())
                    //.cueStCd(cueSheet.getCueStCd())
                    .stdioId(cueSheet.getStdioId())
                    .subrmId(cueSheet.getSubrmId())
                    //.lckDtm(cueSheet.getLckDtm())
                    //.lckYn(cueSheet.getLckYn())
                    //.delDtm(cueSheet.getDelDtm())
                    .inputDtm(cueSheet.getInputDtm())
                    //.delYn(cueSheet.getDelYn())
                    .inputr(cueSheet.getInputrId())
                    //.delr(cueSheet.getDelr()))
                    .pd1(cueSheet.getPd1Id())
                    .pd2(cueSheet.getPd2Id())
                    .anc1(cueSheet.getAnc1Id())
                    .anc2(cueSheet.getAnc2Id())
                    //.lckr(cueSheet.getLckr()))
                    .td1(cueSheet.getTd1Id())
                    //.td2(cueSheet.getTd2()))
                    .remark(cueSheet.getRemark())
                    //.program(toDtoPgm(cueSheet.getProgram()))
                    .build();

            cueSheetDTOList.add(pcsDto);

        }

        // 씨리얼라이즈

        return cueSheetDTOList;
    }
    public ParentProgramDTO ToStringXML(CueSheet cueSheet) {

        ParentProgramDTO pcsDto = ParentProgramDTO.builder()
                .cueId(cueSheet.getCueId())
                .chDivCd(cueSheet.getChDivCd())
                .chDivNm("") // 수정.
                .brdcDt(cueSheet.getBrdcDt())
                .brdcStartTime(cueSheet.getBrdcStartTime())
                .brdcEndTime(cueSheet.getBrdcEndTime())
                .brdcPgmId(0L) //수정.
                .brdcPgmNm(cueSheet.getBrdcPgmNm())
                .urgPgmschPgmNm("") // 수정.몬지모름
                .brdcDivCD("") //수정
                .cmDivCd("") //수정
                .remark(cueSheet.getRemark())
                .inputr(cueSheet.getInputrId())
                .inputrNm(cueSheet.getInputrNm())
                .inputDtm(cueSheet.getInputDtm())
                .pd1(cueSheet.getPd1Id())
                .pd2(cueSheet.getPd2Id())
                .anc1(cueSheet.getAnc1Id())
                .anc2(cueSheet.getAnc2Id())
                .td1(cueSheet.getTd1Id())
                .stdioId(cueSheet.getStdioId())
                .subrmId(cueSheet.getSubrmId())
                .cgId(0L) //수정. 몬지모름
                .cgloc("") //수정. 몬지모름
                .vfId(0L) //수정. 몬지모름
                .vsId(0L) //수정. 몬지모름
                .pd1Nm(cueSheet.getPd1Nm())
                .pd2Nm(cueSheet.getPd2Nm())
                .anc1Nm(cueSheet.getAnc1Nm())
                .anc2Nm(cueSheet.getAnc2Nm())
                .tdNm(cueSheet.getTd1Nm())
                .stdioNm("") //수정.
                .subrmNm("") //수정.
                .rdEdtYn("") //수정.
                .endpgmYn("") //수정.
                .build();

        return pcsDto;
    }


    private TakerCueProgramDTO toDtoPgm(Program program){

        /*TakerCueProgramDTO tpmDTO = TakerCueProgramDTO.builder()
                .brdcPgmId(program.getBrdcPgmId())
                .brdcPgmNm(program.getBrdcPgmNm())
                .chDivCd(program.getChDivCd().getCd())
                .brdcPgmDivCd(program.getBrdcPgmDivCd().getCd())
                .gneDivCd(program.getGneDivCd().getCd())
                .prdDivCd(program.getPrdDivCd().getCd())
                .brdcStartTime(program.getBrdcStartTime())
                .schTime(program.getSchTime())
                .inputDtm(program.getInputDtm())
                .updtDtm(program.getUpdtDtm())
                .delYn(program.getDelYn())
                .delDtm(program.getDelDtm())
                .inputr(program.)
                .updtr(program.getUpdtr()))
                .delr(program.getDelr()))
                .build();*/
        return null;

    }

}

/*
// CuesheetData cd = new CuesheetData();
    // 1. Set Program(List<CueSheet> cueSheets)
    // 2. Set Cuesheet( // 큐시트 목록 조회(아이템) );

    // cd >> List<CueSheet> cueSheets
    // cd.GetCodeXML();

    CuesheetToTaker ctaker = new CuesheetToTaker();
    String xml = ctaker.CuesheetToStringXML();
// ctaker.ItemToStringXML();
        --> ctaker.GetCodeXML();

                CuesheetToPrompt cprompt = new CuesheetToPrompt();
                String xml1 = cprompt.ToString();
                --> ctaker.GetCodeXML();

                CuesheetToPSTaker cprompt = new CuesheetToPSTaker();
                String xml1 = cprompt.ToString();
                --> ctaker.GetCodeXML();
// 채널 코드 001 -->>
*/

/*
    ParentProgramDTO pcsDto = ParentProgramDTO.builder()
            .cueId(cueSheet.getCueId())
            .cueDivCd(cueSheet.getCueDivCd())
            .chDivCd(cueSheet.getChDivCd())
            .brdcDt(cueSheet.getBrdcDt())
            .brdcStartTime(cueSheet.getBrdcStartTime())
            .brdcEndTime(cueSheet.getBrdcEndTime())
            .brdcSchTime(cueSheet.getBrdcSchTime())
            .brdcPgmNm(cueSheet.getBrdcPgmNm())
            .cueStCd(cueSheet.getCueStCd())
            .stdioId(cueSheet.getStdioId())
            .subrmId(cueSheet.getSubrmId())
            .lckDtm(cueSheet.getLckDtm())
            .lckYn(cueSheet.getLckYn())
            .delDtm(cueSheet.getDelDtm())
            .inputDtm(cueSheet.getInputDtm())
            .delYn(cueSheet.getDelYn())
            .inputr(cueSheet.getInputr()))
            .delr(cueSheet.getDelr()))
            .pd1(cueSheet.getPd1()))
            .pd2(cueSheet.getPd2()))
            .anc1(cueSheet.getAnc1()))
            .anc2(cueSheet.getAnc2()))
            .lckr(cueSheet.getLckr()))
            .td1(cueSheet.getTd1()))
            .td2(cueSheet.getTd2()))
            .remark(cueSheet.getRemark())
            //.program(toDtoPgm(cueSheet.getProgram()))
            .build();

        return pcsDto;*/
