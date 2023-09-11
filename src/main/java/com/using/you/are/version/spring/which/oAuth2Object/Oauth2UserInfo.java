package com.using.you.are.version.spring.which.oAuth2Object;

import java.sql.Date;

public interface Oauth2UserInfo {
    String getProviderId();
    String getProvider();
    String getMail();
    String getName();
    String getNickname();
    Date getBirth();
}
