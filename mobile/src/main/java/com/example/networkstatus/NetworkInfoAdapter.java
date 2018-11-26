package com.example.networkstatus;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class NetworkInfoAdapter {

    static final String TAG = "NetworkInfoAdapter";

    private Connectivity mNetworkInfo;

    BehaviorRelay<String> stateName = BehaviorRelay.create();

    CompositeDisposable monitorSubscriptions = new CompositeDisposable();

    public void start(Context context){
        monitorSubscriptions.addAll(setupNetworkConnectivityMonitor(context));
    }

    /**
     * If this monitor is no longer needed, call dispose to release the memory, otherwise this
     * will leak.
     * Calling dispose will render this instance useless.
     */
    public void stop() {
        monitorSubscriptions.clear();
    }

    @NonNull private Disposable setupNetworkConnectivityMonitor(Context context) {
        return ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .doOnNext(t->{
                    Log.w(TAG, "new network connectivity state: "+t.state().name());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setNetworkInfo);
    }

    private void setNetworkInfo(Connectivity networkInfo) {
        mNetworkInfo = networkInfo;
        Log.d("ConnectivityInfo",
                "State: "+mNetworkInfo.state().name()+"/n"
                +"Type: "+mNetworkInfo.typeName());

        onStateNameChange(mNetworkInfo.typeName()+": "+mNetworkInfo.state().name());
    }

    private void onStateNameChange(String newState) {
        stateName.accept(newState);
    }

    public Flowable<String> getStateNameChange() {
        return stateName.toFlowable(BackpressureStrategy.BUFFER);
    }


}
