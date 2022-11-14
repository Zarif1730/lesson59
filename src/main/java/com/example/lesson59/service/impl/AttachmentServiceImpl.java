package com.example.lesson59.service.impl;

import com.example.lesson59.entity.Attachment;
import com.example.lesson59.model.Result;
import com.example.lesson59.repository.AttachmentRepository;
import com.example.lesson59.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Value("${upload.folder}")
    private String uploadFolder;

    @Override
    public Result saveAttachment(MultipartFile multipartFile){ //StudentPayload
        try {
            Attachment attachment=new Attachment();
            attachment.setContentType(multipartFile.getContentType());
            attachment.setFileName(multipartFile.getOriginalFilename().toLowerCase()); //  file1.jpg
            System.out.println(multipartFile.getName()+" "+multipartFile.getOriginalFilename());
            attachment.setFileSize(multipartFile.getSize()/(8*1024));
            attachment.setHashId(UUID.randomUUID().toString());
            attachment.setExtension(getExtension(multipartFile.getOriginalFilename()).toLowerCase());

            LocalDate date=LocalDate.now();

            String uploadPath=String.format("%s/%d/%d/%d/%s",  // hashId.jpg
                    uploadFolder,
                    date.getYear(),
                    date.getMonthValue(),
                    date.getDayOfMonth(),
                    attachment.getExtension()
            );
            File file=new File(uploadPath);

            if (!file.exists()){
                file.mkdirs();
            }

            attachment.setUploadPath(uploadPath);

            attachment.setLink(String.format("%s/%s.%s",
                    file.getAbsolutePath(),  // download/2022/10/28/jpg
                    attachment.getHashId(),
                    attachment.getExtension()
            ));

            attachmentRepository.save(attachment);

            multipartFile.transferTo(new File(attachment.getLink()));

            return new Result("success",true,attachment,null);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new Result("failed",false,null,e.getMessage());
        }
    }

    public String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    @Override
    public Attachment findAttachmentByHashId(String hashId){
        return attachmentRepository.findAttachmentByHashId(hashId);
    }

    @Override
    public boolean deleteAttachmentByHashId(String hashId){
        try {
            Attachment attachment= attachmentRepository.findAttachmentByHashId(hashId);

            File file=new File(attachment.getLink());
            System.out.println( file.delete());
            attachmentRepository.deleteById(attachment.getId());
            return  true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }

    }
}
