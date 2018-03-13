package com.taoding.service.log;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.taoding.domain.log.LogEvent;

@Component
public class LogListener implements ApplicationListener<LogEvent> {

    @Override
    public void onApplicationEvent(LogEvent logEvent) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("\noperator:\t" + logEvent.getOperator());
        strBuffer.append("\noperatorId:\t" + logEvent.getOperatorId());
        strBuffer.append("\ncontent:\t" + logEvent.getContent());
        strBuffer.append("\ndate:\t" + logEvent.getDate());
        strBuffer.append("\nipAddress:\t" + logEvent.getIpAddress());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("****************************");
        System.out.println(strBuffer.toString());
        System.out.println();
    }
    public String toString() {
        return this.getClass().toString();
    }
}