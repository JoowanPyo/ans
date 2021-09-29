package com.gemiso.zodiac.app.ArticleOrder;

import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.app.ArticleOrder.mapper.ArticleOrderCreateMapper;
import com.gemiso.zodiac.app.ArticleOrder.mapper.ArticleOrderMapper;
import com.gemiso.zodiac.app.ArticleOrder.mapper.ArticleOrderUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
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

        String userId = userAuthService.authUser.getUserId();
        articleOrderCreateDTO.setInputrId(userId);

        ArticleOrder articleOrder = articleOrderCreateMapper.toEntity(articleOrderCreateDTO);

        articleOrderRepository.save(articleOrder);

        return articleOrder.getOrderId();
    }

    public void update(ArticleOrderUpdateDTO articleOrderUpdateDTO, Long orderId){

        ArticleOrder articleOrder = articleOrderFindOrFail(orderId);

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
