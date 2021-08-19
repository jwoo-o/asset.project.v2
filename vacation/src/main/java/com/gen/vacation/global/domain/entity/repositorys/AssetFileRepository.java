package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.AssetFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetFileRepository extends JpaRepository<AssetFile, Long> {

    Optional<AssetFile> findByAssetId(String assetId);
}
