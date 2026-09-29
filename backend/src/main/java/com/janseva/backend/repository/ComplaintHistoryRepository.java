package com.janseva.backend.repository;

import com.janseva.backend.model.ComplaintHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintHistoryRepository
extends MongoRepository<ComplaintHistory, String> {


List<ComplaintHistory>
findByComplaintIdOrderByTimestampAsc(
        String complaintId
);


}
