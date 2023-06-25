package com.manofsteel.gd.type.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Setter
public class UserInfoSet {

	@Id
	private Long userId;

}
