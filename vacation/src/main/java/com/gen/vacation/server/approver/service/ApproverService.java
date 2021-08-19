package com.gen.vacation.server.approver.service;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Approver;
import com.gen.vacation.global.domain.entity.repositorys.ApproverRepository;
import com.gen.vacation.server.approver.dto.ApproverVacationListResponseDto;
import com.gen.vacation.server.approver.repository.ApproverRepositorySupport;
import com.gen.vacation.server.vacation.dto.VacationSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

@Validated
@RequiredArgsConstructor
@Service
public class ApproverService {

    private final ApproverRepository approverRepository;

    private final ApproverRepositorySupport approverRepositorySupport;

    public Map<String, Object> selApproverVacationList(@Valid VacationSearchDto dto) throws Exception {

        return approverRepositorySupport.findAllBySearch(dto);
    }

    public Boolean selApproverListAndOrderCheck(String userId, Long vacationId, int orderPosition) throws Exception {

        Approver approver = approverRepository.findByVacationIdAndUserId(vacationId, userId).orElseThrow(() -> new IllegalArgumentException());

        return orderPosition == approver.getOrder();
    }

    public Long selUserApprovalCountByUserId(String userId) throws Exception {

        return approverRepositorySupport.countByUserId(userId);

    }




}
