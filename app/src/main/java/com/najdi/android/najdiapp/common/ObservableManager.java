package com.najdi.android.najdiapp.common;

import android.content.Intent;

import java.util.Observable;


/* Observable design pattern's singleton wrapper class for notifying the data*/
public class ObservableManager extends Observable {

    private static ObservableManager mInstance;

    public synchronized static ObservableManager getInstance() {
        if (mInstance == null) {
            mInstance = new ObservableManager();
        }
        return mInstance;
    }

    private ObservableManager() {
    }

    public void notifyData(Intent data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
