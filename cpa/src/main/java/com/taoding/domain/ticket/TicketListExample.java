package com.taoding.domain.ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TicketListExample() {
        oredCriteria = new ArrayList<Criteria>();
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

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(String value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(String value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(String value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(String value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(String value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(String value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLike(String value) {
            addCriterion("parent_id like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotLike(String value) {
            addCriterion("parent_id not like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<String> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<String> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(String value1, String value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(String value1, String value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdsIsNull() {
            addCriterion("parent_ids is null");
            return (Criteria) this;
        }

        public Criteria andParentIdsIsNotNull() {
            addCriterion("parent_ids is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdsEqualTo(String value) {
            addCriterion("parent_ids =", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsNotEqualTo(String value) {
            addCriterion("parent_ids <>", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsGreaterThan(String value) {
            addCriterion("parent_ids >", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsGreaterThanOrEqualTo(String value) {
            addCriterion("parent_ids >=", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsLessThan(String value) {
            addCriterion("parent_ids <", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsLessThanOrEqualTo(String value) {
            addCriterion("parent_ids <=", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsLike(String value) {
            addCriterion("parent_ids like", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsNotLike(String value) {
            addCriterion("parent_ids not like", value, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsIn(List<String> values) {
            addCriterion("parent_ids in", values, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsNotIn(List<String> values) {
            addCriterion("parent_ids not in", values, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsBetween(String value1, String value2) {
            addCriterion("parent_ids between", value1, value2, "parentIds");
            return (Criteria) this;
        }

        public Criteria andParentIdsNotBetween(String value1, String value2) {
            addCriterion("parent_ids not between", value1, value2, "parentIds");
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andClearingTypeIsNull() {
            addCriterion("clearing_type is null");
            return (Criteria) this;
        }

        public Criteria andClearingTypeIsNotNull() {
            addCriterion("clearing_type is not null");
            return (Criteria) this;
        }

        public Criteria andClearingTypeEqualTo(Byte value) {
            addCriterion("clearing_type =", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeNotEqualTo(Byte value) {
            addCriterion("clearing_type <>", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeGreaterThan(Byte value) {
            addCriterion("clearing_type >", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("clearing_type >=", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeLessThan(Byte value) {
            addCriterion("clearing_type <", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeLessThanOrEqualTo(Byte value) {
            addCriterion("clearing_type <=", value, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeIn(List<Byte> values) {
            addCriterion("clearing_type in", values, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeNotIn(List<Byte> values) {
            addCriterion("clearing_type not in", values, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeBetween(Byte value1, Byte value2) {
            addCriterion("clearing_type between", value1, value2, "clearingType");
            return (Criteria) this;
        }

        public Criteria andClearingTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("clearing_type not between", value1, value2, "clearingType");
            return (Criteria) this;
        }

        public Criteria andProofStrategyIsNull() {
            addCriterion("proof_strategy is null");
            return (Criteria) this;
        }

        public Criteria andProofStrategyIsNotNull() {
            addCriterion("proof_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andProofStrategyEqualTo(Byte value) {
            addCriterion("proof_strategy =", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyNotEqualTo(Byte value) {
            addCriterion("proof_strategy <>", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyGreaterThan(Byte value) {
            addCriterion("proof_strategy >", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyGreaterThanOrEqualTo(Byte value) {
            addCriterion("proof_strategy >=", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyLessThan(Byte value) {
            addCriterion("proof_strategy <", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyLessThanOrEqualTo(Byte value) {
            addCriterion("proof_strategy <=", value, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyIn(List<Byte> values) {
            addCriterion("proof_strategy in", values, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyNotIn(List<Byte> values) {
            addCriterion("proof_strategy not in", values, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyBetween(Byte value1, Byte value2) {
            addCriterion("proof_strategy between", value1, value2, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andProofStrategyNotBetween(Byte value1, Byte value2) {
            addCriterion("proof_strategy not between", value1, value2, "proofStrategy");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingIsNull() {
            addCriterion("usiness_accounting is null");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingIsNotNull() {
            addCriterion("usiness_accounting is not null");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingEqualTo(Byte value) {
            addCriterion("usiness_accounting =", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingNotEqualTo(Byte value) {
            addCriterion("usiness_accounting <>", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingGreaterThan(Byte value) {
            addCriterion("usiness_accounting >", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingGreaterThanOrEqualTo(Byte value) {
            addCriterion("usiness_accounting >=", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingLessThan(Byte value) {
            addCriterion("usiness_accounting <", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingLessThanOrEqualTo(Byte value) {
            addCriterion("usiness_accounting <=", value, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingIn(List<Byte> values) {
            addCriterion("usiness_accounting in", values, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingNotIn(List<Byte> values) {
            addCriterion("usiness_accounting not in", values, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingBetween(Byte value1, Byte value2) {
            addCriterion("usiness_accounting between", value1, value2, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andUsinessAccountingNotBetween(Byte value1, Byte value2) {
            addCriterion("usiness_accounting not between", value1, value2, "usinessAccounting");
            return (Criteria) this;
        }

        public Criteria andIsPresetIsNull() {
            addCriterion("is_preset is null");
            return (Criteria) this;
        }

        public Criteria andIsPresetIsNotNull() {
            addCriterion("is_preset is not null");
            return (Criteria) this;
        }

        public Criteria andIsPresetEqualTo(Byte value) {
            addCriterion("is_preset =", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetNotEqualTo(Byte value) {
            addCriterion("is_preset <>", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetGreaterThan(Byte value) {
            addCriterion("is_preset >", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_preset >=", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetLessThan(Byte value) {
            addCriterion("is_preset <", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetLessThanOrEqualTo(Byte value) {
            addCriterion("is_preset <=", value, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetIn(List<Byte> values) {
            addCriterion("is_preset in", values, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetNotIn(List<Byte> values) {
            addCriterion("is_preset not in", values, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetBetween(Byte value1, Byte value2) {
            addCriterion("is_preset between", value1, value2, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsPresetNotBetween(Byte value1, Byte value2) {
            addCriterion("is_preset not between", value1, value2, "isPreset");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIsNull() {
            addCriterion("is_default is null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIsNotNull() {
            addCriterion("is_default is not null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultEqualTo(Byte value) {
            addCriterion("is_default =", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotEqualTo(Byte value) {
            addCriterion("is_default <>", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThan(Byte value) {
            addCriterion("is_default >", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_default >=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThan(Byte value) {
            addCriterion("is_default <", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThanOrEqualTo(Byte value) {
            addCriterion("is_default <=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIn(List<Byte> values) {
            addCriterion("is_default in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotIn(List<Byte> values) {
            addCriterion("is_default not in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultBetween(Byte value1, Byte value2) {
            addCriterion("is_default between", value1, value2, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotBetween(Byte value1, Byte value2) {
            addCriterion("is_default not between", value1, value2, "isDefault");
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