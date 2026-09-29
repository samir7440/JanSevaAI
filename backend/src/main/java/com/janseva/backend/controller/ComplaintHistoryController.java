package com.janseva.backend.controller;

import com.janseva.backend.model.ComplaintHistory;
import com.janseva.backend.repository.ComplaintHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class ComplaintHistoryController {


@Autowired
private ComplaintHistoryRepository repository;

@GetMapping("/{complaintId}")
public List<ComplaintHistory> getHistory(

        @PathVariable String complaintId

) {

    return repository
            .findByComplaintIdOrderByTimestampAsc(
                    complaintId
            );
}


}
