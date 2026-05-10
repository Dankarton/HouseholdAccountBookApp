package com.example.householdaccountbook.customviews.calendar;

public interface ListableItem {
    public interface OnActionListener {
        public void onListableButtonClicked(boolean visible);
    }
    public void setListener(OnActionListener listener);
}
