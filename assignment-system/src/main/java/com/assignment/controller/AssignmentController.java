package com.assignment.controller;

import com.assignment.dto.AssignmentDTO;
import com.assignment.entity.Assignment;
import com.assignment.entity.Submission;
import com.assignment.entity.User;
import com.assignment.service.AssignmentService;
import com.assignment.service.SubmissionService;
import com.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final UserService userService;
    private final SubmissionService submissionService;

    @GetMapping("/list")
    public String listAssignments(Model model, Authentication authentication) {
        List<AssignmentDTO> assignments = assignmentService.getAllActiveAssignments();
        model.addAttribute("assignments", assignments);
        return "assignment/list";
    }

    @GetMapping("/{id}")
    public String viewAssignment(@PathVariable Long id, Model model, Authentication authentication) {
        AssignmentDTO assignmentDTO = assignmentService.getAssignmentDTOById(id);
        model.addAttribute("assignment", assignmentDTO);

        // 선생님인 경우 제출 목록 조회
        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        
        if (isTeacher) {
            List<Submission> submissions = submissionService.getSubmissionsByAssignment(id);
            // 사용자별로 그룹화
            Map<String, List<Submission>> submissionsByUser = submissions.stream()
                    .collect(Collectors.groupingBy(s -> s.getUser().getFullName()));
            model.addAttribute("submissionsByUser", submissionsByUser);
        } else {
            // 학생인 경우 자신의 제출 목록 조회
            User user = userService.getUserByUsername(authentication.getName());
            List<Submission> mySubmissions = submissionService.getSubmissionsByUserAndAssignment(user.getId(), id);
            model.addAttribute("mySubmissions", mySubmissions);
            // 제출 여부 확인
            boolean hasSubmitted = !mySubmissions.isEmpty();
            model.addAttribute("hasSubmitted", hasSubmitted);
        }

        return "assignment/assignment_detail";
    }

    @GetMapping("/create")
    public String createPage(Authentication authentication) {
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/assignment/list";
        }
        return "assignment/create";
    }

    @PostMapping("/create")
    public String createAssignment(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String dueDate,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // 권한 체크: 선생님만 과제 생성 가능
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            redirectAttributes.addFlashAttribute("error", "과제 생성 권한이 없습니다");
            return "redirect:/assignment/list";
        }

        try {
            User user = userService.getUserByUsername(authentication.getName());

            AssignmentDTO assignmentDTO = AssignmentDTO.builder()
                    .title(title)
                    .description(description)
                    .dueDate(LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            assignmentService.createAssignment(assignmentDTO, user.getId());
            redirectAttributes.addFlashAttribute("success", "과제가 생성되었습니다");
            return "redirect:/assignment/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/assignment/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model, Authentication authentication) {
        // 권한 체크: 선생님만 과제 수정 가능
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/assignment/list";
        }

        Assignment assignment = assignmentService.getAssignmentById(id);

        User user = userService.getUserByUsername(authentication.getName());
        if (!assignment.getTeacher().getId().equals(user.getId())) {
            return "redirect:/assignment/list";
        }

        model.addAttribute("assignment", assignment);
        return "assignment/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateAssignment(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String dueDate,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // 권한 체크: 선생님만 과제 수정 가능
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            redirectAttributes.addFlashAttribute("error", "과제 수정 권한이 없습니다");
            return "redirect:/assignment/list";
        }

        try {
            AssignmentDTO assignmentDTO = AssignmentDTO.builder()
                    .title(title)
                    .description(description)
                    .dueDate(LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            assignmentService.updateAssignment(id, assignmentDTO);
            redirectAttributes.addFlashAttribute("success", "과제가 수정되었습니다");
            return "redirect:/assignment/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/assignment/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteAssignment(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        // 권한 체크: 선생님만 과제 삭제 가능
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            redirectAttributes.addFlashAttribute("error", "과제 삭제 권한이 없습니다");
            return "redirect:/assignment/list";
        }

        try {
            assignmentService.deleteAssignment(id);
            redirectAttributes.addFlashAttribute("success", "과제가 삭제되었습니다");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assignment/list";
    }
}
