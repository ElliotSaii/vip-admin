package com.techguy.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email{
    private String from;
    private String to;
    private String subject;
    private String template;
    private Map<String,Object> properties;

}
