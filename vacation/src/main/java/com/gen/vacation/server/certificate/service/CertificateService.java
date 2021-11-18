package com.gen.vacation.server.certificate.service;

import com.gen.vacation.global.enums.ApproverFlagEnum;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Admin;
import com.gen.vacation.global.domain.entity.CertificateRequestHistory;
import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.global.domain.entity.repositorys.AdminRepository;
import com.gen.vacation.global.domain.entity.repositorys.CertificateRequestHistoryRepository;
import com.gen.vacation.global.domain.entity.repositorys.CommonCodeGroupRepository;
import com.gen.vacation.global.domain.entity.repositorys.UserRepository;
import com.gen.vacation.global.util.CommonUtil;
import com.gen.vacation.server.certificate.dto.CertificatesRequestDto;
import com.gen.vacation.server.certificate.repository.CertificateRequestHistoryRepositorySupport;
import com.gen.vacation.server.cmmn.dto.CommonDetailCodeResponseDto;
import com.gen.vacation.server.cmmn.dto.CommonGroupCodeResponseDto;
import com.gen.vacation.server.user.dto.CertificateRequestDto;
import com.gen.vacation.server.user.dto.CertificateStateRequestDto;
import com.gen.vacation.server.user.repository.AdminRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CertificateService {

    private final CertificateRequestHistoryRepositorySupport certificateRequestHistoryRepositorySupport;

    private final CertificateRequestHistoryRepository certificateRequestHistoryRepository;

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private final AdminRepositorySupport adminRepositorySupport;

    private final CommonCodeGroupRepository commonCodeGroupRepository;

    private final SpringTemplateEngine templateEngine;



    public Map<String,Object> selCertificateById(String userId, SearchRequestDto searchRequestDto) throws Exception {

        return certificateRequestHistoryRepositorySupport.findById(userId,searchRequestDto);

    }
    public Map<String,Object> selCertificate(SearchRequestDto searchRequestDto) throws Exception {

        return certificateRequestHistoryRepositorySupport.findAll(searchRequestDto);

    }

    public MailSenderDto insCertificate(@Valid CertificateRequestDto dto) throws Exception {

        CertificateRequestHistory certificateRequestHistory = certificateRequestHistoryRepository.save(dto.toEntity());
        User user = userRepository.findById(dto.getUserId()).orElseThrow(IllegalArgumentException::new);
        MailSenderDto senderDto = new MailSenderDto();

        senderDto.setTemplate("mail-alarm");
        Map<String, Object> data = new HashMap<>();
        data.put("subject",certificateRequestHistory.getType().getDesc()+" 발급 요청");
        data.put("userNames",user.getOrganization().getOrgFullName()+" - "+user.getUserName()+" "+user.getRankNm());
        data.put("currentDate",certificateRequestHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        senderDto.setSubject("[SYSTEM] " + certificateRequestHistory.getType().getDesc()+" 발급 요청");
        List<String> to = adminRepositorySupport.findEmailById("admin");
        senderDto.setTos(to.toArray(new String[to.size()]));
        senderDto.setCcs(new String[0]);
        senderDto.setFrom(user.getEmail());
        senderDto.setData(data);

        return senderDto;
    }
    public void insCertificate(@Valid CertificatesRequestDto dto) throws Exception {

        for(String userId: dto.getUserId()) {
             certificateRequestHistoryRepository.save(dto.toEntity(userId));
        }
    }


    public Map<String,Object> selPrintByCertificate(Long id) throws Exception {


        CertificateRequestHistory certificateRequestHistory = certificateRequestHistoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        User user = certificateRequestHistory.getUser();


        CommonCodeGroup group = commonCodeGroupRepository.findByGroupCode("company").orElseThrow(IllegalArgumentException::new);

        CommonGroupCodeResponseDto groupCodeResponseDto =  new CommonGroupCodeResponseDto(group);

        Context context = new Context();

        for(CommonDetailCodeResponseDto detailCodeResponseDto : groupCodeResponseDto.getDetailCodes()) {
            context.setVariable(detailCodeResponseDto.getDetailCode(),detailCodeResponseDto.getDetailName());
        }
        context.setVariable("purpose",certificateRequestHistory.getPurpose());
        context.setVariable("submit",certificateRequestHistory.getSubmit());
        context.setVariable("now", CommonUtil.korLocalDateTime(certificateRequestHistory.getModifiedAt()));
        context.setVariable("name",user.getUserName());
        context.setVariable("address",user.getAddress() + " " + user.getAddress1());
        context.setVariable("seal",certificateRequestHistory.isSeal());
        context.setVariable("responsibilities",user.getResponsibilities());
        context.setVariable("orgNm",user.getOrganization().getOrgName());
        context.setVariable("rankNm",user.getRankNm());
        context.setVariable("birthDate",CommonUtil.korLocalDate(user.getBirthDate()));
        context.setVariable("period", CommonUtil.korLocalDate(user.getHireDate()) + " ~ " + (user.getLeaveDate() == null ? "현재" : CommonUtil.korLocalDate(user.getLeaveDate())));


        String html = templateEngine.process(certificateRequestHistory.getType().getKey().toLowerCase()+"-cert", context);

        Map<String,Object> data = new HashMap<>();

        data.put("html",html);

        return data;
    }

    public void updPrintByCertificate(Long id) throws Exception {

        CertificateRequestHistory certificateRequestHistory = certificateRequestHistoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        certificateRequestHistory.success();
    }

    public MailSenderDto updPrintByCertificate(Long id, CertificateStateRequestDto dto) throws Exception {

        CertificateRequestHistory certificateRequestHistory = certificateRequestHistoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        certificateRequestHistory.state(dto);

        Admin admin = adminRepository.findById(dto.getAdminId()).orElseThrow(IllegalArgumentException::new);
        User user = certificateRequestHistory.getUser();
        MailSenderDto senderDto = new MailSenderDto();
        senderDto.setTemplate("mail-alarm");
        Map<String, Object> data = new HashMap<>();
        data.put("subject",certificateRequestHistory.getType().getDesc()+" 발급 "+certificateRequestHistory.getApprovalFlag().getDesc());
        data.put("userNames",certificateRequestHistory.getApprovalFlag().getDesc()+" - 관리자("+ admin.getName()+")");
        data.put("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        senderDto.setSubject("[SYSTEM] " + certificateRequestHistory.getType().getDesc()+" 발급 "+certificateRequestHistory.getApprovalFlag().getDesc());
        senderDto.setTo(user.getEmail());
        senderDto.setFrom(admin.getEmail());

        senderDto.setFrom(user.getEmail());
        senderDto.setData(data);

        return senderDto;
    }

    public int selCertificateCount() {

        return certificateRequestHistoryRepository.countByApprovalFlag(ApproverFlagEnum.WAIT);
    }
}
