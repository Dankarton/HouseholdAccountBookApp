package com.example.householdaccountbook.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EnumSpinnerAdapter<T extends Enum<T>> extends ArrayAdapter<T> {
    private final LabelProvider<T> labelProvider;
    public EnumSpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects, @NonNull LabelProvider<T> labelProvider) {
        super(context, resource, objects);
        this.labelProvider = labelProvider;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = view.findViewById(android.R.id.text1);
        text.setText(labelProvider.getLabel(getItem(position)));
        return view;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView text = view.findViewById(android.R.id.text1);
        text.setText(labelProvider.getLabel(getItem(position)));
        return view;
    }

    public interface LabelProvider<T> {
        String getLabel(T item);
    }
}
