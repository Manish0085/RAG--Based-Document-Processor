package com.substring.docmind.service.impl;

import com.substring.docmind.exceptions.DocumentsProcessingException;
import com.substring.docmind.service.IDocumentParserService;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class DocumentParserService implements IDocumentParserService {

    private final static Logger log = LoggerFactory.getLogger(DocumentParserService.class);
    @Override
    public List<Document> parse(MultipartFile file) {

        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";

        log.info("Parsing document: {}, size: {} bytes, contentType: {}", filename, file.getSize(), contentType);


        try {

            Resource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public @Nullable String getFilename() {
                    return filename;
                }
            };


            if (filename.endsWith(".pdf") || filename.contains(".pdf")) {
                return parsePdf(resource);
            } else {
                return parseGenericFile(resource);
            }
        }
        catch (IOException ex) {
            log.error("Failed to read file bytes {}", filename, ex);
            throw new DocumentsProcessingException("Could not read uploaded file " + filename, ex);
        }
        catch (Exception ex) {
            log.error("Error during document parsing {}", filename, ex);
            throw new DocumentsProcessingException("Failed to parse document content" + filename, ex);
        }
    }

    private List<Document> parseGenericFile(Resource resource) {

        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
//                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                        .withNumberOfTopTextLinesToDelete(0)
//                        .build())
                .withPagesPerDocument(0)
                .build();

        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(resource, config);

        return documentReader.get() ;
    }

    private List<Document> parsePdf(Resource resource) {

        return null;
    }
}
