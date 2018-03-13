package com.taoding.domain.log;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LogEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作内容
	 */
	private final String content;
	
	/**
	 * 操作人登录名
	 */
    private final String operator;
    
    /**
     * 操作人ID
     */
    private final String operatorId;
    
    /**
     * 操作日期
     */
    private final Date date;
    
    /**
     * 操作人IP Address
     */
    private final String ipAddress;
    
    /**
     * 客户Id
     */
    private final String customerId;
    
    /**
     * 账户id
     */
    private final String accuntingId;

    private LogEvent(Object source) {
        super(source);
        this.content = null;
        this.operator = null;
        this.operatorId = null;
        this.date = null;
        this.ipAddress = null;
        this.customerId = null;
        this.accuntingId = null;
    }

    private LogEvent(Builder builder) {
        super(new Object());
        this.content = builder.content;
        this.operator = builder.operator;
        this.operatorId = builder.operatorId;
        this.date = builder.date;
        this.ipAddress = builder.ipAddress;
        this.customerId = builder.customerId;
        this.accuntingId = builder.accuntingId;
    }

    public static class Builder{
        private String content;
        private String operator;
        private String operatorId;
        private Date date;
        private String ipAddress;
        private String customerId;
        private String accuntingId;

        public Builder(){};

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder operatorId(String operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }
        
        public Builder customerId(String customerId) {
        	this.customerId = customerId;
        	return this;
        }
        
        public Builder accuntingId(String accuntingId) {
        	this.accuntingId = accuntingId;
        	return this;
        }

        public LogEvent build() {
            if (null == this.content) {
                throw new IllegalArgumentException("content 不能为空");
            }
            if (null == this.operator) {
                throw new IllegalArgumentException("operator 不能为空");
            }
            if (null == this.operatorId) {
                throw new IllegalArgumentException("operatorId 不能为空");
            }
            if (null == this.date) {
                throw new IllegalArgumentException("date 不能为空");
            }
            if (null == this.ipAddress) {
                throw new IllegalArgumentException("ipAddress 不能为空");
            }
            return new LogEvent(this);
        }
    }
}
