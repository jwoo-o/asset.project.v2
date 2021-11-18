package com.gen.vacation.global.advice;

import com.gen.vacation.global.common.ErrorResult;
import com.gen.vacation.global.contant.ErrorConstant;
import com.gen.vacation.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-20
 * Time: 오전 11:18
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {


    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResult unauthorizedException(HttpServletResponse response, HttpServletRequest request) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResult error = new ErrorResult();

        error.setError("Unauthorized");
        error.setCode(ErrorConstant.UNAUTHORIZED);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResult usernameNotFoundException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResult error = new ErrorResult();
        error.setError("Unauthorized");
        error.setCode(ErrorConstant.WRNG_LOGIN);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));

        return error;
    }
    @ExceptionHandler(NotDeleteException.class)
    public ErrorResult notDeleteException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResult error = new ErrorResult();
        error.setError("Not Delete");
        error.setCode(ErrorConstant.NOT_DELETE);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));

        return error;
    }
    @ExceptionHandler(CommonFailException.class)
    public ErrorResult commonFailException(HttpServletResponse response,Exception e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResult error = new ErrorResult();
        error.setError("Fail");
        error.setCode(ErrorConstant.FAIL);
        error.setMessage(e.getMessage());

        return error;
    }



    @ExceptionHandler(AlreadyException.class)
    public ErrorResult alreadyFoundException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        ErrorResult error = new ErrorResult();
        error.setError("conflict");
        error.setCode(ErrorConstant.REGISTERED);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }
    @ExceptionHandler(AlreadyProcessException.class)
    public ErrorResult alreadyProcessException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        ErrorResult error = new ErrorResult();
        error.setError("conflict");
        error.setCode(ErrorConstant.PROCESSED);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));

        return error;
    }




    @ExceptionHandler(PasswordAlreadyException.class)
    public ErrorResult passwordAlreadyException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        ErrorResult error = new ErrorResult();
        error.setError("conflict");
        error.setCode(ErrorConstant.REGISTERED);
        error.setMessage("");


        return error;
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ErrorResult passwordNotMatchException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResult error = new ErrorResult();

        error.setError("Unauthorized");
        error.setCode(ErrorConstant.WRNG_LOGIN);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalArgumentException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResult error = new ErrorResult();
        error.setError("No Content");
        error.setCode(ErrorConstant.NOT_FOUND);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }
    @ExceptionHandler(NotFoundLicenseException.class)
    public ErrorResult notFoundLicenseException(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResult error = new ErrorResult();
        error.setError("Not Found License");
        error.setCode(ErrorConstant.NOT_FOUND_LICENSE);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResult constraintViolationException(ConstraintViolationException e, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResult error = new ErrorResult();

        error.setError("valid fail");
        error.setCode(ErrorConstant.EMPTY_REQUIRED);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResult dataIntegrityViolationException(HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResult error = new ErrorResult();

        error.setError("Integrity violation");
        error.setCode(ErrorConstant.INTEGRITY_VIOLATION);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));

        return error;
    }


    @ExceptionHandler(Exception.class)
    public ErrorResult defaultException(HttpServletResponse response, Exception e) {

        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);


        ErrorResult error = new ErrorResult();
        error.setError("BAD Request");
        error.setCode(ErrorConstant.SYSTEM_ERROR);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }

    @ExceptionHandler(NotFoundApproverGroupException.class)
    public ErrorResult notFoundApproverGroupException(HttpServletResponse response, Exception e) {

        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);


        ErrorResult error = new ErrorResult();
        error.setError("NOT APPROVERGROUP");
        error.setCode(ErrorConstant.NOT_APPROVERGROUP);
        error.setMessage(ErrorConstant.getMessage(error.getCode()));


        return error;
    }



}
