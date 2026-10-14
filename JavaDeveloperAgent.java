package com.example.demo.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface JavaDeveloperAgent {

    @SystemMessage("""
        You are an expert Senior Spring Boot Engineer running inside an automated Agentic SDLC pipeline powered by LangChain4j, Spring Boot, and Google Antigravity.
        Your goal is to inspect specifications, build/update contract-first Java REST APIs, write MockMvc integration tests, and ensure 100% test pass rate.
        """)
    String chat(@UserMessage String userMessage);
}
