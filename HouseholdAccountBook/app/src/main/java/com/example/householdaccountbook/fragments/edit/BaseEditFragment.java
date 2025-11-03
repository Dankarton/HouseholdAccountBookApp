package com.example.householdaccountbook.fragments.edit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.householdaccountbook.R;

import myclasses.DatabaseEntity;

public abstract class BaseEditFragment<T extends DatabaseEntity> extends Fragment {
    public interface OnInputActionListener<T extends DatabaseEntity> {
        void onSaveButtonClicked(T data);
        void onDeleteButtonClicked(T data);
    }
    protected OnInputActionListener<T> listener;
    protected T databaseEntityData;
    protected Button saveButton;
    protected Button deleteButton;
    protected FrameLayout contentContainer;
    public BaseEditFragment() { /*Do nothing*/ }
    public BaseEditFragment(T data) {
        this.databaseEntityData = data;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_edit, container, false);
        this.saveButton = view.findViewById(R.id.save_button);
        this.deleteButton = view.findViewById(R.id.delete_button);
        this.contentContainer = view.findViewById(R.id.edit_container);
        inflater.inflate(getContainerContentLayoutId(), this.contentContainer, true);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked();
            }
        });
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked();
            }
        });
        this.saveButton.endBatchEdit();
        if(databaseEntityData.getId() != null) {
            this.deleteButton.setVisibility(View.VISIBLE);
            this.deleteButton.setEnabled(true);
        }
        else {
            this.deleteButton.setVisibility(View.GONE);
            this.deleteButton.setEnabled(false);
        }
        return view;
    }
    public void setListener(OnInputActionListener<T> listener) {
        this.listener = listener;
    }
    protected abstract int getContainerContentLayoutId();
    protected abstract void onSaveClicked();
    protected abstract void onDeleteClicked();
}