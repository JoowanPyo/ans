package com.gemiso.zodiac.core.mapper;

import org.mapstruct.*;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * https://mapstruct.org/documentation
 * @param <D>
 * @param <E>
 */
public interface GenericMapper<D, E, UD> {

    D toDto(E e);
    E toEntity(D d);
    List<D> toDtoList(List<E> listE);
    List<E> toEntityList(List<D> listD);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UD updateDto, @MappingTarget E entity);
}