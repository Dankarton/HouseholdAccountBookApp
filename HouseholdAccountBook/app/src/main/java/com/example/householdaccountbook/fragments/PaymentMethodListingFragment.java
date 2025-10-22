package com.example.householdaccountbook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.householdaccountbook.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PaymentMethodListingFragment extends Fragment {

    public PaymentMethodListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_method_listing, container, false);
    }
}