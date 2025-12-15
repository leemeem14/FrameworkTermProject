package com.assignment.repository;

import com.assignment.entity.Assignment;
import com.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTeacher(User teacher);
    List<Assignment> findByTeacherAndIsActiveTrue(User teacher);
    List<Assignment> findByDueDateAfter(LocalDateTime date);
    List<Assignment> findByDueDateBefore(LocalDateTime date);
    List<Assignment> findAllByIsActiveTrue();
}
