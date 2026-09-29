package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

@Autowired
private ComplaintRepository repository;

@GetMapping("/{keyword}")
public List<Complaint> searchComplaints(

        @PathVariable String keyword

) {

    List<Complaint> all =
            repository.findAll();

    List<Complaint> result =
            new ArrayList<>();

    String lower =
            keyword.toLowerCase();

    for (Complaint complaint : all) {

        boolean matched = false;

        if (

                complaint.getComplaintId() != null

                        &&

                        complaint.getComplaintId()
                                .toLowerCase()
                                .contains(lower)
        ) {

            matched = true;
        }

        if (

                complaint.getDescription() != null

                        &&

                        complaint.getDescription()
                                .toLowerCase()
                                .contains(lower)
        ) {

            matched = true;
        }

        if (

                complaint.getMainCategory() != null

                        &&

                        complaint.getMainCategory()
                                .toLowerCase()
                                .contains(lower)
        ) {

            matched = true;
        }

        if (matched) {

            result.add(
                    complaint
            );
        }
    }

    return result;
}


}
