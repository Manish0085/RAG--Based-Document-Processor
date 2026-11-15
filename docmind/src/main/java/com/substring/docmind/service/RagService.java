package com.substring.docmind.service;

import com.substring.docmind.dto.SearchRequestDto;
import com.substring.docmind.dto.SearchResultDto;

public interface RagService {

    public SearchResultDto searchSimilarChunks(SearchRequestDto requestDto);
}
