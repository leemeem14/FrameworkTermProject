package com.assignment.controller;

import com.assignment.service.FileService;
import com.assignment.service.SubmissionService;
import com.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final SubmissionService submissionService;
    private final UserService userService;

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @param assignmentId 과제 ID
     * @param authentication 인증 정보
     * @return 업로드된 파일 정보
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                log.warn("빈 파일 업로드 시도");
                response.put("success", false);
                response.put("message", "파일을 선택해주세요");
                response.put("code", "EMPTY_FILE");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 크기 검사 (10MB)
            long maxFileSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxFileSize) {
                log.warn("파일 크기 초과: {}", file.getOriginalFilename());
                response.put("success", false);
                response.put("message", "파일 크기는 10MB 이하여야 합니다");
                response.put("code", "FILE_TOO_LARGE");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 저장
            String filename = fileService.saveFile(file);
            log.info("파일 업로드 성공: {}", filename);

            // 제출 정보 저장
            String username = authentication.getName();
            Long userId = userService.getUserByUsername(username).getId();
            submissionService.createSubmission(assignmentId, userId, filename, file.getOriginalFilename(), file.getSize());

            response.put("success", true);
            response.put("message", "파일이 업로드되었습니다");
            response.put("filename", filename);
            response.put("originalFilename", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("code", "SUCCESS");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "파일 업로드에 실패했습니다: " + e.getMessage());
            response.put("code", "UPLOAD_FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            response.put("success", false);
            response.put("message", "시스템 오류가 발생했습니다");
            response.put("code", "SYSTEM_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 파일 다운로드
     * @param filename 다운로드할 파일명
     * @return 파일 바이너리 데이터
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            // 파일명 유효성 검사
            if (filename == null || filename.trim().isEmpty()) {
                log.warn("유효하지 않은 파일명: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            // 파일 다운로드
            byte[] fileContent = fileService.downloadFile(filename);
            log.info("파일 다운로드: {}", filename);

            // Submission에서 원본 파일명 조회
            String originalFilename = filename; // 기본값은 저장된 파일명
            try {
                var submission = submissionService.getSubmissionByFilename(filename);
                if (submission != null) {
                    originalFilename = submission.getOriginalFilename();
                }
            } catch (Exception e) {
                log.debug("원본 파일명 조회 실패, 저장된 파일명 사용: {}", filename);
            }

            // UTF-8 인코딩으로 파일명 설정
            String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(fileContent);

        } catch (IOException e) {
            log.error("파일 다운로드 중 오류 발생: {}", filename, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 파일 삭제
     * @param filename 삭제할 파일명
     * @return 삭제 결과
     */
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String filename) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 파일명 유효성 검사
            if (filename == null || filename.trim().isEmpty()) {
                log.warn("유효하지 않은 파일명: {}", filename);
                response.put("success", false);
                response.put("message", "유효한 파일명이 필요합니다");
                response.put("code", "INVALID_FILENAME");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 삭제
            fileService.deleteFile(filename);
            log.info("파일 삭제 성공: {}", filename);

            response.put("success", true);
            response.put("message", "파일이 삭제되었습니다");
            response.put("filename", filename);
            response.put("code", "SUCCESS");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: {}", filename, e);
            response.put("success", false);
            response.put("message", "파일 삭제에 실패했습니다: " + e.getMessage());
            response.put("code", "DELETE_FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            response.put("success", false);
            response.put("message", "시스템 오류가 발생했습니다");
            response.put("code", "SYSTEM_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 파일 존재 여부 확인
     * @param filename 확인할 파일명
     * @return 존재 여부
     */
    @GetMapping("/exists/{filename}")
    public ResponseEntity<Map<String, Object>> fileExists(@PathVariable String filename) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean exists = fileService.fileExists(filename);
            response.put("success", true);
            response.put("filename", filename);
            response.put("exists", exists);
            response.put("code", "SUCCESS");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("파일 존재 확인 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "파일 확인에 실패했습니다");
            response.put("code", "CHECK_FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}