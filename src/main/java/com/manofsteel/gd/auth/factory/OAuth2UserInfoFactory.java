package com.manofsteel.gd.auth.factory;

import com.manofsteel.gd.type.dto.GoogleOAuthUserInfo;
import com.manofsteel.gd.type.dto.OAuthUserInfo;
import com.manofsteel.gd.type.etc.OAuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
	public static OAuthUserInfo of(OAuthProvider oAuthProvider, Map<String, Object> attributes) throws
		IllegalArgumentException {
		switch (oAuthProvider) {
			case GOOGLE:
				return new GoogleOAuthUserInfo(attributes);
			default:
				throw new IllegalArgumentException("OAuthProvider Not Excepted!!");
		}
	}
}
