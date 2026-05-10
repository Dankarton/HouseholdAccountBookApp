package com.example.householdaccountbook.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.calendar.CalendarCustomView;
import com.example.householdaccountbook.customviews.calendar.ListableItem;
import com.example.householdaccountbook.customviews.calendar.WalletRecordCustomView;
import com.example.householdaccountbook.customviews.calendar.BopItemView;
import com.example.householdaccountbook.customviews.calendar.DailyUiItem;
import com.example.householdaccountbook.customviews.calendar.MoneyMovementItemView;
import com.example.householdaccountbook.customviews.calendar.TransactionItemView;
import com.example.householdaccountbook.myclasses.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.DailyUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.HasListVisible;
import com.example.householdaccountbook.myclasses.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.WalletUiModel;

import java.util.List;

public class CalendarUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnActionListener {
        void onListableButtonClicked();
        void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id);
    }
    private List<CalendarDisplayItem> dataList;
    private OnActionListener listener;

    public void setData(List<CalendarDisplayItem> dataList) {
        this.dataList = dataList;
    }
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
    public List<CalendarDisplayItem> getData() {
        return this.dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CalendarDisplayItem.UiLayoutType.CALENDAR.getCode()) {
            return new CalendarViewHolder(new CalendarCustomView(parent.getContext()));
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.DAILY.getCode()) {
            var itemView = new DailyUiItem(parent.getContext());
            var dailyHolder = new DailyUiViewHolder(itemView);
            setupViewHolder(itemView, dailyHolder);
            return dailyHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.WALLET.getCode()) {
            var itemView = new WalletRecordCustomView(parent.getContext());
            var walletHolder = new WalletViewHolder(itemView);
            setupViewHolder(itemView, walletHolder);
            return walletHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.TRANSACTION.getCode()) {
            var itemView = new TransactionItemView(parent.getContext());
            var transactionHolder = new TransactionUiViewHolder(itemView);
            setupViewHolder(itemView, transactionHolder);
            return transactionHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.MONEY_MOVEMENT.getCode()) {
            var itemView = new MoneyMovementItemView(parent.getContext());
            var moneyMovementHolder = new MoneyMovementUiViewHolder(itemView);
            setupViewHolder(itemView, moneyMovementHolder);
            return moneyMovementHolder;
        }
        else {
            throw new IllegalArgumentException("存在しないViewType");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CalendarDisplayItem data = this.dataList.get(position);
        if (data instanceof CalendarUiModel && holder instanceof CalendarViewHolder) {
            ((CalendarViewHolder) holder).bind((CalendarUiModel) data);
        }
        else if (data instanceof DailyUiModel && holder instanceof DailyUiViewHolder) {
            ((DailyUiViewHolder) holder).bind((DailyUiModel) data);
        }
        else if (data instanceof WalletUiModel && holder instanceof WalletViewHolder) {
            ((WalletViewHolder) holder).bind((WalletUiModel) data);
        }
        else if (data instanceof TransactionUiModel && holder instanceof TransactionUiViewHolder) {
            ((TransactionUiViewHolder) holder).bind((TransactionUiModel) data);
        }
        else if (data instanceof MoneyMovementUiModel && holder instanceof MoneyMovementUiViewHolder) {
            ((MoneyMovementUiViewHolder) holder).bind((MoneyMovementUiModel) data);
        }
        else {
            throw new IllegalArgumentException("存在しないViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return this.dataList.get(position).getViewType().getCode();
    }

    /**
     * カレンダーのUI
     */
    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private final CalendarCustomView calendarView;
        public CalendarViewHolder(@NonNull CalendarCustomView itemView) {
            super(itemView);
            this.calendarView = itemView;
        }
        public void bind(CalendarUiModel data) {
            this.calendarView.bind(data.getTargetDate(), data.getDateData(), data.getMaxAmount());
        }
    }

    /**
     * 日時UI
     */
    static class DailyUiViewHolder extends BaseViewHolder<DailyUiModel, DailyUiItem> {
        private final DailyUiItem itemView;
        public DailyUiViewHolder(@NonNull DailyUiItem itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        public void bind(DailyUiModel data) {
            this.itemView.bind(data.getDate(), data.getDeltaAmount(), data.isListVisible());
        }
    }
    /**
     * ウォレットUI
     */
    static class WalletViewHolder extends BaseViewHolder<WalletUiModel, WalletRecordCustomView> {
        public WalletViewHolder(@NonNull WalletRecordCustomView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        @Override
        public void bind(WalletUiModel data) {
            this.itemView.bind(data.getWalletName(), data.getDeltaAmount(), data.getCurrentAmount(), data.isListVisible());
        }
    }
    /**
     * 支出とか収入とかお金を外部とやり取りしたとき用のUi
     */
    static class TransactionUiViewHolder extends BaseViewHolder<TransactionUiModel, TransactionItemView> {
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
    static class MoneyMovementUiViewHolder extends BaseViewHolder<MoneyMovementUiModel, MoneyMovementItemView> {
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
    /**
     *      * このアダプターで使うホルダーのベース
     * @param <T>
     * @param <V>
     */
    static abstract class BaseViewHolder<T extends CalendarDisplayItem, V> extends RecyclerView.ViewHolder {
        protected V itemView;
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = (V) itemView;
        }
        public abstract void bind(T data);
    }
    private<T extends ListableItem> void setupViewHolder(T itemView, RecyclerView.ViewHolder viewHolder) {
        itemView.setListener(
                new ListableItem.OnActionListener() {
                    @Override
                    public void onListableButtonClicked(boolean visible) {
                        int position = viewHolder.getAdapterPosition();
                        if (listener == null) return;
                        if (position == RecyclerView.NO_POSITION) return;
                        CalendarDisplayItem data = dataList.get(position);
                        if (data instanceof HasListVisible) {
                            ((HasListVisible) data).setListVisible(!((HasListVisible) data).isListVisible());
                            listener.onListableButtonClicked();
                        }
                    }
                }
        );
    }
    /**
     * アイテムへのリスナー登録とか毎回書くのめんどくさいからまとめて処理できるようにした関数。Javaの引数はオブジェ参照だからちゃんと動くはず。
     * @param itemView
     * @param viewHolder
     * @param <T>
     * @param <V>
     * @param <VH>
     */
    private<T extends BopBaseUiModel, V extends BopItemView, VH extends BaseViewHolder<T, V>> void setupViewHolder(V itemView, VH viewHolder) {
        itemView.setListener(
                new BopItemView.OnActionListener() {
                    @Override
                    public void onMoreActionButtonClicked() {
                        int position = viewHolder.getAdapterPosition();
                        if (listener == null) return;
                        if (position == RecyclerView.NO_POSITION) return;

                        CalendarDisplayItem data = dataList.get(position);
                        if (data instanceof BopBaseUiModel){
                            listener.onMoreActionButtonClicked(((BopBaseUiModel) data).getDataType(), ((BopBaseUiModel) data).getId());
                        }
                    }
                }
        );
    }

}
