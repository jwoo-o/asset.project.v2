package com.gen.vacation.global.service;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.ListResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.contant.ErrorConstant;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-03-26
 * Time: 오후 12:57
 */
@Service
public class ResponseService {


    /**
     * api 요청 결과에 대한 code, message를 정의합니다.
     */
    public enum CommonResponse {
        SUCCESS(ErrorConstant.SUCCESS, "SUCCESS"),
        FAIL(ErrorConstant.FAIL, "FAIL"),
        PUT(ErrorConstant.SUCCESS, "PATCHED"),
        DELETE(ErrorConstant.SUCCESS, "DELETED");

        String code;
        String result;

        CommonResponse(String code, String result) {
            this.code = code;
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public String getResult() {
            return result;
        }
    }


    /**
     * 단일건 결과를 처리하는 메소드
     */
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    /**
     * 다중건 결과를 처리하는 메소드
     */
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;
    }

    /**
     * 성공 결과만 처리하는 메소드
     */
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    /**
     * 업데이트 결과만 처리하는 메소드
     */
    public CommonResult getPutResult(int put) {
        CommonResult result = new CommonResult();
        if (put > 0) {
            setPutResult(result);
        } else {
            return getFailResult();
        }
        return result;
    }

    /**
     * 삭제 결과만 처리하는 메소드
     */
    public CommonResult getDeleteResult(Long delete) {
        CommonResult result = new CommonResult();
        if (delete > 0) {
            setDeleteResult(result);
        } else {
            return getFailResult();
        }
        return result;
    }

    /**
     * 실패 결과만 처리하는 메소드
     */
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setResult(CommonResponse.FAIL.getResult());
        return result;
    }

    /**
     * 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
     */
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setResult(CommonResponse.SUCCESS.getResult());
    }

    /**
     * 결과 모델에 api 요청 업데이트 데이터를 세팅해주는 메소드
     */
    private void setPutResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.PUT.getCode());
        result.setResult(CommonResponse.PUT.getResult());
    }

    /**
     * 결과 모델에 api 요청 삭제 데이터를 세팅해주는 메소드
     */
    private void setDeleteResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.DELETE.getCode());
        result.setResult(CommonResponse.DELETE.getResult());
    }


}
