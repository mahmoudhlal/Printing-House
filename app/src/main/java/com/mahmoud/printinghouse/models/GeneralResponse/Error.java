package com.mahmoud.printinghouse.models.GeneralResponse;

public class Error {

    private String msg;
    private Throwable throwable;
    private Integer responseCode;
    private Boolean isToast;

    public Error(String msg, Throwable throwable, Integer responseCode, Boolean isToast) {
        this.msg = msg;
        this.throwable = throwable;
        this.responseCode = responseCode;
        this.isToast = isToast;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Boolean getToast() {
        return isToast;
    }

    public void setToast(Boolean toast) {
        isToast = toast;
    }
}
