package com.saber.spring_camel_cxf_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class DeletePersonDto {
    private Integer code;
    private String text;
}
