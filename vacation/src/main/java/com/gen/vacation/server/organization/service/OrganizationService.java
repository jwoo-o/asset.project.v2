package com.gen.vacation.server.organization.service;

import com.gen.vacation.global.domain.entity.*;
import com.gen.vacation.global.domain.entity.repositorys.*;
import com.gen.vacation.global.exception.AlreadyException;
import com.gen.vacation.global.exception.NotDeleteException;
import com.gen.vacation.global.util.JsonUtil;
import com.gen.vacation.server.organization.dto.OrganizationChartDto;
import com.gen.vacation.server.organization.dto.OrganizationChartRequestDto;
import com.gen.vacation.server.organization.dto.OrganizationChartUpdateRequestDto;
import com.gen.vacation.server.organization.repository.OrganizationLevelRepositorySupport;
import com.gen.vacation.server.organization.repository.OrganizationRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-04
 * Time: 오후 1:06
 */
@Validated
@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationRepositorySupport organizationRepositorySupport;

    private final OrganizationLevelRepository organizationLevelRepository;

    private final OrganizationLevelRepositorySupport organizationLevelRepositorySupport;

    private final UserRepository userRepository;

    private final ApproverDetailRepository approverDetailRepository;

    private final ApproverGroupRepository approverGroupRepository;

    private final AssetRepository assetRepository;



    public List<Map<String, Object>> selOrganizationChartByLevel(String orgCode, String type) throws Exception {
        List<Map<String, Object>> result;

        Organization organization = organizationRepository.findById(orgCode).orElseThrow(() -> new IllegalArgumentException());

        List<OrganizationChartDto> data = organizationRepositorySupport.findAllByOrgCode(organization.getOrgCode());


        if (type.equals("self")) {
            /** 검색부터 생성*/
            result = JsonUtil.convertorTreeMap(data, organization.getOrgPaCode(), "orgCode", "orgPaCode");
        } else {
            /** 하위부서 생성*/
            result = JsonUtil.convertorTreeMap(data, organization.getOrgCode(), "orgCode", "orgPaCode");
        }

        String tree = JsonUtil.dtoToString(result);
        tree = tree.replace(",\"children\":[]", "");

        return ((List<Map<String, Object>>) JsonUtil.stringToJson(tree));
    }

    public void insOrganizationChart(@Valid OrganizationChartRequestDto dto) throws Exception {

        int count = organizationRepository.countByOrgCode(dto.getOrgCode());
        if (count > 0) {
            throw new AlreadyException();
        } else {
            organizationRepository.save(dto.toEntity());
            organizationLevelRepository.save(dto.levelToEntity());
        }
    }

    public boolean selOrganizationCheckOrgCode(String orgCode) throws Exception {

        return organizationRepository.countByOrgCode(orgCode) > 0;
    }

    /** 부서삭제 시 */
    public void delOrganizationChart(String orgCode) throws Exception {

        Organization organization = organizationRepository.findById(orgCode).orElseThrow(() -> new IllegalArgumentException());

        List<String> orgCodes = organizationRepositorySupport.findOrgCodeAllByOrgCode(orgCode, organization.getOrder());

        List<User> users = userRepository.findByOrgCodeIn(orgCodes);

        List<Asset> assets = assetRepository.findByOrgCodeIn(orgCodes);
        if(users.isEmpty() && assets.isEmpty()) {
            List<ApproverGroup> groups = approverGroupRepository.findByOrgCodeIn(orgCodes);

            if(!groups.isEmpty()) {
                List<Long> ids = groups.stream().map(ApproverGroup::getApproverGroupCode).collect(Collectors.toList());
                /** 결제라인 세부 삭제 */
                approverDetailRepository.deleteByApproverGroupCodeIn(ids);
                /** 결제라인 삭제*/
                approverGroupRepository.deleteByApproverGroupCodeIn(ids);
            }
            /** 부서단계 삭제*/
            organizationLevelRepository.deleteByOrgCodeIn(orgCodes);

            /** 부서 삭제*/
            organizationRepository.deleteByOrgCodeIn(orgCodes);



        } else {
            throw new NotDeleteException();
        }


    }


    public void updOrganizationBatch() {

        List<Organization> orgs = organizationRepository.findAll();
        List<OrganizationLevel> levels = organizationLevelRepository.findAll();
        for (Organization organization : orgs) {
            boolean check = true;
            for (OrganizationLevel level : levels) {
                if (level.getOrgCode().equals(organization.getOrgCode())) {
                    check = false;
                    levels.remove(level);
                    break;
                }
            }
            if (check) {
                OrganizationChartRequestDto dto = new OrganizationChartRequestDto();
                dto.setOrder(organization.getOrder());
                dto.setOrgCode(organization.getOrgCode());
                dto.setOrgFullCode(organization.getOrgFullCode());

                organizationLevelRepository.save(dto.levelToEntity());

            }
        }
    }

    public String selOrganizationByMaxCode() {

        Organization organization = organizationRepository.findFirstByOrgCodeLikeOrderByOrgCodeDesc("9%").orElse(null);
        String code = "";
        try {
            if (organization == null) {
                code = "90000001";
            } else {
                int icd = Integer.parseInt(organization.getOrgCode());
                icd++;
                code = Integer.toString(icd);
            }
        }catch (Exception e) {
            code = "";
        }

        return code;
    }

    public void updOrganizationChart(OrganizationChartUpdateRequestDto dto) throws Exception {

        Organization organization = organizationRepository.findById(dto.getOrgCode()).orElseThrow(() -> new IllegalArgumentException());
        if(!dto.getOrgPaCode().equals(organization.getOrgPaCode())) {
            List<Organization> organizations =  organizationRepository.findByOrgFullCodeLikeOrderByOrderAsc(organization.getOrgFullCode()+"%");
            List<OrganizationLevel> levels = organizationLevelRepositorySupport.findAllByOrgCode(organization.getOrgCode(),organization.getOrder());

            String orgFullName = organization.getOrgFullName();
            String orgFullCode = organization.getOrgFullCode();
            for(int i = 1;i< organizations.size();i++) {
                Organization org = organizations.get(i);
                OrganizationLevel level = levels.get(i);
                level.update(dto);
                org.update(orgFullName,orgFullCode,dto);
            }
            OrganizationLevel organizationLevel = organizationLevelRepository.findById(dto.getOrgCode()).orElseThrow(() -> new IllegalArgumentException());
            organizationLevel.update(dto);
        }
        if(!dto.getOrgName().equals(organization.getOrgName())) {
            approverDetailRepository.updateOrgNameByOrgCode(dto.getOrgCode(),dto.getOrgName());
        }
        organization.update(dto);
    }

    public List<OrganizationChartDto> selOrganizationByOrder(int order) throws Exception {

        return organizationRepositorySupport.findAllByOrder(order);
    }
}
