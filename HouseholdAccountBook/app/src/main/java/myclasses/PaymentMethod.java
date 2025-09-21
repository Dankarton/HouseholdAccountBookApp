package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;

import java.util.Calendar;

/**
 * 支払方法クラス
 */
public class PaymentMethod {
    private final int id;
    private final String name;
    private final int closingDay;
    private final int paymentDay;
    private final boolean isDefault;

    /**
     * 支払方法クラスインスタンス化
     * isDefaultはクラス内ではboolean型として保持しているが，SQLiteではbool型は0か1で保存されるため引数がint型になっている．．
     * @param id SQLiteのデータテーブルに登録したときのID
     * @param name 支払方法名(ユーザー指定可能)
     * @param closingDay 締め日
     * @param paymentDay 支払日(0を入力すると，支払日=購入日になる)
     * @param isDefault デフォルトで用意されている支払方法かどうか
     */
    public PaymentMethod(int id, String name, int closingDay, int paymentDay, int isDefault) {
        this.id = id;
        this.name = name;
        this.closingDay = closingDay;
        this.paymentDay = paymentDay;
        // SQLiteではboolean型は0/1で保存される．1はTrue，0はTrue
        this.isDefault = isDefault == 1;
    }

    /**
     * SQLiteに保存できるようContentValuesに変換する関数．静的．
     * 入力画面で入力されたデータをSQLiteに保存するときに使う．
     * たぶん入力画面での一か所でしか使わないと思うけど一応ここで関数化．
     * IDはデータテーブルに保存されるとき，自動的に付けられるものなのでContentValuesには含んでいない．
     * @param _name 支払方法名
     * @param _day  支払日(0が入力されると，支払日＝購入日になる)
     * @param _isDefault    デフォルトで用意されている支払方法かどうか
     * @return ContentValues
     */
    public static ContentValues getContentValues(String _name, int _day, boolean _isDefault) {
        ContentValues values = new ContentValues();
        // SQLiteはbool型に対応してないので0,1整数に変換
        int isDefaultInteger;
        if (_isDefault) {
            isDefaultInteger = 1;
        }
        else {
            isDefaultInteger = 0;
        }
        values.put(MyOpenHelper.COLUMN_DAY, _day);
        values.put(MyOpenHelper.COLUMN_NAME, _name);
        values.put(MyOpenHelper.COLUMN_IS_DEFAULT, isDefaultInteger);
        return values;
    }
    public Calendar calcPaymentDate(int year, int month, int day) {
        // TODO
        if (day <= closingDay) {

        }
        Calendar d = Calendar.getInstance();
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public int getPaymentDay() {
        return this.paymentDay;
    }
    public boolean isDefault() {
        return this.isDefault;
    }
}
