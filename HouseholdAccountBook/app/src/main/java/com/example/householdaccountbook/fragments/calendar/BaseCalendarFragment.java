package com.example.householdaccountbook.fragments.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.activities.settings.edit.SettingEditIncomeActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditMoneyMovementActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditPurchaseActivity;
import com.example.householdaccountbook.adapter.CalendarUiAdapter;
import com.example.householdaccountbook.customviews.calendar.CalendarCustomView;
import com.example.householdaccountbook.module.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.module.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.module.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.Purchase;
import com.example.householdaccountbook.module.sharedmodel.DateSharedViewModel;
import com.example.householdaccountbook.repository.RepositoryManager;

import java.util.Calendar;
import java.util.List;

public abstract class BaseCalendarFragment extends Fragment {
    private Context context;
    private DateSharedViewModel dateShareModel;
    private RecyclerView recyclerView;
    private CalendarUiAdapter calendarUiAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dateShareModel = new ViewModelProvider(requireParentFragment()).get(DateSharedViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base_calendar, container, false);
        this.recyclerView = layout.findViewById(R.id.item_list);
        this.calendarUiAdapter = new CalendarUiAdapter();
        this.calendarUiAdapter.setListener(new CalendarUiAdapter.OnActionListener() {
            @Override
            public void onListableButtonClicked() {
                calendarUiAdapter.setData(flatten(calendarUiAdapter.getData()));
            }

            @Override
            public void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("確認")
                        .setMessage("このデータを編集しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startEditActivity(type, id);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        if (this.recyclerView.getLayoutManager() == null) {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        }
        this.recyclerView.setAdapter(this.calendarUiAdapter);
        return layout;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        this.dateShareModel.getDateLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<Calendar>() {
                    @Override
                    public void onChanged(Calendar calendar) {
                        List<CalendarDisplayItem> dataList = getData(calendar);
                        calendarUiAdapter.setData(dataList);
                    }
                }
        );
    }

    private void startEditActivity(BopBaseUiModel.DataType dataType, long id) {
        switch (dataType) {
            case INCOME:{
                Intent incomeEditActIntent = new Intent(this.context, SettingEditIncomeActivity.class);
                Income data = RepositoryManager.getInstance().getDataById(Income.class, id);
                if (data != null){
                    incomeEditActIntent.putExtra("Income", data);
                    this.context.startActivity(incomeEditActIntent);
                }
                break;
            }
            case PURCHASE: {
                Intent purchaseEditActIntent = new Intent(this.context, SettingEditPurchaseActivity.class);
                Purchase data = RepositoryManager.getInstance().getDataById(Purchase.class, id);
                if (data != null) {
                    purchaseEditActIntent.putExtra("Purchase", data);
                    this.context.startActivity(purchaseEditActIntent);
                }
                break;
            }
            case EXPENSES: {
                Intent prcEditActIntent = new Intent(this.context, SettingEditPurchaseActivity.class);
                Expenses data = RepositoryManager.getInstance().getDataById(Expenses.class, id);
                Purchase motherPurchase = RepositoryManager.getInstance().getDataById(Purchase.class, data.getPurchaseId());
                if (motherPurchase != null) {
                    prcEditActIntent.putExtra("Purchase", motherPurchase);
                    this.context.startActivity(prcEditActIntent);
                }
                break;
            }
            case MONEY_MOVEMENT: {
                Intent mmEditActIntent = new Intent(this.context, SettingEditMoneyMovementActivity.class);
                var data = RepositoryManager.getInstance().getDataById(MoneyMovement.class, id);
                if (data != null) {
                    mmEditActIntent.putExtra("MoneyMovement", data);
                    this.context.startActivity(mmEditActIntent);
                }
                break;
            }
        }
    }
    abstract List<CalendarDisplayItem> getData(Calendar targetDate);
    abstract List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData);
}
