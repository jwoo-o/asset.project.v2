package com.gen.vacation.server.vacation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gen.vacation.global.Enum.LoginHistoryEnum;
import com.gen.vacation.global.domain.entity.VacationDeadline;
import com.gen.vacation.global.domain.entity.id.VacationDeadlineId;
import com.gen.vacation.global.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-05-26
 * Time: 오후 5:43
 */
@Getter
@Setter
@ToString
public class VacationDeadlineRequestDto {

   private String year;
   private String userId;
   private String writer;
   private String image;
   private List<Map<String,Object>> deadlines;


   public VacationDeadlineId getId() {
       return new VacationDeadlineId(year,userId, LoginHistoryEnum.valueOf(writer));
   }

   public VacationDeadline toEntity() throws JsonProcessingException {
      return VacationDeadline.builder()
                                 .id(getId())
                                 .deadlines(JsonUtil.dtoToString(deadlines)).build();
   }
}
