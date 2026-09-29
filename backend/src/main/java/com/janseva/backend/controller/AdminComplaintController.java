package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/complaints")
@CrossOrigin(origins = "*")
public class AdminComplaintController {


@Autowired
private ComplaintRepository repository;

@GetMapping("/all")
public List<Complaint> allComplaints() {

    return repository.findAll();
}

@GetMapping("/status/{status}")
public List<Complaint> byStatus(

        @PathVariable String status

) {

    return repository
            .findByStatusIgnoreCase(
                    status
            );
}

@GetMapping("/priority/{priority}")
public List<Complaint> byPriority(

        @PathVariable String priority

) {

    return repository
            .findByPriorityIgnoreCase(
                    priority
            );
}

@GetMapping("/category/{category}")
public List<Complaint> byCategory(

        @PathVariable String category

) {

    return repository
            .findByMainCategoryIgnoreCase(
                    category
            );
}

@GetMapping("/level/{level}")
public List<Complaint> byLevel(

        @PathVariable String level

) {

    return repository
            .findByCurrentLevelIgnoreCase(
                    level
            );
}


}
