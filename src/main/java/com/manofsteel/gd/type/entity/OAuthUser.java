package com.manofsteel.gd.type.entity;

import com.manofsteel.gd.type.etc.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OAuthUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oauthuser_user_id")
	private Long userId;

	@Column(
		unique = true,
		nullable = false,
		length = 100
	)
	private String providerUserId;

	@Column(
		unique = true,
		nullable = false,
		length = 100
	)
	private String email;

	@Column(
		nullable = false
	)
	private String name;

	private String picture;

	@Enumerated(EnumType.STRING)
	@Column
	private OAuthProvider oap;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@Builder
	public OAuthUser(Long userId, String providerUserId, String email, String name,
                     String picture, OAuthProvider oap, User user) {
		this.userId = userId;
		this.providerUserId = providerUserId;
		this.email = email;
		this.name = name;
		this.picture = picture;
		this.oap = oap;
		this.user = user;
	}

}
