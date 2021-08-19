package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오전 10:48
 */
public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {

    Optional<CommonCodeGroup> findByGroupCode(String groupCode);

    int countByGroupCode(String groupCode);

    List<CommonCodeGroup> findAllByGroupCode(String groupCode);
}
