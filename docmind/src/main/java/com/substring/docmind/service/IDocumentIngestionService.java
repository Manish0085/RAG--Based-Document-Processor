package com.substring.docmind.service;

import com.substring.docmind.entity.DocumentMetadata;
import org.springframework.ai.document.Document;

import java.util.List;

public interface IDocumentIngestionService {

    public int ingest(DocumentMetadata documentMetadata, List<Document> parseDocs);
}
