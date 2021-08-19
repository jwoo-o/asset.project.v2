package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.CommonCodeDetail;
import com.gen.vacation.global.domain.entity.id.CommonCodeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오전 10:48
 */
public interface CommonCodeDetailRepository extends JpaRepository<CommonCodeDetail, CommonCodeDetailId> {


    @Modifying
    @Query(value = "DELETE FROM tb_code_detail c WHERE c.id.groupCode = :groupCode and c.id.detailCode not in :detailCodes")
    void deleteAllByNotInDetailCodesAndGroupCode(@Param("detailCodes") Collection<String> detailCodes, @Param("groupCode") String groupCode);

    @Query(value = "SELECT d from tb_code_detail d where d.id.groupCode = :groupCode order by d.order")
    List<CommonCodeDetail> findByGroupCode(@Param("groupCode") String groupCode);
}
