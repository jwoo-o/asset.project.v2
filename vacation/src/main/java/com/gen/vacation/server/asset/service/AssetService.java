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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String upload_directory;

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

        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new IllegalArgumentException());

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

        Asset asset = assetRepository.findById(dto.getAssetId()).orElseThrow(() -> new IllegalArgumentException());

        String fileName = dto.getFiles().getOriginalFilename()+"";
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        String uploadName = fileUploadUtil.fileUpload(dto.getFiles(), upload_directory + "/asset");
        assetFileRepository.save(AssetFile.builder()
                .key(uploadName)
                .assetId(asset.getAssetId())
                .fileName(fileName)
                .fileSize(dto.getFiles().getSize()).build());

    }

    public void delAssetImage(String assetId) throws Exception {

        AssetFile assetFile = assetFileRepository.findByAssetId(assetId).orElseThrow(() -> new IllegalArgumentException());
        fileUploadUtil.fileDelete(assetFile.getKey(),upload_directory + "/asset");
        assetFileRepository.delete(assetFile);
    }

    public void updAsset(AssetRequestDto dto) throws Exception {

        Asset asset = assetRepository.findById(dto.getAssetId()).orElseThrow(() -> new IllegalArgumentException());

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

        int created = 0;
        int modified = 0;
        int disposal = 0;
        Map<String,Object> result = new HashMap<>();

        List<AssetTotalDto> org =  new ArrayList<>();
        List<AssetTotalDto> group = new ArrayList<>();
        List<AssetTotalDto> os = new ArrayList<>();
        List<AssetTotalDto> program = new ArrayList<>();

        LocalDate now = LocalDate.now();
        LocalDate before = now.minusDays(30);
        result.put("now",now);
        result.put("before",before);

        List<Organization> organizations = organizationRepository.findByOrgFullCodeLike("%" +dto.getOrgCode()+"%");
        List<String> orgPaCodes = organizations.stream().filter(organization -> organization.getOrgPaCode().equals(dto.getOrgCode())).map(v -> v.getOrgCode()).collect(Collectors.toList());
        if(orgPaCodes.isEmpty()) {
            orgPaCodes.add(dto.getOrgCode());
        }
        List<String> orgCodes = organizations.stream().map(v -> v.getOrgCode()).collect(Collectors.toList());
        List<CommonCodeDetail> details = commonCodeDetailRepository.findByGroupCode("assetCategory");

        List<Asset> assets = assetRepositorySupport.findAll(orgCodes);

        for(CommonCodeDetail detail : details) {
            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setValue(assets.stream().filter(asset -> asset.getCategory().equals(detail.getId().getDetailCode()) && !asset.getStatus().equals("D")).count());
            totalDto.setName(detail.getDetailDesc());

            group.add(totalDto);
        }


        for (String orgCode : orgPaCodes) {

            List<String> subOrgCode = organizations.stream().filter(organization -> organization.getOrgFullCode().contains(orgCode)).map(v -> v.getOrgCode()).collect(Collectors.toList());

            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setValue(assets.stream().filter(asset -> subOrgCode.toString().contains(asset.getOrgCode()) && !asset.getStatus().equals("D")).count());
            totalDto.setName(organizations.stream().filter(organization -> organization.getOrgCode().equals(orgCode)).findFirst().get().getOrgName());


            org.add(totalDto);
        }
        result.put("org",org);
        result.put("group",group);


        result.put("assetList",  assets.stream().filter(asset -> asset.getModifiedAt()
                .isAfter(before.atTime(0,0,0))).map(AssetResponseDto::new).collect(Collectors.toList()));
        result.put("total",((List)result.get("assetList")).size());

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

                if(assetInfo.get("osInfo") != null) {
                    Long count = (Optional.ofNullable(osTotalMap.get(assetInfo.get("osInfo"))).orElseGet(() -> 0L))+1;
                    osTotalMap.put(assetInfo.get("osInfo")+"", count);
                }
                if(assetInfo.get("programs") != null) {
                    JSONArray programs = (JSONArray) assetInfo.get("programs");
                    for (Object object : programs) {
                        JSONObject programObject = (JSONObject) object;
                        Long count = (Optional.ofNullable(programTotalMap.get(programObject.get("name")+"")).orElseGet(() -> 0L))+1;
                        programTotalMap.put(programObject.get("name")+"", count);
                    }
                }
            }
        }

        result.put("created",created);
        result.put("modified",modified);
        result.put("disposal",disposal);


        osTotalMap.keySet().forEach(key -> {
            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setName(key);
            totalDto.setValue((Long) osTotalMap.get(key));
            os.add(totalDto);
        });
        programTotalMap.keySet().forEach(key -> {
            AssetTotalDto totalDto = new AssetTotalDto();
            totalDto.setName(key);
            totalDto.setValue((Long) programTotalMap.get(key));
            program.add(totalDto);
        });
        result.put("os",os);
        result.put("program",program);






        return result;


    }
}
