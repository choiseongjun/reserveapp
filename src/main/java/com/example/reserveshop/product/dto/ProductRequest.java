package com.example.reserveshop.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.reserveshop.product.entity.Product}
 */
@Value
public class ProductRequest implements Serializable {
    @NotBlank(message = "상품이름을 입력해주세요.")
    String name;
    @NotNull
    @Min(value=0,message = "최소 0원이상이어야합니다")
    Integer price;
}