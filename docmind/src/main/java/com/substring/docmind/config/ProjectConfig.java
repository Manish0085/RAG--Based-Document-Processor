package com.substring.docmind.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("DocMind - AI Document Intelligence and RAG Based Question Answering API")
                                .description("""
                                        DocMind is an AI-powered document question answering system
                                        that allows users to upload large documents and ask questions
                                        about their content.

                                        The system uses Retrieval-Augmented Generation (RAG) to extract,
                                        chunk, embed, and retrieve relevant document content before
                                        generating accurate, context-aware answers using an LLM.
                                        """)
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("DocMind Team")
                                                .email("support@docmind.com")
                                                .url("http://docmind.in")
                                )
                );
    }
}