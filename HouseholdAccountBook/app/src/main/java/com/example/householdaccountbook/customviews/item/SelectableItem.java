package com.example.householdaccountbook.customviews.item;

public interface SelectableItem<T> {
    void setSelectedState(boolean selected);
    boolean isSelected();
    long getEigenvalue();
    T getData();
}
