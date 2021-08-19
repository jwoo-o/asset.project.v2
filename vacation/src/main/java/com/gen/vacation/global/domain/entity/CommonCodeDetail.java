package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.entity.id.CommonCodeDetailId;
import com.gen.vacation.server.cmmn.dto.CommonDetailCodeRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_code_detail")
//@IdClass(CommonCodeDetailId.class)
public class CommonCodeDetail extends BaseTimeEntity implements Persistable<CommonCodeDetailId> {

    @EmbeddedId
    private CommonCodeDetailId id;

    @Column(name = "detail_name", nullable = false)
    private String detailName;

    @Column(name = "detail_desc", nullable = false)
    private String detailDesc;

    @Column(name = "[order]")
    private int order;

    @MapsId("group_code")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code", updatable = false, insertable = false)
    private CommonCodeGroup commonCodeGroup;

    @Builder
    public CommonCodeDetail(CommonCodeDetailId id, String detailName, int order, String detailDesc) {
        this.detailName = detailName;
        this.id = id;
        this.detailDesc = detailDesc;
        this.order = order;
    }

    public void update(CommonDetailCodeRequestDto requestDto) {

        this.detailName = requestDto.getDetailName();
        this.detailDesc = requestDto.getDetailDesc();
        this.order = requestDto.getOrder();
    }

    @Override
    public CommonCodeDetailId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }


}
