package com.example.lesson59.service;

import com.example.lesson59.entity.Attachment;
import com.example.lesson59.model.Result;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Result saveAttachment(MultipartFile multipartFile);
    Attachment findAttachmentByHashId(String hashId);  // public
    boolean deleteAttachmentByHashId(String hashId);
}
