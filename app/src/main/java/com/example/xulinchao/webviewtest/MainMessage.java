package com.example.xulinchao.webviewtest;

import java.util.List;

/**
 * Created by xulinchao on 2017/4/28.
 */

public class MainMessage {
    private String message;
    private String condition;
    private List<TwoMessage> data;

    public List<TwoMessage> getData() {
        return data;
    }

    public void setData(List<TwoMessage> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
