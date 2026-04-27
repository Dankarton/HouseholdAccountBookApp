package com.example.householdaccountbook.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.item.BopItemView;
import com.example.householdaccountbook.customviews.item.MoneyMovementItemView;
import com.example.householdaccountbook.customviews.item.TransactionItemView;
import com.example.householdaccountbook.myclasses.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.MoneyMovementUiModel;
import com.example.householdaccountbook.myclasses.TransactionUiModel;

import java.util.List;

public class DailyUiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BopBaseUiModel> BopUiList;
    public interface OnListItemActionListener {
        void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id);
    }
    private OnListItemActionListener listener;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BopBaseUiModel.UiLayoutType.BASIC_BOP.getCode()) {
            var uiView = new TransactionItemView(parent.getContext());
            var holder = new TransactionUiViewHolder(uiView);
            return setupViewHolder(uiView, holder);
        }
        if (viewType == BopBaseUiModel.UiLayoutType.MONEY_MOVEMENT.getCode()) {
            var uiView = new MoneyMovementItemView(parent.getContext());
            var holder = new MoneyMovementUiViewHolder(uiView);
            return setupViewHolder(uiView, holder);
        }
        throw new IllegalArgumentException("存在しないViewTypeです．");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BopBaseUiModel data = this.BopUiList.get(position);
        // TODO アイテムの追加あったら書き直すのめっちゃメンドくなりそうだから直せ
        if (holder instanceof TransactionUiViewHolder && data instanceof TransactionUiModel) {
            ((TransactionUiViewHolder) holder).bind((TransactionUiModel) data);
        } else if (holder instanceof MoneyMovementUiViewHolder && data instanceof MoneyMovementUiModel) {
            ((MoneyMovementUiViewHolder) holder).bind((MoneyMovementUiModel) data);
        }
    }

    @Override
    public int getItemCount() {
        return this.BopUiList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.BopUiList.get(position).getDataType().getUiType().getCode();
    }
    public void bind(List<BopBaseUiModel> dataList) {
        this.BopUiList = dataList;
    }
    public void setListener(OnListItemActionListener listener) {
        this.listener = listener;
    }
    private<T extends BopBaseUiModel, V extends BopItemView, VH extends BaseViewHolder<T, V>> VH setupViewHolder(V itemView, VH viewHolder) {
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

    // ホルダー系宣言
    /**
     * このアダプターで使うホルダーのベース
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
}
