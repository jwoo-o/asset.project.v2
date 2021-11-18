package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.global.enums.LoginHistoryEnum;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    /**
     * 암호화 유무
     */
    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 2)
    private boolean confirm;

    @Builder
    public VacationDeadline(VacationDeadlineId id, String deadlines, LoginHistoryEnum writeType, boolean confirm) {
        this.id = id;
        this.deadlines = deadlines;
        this.confirm = confirm;
    }

    public void update(String deadlines) {
        this.deadlines = deadlines;
        this.confirm = true;
    }

    public void updateConfirm() {
        this.confirm = true;
    }
}
