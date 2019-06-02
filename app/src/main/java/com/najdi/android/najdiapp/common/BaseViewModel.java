package com.najdi.android.najdiapp.common;

import android.app.Application;

import com.najdi.android.najdiapp.common.NajdiApplication;
import com.najdi.android.najdiapp.repository.Repository;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BaseViewModel extends AndroidViewModel {

    protected final ResourceProvider resourceProvider;
    protected final Repository repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        NajdiApplication najdiApplication = (NajdiApplication) application;
        resourceProvider = najdiApplication.getResourceProvider();
        repository = new Repository(resourceProvider);
    }
}
