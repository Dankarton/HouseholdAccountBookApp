package com.example.householdaccountbook.fragments.listing;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.item.PaymentMethodItemView;

import java.util.ArrayList;

import myclasses.PaymentMethod;

public class PaymentMethodListingFragment extends Fragment {
    public interface OnInputEventListener {
        void onInputDetected(PaymentMethod data);
    }
    private OnInputEventListener listener = null;
    private LinearLayout itemList;
    private int paymentMethodDataNum;

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
        Button createButton = view.findViewById(R.id.create_new_button);
        reload();
        createButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            PaymentMethod newlyMethod = new PaymentMethod();
                            // 新しいデータはListの最終行に挿入されるよううにIndexにデータ数を設定
                            newlyMethod.setIndex(paymentMethodDataNum);
                            listener.onInputDetected(newlyMethod);
                        }
                    }
                }
        );
    }

    private PaymentMethodItemView createPaymentMethodItemView(Context context, PaymentMethod data) {
        PaymentMethodItemView itemView = new PaymentMethodItemView(context);
        itemView.setData(data);
        if (!data.isDefault()) {
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onInputDetected(itemView.getData());
                            }
                        }
                    }
            );
        }
        return itemView;
    }
    public void reload() {
        this.itemList.removeAllViews();
        Context context = requireContext();
        ArrayList<PaymentMethod> methods = MyDbManager.getAll(PaymentMethod.class);
        this.paymentMethodDataNum = methods.size();
        for (PaymentMethod method : methods) {
            this.itemList.addView(createPaymentMethodItemView(context, method));
        }
    }

    public void setListener(OnInputEventListener listener) {
        this.listener = listener;
    }
}