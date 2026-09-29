package com.janseva.backend.controller;

import com.janseva.backend.model.AuditLog;

import com.janseva.backend.repository.AuditLogRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
@CrossOrigin(origins = "*")
public class AuditController {


@Autowired
private AuditLogRepository repository;

@GetMapping("/all")
public List<AuditLog> getLogs() {

    return repository
            .findAllByOrderByTimestampDesc();
}


}
