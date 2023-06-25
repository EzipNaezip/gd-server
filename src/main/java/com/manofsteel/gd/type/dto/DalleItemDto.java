package com.manofsteel.gd.type.dto;


import com.manofsteel.gd.type.entity.DalleImageItem;
import lombok.*;

import java.util.List;

import static com.manofsteel.gd.type.entity.QDalleImageItem.dalleImageItem;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DalleItemDto {

    private Long itemId;

    private String url;

    private Long chatLogSerialNumber;

    public DalleItemDto (DalleImageItem dalleImageItem){
        this.itemId = dalleImageItem.getItemId();
        this.url = dalleImageItem.getUrl();
        this.chatLogSerialNumber = dalleImageItem.getSerialNum().getSerialNum();
    }


    public static List<DalleItemDto> listOf(List<DalleImageItem> response) {
        return response.stream().map(DalleItemDto::new).toList();
    }
}
