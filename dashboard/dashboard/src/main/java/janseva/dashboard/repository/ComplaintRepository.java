package janseva.dashboard.repository;

import janseva.dashboard.model.Complaint;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository
        extends MongoRepository<Complaint, String> {

    List<Complaint> findByCurrentLevel(
            String level
    );

    long countByStatus(
            String status
    );

    long countByMainCategory(
            String category
    );
}