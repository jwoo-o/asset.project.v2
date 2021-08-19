package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.Approver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApproverRepository extends JpaRepository<Approver, Long> {


    Approver findAllByVacationIdAndDivisionAndOrder(Long vacationId, String approver, int order);


    @Modifying
    @Query(value = "UPDATE tb_approver a SET a.approvalFlag = 'Y' WHERE a.id = :id")
    void updateApprovalFlagById(Long id);


    int countByVacationIdAndDivision(Long vacationId, String approver);

    int countByUserId(String userId);


    @Modifying
    @Query(value = "delete from tb_approver a  WHERE a.vacationId = :vacationId")
    void deleteByVacationId(Long vacationId);


    Optional<Approver> findByVacationIdAndUserId(Long vacationId, String userId);

    List<Approver> findAllByVacationIdAndDivisionOrderByOrderAsc(Long vacationId, String division);
}
