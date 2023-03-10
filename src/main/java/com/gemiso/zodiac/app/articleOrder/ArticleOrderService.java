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

        //????????? ?????? page, limit null?????? page = 1 limit = 50 ????????? ??????
        //PageHelper pageHelper = new PageHelper(1, 100);
        Pageable pageable = PageRequest.of(0,100,Sort.by(Sort.Direction.DESC, "inputDtm"));

        //???????????? ?????? build
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, orderDivCd, orderStatus, workrId, inputrId, artclId, orgArtclId, rptrId);

        //?????????????????? ????????????
       /* List<ArticleOrder> articleOrderList = (List<ArticleOrder>) articleOrderRepository.findAll(booleanBuilder,
                Sort.by(Sort.Direction.DESC, "inputDtm"));*/

        Page<ArticleOrder> articleOrderPage = articleOrderRepository.findAll(booleanBuilder,
                pageable);

        List<ArticleOrder> articleOrderList = articleOrderPage.getContent();
        //DTO????????? return
        List<ArticleOrderDTO> articleOrderDTOList = articleOrderMapper.toDtoList(articleOrderList);

        articleOrderDTOList = setArticleFileUrl(articleOrderDTOList);

        return articleOrderDTOList;

    }

    public List<ArticleOrderDTO> setArticleFileUrl(List<ArticleOrderDTO> articleOrderDTOList){

        List<ArticleOrderDTO> returnDtoList = new ArrayList<>(); //????????? ????????????DTO ??????

        for (ArticleOrderDTO articleOrderDTO: articleOrderDTOList){

            List<ArticleOrderFileDTO> setFileDTOList = new ArrayList<>(); //?????? url?????????????????? set?????? contentDTO list
            List<ArticleOrderFileDTO> orgFileDTOList = articleOrderDTO.getArticleOrderFile(); //??????????????? ???????????? List?????? ?????????????????? articleDTO ????????? get

            for (int i = 0; i < orgFileDTOList.size(); i++){

                ArticleOrderFileDTO fileDTO = orgFileDTOList.get(i);

                String fileLoc = fileDTO.getFile().getFileLoc();
                String url = fileUrl + fileLoc; //url + ????????????
                fileDTO.setUrl(url);

                setFileDTOList.add(fileDTO); // newDTO list??? ??????.
            }

            articleOrderDTO.setArticleOrderFile(setFileDTOList); //??????DTO ?????????DTO???????????? url????????? articleDTO ????????? set
            returnDtoList.add(articleOrderDTO); // ?????? articleDTO ???????????? add
        }

        return returnDtoList;
    }

    public ArticleOrderDTO find(Long oderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(oderId);

        ArticleOrderDTO articleOrderDTO = articleOrderMapper.toDto(articleOrder);

        List<ArticleOrderFileDTO> newArticleOrderFileDTO = new ArrayList<>();
        List<ArticleOrderFileDTO> articleOrderFileDTO = articleOrderDTO.getArticleOrderFile(); //?????? ???????????? list get

        for (ArticleOrderFileDTO fileDTO : articleOrderFileDTO){

            String fileLoc = fileDTO.getFile().getFileLoc();
            String url = fileUrl + fileLoc; //url + ????????????
            fileDTO.setUrl(fileUrl);

            newArticleOrderFileDTO.add(fileDTO);// new FileDTO List??? url?????? contentDTO ??????
        }
        articleOrderDTO.setArticleOrderFile(newArticleOrderFileDTO); // ????????? ????????? url??????

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

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId); //?????? ???????????? ???????????? ??????.

        List<ArticleOrderFile> articleOrderFileList= articleOrder.getArticleOrderFile();

        for (ArticleOrderFile articleOrderFile : articleOrderFileList){

            Long id = articleOrderFile.getId();

            //??????????????? ?????? ?????????
           /* AttachFile attachFile = articleOrderFile.getFile();
            String fileLoc = attachFile.getFileLoc();
            String fileNm = attachFile.getFileNm();*/

            articleOrderFileRepository.deleteById(id);
        }
        
        articleOrderRepository.deleteById(orderId);//???????????? ??????.
        
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

    //???????????? ?????? build
    public BooleanBuilder getSearch(Date sdate, Date edate, String orderDivCd, String orderStatus, String workrId,
                                    String inputrId, Long artclId, Long orgArtclId, String rptrId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleOrder qArticleOrder = QArticleOrder.articleOrder;

        //??????????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticleOrder.inputDtm.between(sdate, edate));
        }
        //????????????????????? ??????
        if (orderDivCd != null && orderDivCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.orderDivCd.eq(orderDivCd));
        }
        //?????? ????????? ??????
        if (orderStatus != null && orderStatus.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.orderStatus.eq(orderStatus));
        }
        //????????? ???????????? ??????
        if (workrId != null && workrId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleOrder.workrId.eq(workrId));
        }
        //????????? ???????????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false){
            booleanBuilder.and(qArticleOrder.inputrId.eq(inputrId));
        }
        //????????? ???????????? ??????
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qArticleOrder.article.artclId.eq(artclId));
        }
        //???????????? ?????????
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            booleanBuilder.and(qArticleOrder.article.orgArtclId.eq(orgArtclId));
        }
        //?????? ?????????
        if (rptrId != null && rptrId.trim().isEmpty() == false){
            booleanBuilder.and(qArticleOrder.article.rptrId.eq(rptrId));
        }

        return booleanBuilder;
    }
}
