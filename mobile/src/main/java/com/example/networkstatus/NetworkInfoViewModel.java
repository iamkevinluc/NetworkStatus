package com.example.networkstatus;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkInfoViewModel extends BaseObservable{

    private ObservableField<String> state = new ObservableField<>();

    private NetworkInfoAdapter mNetworkInfoAdapter;
    private CompositeDisposable networkInfoSubscription = new CompositeDisposable();

    NetworkInfoViewModel() {}

    public void bindNetworkInfo(@NonNull NetworkInfoAdapter networkInfoAdapter) {
        unBindNetworkInfo();
        this.mNetworkInfoAdapter = networkInfoAdapter;
        networkInfoSubscription.addAll(this.subscribeNetworkInfo());
    }

    public void unBindNetworkInfo() {
        if(networkInfoSubscription.size() > 0){
            networkInfoSubscription.clear();
        }
        mNetworkInfoAdapter = null;
    }

    private Disposable subscribeNetworkInfo() {
        if(mNetworkInfoAdapter == null) {
            throw new IllegalStateException("attempting to bind to a null networkinfo adapter");
        }
        return mNetworkInfoAdapter.getStateNameChange()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setNetworkState);
    }

    private void setNetworkState(String state) {
        Log.d(Main2ActivityFragment.TAG, "setNetworkState "+state);
        this.state.set(state);
    }

    @Bindable
    public ObservableField<String> getNetworkState() {
        Log.d(Main2ActivityFragment.TAG, "getNetworkState "+state);
        return state;
    }

}
