package com.example.lesson59.controller;

import com.example.lesson59.entity.Attachment;
import com.example.lesson59.model.Result;
import com.example.lesson59.service.impl.AttachmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URLEncoder;

@RequestMapping("/api/auth/file")
@RestController
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentServiceImpl attachmentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile multipartFile){
        Result result=attachmentService.saveAttachment(multipartFile);
        return ResponseEntity.status(result.isStatus()?200:403).body(result);
    }



    @GetMapping("/preview/{hashId}")
    public ResponseEntity<?> preview(@PathVariable String hashId) throws MalformedURLException {
        Attachment attachment = attachmentService.findAttachmentByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.EXPIRES, "inline; fileName=" + URLEncoder.encode(attachment.getFileName()))
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(new FileUrlResource(String.format("%s/%s.%s",
                        attachment.getUploadPath(),
                        attachment.getHashId(),
                        attachment.getExtension())));
    }

    @GetMapping("/download/{hashId}")
    public ResponseEntity<?> download(@PathVariable String hashId) throws MalformedURLException {
        Attachment attachment = attachmentService.findAttachmentByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "file; fileName=" + URLEncoder.encode(attachment.getFileName()))
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(new FileUrlResource(String.format("%s/%s.%s",
                        attachment.getUploadPath(),
                        attachment.getHashId(),
                        attachment.getExtension())));
    }
}
