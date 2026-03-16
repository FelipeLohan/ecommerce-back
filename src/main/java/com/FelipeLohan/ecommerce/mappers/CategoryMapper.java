package com.FelipeLohan.ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.FelipeLohan.ecommerce.dto.CategoryDTO;
import com.FelipeLohan.ecommerce.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category entity);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isFeatured", conditionExpression = "java(dto.getIsFeatured() != null)")
    void updateEntity(CategoryDTO dto, @MappingTarget Category entity);
}
