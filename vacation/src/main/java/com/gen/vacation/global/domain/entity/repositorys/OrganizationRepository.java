package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.Organization;
import com.gen.vacation.server.organization.dto.OrganizationChartUpdateRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface OrganizationRepository extends JpaRepository<Organization, String> {

    int countByOrgCode(String orgCode);

    @Modifying
    @Query(value = "DELETE FROM tb_organization o WHERE o.orgCode in :orgCodes")
    void deleteByOrgCodeIn(@Param("orgCodes") Collection<String> orgCodes);

    Optional<Organization> findFirstByOrgCodeLikeOrderByOrgCodeDesc(String orgCode);

    List<Organization> findByOrgPaCode(String orgCode);

    List<Organization> findByOrgFullCodeLike(String orgCode);

    @Modifying
    @Query(value = "update tb_organization o set o.color = :#{#dto.color} WHERE o.orgFullCode like concat('%',:#{#dto.orgCode},'%')")
    void updateColorByFullCode(@Param("dto") OrganizationChartUpdateRequestDto dto);

    List<Organization> findByOrgFullCodeLikeOrderByOrderAsc(String s);
}
