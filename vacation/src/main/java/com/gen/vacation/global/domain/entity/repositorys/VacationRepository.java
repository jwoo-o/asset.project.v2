package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.enums.ApprovalEnum;
import com.gen.vacation.global.domain.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long> {

    @Modifying
    @Query(value = "UPDATE tb_vacation a SET a.approveState = :approveState, a.orderPosition = :orderPosition WHERE a.vacationId = :id")
    void updateApproveStateAndOrderPositionById(Long id, ApprovalEnum approveState, int orderPosition);

    @Modifying
    @Query(value = "UPDATE tb_vacation a SET a.approveState = :approveState WHERE a.vacationId = :id")
    void updateApproveStateById(Long id, ApprovalEnum approveState);

    Optional<Vacation> findByVacationId(Long vacationId);

    int countByUserIdAndStartDayBetween(String userId, LocalDate startDay, LocalDate endDay);
}
