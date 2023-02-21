package com.techguy.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techguy.entity.product.Product;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class SubProductVo {

    private Long id;
    private Long productId;
    private String productName;
    private String name;
    private Integer buyStatus;
    private BigDecimal unitPrice;
    private String unit;
    private Boolean free ;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String imageUrl;
    private String fromImgUrl;
    private String description;
}
