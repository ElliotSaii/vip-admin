package com.techguy.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class ProductDTO {

        private Long id;
        private Long memberId;
        private String name;
        private BigDecimal buyAmount;
        private Integer buyStatus;
        private BigDecimal totalUnitPrice;
        private String unit;
        private Boolean free;
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
        ProductRecord productRecord;

        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        Product product;

}
