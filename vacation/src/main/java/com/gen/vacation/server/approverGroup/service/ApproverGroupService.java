package com.gen.vacation.server.approverGroup.service;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.ApproverDetail;
import com.gen.vacation.global.domain.entity.ApproverGroup;
import com.gen.vacation.global.domain.entity.repositorys.ApproverDetailRepository;
import com.gen.vacation.global.domain.entity.repositorys.ApproverGroupRepository;
import com.gen.vacation.global.domain.entity.repositorys.OrganizationRepository;
import com.gen.vacation.server.approverGroup.dto.ApproverDetailRequestDto;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupRequestDto;
import com.gen.vacation.server.approverGroup.dto.ApproverGroupResponseDto;
import com.gen.vacation.server.approverGroup.repository.ApproverDetailRepositorySupport;
import com.gen.vacation.server.approverGroup.repository.ApproverGroupRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Validated
@RequiredArgsConstructor
@Service
public class ApproverGroupService {

    private final ApproverGroupRepository approverGroupRepository;

    private final ApproverGroupRepositorySupport approverGroupRepositorySupport;

    private final ApproverDetailRepository approverDetailRepository;

    private final ApproverDetailRepositorySupport approverDetailRepositorySupport;

    private final OrganizationRepository organizationRepository;


    public Map<String, Object> selApproverGroup(SearchRequestDto dto) throws Exception {

        return approverGroupRepositorySupport.findAllBySearch(dto);

    }

    public ApproverGroupResponseDto selApproverDetail(Long approverGroupCode) throws Exception {

        ApproverGroup group = approverGroupRepository.findByApproverGroupCode(approverGroupCode).orElseThrow(() -> new IllegalArgumentException());

        return new ApproverGroupResponseDto(group);
    }

    public ApproverGroupResponseDto selApproverDetailByOrgCode(String orgCode, String jobCode) throws Exception {

        ApproverGroup group = approverGroupRepository.findByOrgCodeAndJobCodeAndUseYn(orgCode, jobCode,true).orElseThrow(() -> new IllegalArgumentException());

        return new ApproverGroupResponseDto(group);
    }


    public void insApproverGroup(@Valid ApproverGroupRequestDto dto) throws Exception {

        ApproverGroup group = approverGroupRepository.save(dto.toEntity());
        int approverOrder = 0;
        for (int i = 0; i < dto.getDetailCodes().size(); i++) {
            ApproverDetailRequestDto requestDto = dto.getDetailCodes().get(i);
            requestDto.setApproverGroupCode(group.getApproverGroupCode());
            if (dto.getDetailCodes().get(i).getDivision().equals("approver")) {
                requestDto.setOrder(approverOrder);
                approverOrder++;
            } else {
                requestDto.setOrder(99 + i);
            }


            approverDetailRepository.save(requestDto.toEntity());

        }

    }


    public void updApproverGroup(@Valid ApproverGroupRequestDto dto) throws Exception {
        ApproverGroup group = approverGroupRepository.findByOrgCodeAndJobCode(dto.getOrgCode(), dto.getJobCode()).orElseThrow(() -> new IllegalArgumentException());

        group.update(dto);

        List<Long> detailCodes = new ArrayList<>();
        int approverOrder = 0;

        for (int i = 0; i < dto.getDetailCodes().size(); i++) {
            ApproverDetailRequestDto requestDetailDto = dto.getDetailCodes().get(i);
            if (requestDetailDto.getApproverDetailCode() != null) {
                detailCodes.add(requestDetailDto.getApproverDetailCode());
            }
        }

        if (!detailCodes.isEmpty()) {
            approverDetailRepository.deleteAllByNotInApproverDetailCodeAndApproverGroupCode(detailCodes, group.getApproverGroupCode());
        } else {
            approverDetailRepository.deleteAllByApproverGroupCode(group.getApproverGroupCode());
        }

        for (int i = 0; i < dto.getDetailCodes().size(); i++) {
            ApproverDetailRequestDto requestDto = dto.getDetailCodes().get(i);

            requestDto.setApproverGroupCode(group.getApproverGroupCode());
            if (dto.getDetailCodes().get(i).getDivision().equals("approver")) {
                requestDto.setOrder(approverOrder);
                approverOrder++;
            } else {
                requestDto.setOrder(99 + i);
            }

            ApproverDetail detail = approverDetailRepository.findByApproverDetailCodeAndApproverGroupCode(requestDto.getApproverDetailCode(), requestDto.getApproverGroupCode()).orElse(null);
            if (detail == null) {
                approverDetailRepository.save(requestDto.toEntity());
            } else {
                detail.update(requestDto);
            }
        }


    }

    public boolean selApproverGroupCheck(String orgCode, String jobCode) throws Exception {

        return approverGroupRepository.countByOrgCodeAndJobCode(orgCode, jobCode) > 0;
    }

    public boolean selApproverCheck(String userId) throws Exception {

        return approverDetailRepository.countByUserId(userId) > 0;
    }

    public void delApproverGroup(Long id) {

        ApproverGroup approverGroup = approverGroupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        approverGroup.delete(false);

    }
}
