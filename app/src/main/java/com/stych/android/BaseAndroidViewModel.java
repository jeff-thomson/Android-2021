package com.stych.android;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseAndroidViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    public final LiveData<Boolean> loading = _loading;

    public MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isLoading() {
        return _loading.getValue() != null && _loading.getValue();
    }

}
