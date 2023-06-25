package com.manofsteel.gd.type.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiKeyRequestDto {
    private String apiKey;
    private Long userId;
}
