package com.using.you.are.version.spring.which.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "board_file")
@Getter
@Setter
public class BoardFileEntity {
    private String fileOriginalName;
    @Id
    private String fileServerName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;
}
