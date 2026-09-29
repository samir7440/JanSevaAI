package com.janseva.backend.repository;

import com.janseva.backend.model.ComplaintMedia;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintMediaRepository
extends MongoRepository<ComplaintMedia, String> {


List<ComplaintMedia>
findByComplaintIdOrderByUploadedAtDesc(
        String complaintId
);


}
