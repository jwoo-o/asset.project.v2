package com.gen.vacation.global.domain.entity.id;

import com.gen.vacation.global.Enum.LoginHistoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-26
 * Time: 오전 11:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class VacationDeadlineId implements Serializable {

    @Column(name = "years")
    private String years;
    @Column(name = "user_id",length = 20)
    private String userId;
    /**
     * 등록자 분류(사용자,관리자)
     */
    @Column(name = "write_type")
    @Enumerated(EnumType.STRING)
    private LoginHistoryEnum writeType;



}
