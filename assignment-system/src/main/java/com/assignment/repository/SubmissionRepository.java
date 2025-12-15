package com.assignment.repository;

import com.assignment.entity.Assignment;
import com.assignment.entity.Submission;
import com.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignment(Assignment assignment);
    List<Submission> findByAssignmentId(Long assignmentId);
    List<Submission> findByUser(User user);
    List<Submission> findByAssignmentAndUser(Assignment assignment, User user);
    Optional<Submission> findByFilename(String filename);
}

