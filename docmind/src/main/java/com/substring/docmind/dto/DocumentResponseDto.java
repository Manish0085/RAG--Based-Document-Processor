package com.substring.docmind.dto;

import com.substring.docmind.entity.DocumentMetadata;
import com.substring.docmind.enums.DocumentStatus;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDto
{

    private UUID id;
    private String filename;
    private Long fileSize;
    private DocumentStatus status;
    private Integer chunkCreated;
    private String message;
}
