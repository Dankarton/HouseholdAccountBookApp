package com.example.householdaccountbook.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.item.CategoryTotalItemView;

import java.util.ArrayList;
import java.util.List;

public class CategoryTotalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryTotalBinder> dataList = new ArrayList<>();

    public CategoryTotalListAdapter(List<CategoryTotalBinder> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryTotalViewHolder(new CategoryTotalItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CategoryTotalViewHolder) holder).bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    static class CategoryTotalViewHolder extends RecyclerView.ViewHolder {
        private final CategoryTotalItemView categoryTotalItemView;

        public CategoryTotalViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryTotalItemView = (CategoryTotalItemView) itemView;
        }
        public void bind(CategoryTotalBinder binder) {
            this.categoryTotalItemView.bind(
                    binder.color,
                    binder.name,
                    binder.totalAmount,
                    binder.ratio
            );
        }
    }
    public static class CategoryTotalBinder {
        private final int color;
        private final String name;
        private final int totalAmount;
        private final float ratio;

        public CategoryTotalBinder (int color, String name, int totalAmount, float ratio) {
            this.color = color;
            this.name = name;
            this.totalAmount = totalAmount;
            this.ratio = ratio;
        }
    }
}
