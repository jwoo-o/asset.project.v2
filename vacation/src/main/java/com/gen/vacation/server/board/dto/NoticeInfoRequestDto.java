package com.gen.vacation.server.board.dto;

import com.gen.vacation.global.enums.BoardEnum;
import com.gen.vacation.global.domain.entity.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 2:29
 */
@Getter
@Setter
@ToString
public class NoticeInfoRequestDto {

    @NotBlank(message = "")
    private String subject;
    private String content;
    private String adminId;
    private String ip;
    private Long[] fileIds;


    public Board toEntity() {

        return Board.builder()
                .adminId(adminId)
                .category(BoardEnum.NOTICE)
                .content(content)
                .count(0)
                .ip(ip)
                .subject(subject).build();
    }
}
