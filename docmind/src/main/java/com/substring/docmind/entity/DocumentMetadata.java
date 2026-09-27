package com.substring.docmind.entity;

import com.substring.docmind.enums.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_metadata")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Filename is required")
    @Size(max = 255, message = "Filename must not exceed 255 characters")
    @Column(nullable = false)
    private String filename;

    @NotBlank(message = "Content type is required")
    @Size(max = 100, message = "Content type must not exceed 100 characters")
    @Column(nullable = false)
    private String contentType;

    @NotNull(message = "File size is required")
    @Positive(message = "File size must be greater than 0")
    @Column(nullable = false)
    private Long fileSize;

    @NotNull(message = "Total pages is required")
    @PositiveOrZero(message = "Total pages cannot be negative")
    private Integer totalPages;

    @NotNull(message = "Total chunks is required")
    @PositiveOrZero(message = "Total chunks cannot be negative")
    private Integer totalChunks;

    @NotNull(message = "Document status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Size(max = 1000, message = "Error message must not exceed 1000 characters")
    private String errorMessage;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}