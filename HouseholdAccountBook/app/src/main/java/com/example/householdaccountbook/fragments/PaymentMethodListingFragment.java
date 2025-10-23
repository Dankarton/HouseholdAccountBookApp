package com.example.householdaccountbook.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.PaymentMethodItemView;

import java.util.ArrayList;

import myclasses.PaymentMethod;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PaymentMethodListingFragment extends Fragment {
    private LinearLayout itemList;
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
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        this.itemList = view.findViewById(R.id.payment_method_list);
        ArrayList<PaymentMethod> methods = MyDbManager.getAllPaymentMethodData();
        PaymentMethodItemView itemView = new PaymentMethodItemView(view.getContext());
        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }
    private PaymentMethodItemView createPaymentMethodItemView(Context context, PaymentMethod data) {
        PaymentMethodItemView itemView = new PaymentMethodItemView(context);
        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                    }
                }
        )
    }
}