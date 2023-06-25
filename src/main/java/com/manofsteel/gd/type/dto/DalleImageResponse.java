package com.manofsteel.gd.type.dto;


import com.manofsteel.gd.type.entity.DalleImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DalleImageResponse {
    private List<DalleImageItem> data;

}