package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, String> {

    Optional<Asset> findFirstByCategoryLikeOrderByAssetIdDesc(String category);

    List<Asset> findByOrgCodeIn(Collection<String> orgCodes);
}
