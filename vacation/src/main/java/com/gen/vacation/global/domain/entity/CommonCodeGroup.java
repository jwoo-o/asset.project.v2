package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.global.domain.common.BooleanToYNConverter;
import com.gen.vacation.server.cmmn.dto.CommonCodeRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_code_group")
@Table(name = "tb_code_group")
public class CommonCodeGroup extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "group_code", length = 50)
    private String groupCode;

    @Column(name = "group_name", length = 100)
    private String groupName;

    @Column(name = "group_desc", length = 255)
    private String groupDesc;

    @Column(name = "use_yn", length = 2)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean useYn;

    @OneToMany(mappedBy = "commonCodeGroup")
    @OrderBy("order asc")
    private List<CommonCodeDetail> commonCodeDetails = new ArrayList<CommonCodeDetail>();

    @Builder
    public CommonCodeGroup(String groupCode, String groupName, String groupDesc, boolean useYn) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.useYn = useYn;
    }

    public void update(CommonCodeRequestDto dto) {

        this.groupName = dto.getGroupName();
        this.groupDesc = dto.getGroupDesc();
        this.useYn = dto.getUseYn();
    }

    @Override
    public String getId() {
        return groupCode;
    }

    @Override
    public boolean isNew() {
        return true;
    }


}
