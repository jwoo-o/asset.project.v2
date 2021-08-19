package com.gen.vacation.server.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 3:46
 */
@Getter
@Setter
@AllArgsConstructor
public class BoardListResponseDto {

    private Long id;
    private String subject;
    private String writer;
    private Long isAttach;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int count;


}
