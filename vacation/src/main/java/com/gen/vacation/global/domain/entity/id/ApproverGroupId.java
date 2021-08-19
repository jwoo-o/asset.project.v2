package com.gen.vacation.global.domain.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 7:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ApproverGroupId implements Serializable {

    @Column(name = "org_code",length = 200)
    private String orgCode;
    @Column(name = "job_cd")
    private String jobCd;

}
