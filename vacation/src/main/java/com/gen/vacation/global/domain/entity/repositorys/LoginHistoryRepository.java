package com.gen.vacation.global.domain.entity.repositorys;

import com.gen.vacation.global.domain.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-10-21
 * Time: 오후 1:40
 */
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
