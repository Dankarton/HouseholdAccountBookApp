package myclasses;

public interface SelectableItem<T> {
    void setSelectedState(boolean selected);
    boolean isSelected();
    T getData();
}
