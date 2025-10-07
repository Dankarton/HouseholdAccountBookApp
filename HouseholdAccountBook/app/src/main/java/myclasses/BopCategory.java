package myclasses;

public class BopCategory {
    private final int _id;
    private final String _name;
    private final int _colorCode;

    public BopCategory(int id, String name, int colorCode) {
        this._id = id;
        this._name = name;
        this._colorCode = colorCode;
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
}
