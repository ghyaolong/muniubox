package com.taoding.ocr.module;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yaochenglong on 2017/11/23.
 */
public class Words {

    private Integer words_result_num;
    private Set<WordsResult> words_result = new HashSet<WordsResult>();

    public Integer getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(Integer words_result_num) {
        this.words_result_num = words_result_num;
    }

    public Set<WordsResult> getWords_result() {
        return words_result;
    }

    public void setWords_result(Set<WordsResult> words_result) {
        this.words_result = words_result;
    }
}
