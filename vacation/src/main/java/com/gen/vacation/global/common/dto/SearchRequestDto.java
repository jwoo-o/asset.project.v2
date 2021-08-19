package com.gen.vacation.global.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 2:27
 */
@Getter
@Setter
@ToString
public class SearchRequestDto {

    @NotNull(message = "")
    private Long limit;
    @NotNull(message = "")
    private Long page;

    private Long offset;

    private String orgCode;

    private String searchType;

    private String searchWord;

    private int order;

    private List<String> codes;

    private String userId;

    private String sort;

    private String seq;

    public Long getOffset() {

        return (page - 1) * limit;
    }

    public Long getTotal() {
        return limit * 10;
    }

    public Long getExcelOffset() {

        return (offset - 1) * limit;
    }


}
