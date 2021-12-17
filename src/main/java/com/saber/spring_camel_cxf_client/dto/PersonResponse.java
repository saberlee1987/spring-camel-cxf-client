package com.saber.spring_camel_cxf_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonResponse {
    private List<PersonDto> persons ;
}
