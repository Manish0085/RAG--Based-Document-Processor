package com.substring.docmind.service;

import com.substring.docmind.dto.ChatRequestDto;
import com.substring.docmind.dto.ChatResponseDto;
import com.substring.docmind.dto.SearchRequestDto;
import com.substring.docmind.dto.SearchResultDto;
import reactor.core.publisher.Flux;

public interface RagService {

    ChatResponseDto askQuestion(ChatRequestDto request);

    Flux<String> streamQuestionAnswer(ChatRequestDto request);
    SearchResultDto searchSimilarChunks(SearchRequestDto requestDto);
}
