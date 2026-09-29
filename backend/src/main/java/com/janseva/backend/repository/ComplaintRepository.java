package com.janseva.backend.repository;

import com.janseva.backend.model.Complaint;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository
extends MongoRepository<Complaint, String> {


List<Complaint> findByUserMobile(
        String userMobile
);

List<Complaint>
findByUserMobileOrderByCreatedAtDesc(
        String mobile
);

Complaint findByComplaintId(
        String complaintId
);

long countByStatus(
        String status
);

long countByCurrentLevel(
        String currentLevel
);

List<Complaint>
findByAssignedPersonContact(
        String contact
);

List<Complaint>
findByAssignedPersonContactAndStatusIgnoreCase(

        String contact,

        String status
);

List<Complaint>
findByPriorityIgnoreCase(
        String priority
);

List<Complaint>
findByStatusIgnoreCase(
        String status
);

List<Complaint>
findByMainCategoryIgnoreCase(
        String category
);

List<Complaint>
findByCurrentLevelIgnoreCase(
        String level
);


}
