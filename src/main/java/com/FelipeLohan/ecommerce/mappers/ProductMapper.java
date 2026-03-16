package com.FelipeLohan.ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.FelipeLohan.ecommerce.dto.ProductDTO;
import com.FelipeLohan.ecommerce.dto.ProductMinDTO;
import com.FelipeLohan.ecommerce.entities.Product;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper {

    ProductDTO toDTO(Product entity);

    ProductMinDTO toMinDTO(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "isFeatured", conditionExpression = "java(dto.getIsFeatured() != null)")
    void updateEntity(ProductDTO dto, @MappingTarget Product entity);
}
