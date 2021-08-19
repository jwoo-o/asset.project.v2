package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.server.user.dto.UserInfoResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ApproverDetailRepository extends JpaRepository<ApproverDetail, Long> {

    @Modifying
    @Query(value = "DELETE FROM tb_approver_detail c WHERE c.approverGroupCode = :approverGroupCode and c.approverDetailCode not in :detailCodes")
    void deleteAllByNotInApproverDetailCodeAndApproverGroupCode(@Param("detailCodes") Collection<Long> detailCodes, @Param("approverGroupCode") Long approverGroupCode);

    Optional<ApproverDetail> findByApproverDetailCodeAndApproverGroupCode(Long approverDetailCode, Long approverGroupCode);

    @Modifying
    @Query(value = "DELETE FROM tb_approver_detail c WHERE c.approverGroupCode = :approverGroupCode")
    void deleteAllByApproverGroupCode(Long approverGroupCode);

    int countByUserId(String userId);

    @Modifying
        @Query(value = "UPDATE tb_approver_detail c SET c.userName = :#{#dto.name} " +
            ",c.orgCode = :#{#dto.orgCode}, c.orgName = :#{#dto.orgName}, c.rankCd = :#{#dto.rankCd}, c.rankNm = :#{#dto.rankNm}" +
            ",c.jobCode = :#{#dto.jobCd}, c.jobName = :#{#dto.jobNm} WHERE c.userId = :#{#dto.userId}")
    void updateUserInfo(@Param("dto") UserInfoResponseDto dto);

    @Modifying
    @Query(value = "DELETE FROM tb_approver_detail c WHERE c.userId = :userId")
    void deleteByUserId(String userId);

    @Modifying
    @Query(value = "UPDATE tb_approver_detail c SET c.orgName =:orgName where c.orgCode =:orgCode")
    void updateOrgNameByOrgCode(@Param("orgCode") String orgCode, @Param("orgName") String orgName);

    @Modifying
    @Query(value = "DELETE from tb_approver_detail d WHERE d.orgCode in :orgCodes")
    void deleteByOrgCodeIn(@Param("orgCodes") List<String> orgCodes);

    @Modifying
    @Query(value = "DELETE from tb_approver_detail d WHERE d.approverGroupCode in :groupCodes")
    void deleteByApproverGroupCodeIn(List<Long> groupCodes);
}
