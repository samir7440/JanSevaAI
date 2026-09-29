package com.janseva.backend.service;

import com.janseva.backend.model.AuditLog;

import com.janseva.backend.repository.AuditLogRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {


@Autowired
private AuditLogRepository repository;

public void log(

        String action,

        String performedBy,

        String targetId,

        String details

) {

    AuditLog log =
            new AuditLog();

    log.setAction(
            action
    );

    log.setPerformedBy(
            performedBy
    );

    log.setTargetId(
            targetId
    );

    log.setDetails(
            details
    );

    log.setTimestamp(
            LocalDateTime.now()
    );

    repository.save(
            log
    );
}


}
