package com.using.you.are.version.spring.which.oAuth2Object;

import java.sql.Date;
import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo {


     private Map<String,Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getMail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getNickname() {
        return "Guest_Google";
    }

    @Override
    public Date getBirth() {
        String birth = "1900-01-01";
        Date bir = Date.valueOf(birth);
        return bir;
    }
}
