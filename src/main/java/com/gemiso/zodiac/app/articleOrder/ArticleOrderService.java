package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderCreateMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    private final ArticleOrderMapper articleOrderMapper;
    private final ArticleOrderCreateMapper articleOrderCreateMapper;
    private final ArticleOrderUpdateMapper articleOrderUpdateMapper;

    private final UserAuthService userAuthService;




    public List<ArticleOrderDTO> findAll(Date sdate, Date edate,
                                         String order_div_cd, String order_status, String workr_id, Long artclId){

        //목록조회 조건 build
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, order_div_cd, order_status, workr_id, artclId);

        //조회조건으로 전체조회
        List<ArticleOrder> articleOrderList = (List<ArticleOrder>) articleOrderRepository.findAll(booleanBuilder,
                Sort.by(Sort.Direction.ASC, "inputDtm"));

        //DTO변환후 return
        List<ArticleOrderDTO> articleOrderDTOList = articleOrderMapper.toDtoList(articleOrderList);

        articleOrderDTOList = setArticleFileUrl(articleOrderDTOList);

        return articleOrderDTOList;

    }

    public List<ArticleOrderDTO> setArticleFileUrl(List<ArticleOrderDTO> articleOrderDTOList){

        List<ArticleOrderDTO> returnDtoList = new ArrayList<>(); //리턴할 기사의뢰DTO 생성

        for (ArticleOrderDTO articleOrderDTO: articleOrderDTOList){

            List<ArticleOrderFileDTO> setFileDTOList = new ArrayList<>(); //새로 url추가생성하여 set해줄 dto list
            List<ArticleOrderFileDTO> orgFileDTOList = articleOrderDTO.getArticleOrderFile(); //목록조회된 기사의뢰 List에서 기사의뢰파일 DTO 리스트 get

            for (int i = 0; i < orgFileDTOList.size(); i++){

                ArticleOrderFileDTO fileDTO = orgFileDTOList.get(i);

                String fileLoc = fileDTO.getFile().getFileLoc();
                String url = fileUrl + fileLoc; //url + 파일로그
                fileDTO.setUrl(url);

                setFileDTOList.add(fileDTO); // newDTO list에 추가.
            }

            articleOrderDTO.setArticleOrderFile(setFileDTOList); //의뢰DTO 파일에DTO리스트에 url추가된 DTO 리스트 set
            returnDtoList.add(articleOrderDTO); // 리턴 DTO 리스트에 add
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

            newArticleOrderFileDTO.add(fileDTO);// new FileDTO List에 url추가 dto 추가
        }
        articleOrderDTO.setArticleOrderFile(newArticleOrderFileDTO); // 조회된 의뢰에 url추가

        return articleOrderDTO;
    }

    public Long create(ArticleOrderCreateDTO articleOrderCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        articleOrderCreateDTO.setInputrId(userId);

        ArticleOrder articleOrder = articleOrderCreateMapper.toEntity(articleOrderCreateDTO);

        articleOrderRepository.save(articleOrder);

        return articleOrder.getOrderId();
    }

    public void update(ArticleOrderUpdateDTO articleOrderUpdateDTO, Long orderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        articleOrderUpdateDTO.setUpdtrId(userId);

        articleOrderUpdateMapper.updateFromDto(articleOrderUpdateDTO, articleOrder);

        articleOrderRepository.save(articleOrder);

    }

    private ArticleOrder articleOrderFindOrFail(Long orderId){

        return articleOrderRepository.findById(orderId)
                .orElseThrow( () -> new ResourceNotFoundException("ArticleOrderId not found. ArticleOrderId : " + orderId));

    }

    //목록조회 조건 build
    public BooleanBuilder getSearch(Date sdate, Date edate,
                                    String order_div_cd, String order_status, String workr_id, Long artclId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleOrder qArticleOrder = QArticleOrder.articleOrder;

        //입력날짜로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticleOrder.inputDtm.between(sdate, edate));
        }
        //의뢰구분코드로 조회
        if (StringUtils.isEmpty(order_div_cd) == false) {
            booleanBuilder.and(qArticleOrder.orderDivCd.eq(order_div_cd));
        }
        //의뢰 상태로 조회
        if (StringUtils.isEmpty(order_status) == false) {
            booleanBuilder.and(qArticleOrder.orderStatus.eq(order_status));
        }
        //작업자 아이디로 조회
        if (StringUtils.isEmpty(workr_id) == false) {
            booleanBuilder.and(qArticleOrder.workrId.eq(workr_id));
        }
        //작업자 아이디로 조회
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qArticleOrder.article.artclId.eq(artclId));
        }

        return booleanBuilder;
    }
}
