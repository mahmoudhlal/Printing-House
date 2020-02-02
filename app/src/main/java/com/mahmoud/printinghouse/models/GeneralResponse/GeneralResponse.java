package com.mahmoud.printinghouse.models.GeneralResponse;

public class
GeneralResponse<T> {

    private Status status;
    private T data;
    private Error error;


    public GeneralResponse<T> loading(){
        this.status = Status.Loading;
        this.data = null;
        this.error = null;
        return this;
    }

    public GeneralResponse<T> success(T data){
        this.status = Status.Success;
        this.data = data;
        this.error = null;
        return this;
    }

    public GeneralResponse<T> error(Error error){
        this.status = Status.Failure;
        this.error = error;
        this.data = null;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
