package com.example.householdaccountbook.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.R;

import myclasses.Income;

public class IncomeSettingsCustomView extends ConstraintLayout {
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView amountTextView;
    private TextView memoTextView;

    private Income myIncome;

    public IncomeSettingsCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_bop_settings, this);
        categoryTextView = layout.findViewById(R.id.category_text_view);
        dateTextView = layout.findViewById(R.id.date_text_view);
        memoTextView = layout.findViewById(R.id.memo_text_view);
        amountTextView = layout.findViewById(R.id.payment_amount_text_view);
        amountTextView.setTextColor(context.getColor(R.color.income_text_color));
        ImageButton deleteButton = layout.findViewById(R.id.imageButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //clickDeleteButton(view);
                new AlertDialog.Builder(view.getContext())
                        .setTitle("削除しますか？")
                        .setPositiveButton(
                                "はい",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clickDeleteButton(view);
                                    }
                                }
                        )
                        .setNegativeButton(
                                "いいえ",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Do nothing.
                                    }
                                })
                        .show();
            }
        });
    }
    public void setData(Income income){
        myIncome = income;
        categoryTextView.setText(myIncome.getCategoryId());
        String formattedDate = myIncome.getYear() + "/" + myIncome.getMonth() + "/" + myIncome.getDay();
        dateTextView.setText(formattedDate);
        memoTextView.setText(myIncome.getMemo());
        amountTextView.setText("+" + myIncome.getAmount());
    }

    public String viewMemo(){
        return myIncome.getMemo();
    }

    public void clickDeleteButton(View view) {
        Log.d("ExpSettingCustomView", "" + myIncome.getId());
        MyDbManager.deleteData(myIncome);
        this.setVisibility(View.GONE);
//        String inpText = "Delete";
//        categoryTextView.setText(inpText);
//        dateTextView.setText("");
//        amountTextView.setText("-");
    }
}
