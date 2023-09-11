package com.using.you.are.version.spring.which.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class MemberInfoDto {


    private Long memberId;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String memberEmail; // member_email
    @Size(min = 10, max = 35, message = "Password length must be between 10 and 35 characters")
    @NotBlank(message = "Password is required")
    private String memberPassword; // member_password
    @NotBlank(message = "Name is required")
    private String memberName; // member_name
    @NotBlank(message = "Nickname is required")
    @NotNull
    @NotEmpty
    private String memberNickname; // member_nickname

    private String memberGender; // member_gender
    private String memberAvailable; // member_available
    private String memberRole; // member_role
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date memberBirth; // member_birth
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date memberLastLogin; // member_last_login
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date memberUpdatePassword; // member_update_password
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date memberJoinDate; // member_join_date


    private String memberEventAgree;
    private String memberPrivateAgree;
}
