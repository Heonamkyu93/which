package com.using.you.are.version.spring.which.oAuth2Object;

import java.sql.Date;
import java.util.Map;

public class NaverUserInfo implements Oauth2UserInfo {


    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "Naver";
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
        String Nickname=(attributes.get("nickname")!=null)?(String) attributes.get("nickname"):"Guest_Naver";
        return Nickname;
    }


    public Date getBirth() {
        String year = (attributes.get("birthyear") != null) ? (String) attributes.get("birthyear") : "1900";
        String birthday = (attributes.get("birthday") != null) ? (String) attributes.get("birthday") : "01-01";
        String birtthYearDay =year+"-"+birthday;
        Date bDate = Date.valueOf(birtthYearDay);
        return bDate;
    }
}
