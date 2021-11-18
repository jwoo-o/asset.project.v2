package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.domain.entity.CertificateRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CertificateRequestHistoryRepository extends JpaRepository<CertificateRequestHistory, Long> {


    int countByApprovalFlag(ApproverFlagEnum approverFlagEnum);
}
