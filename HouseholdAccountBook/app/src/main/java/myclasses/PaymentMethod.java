package myclasses;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.MyOpenHelper;

import java.util.Calendar;

import strategy.closingstrategy.ClosingStrategy;
import strategy.closingstrategy.EndOfMonthClosingRule;
import strategy.closingstrategy.FixedDayClosingRule;
import strategy.closingstrategy.NoneClosingRule;
import strategy.paymentstrategy.AfterClosingPaymentRule;
import strategy.paymentstrategy.EndOfMonthPaymentRule;
import strategy.paymentstrategy.FixedDayPaymentRule;
import strategy.paymentstrategy.PaymentStrategy;
import strategy.paymentstrategy.SameDayPaymentRule;

/**
 * 支払方法クラス
 */
public class PaymentMethod {
    public enum ClosingRule {
        FixedDay(0, "毎月指定日", true, "日付(日)") {
            @Override
            public ClosingStrategy getStrategy(Integer settingNum) {
                return new FixedDayClosingRule(settingNum);
            }
        },    //毎月指定日が締め日
        EndOfMonth(1, "月末", false, "") {
            @Override
            public ClosingStrategy getStrategy(Integer settingNum) {
                return new EndOfMonthClosingRule();
            }
        },  //月末が締め日
        None(2, "締め日なし(当日)", false, "") {
            @Override
            public ClosingStrategy getStrategy(Integer settingNum) {
                return new NoneClosingRule();
            }
        };        //締め日なし(当日払いなどの場合)

        private final int code;
        private final String nameText;
        private final boolean usesSettingNum;
        private final String settingNumText;

        ClosingRule(int code, String text, boolean usesSettingNum, String settingNumText) {
            this.code = code;
            this.nameText = text;
            this.usesSettingNum = usesSettingNum;
            this.settingNumText = settingNumText;
        }
        public int getCode() {
            return this.code;
        }
        public String getNameText() { return this.nameText; }
        public boolean usesSettingNum() { return this.usesSettingNum; }
        public String getSettingNumText() { return this.settingNumText; }
        public static ClosingRule fromCode(int code) {
            for (ClosingRule rule : ClosingRule.values()) {
                if(rule.code == code) {
                    return rule;
                }
            }
            return ClosingRule.None;
        }
        @NonNull
        @Override
        public String toString() {
            return this.nameText;
        }

        public abstract ClosingStrategy getStrategy(Integer settingNum);
    }
    public enum PaymentRule {
        FixedDay(0, "毎月指定日に支払い", true, "日付(日)") {
            @Override
            public PaymentStrategy getStrategy(Integer settingNum) {
                return new FixedDayPaymentRule(settingNum);
            }
        },        // 毎月指定日に支払
        EndOfMonth(1, "月末に支払い", false, "") {
            @Override
            public PaymentStrategy getStrategy(Integer settingNum) {
                return new EndOfMonthPaymentRule();
            }
        },      // 月末に支払
        AfterClosing(2, "締め日の何日後に支払い", true, "日数") {
            @Override
            public PaymentStrategy getStrategy(Integer settingNum) {
                return new AfterClosingPaymentRule(settingNum);
            }
        },    // 締め日の何日後に支払
        SameDay(3, "無し(当日払い)", false, "") {
            @Override
            public PaymentStrategy getStrategy(Integer settingNum) {
                return new SameDayPaymentRule();
            }
        };            // ルール無し(当日払い)

        private final int code;
        private final String nameText;
        private final boolean usesSettingNum;
        private final String settingNumText;


        PaymentRule(int code, String text, boolean usesSettingNum, String settingNumText) {
            this.code = code;
            this.nameText = text;
            this.usesSettingNum = usesSettingNum;
            this.settingNumText = settingNumText;
        }

        public int getCode() {
            return this.code;
        }
        public String getText() { return this.nameText; }
        public boolean usesSettingNum() { return this.usesSettingNum; }
        public String getSettingNumText() { return this.settingNumText; }
        public static PaymentRule fromCode(int code) {
            for (PaymentRule rule : PaymentRule.values()) {
                if(rule.code == code) {
                    return rule;
                }
            }
            return PaymentRule.SameDay;
        }
        @NonNull
        @Override
        public String toString() {
            return this.nameText;
        }
        public abstract PaymentStrategy getStrategy(Integer settingNum);
    }
    private final int id;
    private final String name;
    private final ClosingRule closingRule;
    private final Integer closingDay;
    private final PaymentRule paymentRule;
    private final Integer paymentDay;
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
    public PaymentMethod(int id, String name, int closingRuleCode, Integer closingDay, int paymentRuleCode, Integer paymentDay, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.closingRule = ClosingRule.fromCode(closingRuleCode);
        this.closingDay = closingDay;
        this.paymentRule = PaymentRule.fromCode(paymentRuleCode);
        this.paymentDay = paymentDay;
        // SQLiteではboolean型は0/1で保存される．1はTrue，0はTrue
        this.isDefault = isDefault;
    }

    /**
     * SQLiteに保存できるようContentValuesに変換する関数．静的．
     * 入力画面で入力されたデータをSQLiteに保存するときに使う．
     * たぶん入力画面での一か所でしか使わないと思うけど一応ここで関数化．
     * IDはデータテーブルに保存されるとき，自動的に付けられるものなのでContentValuesには含んでいない．
     * @param _name 支払方法名
     * @param _closingRuleCode  締め日ルールのコード番号
     * @param _closingSettingNum 締め日に設定される日数など(null許容)
     * @param _paymentRuleCode 支払日ルールのコード番号
     * @param _paymentSettingNum 支払日に設定される日数など(null許容)
     * @param _isDefault    デフォルトで用意されている支払方法かどうか
     * @return ContentValues
     */
    public static ContentValues getContentValues(String _name, int _closingRuleCode, Integer _closingSettingNum, int _paymentRuleCode, Integer _paymentSettingNum, boolean _isDefault) {
        ContentValues values = new ContentValues();
        // SQLiteはbool型に対応してないので0,1整数に変換
        int isDefaultInteger;
        if (_isDefault) {
            isDefaultInteger = 1;
        }
        else {
            isDefaultInteger = 0;
        }
        values.put(MyOpenHelper.COLUMN_NAME, _name);
        values.put(MyOpenHelper.COLUMN_CLOSING_RULE_CODE, _closingRuleCode);
        values.put(MyOpenHelper.COLUMN_CLOSING_DAY, _closingSettingNum);
        values.put(MyOpenHelper.COLUMN_PAYMENT_RULE_CODE, _paymentRuleCode);
        values.put(MyOpenHelper.COLUMN_PAYMENT_DAY, _paymentSettingNum);
        values.put(MyOpenHelper.COLUMN_IS_DEFAULT, isDefaultInteger);
        return values;
    }
    public Calendar getPaymentDate(Calendar purchaseDate) {
        ClosingStrategy cs = this.closingRule.getStrategy(this.closingDay);
        PaymentStrategy ps = this.paymentRule.getStrategy(this.paymentDay);
        Calendar closingDate = cs.apply(purchaseDate);
        return ps.apply(closingDate);
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public ClosingRule getClosingRule() {
        return this.closingRule;
    }
    public int getClosingDay() { return this.closingDay; }
    public PaymentRule getPaymentRule() {
        return this.paymentRule;
    }
    public int getPaymentDay() {
        return this.paymentDay;
    }
    public boolean isDefault() {
        return this.isDefault;
    }
}
