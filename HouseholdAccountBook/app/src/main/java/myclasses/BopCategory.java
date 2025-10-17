package myclasses;

public class BopCategory {
    private final int _id;
    private final String _name;
    private final int _colorCode;
    private final int _index;

    public BopCategory(int id, String name, int colorCode, int index) {
        this._id = id;
        this._name = name;
        this._colorCode = colorCode;
        this._index = index;
    }
    public int getId() {
        return this._id;
    }
    public String getName() {
        return this._name;
    }
    public int getColorCode() {
        return this._colorCode;
    }
    public int getIndex() { return this._index; }
}
