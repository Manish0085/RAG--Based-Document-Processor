package com.substring.docmind.service;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentParserService {

    public List<Document> parse(MultipartFile file);
}
