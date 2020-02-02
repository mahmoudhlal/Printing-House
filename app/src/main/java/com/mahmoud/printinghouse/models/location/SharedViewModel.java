package com.mahmoud.printinghouse.models.location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel {
    public MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
    public LiveData<Location> getLocationLiveData(){return locationLiveData;}
}
