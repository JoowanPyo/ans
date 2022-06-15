package com.gemiso.zodiac.app.tag;

import com.gemiso.zodiac.app.tag.dto.TagCreateDTO;
import com.gemiso.zodiac.app.tag.dto.TagDTO;
import com.gemiso.zodiac.app.tag.dto.TagUpdateDTO;
import com.gemiso.zodiac.app.tag.mapper.TagCreateMapper;
import com.gemiso.zodiac.app.tag.mapper.TagMapper;
import com.gemiso.zodiac.app.tag.mapper.TagUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    private final TagCreateMapper tagCreateMapper;
    private final TagMapper tagMapper;
    private final TagUpdateMapper tagUpdateMapper;



    public List<TagDTO> findAll(String tagName){

        BooleanBuilder booleanBuilder = getSearch(tagName);

        List<Tag> tagList = (List<Tag>) tagRepository.findAll(booleanBuilder);

        List<TagDTO> tagDTOList = tagMapper.toDtoList(tagList);

        return tagDTOList;
    }

    private BooleanBuilder getSearch(String tagNam) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QTag qTag = QTag.tag1;

        if(StringUtils.isEmpty(tagNam) == false){
            booleanBuilder.and(qTag.tag.contains(tagNam));
        }


        return booleanBuilder;
    }

    public TagDTO find(Long tagId){

        Tag tag = findTag(tagId);

        TagDTO tagDTO = tagMapper.toDto(tag);

        return tagDTO;
    }

    public Long create(TagCreateDTO tagCreateDTO){ //테그생성

        tagCreateDTO.setTagClicked(0); //0으로 초기화
        
        Tag tag = tagCreateMapper.toEntity(tagCreateDTO);

        tagRepository.save(tag);

        return tag.getTagId();
    }

    public Tag findTag(Long tagId){ //테그 아이디로 해당 태그 찾기.

        Optional<Tag> tag = tagRepository.findByTag(tagId);

        if (tag.isPresent() ==false){
            throw new ResourceNotFoundException("테그를 찾을 수 없습니다. 테그 아이디 :"+tagId);
        }

        return tag.get();
    }

    public void update(TagUpdateDTO tagUpdateDTO, Long tagId){ //테그 업데이트.

        Tag tag = findTag(tagId);

        tagUpdateMapper.updateFromDto(tagUpdateDTO, tag);

        tagRepository.save(tag);

    }

    public void delete(Long tagId){ // 테그 삭제

        Tag tag = findTag(tagId); // 아이디로 테그존재 확인.

        tagRepository.deleteById(tagId); //테크 삭제 (소프트 delete x)
    }

    public Boolean findTagName(String tag){

        Optional<Tag> findTag = tagRepository.findTagName(tag);

        if (findTag.isPresent()){
            return true;
        }

        return false;

    }
}
