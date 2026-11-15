package com.substring.docmind.controller;

import com.substring.docmind.dto.ApiResponse;
import com.substring.docmind.dto.DocumentMetadataDto;
import com.substring.docmind.dto.DocumentResponseDto;
import com.substring.docmind.service.DocumentMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Management",
        description = "Endpoints for uploading, listing and managing documents and their vector embeddings"
)
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentMetadataService documentMetadataService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload and index a document",
            description = "Uploads a single document (DOCX, PDF, TEXT, CSV, MD), extracts its content, "
                    + "generates vector embeddings and stores the metadata."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Document uploaded and indexed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid or empty file / unsupported file type",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while processing the document",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @Parameter(
                    description = "Document file to upload (DOCX, PDF, TXT, CSV, MD)",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file
    ) {
        DocumentResponseDto documentResponseDto = documentMetadataService.uploadAndProcess(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DocumentResponseDto>builder()
                        .success(true)
                        .message("Document Uploaded successfully")
                        .data(documentResponseDto)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    // upload multiple documents
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload and index multiple documents",
            description = "Uploads several documents in one request. Each file is processed, "
                    + "embedded and stored individually."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Documents uploaded and indexed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "One or more files are invalid or unsupported",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while processing the documents",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> uploadMultiple(
            @Parameter(
                    description = "List of document files to upload (DOCX, PDF, TXT, CSV, MD)",
                    required = true,
                    array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<DocumentResponseDto> responseDtos = documentMetadataService.uploadMultipleDocuments(files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<List<DocumentResponseDto>>
                                        builder()
                                .message("Document uploaded successfully")
                                .success(true)
                                .timeStamp(LocalDateTime.now())
                                .data(responseDtos)
                                .build()
                );
    }

    @GetMapping
    @Operation(
            summary = "Get all documents",
            description = "Returns the metadata of every document that has been uploaded."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Documents fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<DocumentMetadataDto>>> getAllDocuments() {
        List<DocumentMetadataDto> documents = documentMetadataService.getAllDocuments();
        return ResponseEntity.ok(
                ApiResponse.<List<DocumentMetadataDto>>
                                builder()
                        .message("All Documents have been fetched")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(documents)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a document by ID",
            description = "Returns the metadata of a single document identified by its UUID."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Document fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Document not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<DocumentMetadataDto>> getDocumentById(
            @Parameter(
                    description = "Unique identifier (UUID) of the document",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID id
    ) {
        DocumentMetadataDto document = documentMetadataService.getDocumentById(id);
        return ResponseEntity.ok(
                ApiResponse.<DocumentMetadataDto>
                                builder()
                        .message("Document has been fetched successfully")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(document)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a document by ID",
            description = "Deletes the document and its associated vector embeddings."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Document deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Document not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteDocumentById(
            @Parameter(
                    description = "Unique identifier (UUID) of the document to delete",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
            @PathVariable UUID id
    ) {
        documentMetadataService.deleteDocumentById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>
                                builder()
                        .message("Document has been deleted successfully")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(null)
                        .build()
        );
    }
}