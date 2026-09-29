package com.janseva.backend.controller;

import com.janseva.backend.model.ComplaintMedia;

import com.janseva.backend.repository.ComplaintMediaRepository;

import com.janseva.backend.service.FileStorageService;
import com.janseva.backend.service.LiveUpdateService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {


@Autowired
private ComplaintMediaRepository repository;

@Autowired
private LiveUpdateService liveUpdateService;

@Autowired
private FileStorageService fileStorageService;

@PostMapping(

        value = "/upload",

        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)

public Map<String, Object> uploadMedia(

        @RequestParam("file")
        MultipartFile file,

        @RequestParam("complaintId")
        String complaintId,

        @RequestParam("uploadedBy")
        String uploadedBy

) {

    String url =
            fileStorageService.saveFile(
                    file
            );

    ComplaintMedia media =
            new ComplaintMedia();

    media.setComplaintId(
            complaintId
    );

    media.setFileName(
            file.getOriginalFilename()
    );

    media.setFileType(
            file.getContentType()
    );

    media.setMediaUrl(
            url
    );

    media.setUploadedBy(
            uploadedBy
    );

    media.setUploadedAt(
            LocalDateTime.now()
    );

    ComplaintMedia saved =
            repository.save(
                    media
            );

    liveUpdateService.sendLiveUpdate(

            "NEW_MEDIA_UPLOADED",

            saved
    );

    return Map.of(

            "success", true,

            "url", url,

            "message",
            "Media uploaded successfully"
    );
}

@GetMapping("/{complaintId}")
public List<ComplaintMedia> getComplaintMedia(

        @PathVariable String complaintId

) {

    return repository
            .findByComplaintIdOrderByUploadedAtDesc(
                    complaintId
            );
}


}
