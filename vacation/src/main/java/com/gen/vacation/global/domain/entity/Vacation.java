package com.gen.vacation.global.domain.entity;

import com.gen.vacation.global.Enum.ApprovalEnum;
import com.gen.vacation.global.Enum.VacationKind;
import com.gen.vacation.global.domain.common.BaseTimeEntity;
import com.gen.vacation.server.vacation.dto.VacationListRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "tb_vacation")
@Table(name = "tb_vacation")
@ToString
public class Vacation extends BaseTimeEntity {

    @Id
    @Column(name = "vacation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "start_day")
    private LocalDate startDay;

    @Column(name = "end_day")
    private LocalDate endDay;

    @Column(name = "vacation_kind")
    @Enumerated(EnumType.STRING)
    private VacationKind vacationKind;

    @Column(name = "vacation_reason")
    private String vacationReason;

    @Column(name = "vacation_tel")
    private String vacationTel;

    @Column(name = "takeover")
    private String takeOver;

    @Column(name = "approve_state")
    @Enumerated(EnumType.STRING)
    private ApprovalEnum approveState;

    @Column(name = "vacation_type")
    private String vacationType;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "count_day")
    private String countDay;

    @Column(name = "order_position")
    private int orderPosition;

    @Column(name = "reject_reason")
    private String rejectReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "vacation")
    private List<VacationFile> vacationFiles = new ArrayList<>();

    @Builder
    public Vacation(Long vacationId, String userId, String userName, LocalDate startDay, LocalDate endDay, VacationKind vacationKind, String vacationReason, String vacationTel, String takeOver, ApprovalEnum approveState, String vacationType, String orgCode, String countDay, int orderPosition) {
        this.vacationId = vacationId;
        this.userId = userId;
        this.userName = userName;
        this.startDay = startDay;
        this.endDay = endDay;
        this.vacationKind = vacationKind;
        this.vacationReason = vacationReason;
        this.vacationTel = vacationTel;
        this.takeOver = takeOver;
        this.approveState = approveState;
        this.vacationType = vacationType;
        this.orgCode = orgCode;
        this.countDay = countDay;
        this.orderPosition = orderPosition;
    }

    public void update(ApprovalEnum approveState,int orderPosition, String rejectReason) {

        this.orderPosition = orderPosition;
        this.approveState = approveState;
        this.rejectReason = rejectReason;
    }

    public void update(ApprovalEnum approveState) {

        this.approveState = approveState;
    }

    public void update(VacationListRequestDto dto) {

        this.startDay = dto.getStartDay().toLocalDateTime().toLocalDate();
        this.endDay = dto.getEndDay().toLocalDateTime().toLocalDate();
        this.vacationKind = VacationKind.valueOf(dto.getVacationKind());
        this.vacationReason = dto.getVacationReason();
        this.vacationTel = dto.getVacationTel();
        this.takeOver = dto.getTakeOver();
        this.vacationType = dto.getVacationType();
        this.countDay = dto.getCountDay();

    }

    public void updateState(ApprovalEnum cancel) {
        this.approveState = cancel;
    }

}
