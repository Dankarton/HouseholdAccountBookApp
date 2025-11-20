package com.example.householdaccountbook.fragments.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.R;

import java.util.ArrayList;

import myclasses.DatabaseEntity;
import com.example.householdaccountbook.customviews.item.SelectableItem;

public class BaseListingFragment <T1 extends View & SelectableItem<T2>, T2 extends DatabaseEntity> extends Fragment {
    public interface OnInputActionListener {
        <T2> void onItemClicked(T2 data);
        void onNewCreateButtonClicked(int lastIndex);
    }
    private OnInputActionListener listener = null;
    private LinearLayout itemListView;
    private ArrayList<T1> listingItems = new ArrayList<>();
    private int itemNum = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base_listing, container, false);
        this.itemListView = layout.findViewById(R.id.item_list);
        applyListingItems();
        layout.findViewById(R.id.create_new_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onNewCreateButtonClicked(itemNum);
                }
            }
        });
        return layout;
    }
    public void  setItems(ArrayList<T1> itemList) {
        this.listingItems = itemList;
        this.itemNum = itemList.size();
        applyListingItems();
    }
    private void applyListingItems() {
        if (this.itemListView == null) return;

        for (T1 item : this.listingItems) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClicked(item.getData());
                    }
                }
            });
            this.itemListView.addView(item);
        }
    }

    public void setListener(OnInputActionListener listener) {
        this.listener = listener;
    }
}
