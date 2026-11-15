package com.substring.docmind.repository;

import com.substring.docmind.entity.DocumentMetadata;
import com.substring.docmind.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, UUID> {
    List<DocumentMetadata> findAllByStatus(DocumentStatus status);
    List<DocumentMetadata> findAllByOrderByCreatedAtDesc();
}
