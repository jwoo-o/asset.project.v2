package com.gen.vacation.server.cmmn.service;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.CommonCodeDetail;
import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import com.gen.vacation.global.domain.entity.id.CommonCodeDetailId;
import com.gen.vacation.global.domain.entity.repositorys.CommonCodeDetailRepository;
import com.gen.vacation.global.domain.entity.repositorys.CommonCodeGroupRepository;
import com.gen.vacation.server.cmmn.dto.CommonCodeRequestDto;
import com.gen.vacation.server.cmmn.dto.CommonDetailCodeRequestDto;
import com.gen.vacation.server.cmmn.dto.CommonGroupCodeResponseDto;
import com.gen.vacation.server.cmmn.repository.CommonCodeGroupRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-02
 * Time: 오후 6:48
 */
@Validated
@RequiredArgsConstructor
@Service
public class CommonCodeService {

    private final CommonCodeGroupRepository commonCodeGroupRepository;

    private final CommonCodeGroupRepositorySupport commonCodeGroupRepositorySupport;

    private final CommonCodeDetailRepository commonCodeDetailRepository;


    public Map<String, Object> selCommonCodeFind(String query) throws Exception {

        Map<String, Object> result = new HashMap<>();
        List<CommonCodeGroup> list;

        if (query.equals("all")) {
            list = commonCodeGroupRepository.findAll();
        } else {
            list = commonCodeGroupRepository.findAllByGroupCode(query);
        }

        if (list != null) {

            for (CommonCodeGroup group : list) {
                List<CommonCodeDetail> codeDetails = group.getCommonCodeDetails();
                List<Map<String, Object>> details = new ArrayList<>();
                if (codeDetails != null) {
                    for (CommonCodeDetail detail : codeDetails) {
                        Map<String, Object> subData = new HashMap<>();
                        subData.put("detailCode", detail.getId().getDetailCode());
                        subData.put("detailDesc", detail.getDetailDesc());
                        subData.put("detailName", detail.getDetailName());

                        details.add(subData);
                    }
                    result.put(group.getGroupCode(), details);
                }
            }

        }
        return result;
    }

    public Map<String, Object> selCommonGroupCode(@Valid SearchRequestDto dto) throws Exception {

        return commonCodeGroupRepositorySupport.findAllBySearch(dto);

    }

    public CommonGroupCodeResponseDto selCommonDetailCode(String groupCode) throws Exception {

        CommonCodeGroup group = commonCodeGroupRepository.findByGroupCode(groupCode).orElseThrow(IllegalArgumentException::new);

        return new CommonGroupCodeResponseDto(group);
    }

    public void insCommonGroupCode(@Valid CommonCodeRequestDto dto) throws Exception {


        commonCodeGroupRepository.save(dto.toEntity());


    }

    public void updCommonGroupCode(@Valid CommonCodeRequestDto dto) throws Exception {

        CommonCodeGroup group = commonCodeGroupRepository.findByGroupCode(dto.getGroupCode()).orElseThrow(IllegalArgumentException::new);

        group.update(dto);
        List<String> detailCodes = new ArrayList<>();
        for (int i = 0; i < dto.getDetailCodes().size(); i++) {
            CommonDetailCodeRequestDto requestDto = dto.getDetailCodes().get(i);

            /** 최종 삭제 대상 코드 검색*/

            requestDto.setGroupCode(dto.getGroupCode());
            requestDto.setOrder(i);
            CommonCodeDetailId id = new CommonCodeDetailId(requestDto.getDetailCode(), requestDto.getGroupCode());
            detailCodes.add(requestDto.getDetailCode());
            CommonCodeDetail detail = commonCodeDetailRepository.findById(id).orElse(null);
            if (detail == null) {
                commonCodeDetailRepository.save(requestDto.toEntity());
            } else {
                detail.update(requestDto);
            }
        }
        commonCodeDetailRepository.deleteAllByNotInDetailCodesAndGroupCode(detailCodes, dto.getGroupCode());

    }


    public boolean selCommonGroupCodeCheck(String groupCode) throws Exception {

        return commonCodeGroupRepository.countByGroupCode(groupCode) > 0;
    }
}
