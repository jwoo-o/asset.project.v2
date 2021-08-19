package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-26
 * Time: 오전 11:16
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_vacation_info")
@Table(name = "tb_vacation_info")
@ToString
public class VacationInfo extends BaseTimeEntity {


    @EmbeddedId
    private VacationInfoId id;
    @Column(name = "vacation_total_cnt")
    private String totalCnt;
    @Column(name = "vacation_use_cnt")
    private String useCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @Builder
    public VacationInfo(VacationInfoId id, String totalCnt, String useCnt) {

        this.id = id;
        this.totalCnt = totalCnt;
        this.useCnt = useCnt;
    }



    public void update(String useCnt) {
        this.useCnt = useCnt;
    }

    public void totalCntUpdate(String totalCnt) {
        this.totalCnt = totalCnt;
    }
}
