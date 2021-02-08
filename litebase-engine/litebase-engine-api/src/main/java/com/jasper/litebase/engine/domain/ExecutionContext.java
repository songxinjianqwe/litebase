package com.jasper.litebase.engine.domain;

import com.jasper.litebase.config.SessionConfig;

public class ExecutionContext {
    private Long queryId;
    private SessionConfig sessionConfig;

    public ExecutionContext(Long queryId, SessionConfig sessionConfig) {
        this.queryId = queryId;
        this.sessionConfig = sessionConfig;
    }

    public Long getQueryId() {
        return queryId;
    }

    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }
}
