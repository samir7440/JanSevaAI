package com.janseva.backend.controller;

import com.janseva.backend.model.InternalNote;

import com.janseva.backend.repository.InternalNoteRepository;

import com.janseva.backend.service.LiveUpdateService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/officer/notes")
@CrossOrigin(origins = "*")
public class InternalNoteController {


@Autowired
private InternalNoteRepository repository;

@Autowired
private LiveUpdateService liveUpdateService;

@PostMapping("/add")
public Map<String, Object> addNote(

        @RequestBody InternalNote note

) {

    note.setTimestamp(
            LocalDateTime.now()
    );

    InternalNote saved =
            repository.save(
                    note
            );

    liveUpdateService.sendLiveUpdate(

            "NEW_INTERNAL_NOTE",

            saved
    );

    return Map.of(

            "success", true,

            "message",
            "Internal note added"
    );
}

@GetMapping("/{complaintId}")
public List<InternalNote> getNotes(

        @PathVariable String complaintId

) {

    return repository
            .findByComplaintIdOrderByTimestampDesc(
                    complaintId
            );
}


}
