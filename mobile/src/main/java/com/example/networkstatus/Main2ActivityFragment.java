package com.example.networkstatus;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.networkstatus.databinding.FragmentMain2Binding;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {
    static final String TAG = "NetworkStatusFragment";
    FragmentMain2Binding binding;

    NetworkInfoViewModel mNetworkInfoViewModel;
    NetworkInfoAdapter mNetworkInfoAdapter;

    public Main2ActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false);

        mNetworkInfoAdapter = new NetworkInfoAdapter();
        mNetworkInfoViewModel = new NetworkInfoViewModel();

        binding.setNetworkInfoViewModel(mNetworkInfoViewModel);

        return binding.getRoot();
    }

    @Override public void onResume() {
        super.onResume();
        mNetworkInfoAdapter.start(getActivity().getApplicationContext());
        mNetworkInfoViewModel.bindNetworkInfo(mNetworkInfoAdapter);
    }

    @Override public void onPause() {
        super.onPause();

        mNetworkInfoAdapter.stop();
        mNetworkInfoViewModel.unBindNetworkInfo();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
