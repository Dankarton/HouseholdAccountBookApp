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

import myclasses.Expenses;
///
/// 支出表示欄クラス
///
public class ExpensesSettingsCustomView extends ConstraintLayout {
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView amountTextView;
    private TextView memoTextView;

    private Expenses myExpenses;

    public ExpensesSettingsCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_bop_settings, this);
        categoryTextView = layout.findViewById(R.id.category_text_view);
        dateTextView = layout.findViewById(R.id.date_text_view);
        memoTextView = layout.findViewById(R.id.memo_text_view);
        amountTextView = layout.findViewById(R.id.amount_text_view);
        amountTextView.setTextColor(context.getColor(R.color.expenses_text_color));
        ImageButton deleteButton = layout.findViewById(R.id.more_action_button);
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
    public void setData(Expenses expenses){
        myExpenses = expenses;
        categoryTextView.setText((int) myExpenses.getCategoryId());
        String formattedDate = myExpenses.getYear() + "/" + myExpenses.getMonth() + "/" + myExpenses.getDay();
        dateTextView.setText(formattedDate);
//        dateTextView.setText(String.valueOf(myExpenses.getPaymentMethodId()));
        memoTextView.setText(myExpenses.getMemo());
        // 支出はマイナス記号を付けて視覚的に見やすくする
        String strAmount = "-" + myExpenses.getAmount();
        amountTextView.setText(strAmount);
    }

    public String viewMemo(){
        return myExpenses.getMemo();
    }

    public void clickDeleteButton(View view) {
        Log.d("ExpSettingCustomView", "" + myExpenses.getId());
        MyDbManager.deleteDataSafely(myExpenses);
        this.setVisibility(View.GONE);
//        String inpText = "Delete";
//        categoryTextView.setText(inpText);
//        dateTextView.setText("");
//        amountTextView.setText("-");
    }
}
