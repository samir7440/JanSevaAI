package com.janseva.backend.repository;

import com.janseva.backend.model.InternalNote;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalNoteRepository
extends MongoRepository<InternalNote, String> {


List<InternalNote>
findByComplaintIdOrderByTimestampDesc(
        String complaintId
);


}
