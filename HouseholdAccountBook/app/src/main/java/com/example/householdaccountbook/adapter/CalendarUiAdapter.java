package com.example.householdaccountbook.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.CalendarCustomView;
import com.example.householdaccountbook.customviews.item.BopItemView;
import com.example.householdaccountbook.customviews.item.DailyUiItem;
import com.example.householdaccountbook.customviews.item.MoneyMovementItemView;
import com.example.householdaccountbook.customviews.item.TransactionItemView;
import com.example.householdaccountbook.myclasses.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.myclasses.calendarentity.DailyUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.TransactionUiModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalendarUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnActionListener {
        void onPullDownButtonClicked();
        void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id);
    }

    private List<CalendarDisplayItem> dataList;

    public CalendarUiAdapter(List<CalendarDisplayItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CalendarDisplayItem.UiLayoutType.CALENDAR.getCode()) {
            return new CalendarViewHolder(new CalendarCustomView(parent.getContext()));
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.DAILY.getCode()) {
            return new DailyUiViewHolder(new DailyUiItem(parent.getContext()));
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.WALLET.getCode()) {

        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.TRANSACTION.getCode()) {

        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.MONEY_MOVEMENT.getCode()) {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // +1してるのはカレンダーオブジェクト分
        return this.dataList.size() + 1;
    }
    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private final CalendarCustomView calendarView;
        public CalendarViewHolder(@NonNull CalendarCustomView itemView) {
            super(itemView);
            this.calendarView = itemView;
        }
        public void bind(Calendar targetDate, HashMap<Integer, Integer> dateList, int maxAmount) {
            this.calendarView.bind(targetDate, dateList, maxAmount);
        }
    }
    static class DailyUiViewHolder extends RecyclerView.ViewHolder {
        private final DailyUiItem itemView;
        public DailyUiViewHolder(@NonNull DailyUiItem itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        public void bind(DailyUiModel data) {
            this.itemView.bind(data.getDate(), data.getDeltaAmount(), data.);
        }
    }
    /**
     *      * このアダプターで使うホルダーのベース
     * @param <T>
     * @param <V>
     */
    static abstract class BaseViewHolder<T extends BopBaseUiModel, V extends BopItemView> extends RecyclerView.ViewHolder {
        protected V itemView;
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = (V) itemView;
        }
        public abstract void bind(T data);
    }

    /**
     * 支出とか収入とかお金を外部とやり取りしたとき用のUi
     */
    static class TransactionUiViewHolder extends BopUiAdapter.BaseViewHolder<TransactionUiModel, TransactionItemView> {
        public TransactionUiViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void bind(TransactionUiModel data) {
            this.itemView.bind(
                    data.getCategoryColor(),
                    data.getCategoryName(),
                    data.getMemo(),
                    data.getAdditionalMemo(),
                    data.getSignedAmount()
            );
        }
    }

    /**
     * 振替とかチャージとか内部のお金の移動を表示すると起用のui
     */
    static class MoneyMovementUiViewHolder extends BopUiAdapter.BaseViewHolder<MoneyMovementUiModel, MoneyMovementItemView> {
        public MoneyMovementUiViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void bind(MoneyMovementUiModel data) {
            this.itemView.bind(
                    data.getToWalletName(),
                    data.getFromWalletName(),
                    data.getMemo(),
                    data.getAmount()
            );
        }
    }
    private<T extends BopBaseUiModel, V extends BopItemView, VH extends BopUiAdapter.BaseViewHolder<T, V>> VH setupViewHolder(V itemView, VH viewHolder) {
        itemView.setListener(
                new BopItemView.OnActionListener() {
                    @Override
                    public void onMoreActionButtonClicked() {
                        int position = viewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            BopBaseUiModel data = BopUiList.get(position);
                            listener.onMoreActionButtonClicked(data.getDataType(), data.getId());
                        }

                    }
                }
        );
        return viewHolder;
    }
}
