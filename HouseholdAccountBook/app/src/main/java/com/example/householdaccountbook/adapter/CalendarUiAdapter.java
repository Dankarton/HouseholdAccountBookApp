package com.example.householdaccountbook.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.calendar.CalendarCustomView;
import com.example.householdaccountbook.customviews.calendar.GroupableItem;
import com.example.householdaccountbook.customviews.calendar.ListableItem;
import com.example.householdaccountbook.customviews.calendar.WalletRecordCustomView;
import com.example.householdaccountbook.customviews.calendar.BopItemView;
import com.example.householdaccountbook.customviews.calendar.DailyUiItem;
import com.example.householdaccountbook.customviews.calendar.MoneyMovementItemView;
import com.example.householdaccountbook.customviews.calendar.TransactionItemView;
import com.example.householdaccountbook.module.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.module.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.module.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.module.calendarentity.DailyUiModel;
import com.example.householdaccountbook.module.calendarentity.HasGroupable;
import com.example.householdaccountbook.module.calendarentity.HasListVisible;
import com.example.householdaccountbook.module.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.module.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.module.calendarentity.WalletUiModel;

import java.util.List;
import java.util.Objects;

public class CalendarUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnActionListener {
        void onListableButtonClicked();
        void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id);
    }
    private List<CalendarDisplayItem> dataList;
    private OnActionListener listener;

    public void setData(List<CalendarDisplayItem> newDataList) {
        if (this.dataList == null) {
            this.dataList = newDataList;
            notifyItemRangeInserted(0, newDataList.size());
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return dataList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newDataList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        CalendarDisplayItem oldData = dataList.get(oldItemPosition);
                        CalendarDisplayItem newData = newDataList.get(newItemPosition);
                        return Objects.equals(oldData.getUniqueKey(), newData.getUniqueKey());
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        CalendarDisplayItem oldData = dataList.get(oldItemPosition);
                        CalendarDisplayItem newData = newDataList.get(newItemPosition);
                        if (oldData.equalData(newData)) {
                            // データがアップデートされてたら更新しないといけないのでfalseを返す
                            return !newData.didUpdated();
                        }
                        else {
                            return false;
                        }
                    }
                }
        );
        this.dataList = newDataList;
        result.dispatchUpdatesTo(this);
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
            CalendarCustomView calendarView = new CalendarCustomView(parent.getContext());

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int margin = (int) (24 * parent.getContext().getResources().getDisplayMetrics().density);
            params.setMargins(margin, 0, margin, 0);

            calendarView.setLayoutParams(params);

            return new CalendarViewHolder(calendarView);
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.DAILY.getCode()) {
            var itemView = new DailyUiItem(parent.getContext());
            var dailyHolder = new DailyUiViewHolder(itemView);
            setupViewHolder(itemView, dailyHolder);

            itemView.setLayoutParams(generateLayoutParams(parent));

            return dailyHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.WALLET.getCode()) {
            var itemView = new WalletRecordCustomView(parent.getContext());
            var walletHolder = new WalletViewHolder(itemView);
            setupViewHolder(itemView, walletHolder);

            itemView.setLayoutParams(generateLayoutParams(parent));

            return walletHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.TRANSACTION.getCode()) {
            var itemView = new TransactionItemView(parent.getContext());
            var transactionHolder = new TransactionUiViewHolder(itemView);
            setupViewHolder(itemView, transactionHolder);

            itemView.setLayoutParams(generateLayoutParams(parent));

            return transactionHolder;
        }
        else if (viewType == CalendarDisplayItem.UiLayoutType.MONEY_MOVEMENT.getCode()) {
            var itemView = new MoneyMovementItemView(parent.getContext());
            var moneyMovementHolder = new MoneyMovementUiViewHolder(itemView);
            setupViewHolder(itemView, moneyMovementHolder);

            itemView.setLayoutParams(generateLayoutParams(parent));

            return moneyMovementHolder;
        }
        else {
            throw new IllegalArgumentException("存在しないViewType");
        }
    }
    private RecyclerView.LayoutParams generateLayoutParams(ViewGroup parent) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        float density = parent.getContext().getResources().getDisplayMetrics().density;
        int marginVertical = (int) (4 * density);
        int marginHorizontal = (int) (8 * density);
        params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
        return params;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CalendarDisplayItem data = this.dataList.get(position);
        if (data instanceof CalendarUiModel && holder instanceof CalendarViewHolder) {
            Log.d("CalendarUiAdapter", "CalendarUiModel and CalendarViewHolder");
            ((CalendarViewHolder) holder).bind((CalendarUiModel) data);
            return;
        }
        else if (data instanceof DailyUiModel && holder instanceof DailyUiViewHolder) {
            ((DailyUiViewHolder) holder).bind((DailyUiModel) data);
        }
        else if (data instanceof WalletUiModel && holder instanceof WalletViewHolder) {
            ((WalletViewHolder) holder).bind((WalletUiModel) data);
        }
        else if (data instanceof TransactionUiModel && holder instanceof TransactionUiViewHolder) {
            Log.d("CalendarUiAdapter", "TransactionUiModel and TransactionUiViewHolder");
            ((TransactionUiViewHolder) holder).bind((TransactionUiModel) data);
        }
        else if (data instanceof MoneyMovementUiModel && holder instanceof MoneyMovementUiViewHolder) {
            Log.d("CalendarUiAdapter", "MoneyMovementUiModel and MoneyMovementUiViewHolder");
            ((MoneyMovementUiViewHolder) holder).bind((MoneyMovementUiModel) data);
        }
        else {
            throw new IllegalArgumentException("存在しないViewHolder");
        }

        ViewGroup.LayoutParams rawParams = holder.itemView.getLayoutParams();

        if (rawParams instanceof RecyclerView.LayoutParams params) {
            int marginTop = 0;
            int marginBottom = 0;

            int space = (int) (4 * holder.itemView.getContext().getResources().getDisplayMetrics().density);
            if (((HasGroupable) data).getPositionType() == GroupableItem.PositionType.SINGLE || ((HasGroupable) data).getPositionType() == GroupableItem.PositionType.TOP) {
                marginTop = space;
            }
            if (((HasGroupable) data).getPositionType() == GroupableItem.PositionType.SINGLE || ((HasGroupable) data).getPositionType() == GroupableItem.PositionType.BOTTOM) {
                marginBottom = space;
            }
            params.setMargins(params.leftMargin, marginTop, params.rightMargin, marginBottom);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        if (this.dataList != null)
            return this.dataList.size();
        else{
            Log.d("CalendarUiAdapter", "データが挿入される前に描画が始まりました。");
            return 0;
        }

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
            data.used();
        }
    }

    /**
     * 日時UI
     */
    static class DailyUiViewHolder extends BaseViewHolder<DailyUiModel, DailyUiItem> {
        DailyUiItem itemView;
        public DailyUiViewHolder(@NonNull DailyUiItem itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        public void bind(DailyUiModel data) {
            this.itemView.bind(data.getDate(), data.getDeltaAmount(), data.getPositionType(), data.isListVisible());
            this.itemView.setListButtonValid(data.isListVisibleValid());
            data.used();
        }
    }
    /**
     * ウォレットUI
     */
    static class WalletViewHolder extends BaseViewHolder<WalletUiModel, WalletRecordCustomView> {
        WalletRecordCustomView itemView;
        public WalletViewHolder(@NonNull WalletRecordCustomView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        @Override
        public void bind(WalletUiModel data) {
            this.itemView.bind(data.getWalletName(), data.getDeltaAmount(), data.getCurrentAmount(), data.getPositionType(), data.isListVisible());
            this.itemView.setListButtonValid(data.isListVisibleValid());
            data.used();
        }
    }
    /**
     * 支出とか収入とかお金を外部とやり取りしたとき用のUi
     */
    static class TransactionUiViewHolder extends BaseViewHolder<TransactionUiModel, TransactionItemView> {
        TransactionItemView itemView;
        public TransactionUiViewHolder(@NonNull TransactionItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        public void bind(TransactionUiModel data) {
            this.itemView.bind(
                    data.getCategoryColor(),
                    data.getCategoryName(),
                    data.getMemo(),
                    data.getAdditionalMemo(),
                    data.getSignedAmount(),
                    data.getPositionType()
            );
            data.used();
        }
    }

    /**
     * 振替とかチャージとか内部のお金の移動を表示すると起用のui
     */
    static class MoneyMovementUiViewHolder extends BaseViewHolder<MoneyMovementUiModel, MoneyMovementItemView> {
        MoneyMovementItemView itemView;
        public MoneyMovementUiViewHolder(@NonNull MoneyMovementItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        public void bind(MoneyMovementUiModel data) {
            this.itemView.bind(
                    data.getToWalletName(),
                    data.getFromWalletName(),
                    data.getMemo(),
                    data.getSignedAmount(),
                    data.getPositionType()
            );
            data.used();
        }
    }
    /**
     *      * このアダプターで使うホルダーのベース
     * @param <T>
     * @param <V>
     */
    static abstract class BaseViewHolder<T extends CalendarDisplayItem, V> extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
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
                            Log.d(this.getClass()+"", "Data instanceof HasListVisible class: " + data.getClass() + ", Visible: " + visible);
                            ((HasListVisible) data).setListVisible(visible);
                            listener.onListableButtonClicked();
                        }
                        else {
                            Log.d("BaseViewHolder", "HasListVisibleが継承されてないクラス");
                        }
                    }
                }
        );
    }
    /**
     * アイテムへのリスナー登録とか毎回書くのめんどくさいからまとめて処理できるようにした関数。
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
                        else {
                            Log.d("BaseViewHolder", "BopBaseUiModelが継承されてないクラス");
                        }
                    }
                }
        );
    }

}
