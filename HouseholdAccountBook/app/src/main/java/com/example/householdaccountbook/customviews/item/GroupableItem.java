package com.example.householdaccountbook.customviews.item;

import android.graphics.drawable.Drawable;

import com.example.householdaccountbook.R;

public interface GroupableItem {
    public enum PositionType {
        TOP(0, R.drawable.group_item_top),
        MIDDLE(1, R.drawable.group_item_mid),
        BOTTOM(2, R.drawable.group_item_bottom),
        SINGLE(3, R.drawable.group_item_single);

        private final int code;
        private final int resourceIntCode;
        PositionType(int code, int resource) {
            this.code = code;
            this.resourceIntCode = resource;
        }
        public int getCode() { return this.code; }
        public int getResource() { return this.resourceIntCode; }
    }
    void setGroupPosition(PositionType type);
    PositionType getGroupPosition();
}
