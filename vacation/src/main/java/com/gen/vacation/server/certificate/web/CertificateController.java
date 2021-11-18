package com.gen.vacation.server.certificate.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.MailSenderDto;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.global.util.CustomMailSenderUtil;
import com.gen.vacation.server.certificate.dto.CertificatesRequestDto;
import com.gen.vacation.server.certificate.service.CertificateService;
import com.gen.vacation.server.user.dto.CertificateRequestDto;
import com.gen.vacation.server.user.dto.CertificateStateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CertificateController {

    private final ResponseService responseService;

    private final CertificateService certificateService;

    private final CustomMailSenderUtil mailSenderUtil;

    @GetMapping("/certificate-request")
    public SingleResult<Map<String,Object>> getCertificate(SearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(certificateService.selCertificate(dto));
    }
    @GetMapping("/certificate-request/{userId}")
    public SingleResult<Map<String,Object>> getCertificateById (@PathVariable String userId,SearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(certificateService.selCertificateById(userId,dto));
    }
    @PostMapping("/certificate-request")
    public CommonResult postCertificate(@RequestBody CertificateRequestDto dto) throws Exception {

        MailSenderDto senderDto = certificateService.insCertificate(dto);
        mailSenderUtil.sendMails(senderDto);
        return responseService.getSuccessResult();
    }
    @PostMapping("/certificate-request/all")
    public CommonResult postCertificates(@RequestBody CertificatesRequestDto dto) throws Exception {

        certificateService.insCertificate(dto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/certificate-request/{id}/download")
    public SingleResult<Map<String,Object>> getCertificateDownload(@PathVariable Long id) throws Exception {

        return responseService.getSingleResult(certificateService.selPrintByCertificate(id));
    }
    @PatchMapping("/certificate-request/{id}/download")
    public CommonResult pathCertificateDownload(@PathVariable Long id) throws Exception {
        certificateService.updPrintByCertificate(id);
        return responseService.getSuccessResult();
    }
    @PatchMapping("/certificate-request/{id}")
    public CommonResult pathCertificate(@PathVariable Long id, @RequestBody CertificateStateRequestDto dto) throws Exception {
        MailSenderDto senderDto = certificateService.updPrintByCertificate(id,dto);
        mailSenderUtil.sendMail(senderDto);
        return responseService.getSuccessResult();
    }
    @GetMapping("/certificate-request/count")
    public SingleResult<Integer> getCertificateCount() throws Exception {

        return responseService.getSingleResult(certificateService.selCertificateCount());
    }
}
