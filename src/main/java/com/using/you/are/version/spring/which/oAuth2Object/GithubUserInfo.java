package com.using.you.are.version.spring.which.oAuth2Object;

import java.sql.Date;
import java.util.Map;

public class GithubUserInfo implements Oauth2UserInfo {


     private Map<String,Object> attributes;

    public GithubUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getMail() {
        String email = (attributes.get("email")!=null)? (String)attributes.get("email"):(String)attributes.get("login")+"_github";
        return email;
    }

    @Override
    public String getName() {
        String name = (attributes.get("name")!=null)? (String)attributes.get("name"):(String) attributes.get("login");
        return name;
    }

    @Override
    public String getNickname() {
        return "Guest_Github";
    }

    @Override
    public Date getBirth() {
        String birth = "1900-01-01";
        Date bir = Date.valueOf(birth);
        return bir;
    }
}
