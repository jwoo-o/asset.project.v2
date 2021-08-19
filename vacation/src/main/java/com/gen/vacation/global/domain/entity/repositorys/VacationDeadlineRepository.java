package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.VacationDeadline;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VacationDeadlineRepository extends JpaRepository<VacationDeadline, VacationDeadlineId> {

}
