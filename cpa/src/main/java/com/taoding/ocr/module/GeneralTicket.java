package com.taoding.ocr.module;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaochenglong on 2017/11/30.
 * 通用票据实体类，和百度通用票据API返回的结果保持一致
 */
public class GeneralTicket {

    private String log_id;

    private String words_result_num;

    List<WordsResult> words_result = new ArrayList<WordsResult>();

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(String words_result_num) {
        this.words_result_num = words_result_num;
    }

	public List<WordsResult> getWords_result() {
		return words_result;
	}

	public void setWords_result(List<WordsResult> words_result) {
		this.words_result = words_result;
	}

}
