package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.OrganizationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-04
 * Time: 오후 2:16
 */
public interface OrganizationLevelRepository extends JpaRepository<OrganizationLevel, String> {

    @Modifying
    @Query(value = "DELETE FROM tb_organization_level l WHERE l.orgCode in :orgCodes")
    void deleteByOrgCodeIn(@Param("orgCodes") Collection<String> orgCodes);
}
