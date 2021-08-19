package com.gen.vacation.server.board.web;

import com.gen.vacation.global.common.CommonResult;
import com.gen.vacation.global.common.ListResult;
import com.gen.vacation.global.common.SingleResult;
import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.service.ResponseService;
import com.gen.vacation.server.board.dto.NoticeInfoRequestDto;
import com.gen.vacation.server.board.dto.NoticeInfoResponseDto;
import com.gen.vacation.server.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 12:54
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/*")
public class BoardApiController {

    private final ResponseService responseService;

    private final BoardService boardService;

    @GetMapping("/notice")
    public SingleResult<Map<String, Object>> getAllNotice(SearchRequestDto dto) throws Exception {

        return responseService.getSingleResult(boardService.selNotice(dto));
    }

    @GetMapping("/notice/{id}")
    public SingleResult<NoticeInfoResponseDto> getNoticeById(@PathVariable Long id) throws Exception {

        return responseService.getSingleResult(boardService.selNoticeById(id));
    }


    @PatchMapping("/notice/{id}")
    public SingleResult<NoticeInfoResponseDto> patchNoticeById(@PathVariable Long id) throws Exception {

        return responseService.getSingleResult(boardService.updSearchNoticeById(id));
    }

    @PutMapping("/notice/{id}")
    public CommonResult putNotice(@PathVariable Long id, @RequestBody NoticeInfoRequestDto dto) throws Exception {


        boardService.updNotice(id, dto);
        return responseService.getSuccessResult();
    }


    @PostMapping("/notice")
    public CommonResult postNotice(@RequestBody NoticeInfoRequestDto dto, HttpServletRequest request) throws Exception {

        boardService.insNotice(dto, request);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/notice/{id}")
    public CommonResult deleteNoticeById(@PathVariable Long id) throws Exception {

        boardService.delNotice(id);
        return responseService.getSuccessResult();
    }

    @PostMapping("/notice/file")
    public ListResult<Long> insFiles(MultipartHttpServletRequest request) throws Exception {
        return responseService.getListResult(boardService.insNoticeFile(request));
    }

    @DeleteMapping("/notice/file/{id}")
    public CommonResult delNoticeFileById(@PathVariable Long id) throws Exception {

        boardService.delNoticeFile(id);
        return responseService.getSuccessResult();
    }

}
