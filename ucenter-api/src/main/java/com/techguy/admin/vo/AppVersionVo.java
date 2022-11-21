package com.techguy.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class AppVersionVo {

    private Long id;
    private Integer platform;
    private String version;
    private String name;
    private String link;

    private String description;
    private Date createTime;
    private Date updateTime;
}
