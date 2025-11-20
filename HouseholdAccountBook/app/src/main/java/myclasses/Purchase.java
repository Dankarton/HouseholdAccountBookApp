package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class Purchase extends BOP {
    public enum PaymentTiming {
        SAME_DAY(0),
        THIS_MONTH(1),
        NEXT_MONTH(2);

        private final int code;

        PaymentTiming(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static PaymentTiming fromCode(int code) {
            for (PaymentTiming t : PaymentTiming.values()) {
                if (t.code == code) {
                    return t;
                }
            }
            return PaymentTiming.SAME_DAY;
        }

        public static PaymentTiming calcPaymentTiming(Calendar purchaseDate, Calendar expensesDate) {
            // 年が一致しない．(年を跨いでいるので支払いタイミングは翌月以降)
            if (purchaseDate.get(Calendar.YEAR) != expensesDate.get(Calendar.YEAR))
                return PaymentTiming.NEXT_MONTH;
            // 月が一致しない
            if (purchaseDate.get(Calendar.MONTH) != expensesDate.get(Calendar.MONTH))
                return PaymentTiming.NEXT_MONTH;
            // 日付が一致しない
            if (purchaseDate.get(Calendar.DATE) != expensesDate.get(Calendar.DATE))
                return PaymentTiming.THIS_MONTH;
            // それ以外(年月日が全て一致する)
            return PaymentTiming.SAME_DAY;

        }
    }

    private final long paymentMethodId;
    private final PaymentTiming paymentTiming;

    public Purchase(Long id, Calendar date, int amount, String memo, long categoryId, long paymentMethodId, PaymentTiming timing) {
        super(id, date, amount, memo, categoryId);
        this.paymentMethodId = paymentMethodId;
        this.paymentTiming = timing;
    }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public PaymentTiming getPaymentTiming() {
        return this.paymentTiming;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.PurchaseEntry.COLUMN_YEAR, this.getYear());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MONTH, this.getMonth());
        values.put(MyDbContract.PurchaseEntry.COLUMN_DAY, this.getDay());
        values.put(MyDbContract.PurchaseEntry.COLUMN_AMOUNT, this.getAmount());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MEMO, this.getMemo());
        values.put(MyDbContract.PurchaseEntry.COLUMN_CATEGORY_ID, this.getCategoryId());
        values.put(MyDbContract.PurchaseEntry.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        values.put(MyDbContract.PurchaseEntry.COLUMN_PAYMENT_TIMING_CODE, this.paymentTiming.getCode());
        return values;
    }
}
