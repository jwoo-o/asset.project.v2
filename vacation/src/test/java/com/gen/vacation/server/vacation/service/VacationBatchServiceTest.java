package com.gen.vacation.server.vacation.service;

import com.gen.vacation.global.common.dto.MailSenderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("local  ")
@SpringBootTest
@RunWith(SpringRunner.class)
public class VacationBatchServiceTest {

    @Autowired
    private VacationBatchService vacationBatchService;

    @Test
    public void updBatchNextYearsByUserId() {
        try{
            vacationBatchService.updBatchNextYearsByUserId();
        }catch (Exception e) {

            log.error(e.getMessage());
        }

    }

    @Test
    public void updBatchLess1YearByUserId() {
        try{
            vacationBatchService.updBatchLess1YearByUserId();
        }catch (Exception e) {

            log.error(e.getMessage());
        }
    }

    @Test
    public void selBatchUserHireDateOfEntry() {

        try{
            List<String> list = vacationBatchService.selBatchUserHireDateOfEntry();

            log.info(list.toString());
        }catch (Exception e) {

            log.error(e.getMessage());
        }
    }

    @Test
    public void updBatchVacationDeadlineByUser() {

        try{
            List<MailSenderDto> list =  vacationBatchService.updBatchVacationDeadlineByUser();

            log.info(list.toString());
        }catch (Exception e) {

            log.error(e.getMessage());
        }
    }
}