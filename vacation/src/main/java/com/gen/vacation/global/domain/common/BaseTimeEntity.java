package com.gen.vacation.global.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-24
 * Time: 오후 5:09
 */
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

    /**
     * 최초등록시간
     */
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 마지막수정시간
     */
    @Column(name = "modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
