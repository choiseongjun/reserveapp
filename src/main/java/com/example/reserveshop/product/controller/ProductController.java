package com.example.reserveshop.product.controller;


import com.example.reserveshop.common.dto.ApiResponse;
import com.example.reserveshop.product.dto.ProductRequest;
import com.example.reserveshop.product.entity.Product;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ProductController {
    @GetMapping("/products/{id}")
    @ResponseBody
    public ApiResponse<?> getById(@PathVariable("id") final long id) {
        Product product = Product.builder()
                .id(1L)
                .price(1000)
                .name("1번 product")
                .build();
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message"); // default
        log.warn("warn message");
        log.error("error message");
        if(product.getId()==id){
            return ApiResponse.createSuccess(product,"success");
        }else{
            return ApiResponse.createError("값이 다릅니다");
        }
    }
    @PostMapping("/products")
    public ApiResponse<?> postProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = Product.builder()
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .build();

        return ApiResponse.createSuccess(product,"success");
    }
}
