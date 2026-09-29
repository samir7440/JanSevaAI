package com.janseva.backend.repository;

import com.janseva.backend.model.Officer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficerRepository
        extends MongoRepository<Officer, String> {

    Officer findFirstByDepartmentIgnoreCaseAndLevelIgnoreCaseAndAvailableTrue(

            String department,

            String level
    );
} 