package com.gemiso.zodiac.app.issue;

import com.gemiso.zodiac.app.issue.dto.IssueCopyDTO;
import com.gemiso.zodiac.app.issue.dto.IssueCreateDTO;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.issue.dto.IssueUpdateDTO;
import com.gemiso.zodiac.app.issue.mapper.IssueCreateMapper;
import com.gemiso.zodiac.app.issue.mapper.IssueMapper;
import com.gemiso.zodiac.app.issue.mapper.IssueUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final IssueRepositoy issueRepositoy;

    private final IssueCreateMapper issueCreateMapper;
    private final IssueMapper issueMapper;
    private final IssueUpdateMapper issueUpdateMapper;
    private final UserAuthService userAuthService;


    public List<IssueDTO> findAll(Date sdate, Date edate, String issuDelYn){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, issuDelYn);

        List<Issue> issueEntity = (List<Issue>) issueRepositoy.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "issuOrd"));

        List<IssueDTO> issueDTOList = issueMapper.toDtoList(issueEntity);

        return issueDTOList;
    }

    //public PageResultDTO<IssueDTO, Issue> getIssueList(Date sdate, Date edate, String issu_del_yn) {


        //Pageable pageable = PageRequest.of(0, 10, Sort.by("issuId").ascending());

        //  BooleanBuilder booleanBuilder = getSearch(sdate, edate, issu_del_yn);

        //  Page<Issue> result = issueRepositoy.findAll(booleanBuilder, pageable);

        //Function<Issue, IssueDTO> fn = (entity -> issueMapper.toDtoPage(entity));

        //    return new PageResultDTO<IssueDTO, Issue>(result, fn);
        //return null;
    //}

    public IssueDTO find(Long issuId) {

        Issue IssueEntity = issueFindOrFail(issuId);

        return issueMapper.toDto(IssueEntity);
    }


    public IssueDTO create(IssueCreateDTO issueCreateDTO) throws Exception {

        Integer issuOrd = getOrd();

        issueCreateDTO.setIssuOrd(issuOrd + 1);
        issueCreateDTO.setInputrId(userAuthService.authUser.getUserId());

        Issue issue = issueCreateMapper.toEntity(issueCreateDTO);
        issueRepositoy.save(issue);

        return issueMapper.toDto(issue);
    }

    public void update(IssueUpdateDTO issueUpdateDTO, Long issuId) { //이부분 찾아보기.

        Issue issue = issueFindOrFail(issuId);

        issueUpdateDTO.setUpdtDtm(new Date());
        issueUpdateDTO.setUpdtrId(userAuthService.authUser.getUserId());
        issueUpdateDTO.setUpdtDtm(new Date());

        issueUpdateMapper.updateFromDto(issueUpdateDTO, issue);
        issueRepositoy.save(issue);

    }

    public void delete(Long issuId) {

        Issue issue = issueFindOrFail(issuId);

        IssueDTO issueDto = issueMapper.toDto(issue);

        issueDto.setIssuDelYn("Y");
        issueDto.setDelDtm(new Date());
        issueDto.setDelrId(userAuthService.authUser.getUserId());

        issueMapper.updateFromDto(issueDto, issue);

        issueRepositoy.save(issue);
    }

    private Issue issueFindOrFail(Long issuId) {
        /*return issueRepositoy.findById(issuId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found. issueId : " + issuId));*/

        Optional<Issue> issue = issueRepositoy.findByIssuId(issuId);

        if (!issue.isPresent()){
            throw new ResourceNotFoundException("Issue not found. issueId : " + issuId);
        }

        return issue.get();
    }

    public BooleanBuilder getSearch(Date sdate, Date edate, String issuDelYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QIssue qIssue = QIssue.issue;

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qIssue.inputDtm.between(sdate, edate));
        }
        if (!StringUtils.isEmpty(issuDelYn)){
            booleanBuilder.and(qIssue.issuDelYn.eq(issuDelYn));
        }

        return booleanBuilder;
    }

    public Date copy(List<IssueCopyDTO> issueCopyDTO, Date targetDate) throws Exception {

        if (!CollectionUtils.isEmpty(issueCopyDTO)){
            for (IssueCopyDTO issueCopyDto : issueCopyDTO){

                Long issueId = issueCopyDto.getIssuId();
                IssueDTO issueDTO = find(issueId);

                IssueDTO issueDTOCopy = new IssueDTO();

                Integer issuOrd = targetOrd(targetDate);

                issueDTOCopy.setChDivCd(issueDTO.getChDivCd());
                issueDTOCopy.setIssuDtm(issueDTO.getIssuDtm());
                issueDTOCopy.setIssuOrd(issuOrd + 1);
                issueDTOCopy.setIssuKwd(issueDTO.getIssuKwd());
                issueDTOCopy.setIssuCtt(issueDTO.getIssuCtt());
                issueDTOCopy.setIssuFnshYn(issueDTO.getIssuFnshYn());
                issueDTOCopy.setIssuDelYn(issueDTO.getIssuDelYn());
                issueDTOCopy.setIssuFnshDtm(issueDTO.getIssuFnshDtm());
                issueDTOCopy.setIssuOrgId(issueDTO.getIssuOrgId());
                issueDTOCopy.setUpdtrId(issueDTO.getUpdtrId());
                issueDTOCopy.setUpdtDtm(issueDTO.getUpdtDtm());
                issueDTOCopy.setInputDtm(targetDate);
                issueDTOCopy.setInputrId(userAuthService.authUser.getUserId());

                Issue issueEntity = issueMapper.toEntity(issueDTOCopy);
                issueRepositoy.save(issueEntity);
            }
        }

        Date edate = getTargetDate(targetDate); //조회를 위한 타겟날자 +1 day 값을 Date 형식으로 파싱하여 보내준다
        //조회를 위한 타겟날자 +1 day 값을 Date 형식으로 파싱하여 보내준다
       /* Calendar cal = Calendar.getInstance();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(targetDate);
        cal.add(cal.DATE, +1); //날짜를 하루 더한다.
        String tDate = sdf.format(cal.getTime());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date edate = transFormat.parse(tDate);*/

        return edate;

    }

    public List<IssueDTO> changeOrder(Long issuId, Integer issuOrd) throws Exception {

        //이슈 조회후 해당 이슈 오더값 업데이트
        IssueDTO issueDTO = find(issuId);
        issueDTO.setIssuOrd(issuOrd);
        Issue issue = issueMapper.toEntity(issueDTO);
        issueRepositoy.save(issue);

        Date issuDtm = issueDTO.getInputDtm(); //조회한 이슈값에서 입력날짜를 통해 List 조회 sdate값을 가져온다.

        // yyyy-MM-dd ss:hh:mm 형식을 yyyy-MM-dd로 변환
        Calendar cal = Calendar.getInstance();//정확한 데이터를 조회 하기위해 파싱
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(issuDtm);
        String tDate = sdf.format(cal.getTime());
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = transFormat.parse(tDate);

        Date edate = getTargetDate(issuDtm);

        List<IssueDTO> issueDTOList = findAll(sdate, edate, "N");

        if (!CollectionUtils.isEmpty(issueDTOList)){

            for(int i = 0; i < issueDTOList.size(); i++){
                if (issuId.equals(issueDTOList.get(i).getIssuId())){
                    issueDTOList.remove(i);
                }
            }

            issueDTO.setIssuId(issuId);
            issueDTOList.add(issuOrd, issueDTO);

            Integer index = 0;

            for (IssueDTO issueDto : issueDTOList){
                issueDto.setIssuOrd(index);
                Issue issueEntity = issueMapper.toEntity(issueDto);
                issueRepositoy.save(issueEntity);
                index++;
            }

        }

        return findAll(sdate, edate, "N");
    }

    public Integer getOrd() throws Exception {

        Calendar cal = Calendar.getInstance();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nDate = sdf.format(cal.getTime()); //현재 날짜를 구한다.

        cal.add(cal.DATE, +1); //날짜를 하루 더한다.
        String tDate = sdf.format(cal.getTime());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sdate = transFormat.parse(nDate);
        Date edate = transFormat.parse(tDate);

        Optional<Integer> issuOrd = issueRepositoy.findByOrd(sdate, edate);

        if (!issuOrd.isPresent()){
            return issuOrd.orElse(0);
        }

        return issuOrd.get();
    }

    public Integer targetOrd(Date targetDate) throws Exception {

        //Ord검색을 위해 내일 날짜를 구해온다
        Calendar cal = Calendar.getInstance();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(targetDate);
        String nDate = sdf.format(cal.getTime());

        cal.add(cal.DATE, +1); //날짜를 하루 더한다.
        String tDate = sdf.format(cal.getTime());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sdate = transFormat.parse(nDate);
        Date edate = transFormat.parse(tDate);

        Optional<Integer> issuOrd = issueRepositoy.findByOrd(sdate, edate);

        if (!issuOrd.isPresent()){
            return issuOrd.orElse(0);
        }

        return issuOrd.get();
    }

    public Date getTargetDate(Date targetDate) throws Exception { //타겟데이트 +1day 날짜를 Date형식으로 return

        Calendar cal = Calendar.getInstance();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(targetDate);
        cal.add(cal.DATE, +1); //날짜를 하루 더한다.
        String tDate = sdf.format(cal.getTime());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date edate = transFormat.parse(tDate);

        return edate;
    }
}
