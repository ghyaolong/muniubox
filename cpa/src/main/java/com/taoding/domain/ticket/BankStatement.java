package com.taoding.domain.ticket;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BankStatement {
    private String id;

    private String lineNo;

    private String bookId;

    private String ticketId;

    private String bankId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date accountDate;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date statementDate;

    private Byte direction;

    private BigDecimal amount;
    
    private Byte deleted;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date created;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updated;
}