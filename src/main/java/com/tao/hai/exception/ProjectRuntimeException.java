package com.tao.hai.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

//@Slf4j
public class ProjectRuntimeException extends AbstractFailureAnalyzer<RuntimeException> {
    private Logger logger= LoggerFactory.getLogger(ProjectRuntimeException.class);
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RuntimeException cause) {
        logger.error("运行时异常：",cause);
        return new FailureAnalysis(cause.getMessage(),"error",rootFailure);
    }
}
