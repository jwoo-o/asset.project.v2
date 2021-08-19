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
public class CommonCodeDetailId implements Serializable {

    @Column(name = "detail_code")
    private String detailCode;
    @Column(name = "group_code")
    private String groupCode;
}
