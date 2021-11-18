package com.gen.vacation.server.asset.service;

import com.gen.vacation.global.domain.entity.*;
import com.gen.vacation.global.domain.entity.repositorys.AssetFileRepository;
import com.gen.vacation.global.domain.entity.repositorys.AssetRepository;
import com.gen.vacation.global.domain.entity.repositorys.CommonCodeDetailRepository;
import com.gen.vacation.global.domain.entity.repositorys.OrganizationRepository;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.global.util.JsonUtil;
import com.gen.vacation.server.asset.dto.*;
import com.gen.vacation.server.asset.repository.AssetRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-07-02
 * Time: 오전 10:15
 */
@Validated
@RequiredArgsConstructor
@Service
public class AssetService {

    private final AssetRepository assetRepository;

    private final AssetRepositorySupport assetRepositorySupport;

    private final AssetFileRepository assetFileRepository;

    private final FileUploadUtil fileUploadUtil;

    private final OrganizationRepository organizationRepository;

    private final CommonCodeDetailRepository commonCodeDetailRepository;


    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Value("${server.servlet.context-path}")
    private String basicUrl;

    public String selNextId(String category) throws Exception {

        String id;

        Asset asset = assetRepository.findFirstByCategoryLikeOrderByAssetIdDesc(category + "%").orElse(null);
        if (asset == null) {
            id = category + "00001";
        } else {
            id = category + String.format("%05d",  (Integer.parseInt(asset.getAssetId().replace(category, "")) + 1));
        }

        return id;
    }

    public void insAsset(AssetRequestDto dto) throws Exception {

        assetRepository.save(dto.toEntity());
    }

    public Map<String,Object> selAsset(AssetSearchRequestDto dto) throws Exception {

        return assetRepositorySupport.findAllBySearch(dto);
    }

    public AssetResponseDto selAssetById(String assetId) throws Exception {

        Asset asset = assetRepository.findById(assetId).orElseThrow(IllegalArgumentException::new);

        AssetResponseDto dto = new AssetResponseDto(asset);
        AssetFile assetFile = assetFileRepository.findByAssetId(assetId).orElse(null);
        List<Map<String, Object>> fileList = new ArrayList<>();
        if(assetFile != null) {
            String url = basicUrl + "/" + resourcesUriPath + "/asset/"+assetFile.getKey();
            dto.setImgSrc(url);
            Map<String,Object> file = new HashMap<>();
            file.put("name", assetFile.getFileName());
            file.put("url", url);

            fileList.add(file);
        }
        dto.setFileList(fileList);

        return dto;
    }

    public void insAssetImage(AssetUploadDto dto) throws Exception {

        Asset asset = assetRepository.findById(dto.getAssetId()).orElseThrow(IllegalArgumentException::new);

        String fileName = dto.getFiles().getOriginalFilename()+"";
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        String uploadName = fileUploadUtil.fileUpload(dto.getFiles(), uploadDirectory + "/asset");
        assetFileRepository.save(AssetFile.builder()
                .key(uploadName)
                .assetId(asset.getAssetId())
                .fileName(fileName)
                .fileSize(dto.getFiles().getSize()).build());

    }

    public void delAssetImage(String assetId) throws Exception {

        AssetFile assetFile = assetFileRepository.findByAssetId(assetId).orElseThrow(IllegalArgumentException::new);
        fileUploadUtil.fileDelete(assetFile.getKey(),uploadDirectory + "/asset");
        assetFileRepository.delete(assetFile);
    }

    public void updAsset(AssetRequestDto dto) throws Exception {

        Asset asset = assetRepository.findById(dto.getAssetId()).orElseThrow(IllegalArgumentException::new);

        asset.update(dto);
    }

    public Map<String,Object> selAssetToExcel(AssetSearchRequestDto dto) throws Exception {

        return assetRepositorySupport.findAllToExcelBySearch(dto);
    }

    public AssetResponseDto selAssetTokenById(String assetId) throws Exception {

        AssetResponseDto dto = assetRepositorySupport.findById(assetId);
        AssetFile assetFile = assetFileRepository.findByAssetId(assetId).orElse(null);
        List<Map<String, Object>> fileList = new ArrayList<>();
        if(assetFile != null) {
            String url = basicUrl + "/" + resourcesUriPath + "/asset/"+assetFile.getKey();
            dto.setImgSrc(url);
            Map<String,Object> file = new HashMap<>();
            file.put("name", assetFile.getFileName());
            file.put("url", url);

            fileList.add(file);
        }
        dto.setFileList(fileList);

        return dto;
    }



    public Map<String,Object> selAssetTotal(AssetSearchRequestDto dto) throws Exception {


        List<Organization> organizations = organizationRepository.findByOrgFullCodeLike("%" +dto.getOrgCode()+"%");
        String fullCode = organizations.stream().filter(organization -> organization.getOrgCode().equals(dto.getOrgCode())).map(Organization::getOrgFullCode).collect(Collectors.joining());
        List<Asset> assets = assetRepositorySupport.findAll(fullCode);

        /** 자산 세부 내역 */
        Map<String,Object> result = getAssetDetail(assets);
        /** 부서별 현황 */
        result.put("org",getOrgList(assets,organizations,dto.getOrgCode()));
        /** 종류별 현황 */
        result.put("group",getGroupList(assets));

        return result;

    }




    private List<AssetTotalDto> getOrgList(List<Asset> assets, List<Organization> organizations, String code) {

        List<String> subOrgCodes = organizations.stream().filter(organization -> organization.getOrgPaCode().equals(code)).map(Organization::getOrgCode).collect(Collectors.toList());
        if(subOrgCodes.isEmpty()) {
            subOrgCodes.add(code);
        }

        List<AssetTotalDto> org = new ArrayList<>();
        for (String orgCode : subOrgCodes) {

            List<String> subOrgCode = organizations.stream().filter(organization -> organization.getOrgFullCode().contains(orgCode)).map(Organization::getOrgCode).collect(Collectors.toList());

            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setValue(assets.stream().filter(asset -> subOrgCode.toString().contains(asset.getOrgCode()) && !asset.getStatus().equals("D")).count());
            totalDto.setName(organizations.stream().filter(organization -> organization.getOrgCode().equals(orgCode)).findFirst().orElseThrow(IllegalArgumentException::new).getOrgName());


            org.add(totalDto);
        }

        return org;
    }
    private List<AssetTotalDto> getGroupList(List<Asset> assets) {

        List<CommonCodeDetail> details = commonCodeDetailRepository.findByGroupCode("assetCategory");

        List<AssetTotalDto> group = new ArrayList<>();
        for(CommonCodeDetail detail : details) {
            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setValue(assets.stream()
                    .filter(asset -> asset.getCategory().equals(Objects.requireNonNull(detail.getId()).getDetailCode()) && !asset.getStatus().equals("D")).count());
            totalDto.setName(detail.getDetailDesc());

            group.add(totalDto);
        }
        return group;
    }
    private Map<String,Object> getAssetDetail(List<Asset> assets) {

        Map<String,Object> result = new HashMap<>();

        int created = 0;
        int modified = 0;
        int disposal = 0;

        LocalDate now = LocalDate.now();
        LocalDate before = now.minusDays(30);


        result.put("now",now);
        result.put("before",before);


        List<AssetResponseDto> filterAsset = assets.stream().filter(asset -> asset.getModifiedAt()
                .isAfter(before.atTime(0,0,0))).map(AssetResponseDto::new).collect(Collectors.toList());
        result.put("assetList",  filterAsset);
        result.put("total",filterAsset.size());



        Map<String,Long> osTotalMap = new HashMap<>();
        Map<String,Long> programTotalMap = new HashMap<>();
        for(Asset asset : assets) {

            if(asset.getModifiedAt().isAfter(before.atTime(0,0,0))) {
                if(asset.getCreatedAt().equals(asset.getModifiedAt())){
                    created++;
                } else if (asset.getStatus().equals("D")) {
                    disposal++;

                } else {
                    modified++;
                }
            }
            if(asset.getCategory().equals("PC")) {
                Map<String, Object> assetInfo = (Map<String, Object>) JsonUtil.stringToJson(asset.getAssetInfo());

                String osInfoName = (String) assetInfo.get("osInfo");
                if(osInfoName != null) {
                    Long count = (Optional.ofNullable(osTotalMap.get(osInfoName)).orElse(0L))+1;
                    osTotalMap.put(osInfoName, count);
                }
                if(assetInfo.get("programs") != null) {
                    JSONArray programs = (JSONArray) assetInfo.get("programs");
                    for (Object object : programs) {
                        JSONObject programObject = (JSONObject) object;
                        Long count = (Optional.ofNullable(programTotalMap.get(programObject.get("name")+"")).orElse(0L))+1;
                        programTotalMap.put(programObject.get("name")+"", count);
                    }
                }
            }
        }

        result.put("created",created);
        result.put("modified",modified);
        result.put("disposal",disposal);


        List<AssetTotalDto> os = osTotalMap.entrySet().stream().map(AssetTotalDto::new).collect(Collectors.toList());
        List<AssetTotalDto> program = programTotalMap.entrySet().stream().map(AssetTotalDto::new).collect(Collectors.toList());


        result.put("os",os);
        result.put("program",program);

        return result;
    }
}
