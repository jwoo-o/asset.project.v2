package com.gen.vacation.server.board.service;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.global.domain.entity.Board;
import com.gen.vacation.global.domain.entity.BoardFile;
import com.gen.vacation.global.domain.entity.repositorys.BoardFileRepository;
import com.gen.vacation.global.domain.entity.repositorys.BoardRepository;
import com.gen.vacation.global.util.FileUploadUtil;
import com.gen.vacation.server.board.dto.NoticeInfoRequestDto;
import com.gen.vacation.server.board.dto.NoticeInfoResponseDto;
import com.gen.vacation.server.board.repository.BoardRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 12:55
 */
@Validated
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardFileRepository boardFileRepository;

    private final BoardRepository boardRepository;

    private final BoardRepositorySupport boardRepositorySupport;

    private final FileUploadUtil fileUploadUtil;


    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Value("${server.servlet.context-path}")
    private String basicUrl;

    public void insNotice(@Valid NoticeInfoRequestDto dto, HttpServletRequest request) throws Exception {

        dto.setIp(request.getRemoteAddr());

        Board board = boardRepository.save(dto.toEntity());

        if (dto.getFileIds() != null) {

            boardFileRepository.updateBoardIdByIds(Arrays.asList(dto.getFileIds()), board.getId());

        }
    }

    public Map<String, Object> selNotice(@Valid SearchRequestDto dto) throws Exception {

        return boardRepositorySupport.findAllBySearch(dto);
    }

    public NoticeInfoResponseDto updSearchNoticeById(Long id) {

        Board board = boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        board.updateCount();

        List<Map<String, Object>> fileList = new ArrayList<>();
        if (board.getBoardFiles() != null) {

            for (BoardFile boardFile : board.getBoardFiles()) {
                Map<String, Object> file = new HashMap<>();
                file.put("name", boardFile.getFileName());
                file.put("url", basicUrl + "/" + resourcesUriPath + "/notice/"  + boardFile.getKey());
                fileList.add(file);
            }
        }
        NoticeInfoResponseDto noticeInfoResponseDto = new NoticeInfoResponseDto(board);
        noticeInfoResponseDto.setFileList(fileList);

        return noticeInfoResponseDto;
    }

    public NoticeInfoResponseDto selNoticeById(Long id) throws Exception {

        Board board = boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        List<Map<String, Object>> fileList = new ArrayList<>();
        if (board.getBoardFiles() != null) {

            for (BoardFile boardFile : board.getBoardFiles()) {
                Map<String, Object> file = new HashMap<>();
                file.put("name", boardFile.getFileName());
                file.put("id", boardFile.getId());
                fileList.add(file);
            }
        }
        NoticeInfoResponseDto noticeInfoResponseDto = new NoticeInfoResponseDto(board);
        noticeInfoResponseDto.setFileList(fileList);

        return noticeInfoResponseDto;
    }

    public void delNoticeFile(Long id) throws Exception {

        BoardFile boardFile = boardFileRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        String key = boardFile.getKey();
        boardFileRepository.delete(boardFile);
        fileUploadUtil.fileDelete(key, uploadDirectory + "/notice");

    }

    public void updNotice(Long id, @Valid NoticeInfoRequestDto dto) throws Exception {

        Board board = boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        board.update(dto);


        if (dto.getFileIds() != null) {

            boardFileRepository.updateBoardIdByIds(Arrays.asList(dto.getFileIds()), board.getId());

        }


    }

    public void delNotice(Long id) throws Exception {

        Board board = boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        List<BoardFile> files = board.getBoardFiles();

        if (files != null) {
            for (BoardFile file : files) {
                String key = file.getKey();
                boardFileRepository.deleteById(file.getId());
                fileUploadUtil.fileDelete(key, uploadDirectory + "/notice");
            }
        }
        boardRepository.deleteById(id);
    }

    public List<Long> insNoticeFile(MultipartHttpServletRequest request) throws Exception {
        List<Long> ids = new ArrayList<>();
        List<MultipartFile> files = request.getFiles("files");
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            String uploadName = fileUploadUtil.fileUpload(file, uploadDirectory + "/notice");
            BoardFile boardFile = boardFileRepository.save(BoardFile.builder()
                    .key(uploadName)
                    .fileName(fileName)
                    .fileSize(file.getSize()).build());
            ids.add(boardFile.getId());
        }
        return ids;
    }
}
