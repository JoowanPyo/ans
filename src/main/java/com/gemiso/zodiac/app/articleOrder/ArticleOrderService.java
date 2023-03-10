package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderStatusDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderCreateMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderUpdateMapper;
import com.gemiso.zodiac.app.articleOrderFile.ArticleOrderFile;
import com.gemiso.zodiac.app.articleOrderFile.ArticleOrderFileRepository;
import com.gemiso.zodiac.app.articleOrderFile.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleOrderService {

    @Value("${files.url-key}")
    private String fileUrl;

    private final ArticleOrderRepository articleOrderRepository;
    private final ArticleOrderFileRepository articleOrderFileRepository;

    private final ArticleOrderMapper articleOrderMapper;
    private final ArticleOrderCreateMapper articleOrderCreateMapper;
    private final ArticleOrderUpdateMapper articleOrderUpdateMapper;

    //private final UserAuthService userAuthService;




    public List<ArticleOrderDTO> findAll(Date sdate, Date edate, String orderDivCd, String orderStatus, String workrId,
                                         String inputrId, Long artclId, Long orgArtclId, String rptrId){

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        //PageHelper pageHelper = new PageHelper(1, 100);
        Pageable pageable = PageRequest.of(0,100,Sort.by(Sort.Direction.DESC, "inputDtm"));

        //목록조회 조건 build
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, orderDivCd, orderStatus, workrId, inputrId, artclId, orgArtclId, rptrId);

        //조회조건으로 전체조회
       /* List<ArticleOrder> articleOrderList = (List<ArticleOrder>) articleOrderRepository.findAll(booleanBuilder,
                Sort.by(Sort.Direction.DESC, "inputDtm"));*/

        Page<ArticleOrder> articleOrderPage = articleOrderRepository.findAll(booleanBuilder,
                pageable);

        List<ArticleOrder> articleOrderList = articleOrderPage.getContent();
        //DTO변환후 return
        List<ArticleOrderDTO> articleOrderDTOList = articleOrderMapper.toDtoList(articleOrderList);

        articleOrderDTOList = setArticleFileUrl(articleOrderDTOList);

        return articleOrderDTOList;

    }

    public List<ArticleOrderDTO> setArticleFileUrl(List<ArticleOrderDTO> articleOrderDTOList){

        List<ArticleOrderDTO> returnDtoList = new ArrayList<>(); //리턴할 기사의뢰DTO 생성

        for (ArticleOrderDTO articleOrderDTO: articleOrderDTOList){

            List<ArticleOrderFileDTO> setFileDTOList = new ArrayList<>(); //새로 url추가생성하여 set해줄 contentDTO list
            List<ArticleOrderFileDTO> orgFileDTOList = articleOrderDTO.getArticleOrderFile(); //목록조회된 기사의뢰 List에서 기사의뢰파일 articleDTO 리스트 get

            for (int i = 0; i < orgFileDTOList.size(); i++){

                ArticleOrderFileDTO fileDTO = orgFileDTOList.get(i);

                String fileLoc = fileDTO.getFile().getFileLoc();
                String url = fileUrl + fileLoc; //url + 파일로그
                fileDTO.setUrl(url);

                setFileDTOList.add(fileDTO); // newDTO list에 추가.
            }

            articleOrderDTO.setArticleOrderFile(setFileDTOList); //의뢰DTO 파일에DTO리스트에 url추가된 articleDTO 리스트 set
            returnDtoList.add(articleOrderDTO); // 리턴 articleDTO 리스트에 add
        }

        return returnDtoList;
    }

    public ArticleOrderDTO find(Long oderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(oderId);

        ArticleOrderDTO articleOrderDTO = articleOrderMapper.toDto(articleOrder);

        List<ArticleOrderFileDTO> newArticleOrderFileDTO = new ArrayList<>();
        List<ArticleOrderFileDTO> articleOrderFileDTO = articleOrderDTO.getArticleOrderFile(); //의뢰 첨부파일 list get

        for (ArticleOrderFileDTO fileDTO : articleOrderFileDTO){

            String fileLoc = fileDTO.getFile().getFileLoc();
            String url = fileUrl + fileLoc; //url + 파일로그
            fileDTO.setUrl(fileUrl);

            newArticleOrderFileDTO.add(fileDTO);// new FileDTO List에 url추가 contentDTO 추가
        }
        articleOrderDTO.setArticleOrderFile(newArticleOrderFileDTO); // 조회된 의뢰에 url추가

        return articleOrderDTO;
    }

    public Long create(ArticleOrderCreateDTO articleOrderCreateDTO, String userId){


        articleOrderCreateDTO.setInputrId(userId);

        ArticleOrder articleOrder = articleOrderCreateMapper.toEntity(articleOrderCreateDTO);

        articleOrderRepository.save(articleOrder);

        return articleOrder.getOrderId();
    }

    public void update(ArticleOrderUpdateDTO articleOrderUpdateDTO, Long orderId, String userId){

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId);

        articleOrderUpdateDTO.setUpdtrId(userId);

        articleOrderUpdateMapper.updateFromDto(articleOrderUpdateDTO, articleOrder);

        articleOrderRepository.save(articleOrder);

    }
    
    public void delete(long orderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId); //해당 기사의뢰 존재유무 확인.

        List<ArticleOrderFile> articleOrderFileList= articleOrder.getArticleOrderFile();

        for (ArticleOrderFile articleOrderFile : articleOrderFileList){

            Long id = articleOrderFile.getId();

            //파일삭제는 하지 않기로
           /* AttachFile attachFile = articleOrderFile.getFile();
            String fileLoc = attachFile.getFileLoc();
            String fileNm = attachFile.getFileNm();*/

            articleOrderFileRepository.deleteById(id);
        }
        
        articleOrderRepository.deleteById(orderId);//기사의뢰 삭제.
        
    }

    public void updateStatus(ArticleOrderStatusDTO articleOrderStatusDTO, Long orderId, String userId){

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId);

        ArticleOrderDTO articleOrderDTO = articleOrderMapper.toDto(articleOrder);
        articleOrderDTO.setOrderStatus(articleOrderStatusDTO.getOrderStatus());
        //articleOrderDTO.setWorkrId(userId);

        articleOrderMapper.updateFromDto(articleOrderDTO, articleOrder);

        articleOrderRepository.save(articleOrder);

    }

    private ArticleOrder articleOrderFindOrFail(Long orderId){

        return articleOrderRepository.findById(orderId)
                .orElseThrow( () -> new ResourceNotFoundException("ArticleOrderId not found. ArticleOrderId : " + orderId));

    }

    //목록조회 조건 build
    public BooleanBuilder getSearch(Date sdate, Date edate, String orderDivCd, String orderStatus, String workrId,
                                    String inputrId, Long artclId, Long orgArtclId, String rptrId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleOrder qArticleOrder = QArticleOrder.articleOrder;

        //입력날짜로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticleOrder.inputDtm.between(sdate, edate));
        }
        //의뢰구분코드로 조회
        if (orderDivCd != null && orderDivCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.orderDivCd.eq(orderDivCd));
        }
        //의뢰 상태로 조회
        if (orderStatus != null && orderStatus.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.orderStatus.eq(orderStatus));
        }
        //작업자 아이디로 조회
        if (workrId != null && workrId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.workrId.eq(workrId));
        }
        //등록자 아이디로 조회
        if (inputrId != null && inputrId.trim().isEmpty() == false){
            booleanBuilder.and(qArticleOrder.inputrId.eq(inputrId));
        }
        //작업자 아이디로 조회
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qArticleOrder.article.artclId.eq(artclId));
        }
        //원본기사 아이디
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            booleanBuilder.and(qArticleOrder.article.orgArtclId.eq(orgArtclId));
        }
        //기자 아이디
        if (rptrId != null && rptrId.trim().isEmpty() == false){
            booleanBuilder.and(qArticleOrder.article.rptrId.eq(rptrId));
        }

        return booleanBuilder;
    }
}
