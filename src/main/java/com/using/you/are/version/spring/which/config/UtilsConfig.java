package com.using.you.are.version.spring.which.config;

import com.using.you.are.version.spring.which.transObject.BoardTransMapper;
import com.using.you.are.version.spring.which.transObject.BoardTransMapperImpl;
import com.using.you.are.version.spring.which.transObject.MemberTransMapper;
import com.using.you.are.version.spring.which.transObject.MemberTransMapperImpl;
import com.using.you.are.version.spring.which.utils.MainSend;
import com.using.you.are.version.spring.which.utils.MultipleFileUpload;
import com.using.you.are.version.spring.which.utils.UtilsPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
@RequiredArgsConstructor
public class UtilsConfig {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Bean
    public PasswordEncoder utilsPasswordEncoder(){
        return new UtilsPasswordEncoder();
    }



    @Bean
    public MainSend mainSend(){
        return new MainSend(mailSender,templateEngine);
    }


    @Bean
    public MultipleFileUpload multipleFileUpload(){
        return new MultipleFileUpload();
    }

}
