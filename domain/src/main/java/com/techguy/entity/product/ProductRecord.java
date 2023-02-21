package com.techguy.entity.product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private Long productId;

    private Long subProductId;
    private String productName;

    private String name;
    private BigDecimal buyAmount;
    private Integer buyStatus;
    private BigDecimal totalUnitPrice;
    private String unit;
    private Boolean free =false;
    private Integer productType;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String imageUrl;
    private BigDecimal unitPrice;
    private String AdminImgUrl;
    private String fromImgUrl;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductRecord productRecord = (ProductRecord) o;
        return id != null && Objects.equals(id, productRecord.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
