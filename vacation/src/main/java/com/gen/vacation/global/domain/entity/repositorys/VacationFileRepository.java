package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.VacationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 1:41
 */
public interface VacationFileRepository extends JpaRepository<VacationFile, Long> {

    @Modifying
    @Query(value = "UPDATE tb_vacation_file a SET a.vacationId = :vacationId WHERE a.id in :id")
    void updateVacationFileVacationIdById(Long vacationId, Collection<Long> id);


    List<VacationFile> findAllByVacationId(Long vacationId);
}
