package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderCreateMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderMapper;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleOrderService {

    private final ArticleOrderRepository articleOrderRepository;

    private final ArticleOrderMapper articleOrderMapper;
    private final ArticleOrderCreateMapper articleOrderCreateMapper;
    private final ArticleOrderUpdateMapper articleOrderUpdateMapper;

    private final UserAuthService userAuthService;


    public ArticleOrderDTO find(Long oderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(oderId);

        ArticleOrderDTO articleOrderDTO = articleOrderMapper.toDto(articleOrder);

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
}
