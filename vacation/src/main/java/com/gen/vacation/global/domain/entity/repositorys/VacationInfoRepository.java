package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.VacationInfo;
import com.gen.vacation.global.domain.entity.id.VacationInfoId;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface VacationInfoRepository extends JpaRepository<VacationInfo, VacationInfoId> {


    @Query(value = "select v.id.years from tb_vacation_info v group by v.id.years order by v.id.years desc")
    List<String> findGroupByYears();

    @Query(value = "select v.id.years from tb_vacation_info v where v.id.userId = :userId group by v.id.years order by v.id.years desc")
    List<String> findByUserIdGroupByYears(@Param("userId") String userId);

    @Query(value = "select v from tb_vacation_info v where v.id.userId = :userId AND v.id.years = :year")
    Optional<VacationInfo> findByUserIdAndYears(String userId, String year);

    @Modifying
    @Query(value = "UPDATE tb_vacation_info v SET v.useCnt = :vacationUseCnt WHERE v.id.userId = :userId AND v.id.years = :year")
    void updateVacationCntByUserIdAndYears(String userId, String year, String vacationUseCnt);
}
