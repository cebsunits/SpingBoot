package com.tao.hai.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.web.server.PortInUseException;

/**
 * 端口占用异常统计
 * */
public class PortInUseFailureAnalyzer extends AbstractFailureAnalyzer<PortInUseException> {
    private Logger logger= LoggerFactory.getLogger(PortInUseFailureAnalyzer.class);
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, PortInUseException cause) {
        logger.error("端口被占用：",cause);
        System.out.println("端口被占用："+cause);
        return new FailureAnalysis("端口号："+cause.getPort()+"被占用！","PortInUseException",rootFailure);
    }
}
