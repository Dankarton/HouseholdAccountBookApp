package myclasses;

public interface SelectableItem {
    void setSelectedState(boolean selected);
    boolean isSelected();
    Object getData();
}
