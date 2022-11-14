package com.example.lesson59.repository;

import com.example.lesson59.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Attachment findAttachmentByHashId (String hashId);
}
