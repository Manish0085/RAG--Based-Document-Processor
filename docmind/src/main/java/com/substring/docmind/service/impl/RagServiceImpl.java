package com.substring.docmind.service.impl;

import com.substring.docmind.config.AppProperties;
import com.substring.docmind.dto.*;
import com.substring.docmind.service.RagService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private Logger log = LoggerFactory.getLogger(RagService.class);
    private VectorStore vectorStore;
    private final AppProperties appProperties;
    private final ChatClient chatClient;

    public ChatResponseDto askQuestion(ChatRequestDto request) {
        long startTime = System.currentTimeMillis();
        log.info("Processing query: '{}', scoped documentId: {}", request.getQuestion(), request.getDocumentId());

        List<Document> similarDocuments = this.retrieveRelevantDocuments(
                request.getQuestion(),
                request.getDocumentId(),
                request.getTopK(),
                request.getMinSimilarity()
        );

        List<CitationDto> citations = similarDocuments.stream()
                .map(this::mapToCitation)
                .toList();

        String contextText = buildContextString(similarDocuments);
        String prompt = buildPrompt(request.getQuestion(), contextText);
        String answer = this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        long responseTime = System.currentTimeMillis() - startTime;
        log.info("Complete Q&A in {} ms with {} citations", responseTime, citations.size());
        return ChatResponseDto.builder()
                .answer(answer)
                .conversationId(request.getConversationId() != null? request.getConversationId(): UUID.randomUUID().toString())
                .citations(citations)
                .responseTimeMs(responseTime)
                .build();
    }


    public Flux<String> streamQuestionAnswer(ChatRequestDto request) {
        return null;
    }
    private String buildPrompt(String question, String contextText) {
        if (contextText != null || !contextText.isEmpty()) {
            return String.format("""
                    
                    """, contextText, question);
        }
        else {
            return String.format("""
                    
                    """, question);
        }
    }

    private String buildContextString(List<Document> similarDocuments) {
        if (similarDocuments == null || similarDocuments.isEmpty())
            return "";

        return similarDocuments.stream()
                .map(document -> {
                    String fileName = (String) document.getMetadata().getOrDefault("fileName", "Unknown file");
                    Object page = document.getMetadata().getOrDefault("pageNumber", "N/A");
                    return String.format("[Source %s | Page: %s]\n%s", fileName, page, document.getText());
                })
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    @Override
    public SearchResultDto searchSimilarChunks(SearchRequestDto requestDto) {

        List<Document> matchedDocs = retrieveRelevantDocuments(
                requestDto.getQuery(),
                requestDto.getDocumentId(),
                requestDto.getTopK(),
                requestDto.getSimilarityScore()
        );

        List<CitationDto> citations = matchedDocs.stream()
                .map(this::mapToCitation)
                .toList();

        return SearchResultDto.builder()
                .query(requestDto.getQuery())
                .totalMatches(citations.size())
                .matches(citations)
                .build();
    }

    private CitationDto mapToCitation(Document document) {
        Map<String, Object> meta = document.getMetadata();
        UUID docId  = null;
        if (meta.get("documentId") != null) {
            try {
                docId = UUID.fromString(meta.get("documentId").toString());
            } catch (Exception ignore) {}
        }

        Integer chunkIndex = null;
        if (meta.get("chunkIndex") instanceof Number n) {
            chunkIndex = n.intValue();
        }

        Integer pageNumber = null;
        if (meta.get("pageNumber") instanceof Number n) {
            pageNumber = n.intValue();
        }else if (meta.get("page_number") instanceof Number n) {
            pageNumber = n.intValue();
        }

        Double score = null;
        if (meta.get("distance") instanceof Number n) {
            score =  1.0 - n.doubleValue();
        }

        return CitationDto.builder()
                .documentId(docId)
                .fileName((String) meta.getOrDefault("fileName", "Unknown"))
                .chunkIndex(chunkIndex)
                .pageNumber(pageNumber)
                .snippet(document.getText())
                .similarityScore(score)
                .metadata(meta)
                .build();
    }

    private List<Document> retrieveRelevantDocuments(String query, UUID documentId, Integer topK, Double similarityScore) {
        int effectiveTopK = (topK != null && topK > 0)? topK: appProperties.getRag().getTopK();
        double effectiveSimilarityScore = (similarityScore != null)? similarityScore: appProperties.getRag().getSimilarityThreshold();

        SearchRequest.Builder searchRequestBuilder = SearchRequest.builder()
                .query(query)
                .topK(effectiveTopK);
        if (effectiveSimilarityScore > 0.0) {
            searchRequestBuilder.similarityThreshold(effectiveSimilarityScore);
        }

        if (documentId != null) {
            FilterExpressionBuilder builder = new FilterExpressionBuilder();
            searchRequestBuilder.filterExpression(
                    builder.eq("documentId", documentId.toString()).build()
            );
        }

        try {

            List<Document> documents = vectorStore.similaritySearch(searchRequestBuilder.build());
            log.info("Retrieved {} chunks for query: '{}' (scoped docId: {})", documents.size(), query, documentId);
            return documents;
        } catch (Exception ex) {
            log.error("Similarity search failed for query: '{}'", query, ex);
            return Collections.emptyList();
        }
    }
}
