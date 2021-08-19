package com.gen.vacation.server.board.dto;

import com.gen.vacation.global.domain.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 5:16
 */
@Getter
@Setter
@NoArgsConstructor
public class NoticeInfoResponseDto {

    private Long id;
    private String subject;
    private String content;
    private int count;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Map<String, Object>> fileList;
    private String writer;

    public NoticeInfoResponseDto(Board board) {
        this.id = board.getId();
        this.subject = board.getSubject();
        this.content = board.getContent();
        this.count = board.getCount();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.writer = board.getAdmin().getName();
    }
}
