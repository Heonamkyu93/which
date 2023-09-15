package com.using.you.are.version.spring.which.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "member_board")
@Getter
@Setter
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boardNumber_generator")
   @SequenceGenerator(name = "boardNumber_generator", sequenceName = "board_sequence", allocationSize = 1)

 //   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;  // 게시글번호

   // private Long memberId; // 작성자  이게 회원이랑 연관관계를 맺어서 처리해야하는거지


    private String boardTitle; // 제목
    @Column(name = "board_content", columnDefinition = "NCLOB")
    private String boardContent; // 내용
    @Temporal(TemporalType.TIMESTAMP)
    private Date boardCreatedDate; // 작성일
    @Temporal(TemporalType.TIMESTAMP)
    private Date boardLastModifiedDate; // 최종수정일

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberInfo memberInfo;



    @OneToMany (mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardFileEntity> boardFileEntities = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private  List<ReplyEntity> replyEntities = new ArrayList<>();



    @PrePersist
    protected void onCreate() {
        boardLastModifiedDate = new Date();
        boardCreatedDate = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        boardLastModifiedDate = new Date();
    }

}
