package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-30
 * Time: 오전 9:03
 */

@NoArgsConstructor
@Getter
@Entity(name = "tb_seat")
@Table(name = "tb_seat")
public class Seat extends BaseEntity {

    private String className;

    private String left;

    private String top;
}
