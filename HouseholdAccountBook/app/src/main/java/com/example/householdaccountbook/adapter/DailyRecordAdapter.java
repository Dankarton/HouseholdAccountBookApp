package com.example.householdaccountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.HouseHoldApp;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.item.ExpensesItemView;
import com.example.householdaccountbook.customviews.item.IncomeItemView;
import com.example.householdaccountbook.customviews.item.PurchaseItemView;

import java.util.List;

import myclasses.BOP;
import myclasses.Expenses;
import myclasses.Income;
import myclasses.IncomeCategory;
import myclasses.PaymentMethod;
import myclasses.Purchase;
import myclasses.PurchaseCategory;

public class DailyRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private enum ViewType {
        INCOME,
        PURCHASE,
        EXPENSE
    }
    public interface OnListItemActionListener {
        void onMoreActionButtonClicked(BOP data);
    }

    private OnListItemActionListener listener;
    private final Context context;
    private final List<BOP> bopDataList;

    public DailyRecordAdapter(Context context, List<BOP> dataList) {
        this.context = context;
        this.bopDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewType.INCOME.ordinal()) {
            return new IncomeViewHolder(new IncomeItemView(parent.getContext()));
        } else if (viewType == ViewType.PURCHASE.ordinal()){
            return new PurchaseViewHolder(new PurchaseItemView(parent.getContext()));
        }
        else {
            return new ExpensesViewHolder(new ExpensesItemView(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BOP data = this.bopDataList.get(position);
        HouseHoldApp app = (HouseHoldApp) this.context.getApplicationContext();
        if (data instanceof Income) {
            IncomeCategory category = app.getIncomeCategoryRepository().getDataById(data.getCategoryId());
            IncomeViewHolder incomeHolder = (IncomeViewHolder) holder;
            incomeHolder.bind(category.getColorCode(), category.getName(), data.getMemo(), data.getAmount());
            incomeHolder.setListener(this.listener, (Income) data);
        } else if (data instanceof Purchase) {
            PurchaseCategory category = app.getPurchaseCategoryRepository().getDataById(data.getCategoryId());
            PaymentMethod method = app.getPaymentMethodRepository().getDataById(((Purchase) data).getPaymentMethodId());
            PurchaseViewHolder purchaseHolder = (PurchaseViewHolder) holder;
            purchaseHolder.bind(category.getColorCode(), category.getName(), data.getMemo(), method.getName(), data.getAmount());
            purchaseHolder.setListener(this.listener, (Purchase) data);

        } else if (data instanceof Expenses) {
            PurchaseCategory category = app.getPurchaseCategoryRepository().getDataById(data.getCategoryId());
            PaymentMethod method = app.getPaymentMethodRepository().getDataById(((Expenses) data).getPaymentMethodId());
            ExpensesViewHolder expensesHolder = (ExpensesViewHolder) holder;
            expensesHolder.bind(category.getColorCode(), category.getName(), data.getMemo(), method.getName(), data.getAmount());
            expensesHolder.setListener(this.listener, (Expenses) data);
        }
    }

    @Override
    public int getItemCount() {
        return bopDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BOP obj = this.bopDataList.get(position);
        if (obj instanceof Income) {
            return ViewType.INCOME.ordinal();
        } else if (obj instanceof Purchase) {
            return ViewType.PURCHASE.ordinal();
        } else if (obj instanceof Expenses) {
            return ViewType.EXPENSE.ordinal();
        }
        return -1;
    }
    public void setListener(OnListItemActionListener listener) {
        this.listener = listener;
    }

    static class IncomeViewHolder extends RecyclerView.ViewHolder {

        IncomeItemView incomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.incomeView = (IncomeItemView) itemView;
        }

        public void bind(int categoryColor, String categoryName, String memo, int amount) {

            this.incomeView.bind(categoryColor, categoryName, memo, amount);
        }
        public void setListener(OnListItemActionListener listener, Income data) {
            this.incomeView.setListener(new IncomeItemView.OnActionListener() {
                @Override
                public void onMoreActionButtonClicked() {
                    if (listener != null) listener.onMoreActionButtonClicked(data);
                }
            });
        }
    }

    static class PurchaseViewHolder extends RecyclerView.ViewHolder {

        PurchaseItemView purchaseView;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.purchaseView = (PurchaseItemView) itemView;
        }

        public void bind(int categoryColor, String categoryName, String memo, String paymentMethodName, int amount) {

            this.purchaseView.bind(categoryColor, categoryName, memo, paymentMethodName, amount);
        }
        public void setListener(OnListItemActionListener listener, Purchase data) {
            this.purchaseView.setListener(new PurchaseItemView.OnActionListener() {
                @Override
                public void onMoreActionButtonClicked() {
                    if (listener != null) listener.onMoreActionButtonClicked(data);
                }
            });
        }
    }

    static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        ExpensesItemView expensesView;

        public ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.expensesView = (ExpensesItemView) itemView;
        }
        public void bind(int categoryColor, String categoryName, String memo, String paymentMethodName, int amount) {

            this.expensesView.bind(categoryColor, categoryName, memo, paymentMethodName, amount);
        }
        public void setListener(OnListItemActionListener listener, Expenses data) {
            this.expensesView.setListener(new ExpensesItemView.OnActionListener() {
                @Override
                public void onMoreActionButtonClicked() {
                    if (listener != null) listener.onMoreActionButtonClicked(data);
                }
            });
        }
    }
}
