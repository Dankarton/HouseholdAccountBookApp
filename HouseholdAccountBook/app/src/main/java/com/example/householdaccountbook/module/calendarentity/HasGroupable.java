package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.item.GroupableItem;

public interface HasGroupable {
    public GroupableItem.PositionType getPositionType();
    public void setPositionType(GroupableItem.PositionType type);
}
