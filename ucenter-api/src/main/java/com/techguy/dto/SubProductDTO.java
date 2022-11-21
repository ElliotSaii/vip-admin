package com.techguy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techguy.entity.product.ProductRecord;
import com.techguy.entity.product.SubProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class SubProductDTO {

    private Long id;
    private Long memberId;
    private Long productId;
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
    @Column(columnDefinition = "MEDIUMTEXT")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    private ProductRecord productRecord;
//    private SubProduct subProduct;
}
