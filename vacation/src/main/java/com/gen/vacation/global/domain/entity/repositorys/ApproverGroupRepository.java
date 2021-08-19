package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.ApproverGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApproverGroupRepository extends JpaRepository<ApproverGroup, Long> {
    Optional<ApproverGroup> findByApproverGroupCode(Long approverGroupCode);

    List<ApproverGroup> findAllByApproverGroupCode(String query);

    Optional<ApproverGroup> findByOrgCode(String orgCode);

    Optional<ApproverGroup> findByOrgCodeAndJobCodeAndUseYn(String orgCode, String jobCode,boolean useYn);

    int countByOrgCodeAndJobCode(String orgCode, String jobCode);

    Optional<ApproverGroup> findByOrgCodeAndJobCode(String orgCode, String jobCode);

    List<ApproverGroup> findByOrgCodeIn(List<String> orgCodes);

    @Modifying
    @Query(value = "DELETE from tb_approver_group g WHERE g.approverGroupCode in :groupCodes")
    void deleteByApproverGroupCodeIn(List<Long> groupCodes);


}
