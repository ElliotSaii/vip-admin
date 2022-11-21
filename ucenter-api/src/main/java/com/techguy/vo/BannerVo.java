package com.techguy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BannerVo {
    private Long id;
    private String imageUrl;

    private Date createTime;
}
