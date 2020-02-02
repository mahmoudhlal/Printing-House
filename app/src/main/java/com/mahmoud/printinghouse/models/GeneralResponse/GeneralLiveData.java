package com.mahmoud.printinghouse.models.GeneralResponse;

import androidx.lifecycle.MutableLiveData;

public class GeneralLiveData<T> extends MutableLiveData<GeneralResponse<T>> {

    public void postLoading() {
        postValue(new GeneralResponse<T>().loading());
    }

    public void postError(Error error) {
        postValue(new GeneralResponse<T>().error(error));
    }

    public void postSuccess(T data) {
        postValue(new GeneralResponse<T>().success(data));
    }


}
