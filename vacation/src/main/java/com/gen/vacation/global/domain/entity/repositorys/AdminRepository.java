package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 1:39
 */
public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByAdminIdAndUseYn(String adminId, boolean useYn);

    int countByAdminIdAndUseYn(String adminId, boolean useYn);

    @Query("select a.email from tb_admin a where a.useYn = :useYn and a.adminId <> :adminId")
    List<String> findByAdminIdNotAndUseYn(@Param("adminId") String adminId, @Param("useYn") boolean useYn);
}

