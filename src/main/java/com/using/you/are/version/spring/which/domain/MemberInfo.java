package com.using.you.are.version.spring.which.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Valid
@Getter
@Setter
@Table(name = "member_info")
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usernumber_generator")
    @SequenceGenerator(name = "usernumber_generator", sequenceName = "userseq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // member_id
    @Column(name = "member_email")
    @NotBlank(message = "Email is required")
    private String memberEmail; // member_email
    @Column(name = "member_password")
    private String memberPassword; // member_password
    @Column(name = "member_name")
    @NotBlank(message = "Name is required")
    private String memberName; // member_name
    @Column(name = "member_nickname")
    @NotBlank(message = "Nickname is required")
    private String memberNickname; // member_nickname
    @Column(name = "member_gender")
    private String memberGender; // member_gender
    @Column(name = "member_available")
    private String memberAvailable; // member_available
    @Column(name = "member_role")
    private String memberRole; // member_role
    @Column(name = "member_birth")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date memberBirth; // member_birth

    @Column(name = "member_last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date memberLastLogin; // member_last_login



    @Column(name = "member_update_password")
    @Temporal(TemporalType.TIMESTAMP)
    private Date memberUpdatePassword; // member_update_password
    @Column(name = "member_join_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date memberJoinDate; // member_join_date


    @OneToMany (mappedBy = "memberInfo")
    private List<BoardEntity> boardEntityList=new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        memberLastLogin = new Date();
    }

}
