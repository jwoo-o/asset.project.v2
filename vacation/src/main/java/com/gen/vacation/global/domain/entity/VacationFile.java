package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-26
 * Time: 오후 1:20
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_vacation_file")
public class VacationFile extends BaseEntity {

    /**
     * 파일이름
     */
    @Column
    private String fileName;

    /**
     * 업로드된  파일이름
     */
    @Column
    private String key;

    /**
     * 파일 사이즈
     */
    @Column
    private Long fileSize;

    @Column(name = "vacation_id")
    private Long vacationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id", updatable = false, insertable = false)
    private Vacation vacation;

    @Builder
    public VacationFile(String fileName, String key, Long fileSize) {
        this.fileName = fileName;
        this.key = key;
        this.fileSize = fileSize;
    }

    public void update(Long vacationId){
        this.vacationId = vacationId;
    }
}
