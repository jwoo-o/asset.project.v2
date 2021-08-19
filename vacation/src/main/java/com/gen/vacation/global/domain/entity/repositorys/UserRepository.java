package com.gen.vacation.global.domain.entity.repositorys;


import com.gen.vacation.global.domain.entity.User;
import com.gen.vacation.server.user.dto.UserInfoResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserIdAndUseYn(String userId, boolean useYn);

    List<User> findAll();

    int countByUserIdAndUseYn(String userId, boolean useYn);

    List<User> findByOrgCodeLike(String orgCode);

    List<User> findByOrgCodeIn(Collection<String> orgCodes);

    Stream<User> findByOrgCodeAndUseYn(String orgCode, boolean useYn);

    int countByUserId(String userId);

    @Modifying
    @Query(value = "UPDATE tb_user u SET u.orgCode = :orgCode,u.modifiedAt = current_timestamp WHERE u.orgCode in :orgCodes")
    void updateOrgCodeByOrgCodes(@Param("orgCodes") Collection<String> orgCodes, @Param("orgCode") String orgCode);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM tb_user u WHERE u.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    List<User> findByUseYn(boolean useYn);

}
