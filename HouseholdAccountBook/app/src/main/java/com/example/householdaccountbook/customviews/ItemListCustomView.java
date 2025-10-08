package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.householdaccountbook.R;

import java.util.ArrayList;

import myclasses.SelectableItem;

public class ItemListCustomView<T extends View & SelectableItem> extends ScrollView {
    private LinearLayout listLinearLayout;
    private T selectedItem = null;
    public ItemListCustomView(Context context) {
        super(context);
        init(context);
    }

    public ItemListCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemListCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_item_list, this, true);
        listLinearLayout = findViewById(R.id.item_list_linear_layout);
    }
    public void setItem(ArrayList<T> items) {
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(item);
                }
            });
            // 一番最初の項目を選択した状態にする
            if (i == 0) {
                selectItem(item);
            }
            else {
                item.setSelectedState(false);
            }
            listLinearLayout.addView(item);
        }
    }
    private void selectItem(T selectableItem) {
        if (this.selectedItem != null) {
            selectedItem.setSelectedState(false);
        }
        selectableItem.setSelectedState(true);
        this.selectedItem = selectableItem;
    }
    public T getSelectedItem() {
        return this.selectedItem;
    }
}
