package com.stych.android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseViewModel {
    protected MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    public final LiveData<Boolean> loading = _loading;

    protected MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public boolean isLoading() {
        return _loading.getValue() != null && _loading.getValue();
    }

    public boolean canShowEmptyMessage() {
        return !isLoading() && !hasItems();
    }

    public abstract boolean hasItems();
}
