package com.manofsteel.gd.type.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DalleImageRequest {
    private String prompt;
    private int n;
    private String size;



}
