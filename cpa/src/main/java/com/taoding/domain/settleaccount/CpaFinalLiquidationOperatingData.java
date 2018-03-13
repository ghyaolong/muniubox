package com.taoding.domain.settleaccount;

import java.math.BigDecimal;
import java.util.Date;

public class CpaFinalLiquidationOperatingData {
    private String id;

    private String bookId;

    private String customerId;

    private Date currentPeriod;

    private String subKey;

    private BigDecimal proportion;

    private String createBy;

    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId == null ? null : bookId.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public Date getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(Date currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey == null ? null : subKey.trim();
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}