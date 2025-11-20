package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.ArrayList;

import com.example.householdaccountbook.customviews.item.SelectableItem;

/**
 * SelectableItemを継承したCustomView用の選択可能なリスト
 * 選択は一つだけ(ラジオボタン的な使用)
 * @param <T1> extends View & SelectableItem
 * @param <T2> SelectableItemが持つobject
 */
public class SelectableListCustomView<T1 extends View & SelectableItem<T2>, T2> extends ConstraintLayout {
    public interface OnItemSelectedListener {
        <T1> void onItemSelected(T1 itemView);
    }
    private OnItemSelectedListener listener;
    private GridLayout listLinearLayout;
    private T1 selectedItem = null;
    private int columnCount = 1;
    public SelectableListCustomView(Context context) {
        super(context);
        init(context);
    }

    public SelectableListCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectableListCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_item_list, this, true);
        this.listLinearLayout = findViewById(R.id.item_list_grid_layout);
        this.listLinearLayout.setColumnCount(this.columnCount);
    }
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.listLinearLayout.setColumnCount(this.columnCount);
        this.listLinearLayout.requestLayout();
        this.listLinearLayout.invalidate();
    }
    public void setItem(ArrayList<T1> items) {

        for (int i = 0; i < items.size(); i++) {
            T1 item = items.get(i);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(item);
                    if (listener != null) listener.onItemSelected(selectedItem);
                }
            });
            // 選択された状態でオブジェが渡されたらそれを選択状態にする．
            if (item.isSelected()) {
                selectItem(item);
            }
            else {
                item.setSelectedState(false);
            }
            // GridLayout内のItemのパラメータ設定
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;   // このリストは列方向に固定数並べるので，GridLayoutが自動で合わせれるよう0にしておく
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;    // 高さ方向はItem固有の高さ
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0f);
            item.setLayoutParams(params);
            listLinearLayout.addView(item);
        }
    }

    /**
     * アイテムを選択状態にする
     * @param selectableItem 選択されたアイテム
     */
    private void selectItem(T1 selectableItem) {
        if (this.selectedItem != null) {
            selectedItem.setSelectedState(false);
        }
        selectableItem.setSelectedState(true);
        this.selectedItem = selectableItem;
    }
    /**
     * 選択解除
     */
    public void deselect() {
        if (this.selectedItem != null) {
            selectedItem.setSelectedState(false);
            this.selectedItem = null;
        }
    }
    public T1 getSelectedItem() {
        return this.selectedItem;
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
