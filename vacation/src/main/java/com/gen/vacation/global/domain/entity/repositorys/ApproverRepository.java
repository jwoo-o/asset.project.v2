package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.enums.DivisionEnum;
import com.gen.vacation.global.domain.entity.Approver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApproverRepository extends JpaRepository<Approver, Long> {


    List<Approver> findAllByVacationIdAndDivision(Long vacationId, DivisionEnum approver);

    @Modifying
    @Query(value = "DELETE FROM tb_approver a  WHERE a.vacationId = :vacationId")
    void deleteByVacationId(Long vacationId);


    Optional<Approver> findByVacationIdAndUserId(Long vacationId, String userId);

    List<Approver> findAllByVacationIdAndDivisionOrderByOrderAsc(Long vacationId, DivisionEnum division);
}
