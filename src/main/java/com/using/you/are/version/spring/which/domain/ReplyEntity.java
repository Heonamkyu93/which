package com.using.you.are.version.spring.which.domain;

import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "board_reply")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replyNumber_generator")
    @SequenceGenerator(name = "replyNumber_generator", sequenceName = "reply_sequence", allocationSize = 1)
    private Long replyId;


    private String reply;
    @Temporal(TemporalType.TIMESTAMP)
    private Date replyCreatedDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date replyLastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberInfo memberInfo;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @PreUpdate
    protected void onCreate() {
        replyLastModifiedDate = new Date();
    }

}
