package com.assignment.service;

import com.assignment.dto.AssignmentDTO;
import com.assignment.entity.Assignment;
import com.assignment.entity.User;
import com.assignment.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserService userService;

    public Assignment createAssignment(AssignmentDTO assignmentDTO, Long teacherId) {
        User teacher = userService.getUserById(teacherId);

        Assignment assignment = Assignment.builder()
                .title(assignmentDTO.getTitle())
                .description(assignmentDTO.getDescription())
                .teacher(teacher)
                .dueDate(assignmentDTO.getDueDate())
                .isActive(true)
                .build();

        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Long assignmentId, AssignmentDTO assignmentDTO) {
        Assignment assignment = getAssignmentById(assignmentId);

        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());

        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("과제를 찾을 수 없습니다"));
    }

    public AssignmentDTO getAssignmentDTOById(Long id) {
        Assignment assignment = getAssignmentById(id);
        return convertToDTO(assignment);
    }

    public List<AssignmentDTO> getAllActiveAssignments() {
        return assignmentRepository.findAllByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AssignmentDTO> getAssignmentsByTeacher(Long teacherId) {
        User teacher = userService.getUserById(teacherId);
        return assignmentRepository.findByTeacherAndIsActiveTrue(teacher)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AssignmentDTO> getOverdueAssignments() {
        return assignmentRepository.findByDueDateBefore(LocalDateTime.now())
                .stream()
                .filter(Assignment::getIsActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAssignment(Long assignmentId) {
        Assignment assignment = getAssignmentById(assignmentId);
        assignment.setIsActive(false);
        assignmentRepository.save(assignment);
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        return AssignmentDTO.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .teacherId(assignment.getTeacher().getId())
                .teacherName(assignment.getTeacher().getFullName())
                .dueDate(assignment.getDueDate())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .isActive(assignment.getIsActive())
                .isOverdue(assignment.isOverdue())
                .build();
    }
}
