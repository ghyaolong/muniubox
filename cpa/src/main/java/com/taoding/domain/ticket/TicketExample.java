package com.taoding.domain.ticket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TicketExample {
    protected String orderByClause;

    protected boolean distinct;
    
    protected String bookId;

	protected List<Criteria> oredCriteria;

    public TicketExample() {
        oredCriteria = new ArrayList<Criteria>();
    }
    
    public void setBookId(String bookId) {
    	this.bookId = bookId;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTicketNoIsNull() {
            addCriterion("ticket_no is null");
            return (Criteria) this;
        }

        public Criteria andTicketNoIsNotNull() {
            addCriterion("ticket_no is not null");
            return (Criteria) this;
        }

        public Criteria andTicketNoEqualTo(String value) {
            addCriterion("ticket_no =", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoNotEqualTo(String value) {
            addCriterion("ticket_no <>", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoGreaterThan(String value) {
            addCriterion("ticket_no >", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoGreaterThanOrEqualTo(String value) {
            addCriterion("ticket_no >=", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoLessThan(String value) {
            addCriterion("ticket_no <", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoLessThanOrEqualTo(String value) {
            addCriterion("ticket_no <=", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoLike(String value) {
            addCriterion("ticket_no like", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoNotLike(String value) {
            addCriterion("ticket_no not like", value, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoIn(List<String> values) {
            addCriterion("ticket_no in", values, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoNotIn(List<String> values) {
            addCriterion("ticket_no not in", values, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoBetween(String value1, String value2) {
            addCriterion("ticket_no between", value1, value2, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andTicketNoNotBetween(String value1, String value2) {
            addCriterion("ticket_no not between", value1, value2, "ticketNo");
            return (Criteria) this;
        }

        public Criteria andVoucherIdIsNull() {
            addCriterion("voucher_id is null");
            return (Criteria) this;
        }

        public Criteria andVoucherIdIsNotNull() {
            addCriterion("voucher_id is not null");
            return (Criteria) this;
        }

        public Criteria andVoucherIdEqualTo(String value) {
            addCriterion("voucher_id =", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdNotEqualTo(String value) {
            addCriterion("voucher_id <>", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdGreaterThan(String value) {
            addCriterion("voucher_id >", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdGreaterThanOrEqualTo(String value) {
            addCriterion("voucher_id >=", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdLessThan(String value) {
            addCriterion("voucher_id <", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdLessThanOrEqualTo(String value) {
            addCriterion("voucher_id <=", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdLike(String value) {
            addCriterion("voucher_id like", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdNotLike(String value) {
            addCriterion("voucher_id not like", value, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdIn(List<String> values) {
            addCriterion("voucher_id in", values, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdNotIn(List<String> values) {
            addCriterion("voucher_id not in", values, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdBetween(String value1, String value2) {
            addCriterion("voucher_id between", value1, value2, "voucherId");
            return (Criteria) this;
        }

        public Criteria andVoucherIdNotBetween(String value1, String value2) {
            addCriterion("voucher_id not between", value1, value2, "voucherId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIsNull() {
            addCriterion("customer_id is null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIsNotNull() {
            addCriterion("customer_id is not null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdEqualTo(String value) {
            addCriterion("customer_id =", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotEqualTo(String value) {
            addCriterion("customer_id <>", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThan(String value) {
            addCriterion("customer_id >", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThanOrEqualTo(String value) {
            addCriterion("customer_id >=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThan(String value) {
            addCriterion("customer_id <", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThanOrEqualTo(String value) {
            addCriterion("customer_id <=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLike(String value) {
            addCriterion("customer_id like", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotLike(String value) {
            addCriterion("customer_id not like", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIn(List<String> values) {
            addCriterion("customer_id in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotIn(List<String> values) {
            addCriterion("customer_id not in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdBetween(String value1, String value2) {
            addCriterion("customer_id between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotBetween(String value1, String value2) {
            addCriterion("customer_id not between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andBookIdIsNull() {
            addCriterion("book_id is null");
            return (Criteria) this;
        }

        public Criteria andBookIdIsNotNull() {
            addCriterion("book_id is not null");
            return (Criteria) this;
        }

        public Criteria andBookIdEqualTo(String value) {
            addCriterion("book_id =", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotEqualTo(String value) {
            addCriterion("book_id <>", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdGreaterThan(String value) {
            addCriterion("book_id >", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdGreaterThanOrEqualTo(String value) {
            addCriterion("book_id >=", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdLessThan(String value) {
            addCriterion("book_id <", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdLessThanOrEqualTo(String value) {
            addCriterion("book_id <=", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdLike(String value) {
            addCriterion("book_id like", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotLike(String value) {
            addCriterion("book_id not like", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdIn(List<String> values) {
            addCriterion("book_id in", values, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotIn(List<String> values) {
            addCriterion("book_id not in", values, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdBetween(String value1, String value2) {
            addCriterion("book_id between", value1, value2, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotBetween(String value1, String value2) {
            addCriterion("book_id not between", value1, value2, "bookId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdIsNull() {
            addCriterion("accounting_id is null");
            return (Criteria) this;
        }

        public Criteria andAccountingIdIsNotNull() {
            addCriterion("accounting_id is not null");
            return (Criteria) this;
        }

        public Criteria andAccountingIdEqualTo(String value) {
            addCriterion("accounting_id =", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdNotEqualTo(String value) {
            addCriterion("accounting_id <>", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdGreaterThan(String value) {
            addCriterion("accounting_id >", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdGreaterThanOrEqualTo(String value) {
            addCriterion("accounting_id >=", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdLessThan(String value) {
            addCriterion("accounting_id <", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdLessThanOrEqualTo(String value) {
            addCriterion("accounting_id <=", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdLike(String value) {
            addCriterion("accounting_id like", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdNotLike(String value) {
            addCriterion("accounting_id not like", value, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdIn(List<String> values) {
            addCriterion("accounting_id in", values, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdNotIn(List<String> values) {
            addCriterion("accounting_id not in", values, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdBetween(String value1, String value2) {
            addCriterion("accounting_id between", value1, value2, "accountingId");
            return (Criteria) this;
        }

        public Criteria andAccountingIdNotBetween(String value1, String value2) {
            addCriterion("accounting_id not between", value1, value2, "accountingId");
            return (Criteria) this;
        }

        public Criteria andListIdIsNull() {
            addCriterion("list_id is null");
            return (Criteria) this;
        }

        public Criteria andListIdIsNotNull() {
            addCriterion("list_id is not null");
            return (Criteria) this;
        }

        public Criteria andListIdEqualTo(String value) {
            addCriterion("list_id =", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdNotEqualTo(String value) {
            addCriterion("list_id <>", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdGreaterThan(String value) {
            addCriterion("list_id >", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdGreaterThanOrEqualTo(String value) {
            addCriterion("list_id >=", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdLessThan(String value) {
            addCriterion("list_id <", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdLessThanOrEqualTo(String value) {
            addCriterion("list_id <=", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdLike(String value) {
            addCriterion("list_id like", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdNotLike(String value) {
            addCriterion("list_id not like", value, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdIn(List<String> values) {
            addCriterion("list_id in", values, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdNotIn(List<String> values) {
            addCriterion("list_id not in", values, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdBetween(String value1, String value2) {
            addCriterion("list_id between", value1, value2, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdNotBetween(String value1, String value2) {
            addCriterion("list_id not between", value1, value2, "listId");
            return (Criteria) this;
        }

        public Criteria andListIdsIsNull() {
            addCriterion("list_ids is null");
            return (Criteria) this;
        }

        public Criteria andListIdsIsNotNull() {
            addCriterion("list_ids is not null");
            return (Criteria) this;
        }

        public Criteria andListIdsEqualTo(String value) {
            addCriterion("list_ids =", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsNotEqualTo(String value) {
            addCriterion("list_ids <>", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsGreaterThan(String value) {
            addCriterion("list_ids >", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsGreaterThanOrEqualTo(String value) {
            addCriterion("list_ids >=", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsLessThan(String value) {
            addCriterion("list_ids <", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsLessThanOrEqualTo(String value) {
            addCriterion("list_ids <=", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsLike(String value) {
            addCriterion("list_ids like", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsNotLike(String value) {
            addCriterion("list_ids not like", value, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsIn(List<String> values) {
            addCriterion("list_ids in", values, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsNotIn(List<String> values) {
            addCriterion("list_ids not in", values, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsBetween(String value1, String value2) {
            addCriterion("list_ids between", value1, value2, "listIds");
            return (Criteria) this;
        }

        public Criteria andListIdsNotBetween(String value1, String value2) {
            addCriterion("list_ids not between", value1, value2, "listIds");
            return (Criteria) this;
        }

        public Criteria andSubjectContentIsNull() {
            addCriterion("subject_content is null");
            return (Criteria) this;
        }

        public Criteria andSubjectContentIsNotNull() {
            addCriterion("subject_content is not null");
            return (Criteria) this;
        }

        public Criteria andSubjectContentEqualTo(String value) {
            addCriterion("subject_content =", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentNotEqualTo(String value) {
            addCriterion("subject_content <>", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentGreaterThan(String value) {
            addCriterion("subject_content >", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentGreaterThanOrEqualTo(String value) {
            addCriterion("subject_content >=", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentLessThan(String value) {
            addCriterion("subject_content <", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentLessThanOrEqualTo(String value) {
            addCriterion("subject_content <=", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentLike(String value) {
            addCriterion("subject_content like", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentNotLike(String value) {
            addCriterion("subject_content not like", value, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentIn(List<String> values) {
            addCriterion("subject_content in", values, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentNotIn(List<String> values) {
            addCriterion("subject_content not in", values, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentBetween(String value1, String value2) {
            addCriterion("subject_content between", value1, value2, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andSubjectContentNotBetween(String value1, String value2) {
            addCriterion("subject_content not between", value1, value2, "subjectContent");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTicketKeyIsNull() {
            addCriterion("ticket_key is null");
            return (Criteria) this;
        }

        public Criteria andTicketKeyIsNotNull() {
            addCriterion("ticket_key is not null");
            return (Criteria) this;
        }

        public Criteria andTicketKeyEqualTo(String value) {
            addCriterion("ticket_key =", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyNotEqualTo(String value) {
            addCriterion("ticket_key <>", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyGreaterThan(String value) {
            addCriterion("ticket_key >", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyGreaterThanOrEqualTo(String value) {
            addCriterion("ticket_key >=", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyLessThan(String value) {
            addCriterion("ticket_key <", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyLessThanOrEqualTo(String value) {
            addCriterion("ticket_key <=", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyLike(String value) {
            addCriterion("ticket_key like", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyNotLike(String value) {
            addCriterion("ticket_key not like", value, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyIn(List<String> values) {
            addCriterion("ticket_key in", values, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyNotIn(List<String> values) {
            addCriterion("ticket_key not in", values, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyBetween(String value1, String value2) {
            addCriterion("ticket_key between", value1, value2, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketKeyNotBetween(String value1, String value2) {
            addCriterion("ticket_key not between", value1, value2, "ticketKey");
            return (Criteria) this;
        }

        public Criteria andTicketCodeIsNull() {
            addCriterion("ticket_code is null");
            return (Criteria) this;
        }

        public Criteria andTicketCodeIsNotNull() {
            addCriterion("ticket_code is not null");
            return (Criteria) this;
        }

        public Criteria andTicketCodeEqualTo(String value) {
            addCriterion("ticket_code =", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeNotEqualTo(String value) {
            addCriterion("ticket_code <>", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeGreaterThan(String value) {
            addCriterion("ticket_code >", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ticket_code >=", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeLessThan(String value) {
            addCriterion("ticket_code <", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeLessThanOrEqualTo(String value) {
            addCriterion("ticket_code <=", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeLike(String value) {
            addCriterion("ticket_code like", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeNotLike(String value) {
            addCriterion("ticket_code not like", value, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeIn(List<String> values) {
            addCriterion("ticket_code in", values, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeNotIn(List<String> values) {
            addCriterion("ticket_code not in", values, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeBetween(String value1, String value2) {
            addCriterion("ticket_code between", value1, value2, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCodeNotBetween(String value1, String value2) {
            addCriterion("ticket_code not between", value1, value2, "ticketCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeIsNull() {
            addCriterion("ticket_check_code is null");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeIsNotNull() {
            addCriterion("ticket_check_code is not null");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeEqualTo(String value) {
            addCriterion("ticket_check_code =", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeNotEqualTo(String value) {
            addCriterion("ticket_check_code <>", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeGreaterThan(String value) {
            addCriterion("ticket_check_code >", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ticket_check_code >=", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeLessThan(String value) {
            addCriterion("ticket_check_code <", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeLessThanOrEqualTo(String value) {
            addCriterion("ticket_check_code <=", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeLike(String value) {
            addCriterion("ticket_check_code like", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeNotLike(String value) {
            addCriterion("ticket_check_code not like", value, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeIn(List<String> values) {
            addCriterion("ticket_check_code in", values, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeNotIn(List<String> values) {
            addCriterion("ticket_check_code not in", values, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeBetween(String value1, String value2) {
            addCriterion("ticket_check_code between", value1, value2, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andTicketCheckCodeNotBetween(String value1, String value2) {
            addCriterion("ticket_check_code not between", value1, value2, "ticketCheckCode");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPayerNameIsNull() {
            addCriterion("payer_name is null");
            return (Criteria) this;
        }

        public Criteria andPayerNameIsNotNull() {
            addCriterion("payer_name is not null");
            return (Criteria) this;
        }

        public Criteria andPayerNameEqualTo(String value) {
            addCriterion("payer_name =", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameNotEqualTo(String value) {
            addCriterion("payer_name <>", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameGreaterThan(String value) {
            addCriterion("payer_name >", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameGreaterThanOrEqualTo(String value) {
            addCriterion("payer_name >=", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameLessThan(String value) {
            addCriterion("payer_name <", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameLessThanOrEqualTo(String value) {
            addCriterion("payer_name <=", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameLike(String value) {
            addCriterion("payer_name like", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameNotLike(String value) {
            addCriterion("payer_name not like", value, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameIn(List<String> values) {
            addCriterion("payer_name in", values, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameNotIn(List<String> values) {
            addCriterion("payer_name not in", values, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameBetween(String value1, String value2) {
            addCriterion("payer_name between", value1, value2, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerNameNotBetween(String value1, String value2) {
            addCriterion("payer_name not between", value1, value2, "payerName");
            return (Criteria) this;
        }

        public Criteria andPayerCodeIsNull() {
            addCriterion("payer_code is null");
            return (Criteria) this;
        }

        public Criteria andPayerCodeIsNotNull() {
            addCriterion("payer_code is not null");
            return (Criteria) this;
        }

        public Criteria andPayerCodeEqualTo(String value) {
            addCriterion("payer_code =", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeNotEqualTo(String value) {
            addCriterion("payer_code <>", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeGreaterThan(String value) {
            addCriterion("payer_code >", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeGreaterThanOrEqualTo(String value) {
            addCriterion("payer_code >=", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeLessThan(String value) {
            addCriterion("payer_code <", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeLessThanOrEqualTo(String value) {
            addCriterion("payer_code <=", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeLike(String value) {
            addCriterion("payer_code like", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeNotLike(String value) {
            addCriterion("payer_code not like", value, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeIn(List<String> values) {
            addCriterion("payer_code in", values, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeNotIn(List<String> values) {
            addCriterion("payer_code not in", values, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeBetween(String value1, String value2) {
            addCriterion("payer_code between", value1, value2, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerCodeNotBetween(String value1, String value2) {
            addCriterion("payer_code not between", value1, value2, "payerCode");
            return (Criteria) this;
        }

        public Criteria andPayerAddressIsNull() {
            addCriterion("payer_address is null");
            return (Criteria) this;
        }

        public Criteria andPayerAddressIsNotNull() {
            addCriterion("payer_address is not null");
            return (Criteria) this;
        }

        public Criteria andPayerAddressEqualTo(String value) {
            addCriterion("payer_address =", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressNotEqualTo(String value) {
            addCriterion("payer_address <>", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressGreaterThan(String value) {
            addCriterion("payer_address >", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressGreaterThanOrEqualTo(String value) {
            addCriterion("payer_address >=", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressLessThan(String value) {
            addCriterion("payer_address <", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressLessThanOrEqualTo(String value) {
            addCriterion("payer_address <=", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressLike(String value) {
            addCriterion("payer_address like", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressNotLike(String value) {
            addCriterion("payer_address not like", value, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressIn(List<String> values) {
            addCriterion("payer_address in", values, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressNotIn(List<String> values) {
            addCriterion("payer_address not in", values, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressBetween(String value1, String value2) {
            addCriterion("payer_address between", value1, value2, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAddressNotBetween(String value1, String value2) {
            addCriterion("payer_address not between", value1, value2, "payerAddress");
            return (Criteria) this;
        }

        public Criteria andPayerAccountIsNull() {
            addCriterion("payer_account is null");
            return (Criteria) this;
        }

        public Criteria andPayerAccountIsNotNull() {
            addCriterion("payer_account is not null");
            return (Criteria) this;
        }

        public Criteria andPayerAccountEqualTo(String value) {
            addCriterion("payer_account =", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountNotEqualTo(String value) {
            addCriterion("payer_account <>", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountGreaterThan(String value) {
            addCriterion("payer_account >", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountGreaterThanOrEqualTo(String value) {
            addCriterion("payer_account >=", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountLessThan(String value) {
            addCriterion("payer_account <", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountLessThanOrEqualTo(String value) {
            addCriterion("payer_account <=", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountLike(String value) {
            addCriterion("payer_account like", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountNotLike(String value) {
            addCriterion("payer_account not like", value, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountIn(List<String> values) {
            addCriterion("payer_account in", values, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountNotIn(List<String> values) {
            addCriterion("payer_account not in", values, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountBetween(String value1, String value2) {
            addCriterion("payer_account between", value1, value2, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayerAccountNotBetween(String value1, String value2) {
            addCriterion("payer_account not between", value1, value2, "payerAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeNameIsNull() {
            addCriterion("payee_name is null");
            return (Criteria) this;
        }

        public Criteria andPayeeNameIsNotNull() {
            addCriterion("payee_name is not null");
            return (Criteria) this;
        }

        public Criteria andPayeeNameEqualTo(String value) {
            addCriterion("payee_name =", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameNotEqualTo(String value) {
            addCriterion("payee_name <>", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameGreaterThan(String value) {
            addCriterion("payee_name >", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameGreaterThanOrEqualTo(String value) {
            addCriterion("payee_name >=", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameLessThan(String value) {
            addCriterion("payee_name <", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameLessThanOrEqualTo(String value) {
            addCriterion("payee_name <=", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameLike(String value) {
            addCriterion("payee_name like", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameNotLike(String value) {
            addCriterion("payee_name not like", value, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameIn(List<String> values) {
            addCriterion("payee_name in", values, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameNotIn(List<String> values) {
            addCriterion("payee_name not in", values, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameBetween(String value1, String value2) {
            addCriterion("payee_name between", value1, value2, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeNameNotBetween(String value1, String value2) {
            addCriterion("payee_name not between", value1, value2, "payeeName");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeIsNull() {
            addCriterion("payee_code is null");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeIsNotNull() {
            addCriterion("payee_code is not null");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeEqualTo(String value) {
            addCriterion("payee_code =", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeNotEqualTo(String value) {
            addCriterion("payee_code <>", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeGreaterThan(String value) {
            addCriterion("payee_code >", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("payee_code >=", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeLessThan(String value) {
            addCriterion("payee_code <", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeLessThanOrEqualTo(String value) {
            addCriterion("payee_code <=", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeLike(String value) {
            addCriterion("payee_code like", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeNotLike(String value) {
            addCriterion("payee_code not like", value, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeIn(List<String> values) {
            addCriterion("payee_code in", values, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeNotIn(List<String> values) {
            addCriterion("payee_code not in", values, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeBetween(String value1, String value2) {
            addCriterion("payee_code between", value1, value2, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeCodeNotBetween(String value1, String value2) {
            addCriterion("payee_code not between", value1, value2, "payeeCode");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressIsNull() {
            addCriterion("payee_address is null");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressIsNotNull() {
            addCriterion("payee_address is not null");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressEqualTo(String value) {
            addCriterion("payee_address =", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressNotEqualTo(String value) {
            addCriterion("payee_address <>", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressGreaterThan(String value) {
            addCriterion("payee_address >", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressGreaterThanOrEqualTo(String value) {
            addCriterion("payee_address >=", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressLessThan(String value) {
            addCriterion("payee_address <", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressLessThanOrEqualTo(String value) {
            addCriterion("payee_address <=", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressLike(String value) {
            addCriterion("payee_address like", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressNotLike(String value) {
            addCriterion("payee_address not like", value, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressIn(List<String> values) {
            addCriterion("payee_address in", values, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressNotIn(List<String> values) {
            addCriterion("payee_address not in", values, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressBetween(String value1, String value2) {
            addCriterion("payee_address between", value1, value2, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAddressNotBetween(String value1, String value2) {
            addCriterion("payee_address not between", value1, value2, "payeeAddress");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountIsNull() {
            addCriterion("payee_account is null");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountIsNotNull() {
            addCriterion("payee_account is not null");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountEqualTo(String value) {
            addCriterion("payee_account =", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountNotEqualTo(String value) {
            addCriterion("payee_account <>", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountGreaterThan(String value) {
            addCriterion("payee_account >", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountGreaterThanOrEqualTo(String value) {
            addCriterion("payee_account >=", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountLessThan(String value) {
            addCriterion("payee_account <", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountLessThanOrEqualTo(String value) {
            addCriterion("payee_account <=", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountLike(String value) {
            addCriterion("payee_account like", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountNotLike(String value) {
            addCriterion("payee_account not like", value, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountIn(List<String> values) {
            addCriterion("payee_account in", values, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountNotIn(List<String> values) {
            addCriterion("payee_account not in", values, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountBetween(String value1, String value2) {
            addCriterion("payee_account between", value1, value2, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andPayeeAccountNotBetween(String value1, String value2) {
            addCriterion("payee_account not between", value1, value2, "payeeAccount");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andSummaryWordIsNull() {
            addCriterion("summary_word is null");
            return (Criteria) this;
        }

        public Criteria andSummaryWordIsNotNull() {
            addCriterion("summary_word is not null");
            return (Criteria) this;
        }

        public Criteria andSummaryWordEqualTo(String value) {
            addCriterion("summary_word =", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordNotEqualTo(String value) {
            addCriterion("summary_word <>", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordGreaterThan(String value) {
            addCriterion("summary_word >", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordGreaterThanOrEqualTo(String value) {
            addCriterion("summary_word >=", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordLessThan(String value) {
            addCriterion("summary_word <", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordLessThanOrEqualTo(String value) {
            addCriterion("summary_word <=", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordLike(String value) {
            addCriterion("summary_word like", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordNotLike(String value) {
            addCriterion("summary_word not like", value, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordIn(List<String> values) {
            addCriterion("summary_word in", values, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordNotIn(List<String> values) {
            addCriterion("summary_word not in", values, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordBetween(String value1, String value2) {
            addCriterion("summary_word between", value1, value2, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andSummaryWordNotBetween(String value1, String value2) {
            addCriterion("summary_word not between", value1, value2, "summaryWord");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNull() {
            addCriterion("remarks is null");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNotNull() {
            addCriterion("remarks is not null");
            return (Criteria) this;
        }

        public Criteria andRemarksEqualTo(String value) {
            addCriterion("remarks =", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotEqualTo(String value) {
            addCriterion("remarks <>", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThan(String value) {
            addCriterion("remarks >", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThanOrEqualTo(String value) {
            addCriterion("remarks >=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThan(String value) {
            addCriterion("remarks <", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThanOrEqualTo(String value) {
            addCriterion("remarks <=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLike(String value) {
            addCriterion("remarks like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotLike(String value) {
            addCriterion("remarks not like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksIn(List<String> values) {
            addCriterion("remarks in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotIn(List<String> values) {
            addCriterion("remarks not in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksBetween(String value1, String value2) {
            addCriterion("remarks between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotBetween(String value1, String value2) {
            addCriterion("remarks not between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNull() {
            addCriterion("summary is null");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNotNull() {
            addCriterion("summary is not null");
            return (Criteria) this;
        }

        public Criteria andSummaryEqualTo(String value) {
            addCriterion("summary =", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotEqualTo(String value) {
            addCriterion("summary <>", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThan(String value) {
            addCriterion("summary >", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThanOrEqualTo(String value) {
            addCriterion("summary >=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThan(String value) {
            addCriterion("summary <", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThanOrEqualTo(String value) {
            addCriterion("summary <=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLike(String value) {
            addCriterion("summary like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotLike(String value) {
            addCriterion("summary not like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryIn(List<String> values) {
            addCriterion("summary in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotIn(List<String> values) {
            addCriterion("summary not in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryBetween(String value1, String value2) {
            addCriterion("summary between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotBetween(String value1, String value2) {
            addCriterion("summary not between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andPayeeIsNull() {
            addCriterion("payee is null");
            return (Criteria) this;
        }

        public Criteria andPayeeIsNotNull() {
            addCriterion("payee is not null");
            return (Criteria) this;
        }

        public Criteria andPayeeEqualTo(String value) {
            addCriterion("payee =", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeNotEqualTo(String value) {
            addCriterion("payee <>", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeGreaterThan(String value) {
            addCriterion("payee >", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeGreaterThanOrEqualTo(String value) {
            addCriterion("payee >=", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeLessThan(String value) {
            addCriterion("payee <", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeLessThanOrEqualTo(String value) {
            addCriterion("payee <=", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeLike(String value) {
            addCriterion("payee like", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeNotLike(String value) {
            addCriterion("payee not like", value, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeIn(List<String> values) {
            addCriterion("payee in", values, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeNotIn(List<String> values) {
            addCriterion("payee not in", values, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeBetween(String value1, String value2) {
            addCriterion("payee between", value1, value2, "payee");
            return (Criteria) this;
        }

        public Criteria andPayeeNotBetween(String value1, String value2) {
            addCriterion("payee not between", value1, value2, "payee");
            return (Criteria) this;
        }

        public Criteria andRecheckIsNull() {
            addCriterion("recheck is null");
            return (Criteria) this;
        }

        public Criteria andRecheckIsNotNull() {
            addCriterion("recheck is not null");
            return (Criteria) this;
        }

        public Criteria andRecheckEqualTo(String value) {
            addCriterion("recheck =", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckNotEqualTo(String value) {
            addCriterion("recheck <>", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckGreaterThan(String value) {
            addCriterion("recheck >", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckGreaterThanOrEqualTo(String value) {
            addCriterion("recheck >=", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckLessThan(String value) {
            addCriterion("recheck <", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckLessThanOrEqualTo(String value) {
            addCriterion("recheck <=", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckLike(String value) {
            addCriterion("recheck like", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckNotLike(String value) {
            addCriterion("recheck not like", value, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckIn(List<String> values) {
            addCriterion("recheck in", values, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckNotIn(List<String> values) {
            addCriterion("recheck not in", values, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckBetween(String value1, String value2) {
            addCriterion("recheck between", value1, value2, "recheck");
            return (Criteria) this;
        }

        public Criteria andRecheckNotBetween(String value1, String value2) {
            addCriterion("recheck not between", value1, value2, "recheck");
            return (Criteria) this;
        }

        public Criteria andDrawerIsNull() {
            addCriterion("drawer is null");
            return (Criteria) this;
        }

        public Criteria andDrawerIsNotNull() {
            addCriterion("drawer is not null");
            return (Criteria) this;
        }

        public Criteria andDrawerEqualTo(String value) {
            addCriterion("drawer =", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerNotEqualTo(String value) {
            addCriterion("drawer <>", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerGreaterThan(String value) {
            addCriterion("drawer >", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerGreaterThanOrEqualTo(String value) {
            addCriterion("drawer >=", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerLessThan(String value) {
            addCriterion("drawer <", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerLessThanOrEqualTo(String value) {
            addCriterion("drawer <=", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerLike(String value) {
            addCriterion("drawer like", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerNotLike(String value) {
            addCriterion("drawer not like", value, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerIn(List<String> values) {
            addCriterion("drawer in", values, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerNotIn(List<String> values) {
            addCriterion("drawer not in", values, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerBetween(String value1, String value2) {
            addCriterion("drawer between", value1, value2, "drawer");
            return (Criteria) this;
        }

        public Criteria andDrawerNotBetween(String value1, String value2) {
            addCriterion("drawer not between", value1, value2, "drawer");
            return (Criteria) this;
        }

        public Criteria andSellerIsNull() {
            addCriterion("seller is null");
            return (Criteria) this;
        }

        public Criteria andSellerIsNotNull() {
            addCriterion("seller is not null");
            return (Criteria) this;
        }

        public Criteria andSellerEqualTo(String value) {
            addCriterion("seller =", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerNotEqualTo(String value) {
            addCriterion("seller <>", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerGreaterThan(String value) {
            addCriterion("seller >", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerGreaterThanOrEqualTo(String value) {
            addCriterion("seller >=", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerLessThan(String value) {
            addCriterion("seller <", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerLessThanOrEqualTo(String value) {
            addCriterion("seller <=", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerLike(String value) {
            addCriterion("seller like", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerNotLike(String value) {
            addCriterion("seller not like", value, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerIn(List<String> values) {
            addCriterion("seller in", values, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerNotIn(List<String> values) {
            addCriterion("seller not in", values, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerBetween(String value1, String value2) {
            addCriterion("seller between", value1, value2, "seller");
            return (Criteria) this;
        }

        public Criteria andSellerNotBetween(String value1, String value2) {
            addCriterion("seller not between", value1, value2, "seller");
            return (Criteria) this;
        }

        public Criteria andTaxIsNull() {
            addCriterion("tax is null");
            return (Criteria) this;
        }

        public Criteria andTaxIsNotNull() {
            addCriterion("tax is not null");
            return (Criteria) this;
        }

        public Criteria andTaxEqualTo(BigDecimal value) {
            addCriterion("tax =", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotEqualTo(BigDecimal value) {
            addCriterion("tax <>", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThan(BigDecimal value) {
            addCriterion("tax >", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tax >=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThan(BigDecimal value) {
            addCriterion("tax <", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tax <=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxIn(List<BigDecimal> values) {
            addCriterion("tax in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotIn(List<BigDecimal> values) {
            addCriterion("tax not in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax not between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxRateIsNull() {
            addCriterion("tax_rate is null");
            return (Criteria) this;
        }

        public Criteria andTaxRateIsNotNull() {
            addCriterion("tax_rate is not null");
            return (Criteria) this;
        }

        public Criteria andTaxRateEqualTo(BigDecimal value) {
            addCriterion("tax_rate =", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotEqualTo(BigDecimal value) {
            addCriterion("tax_rate <>", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateGreaterThan(BigDecimal value) {
            addCriterion("tax_rate >", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tax_rate >=", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateLessThan(BigDecimal value) {
            addCriterion("tax_rate <", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tax_rate <=", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateIn(List<BigDecimal> values) {
            addCriterion("tax_rate in", values, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotIn(List<BigDecimal> values) {
            addCriterion("tax_rate not in", values, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax_rate between", value1, value2, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax_rate not between", value1, value2, "taxRate");
            return (Criteria) this;
        }

        public Criteria andAccountDateIsNull() {
            addCriterion("account_date is null");
            return (Criteria) this;
        }

        public Criteria andAccountDateIsNotNull() {
            addCriterion("account_date is not null");
            return (Criteria) this;
        }

        public Criteria andAccountDateEqualTo(Date value) {
            addCriterionForJDBCDate("account_date =", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("account_date <>", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateGreaterThan(Date value) {
            addCriterionForJDBCDate("account_date >", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("account_date >=", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateLessThan(Date value) {
            addCriterionForJDBCDate("account_date <", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("account_date <=", value, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateIn(List<Date> values) {
            addCriterionForJDBCDate("account_date in", values, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("account_date not in", values, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("account_date between", value1, value2, "accountDate");
            return (Criteria) this;
        }

        public Criteria andAccountDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("account_date not between", value1, value2, "accountDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateIsNull() {
            addCriterion("ticket_date is null");
            return (Criteria) this;
        }

        public Criteria andTicketDateIsNotNull() {
            addCriterion("ticket_date is not null");
            return (Criteria) this;
        }

        public Criteria andTicketDateEqualTo(Date value) {
            addCriterionForJDBCDate("ticket_date =", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("ticket_date <>", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateGreaterThan(Date value) {
            addCriterionForJDBCDate("ticket_date >", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("ticket_date >=", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateLessThan(Date value) {
            addCriterionForJDBCDate("ticket_date <", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("ticket_date <=", value, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateIn(List<Date> values) {
            addCriterionForJDBCDate("ticket_date in", values, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("ticket_date not in", values, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("ticket_date between", value1, value2, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andTicketDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("ticket_date not between", value1, value2, "ticketDate");
            return (Criteria) this;
        }

        public Criteria andDiffTaxIsNull() {
            addCriterion("diff_tax is null");
            return (Criteria) this;
        }

        public Criteria andDiffTaxIsNotNull() {
            addCriterion("diff_tax is not null");
            return (Criteria) this;
        }

        public Criteria andDiffTaxEqualTo(BigDecimal value) {
            addCriterion("diff_tax =", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxNotEqualTo(BigDecimal value) {
            addCriterion("diff_tax <>", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxGreaterThan(BigDecimal value) {
            addCriterion("diff_tax >", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("diff_tax >=", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxLessThan(BigDecimal value) {
            addCriterion("diff_tax <", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("diff_tax <=", value, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxIn(List<BigDecimal> values) {
            addCriterion("diff_tax in", values, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxNotIn(List<BigDecimal> values) {
            addCriterion("diff_tax not in", values, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("diff_tax between", value1, value2, "diffTax");
            return (Criteria) this;
        }

        public Criteria andDiffTaxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("diff_tax not between", value1, value2, "diffTax");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNull() {
            addCriterion("total_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("total_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(BigDecimal value) {
            addCriterion("total_amount =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(BigDecimal value) {
            addCriterion("total_amount <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(BigDecimal value) {
            addCriterion("total_amount >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_amount >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(BigDecimal value) {
            addCriterion("total_amount <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_amount <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<BigDecimal> values) {
            addCriterion("total_amount in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<BigDecimal> values) {
            addCriterion("total_amount not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_amount between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_amount not between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andIsLllegalIsNull() {
            addCriterion("is_lllegal is null");
            return (Criteria) this;
        }

        public Criteria andIsLllegalIsNotNull() {
            addCriterion("is_lllegal is not null");
            return (Criteria) this;
        }

        public Criteria andIsLllegalEqualTo(Byte value) {
            addCriterion("is_lllegal =", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalNotEqualTo(Byte value) {
            addCriterion("is_lllegal <>", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalGreaterThan(Byte value) {
            addCriterion("is_lllegal >", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_lllegal >=", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalLessThan(Byte value) {
            addCriterion("is_lllegal <", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalLessThanOrEqualTo(Byte value) {
            addCriterion("is_lllegal <=", value, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalIn(List<Byte> values) {
            addCriterion("is_lllegal in", values, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalNotIn(List<Byte> values) {
            addCriterion("is_lllegal not in", values, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalBetween(Byte value1, Byte value2) {
            addCriterion("is_lllegal between", value1, value2, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andIsLllegalNotBetween(Byte value1, Byte value2) {
            addCriterion("is_lllegal not between", value1, value2, "isLllegal");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainIsNull() {
            addCriterion("lllegal_explain is null");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainIsNotNull() {
            addCriterion("lllegal_explain is not null");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainEqualTo(String value) {
            addCriterion("lllegal_explain =", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainNotEqualTo(String value) {
            addCriterion("lllegal_explain <>", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainGreaterThan(String value) {
            addCriterion("lllegal_explain >", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainGreaterThanOrEqualTo(String value) {
            addCriterion("lllegal_explain >=", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainLessThan(String value) {
            addCriterion("lllegal_explain <", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainLessThanOrEqualTo(String value) {
            addCriterion("lllegal_explain <=", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainLike(String value) {
            addCriterion("lllegal_explain like", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainNotLike(String value) {
            addCriterion("lllegal_explain not like", value, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainIn(List<String> values) {
            addCriterion("lllegal_explain in", values, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainNotIn(List<String> values) {
            addCriterion("lllegal_explain not in", values, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainBetween(String value1, String value2) {
            addCriterion("lllegal_explain between", value1, value2, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andLllegalExplainNotBetween(String value1, String value2) {
            addCriterion("lllegal_explain not between", value1, value2, "lllegalExplain");
            return (Criteria) this;
        }

        public Criteria andTicketUrlIsNull() {
            addCriterion("ticket_url is null");
            return (Criteria) this;
        }

        public Criteria andTicketUrlIsNotNull() {
            addCriterion("ticket_url is not null");
            return (Criteria) this;
        }

        public Criteria andTicketUrlEqualTo(String value) {
            addCriterion("ticket_url =", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlNotEqualTo(String value) {
            addCriterion("ticket_url <>", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlGreaterThan(String value) {
            addCriterion("ticket_url >", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlGreaterThanOrEqualTo(String value) {
            addCriterion("ticket_url >=", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlLessThan(String value) {
            addCriterion("ticket_url <", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlLessThanOrEqualTo(String value) {
            addCriterion("ticket_url <=", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlLike(String value) {
            addCriterion("ticket_url like", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlNotLike(String value) {
            addCriterion("ticket_url not like", value, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlIn(List<String> values) {
            addCriterion("ticket_url in", values, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlNotIn(List<String> values) {
            addCriterion("ticket_url not in", values, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlBetween(String value1, String value2) {
            addCriterion("ticket_url between", value1, value2, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andTicketUrlNotBetween(String value1, String value2) {
            addCriterion("ticket_url not between", value1, value2, "ticketUrl");
            return (Criteria) this;
        }

        public Criteria andIsRealIsNull() {
            addCriterion("is_real is null");
            return (Criteria) this;
        }

        public Criteria andIsRealIsNotNull() {
            addCriterion("is_real is not null");
            return (Criteria) this;
        }

        public Criteria andIsRealEqualTo(Byte value) {
            addCriterion("is_real =", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealNotEqualTo(Byte value) {
            addCriterion("is_real <>", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealGreaterThan(Byte value) {
            addCriterion("is_real >", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_real >=", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealLessThan(Byte value) {
            addCriterion("is_real <", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealLessThanOrEqualTo(Byte value) {
            addCriterion("is_real <=", value, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealIn(List<Byte> values) {
            addCriterion("is_real in", values, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealNotIn(List<Byte> values) {
            addCriterion("is_real not in", values, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealBetween(Byte value1, Byte value2) {
            addCriterion("is_real between", value1, value2, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsRealNotBetween(Byte value1, Byte value2) {
            addCriterion("is_real not between", value1, value2, "isReal");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfIsNull() {
            addCriterion("is_behalf_of is null");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfIsNotNull() {
            addCriterion("is_behalf_of is not null");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfEqualTo(Byte value) {
            addCriterion("is_behalf_of =", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfNotEqualTo(Byte value) {
            addCriterion("is_behalf_of <>", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfGreaterThan(Byte value) {
            addCriterion("is_behalf_of >", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_behalf_of >=", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfLessThan(Byte value) {
            addCriterion("is_behalf_of <", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfLessThanOrEqualTo(Byte value) {
            addCriterion("is_behalf_of <=", value, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfIn(List<Byte> values) {
            addCriterion("is_behalf_of in", values, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfNotIn(List<Byte> values) {
            addCriterion("is_behalf_of not in", values, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfBetween(Byte value1, Byte value2) {
            addCriterion("is_behalf_of between", value1, value2, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsBehalfOfNotBetween(Byte value1, Byte value2) {
            addCriterion("is_behalf_of not between", value1, value2, "isBehalfOf");
            return (Criteria) this;
        }

        public Criteria andIsVoidIsNull() {
            addCriterion("is_void is null");
            return (Criteria) this;
        }

        public Criteria andIsVoidIsNotNull() {
            addCriterion("is_void is not null");
            return (Criteria) this;
        }

        public Criteria andIsVoidEqualTo(Byte value) {
            addCriterion("is_void =", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidNotEqualTo(Byte value) {
            addCriterion("is_void <>", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidGreaterThan(Byte value) {
            addCriterion("is_void >", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_void >=", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidLessThan(Byte value) {
            addCriterion("is_void <", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidLessThanOrEqualTo(Byte value) {
            addCriterion("is_void <=", value, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidIn(List<Byte> values) {
            addCriterion("is_void in", values, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidNotIn(List<Byte> values) {
            addCriterion("is_void not in", values, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidBetween(Byte value1, Byte value2) {
            addCriterion("is_void between", value1, value2, "isVoid");
            return (Criteria) this;
        }

        public Criteria andIsVoidNotBetween(Byte value1, Byte value2) {
            addCriterion("is_void not between", value1, value2, "isVoid");
            return (Criteria) this;
        }
        
        public Criteria andIsIdentifyIsNull() {
            addCriterion("is_identify is null");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyIsNotNull() {
            addCriterion("is_identify is not null");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyEqualTo(Byte value) {
            addCriterion("is_identify =", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyNotEqualTo(Byte value) {
            addCriterion("is_identify <>", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyGreaterThan(Byte value) {
            addCriterion("is_identify >", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_identify >=", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyLessThan(Byte value) {
            addCriterion("is_identify <", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyLessThanOrEqualTo(Byte value) {
            addCriterion("is_identify <=", value, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyIn(List<Byte> values) {
            addCriterion("is_identify in", values, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyNotIn(List<Byte> values) {
            addCriterion("is_identify not in", values, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyBetween(Byte value1, Byte value2) {
            addCriterion("is_identify between", value1, value2, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andIsIdentifyNotBetween(Byte value1, Byte value2) {
            addCriterion("is_identify not between", value1, value2, "IsIdentify");
            return (Criteria) this;
        }

        public Criteria andCompletedIsNull() {
            addCriterion("completed is null");
            return (Criteria) this;
        }

        public Criteria andCompletedIsNotNull() {
            addCriterion("completed is not null");
            return (Criteria) this;
        }

        public Criteria andCompletedEqualTo(Byte value) {
            addCriterion("completed =", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedNotEqualTo(Byte value) {
            addCriterion("completed <>", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedGreaterThan(Byte value) {
            addCriterion("completed >", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedGreaterThanOrEqualTo(Byte value) {
            addCriterion("completed >=", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedLessThan(Byte value) {
            addCriterion("completed <", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedLessThanOrEqualTo(Byte value) {
            addCriterion("completed <=", value, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedIn(List<Byte> values) {
            addCriterion("completed in", values, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedNotIn(List<Byte> values) {
            addCriterion("completed not in", values, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedBetween(Byte value1, Byte value2) {
            addCriterion("completed between", value1, value2, "completed");
            return (Criteria) this;
        }

        public Criteria andCompletedNotBetween(Byte value1, Byte value2) {
            addCriterion("completed not between", value1, value2, "completed");
            return (Criteria) this;
        }
        
        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Byte value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Byte value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Byte value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Byte value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Byte value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Byte value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Byte> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Byte> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Byte value1, Byte value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Byte value1, Byte value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("updated is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("updated is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Date value) {
            addCriterion("updated =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Date value) {
            addCriterion("updated <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Date value) {
            addCriterion("updated >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("updated >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Date value) {
            addCriterion("updated <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("updated <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Date> values) {
            addCriterion("updated in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Date> values) {
            addCriterion("updated not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Date value1, Date value2) {
            addCriterion("updated between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("updated not between", value1, value2, "updated");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}