package com.techguy.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class BankVo {
    private Long id;
    private String name;
    private String accountName;
    private String accountNo;
    private String branchName;
    private Integer bankVersion;
}
