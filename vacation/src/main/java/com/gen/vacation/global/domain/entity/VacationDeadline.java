package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.Enum.LoginHistoryEnum;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-26
 * Time: 오후 5:26
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_vacation_deadline")
@Table(name = "tb_vacation_deadline")
public class VacationDeadline extends BaseTimeEntity {

    @EmbeddedId
    private VacationDeadlineId id;

    @Column(name = "deadlines",length = 2056)
    private String deadlines;

    @Builder
    public VacationDeadline(VacationDeadlineId id, String deadlines, LoginHistoryEnum writeType) {
        this.id = id;
        this.deadlines = deadlines;
    }

    public void update(String deadlines) {
        this.deadlines = deadlines;
    }
}
