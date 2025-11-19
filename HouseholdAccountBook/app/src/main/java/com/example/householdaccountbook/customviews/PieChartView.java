package com.example.householdaccountbook.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.R;

import java.util.Locale;

public class PieChartView extends View {
    private final float MIN_ANGLE = 12f; // 最小角度．この角度より小さい扇のラベルは表示しない．
    private final Paint paint = new Paint();
    private final Paint centerTextPaint = new Paint();
    private final Paint holePaint = new Paint();
    private final Paint borderLinePaint = new Paint();
    private final Paint labelTextPaint = new Paint();
    private final Paint labelTextStrokePaint = new Paint();
    private float[] data = { 40f, 30f, 20f, 10 };  // 表示するデータ
    private int[] colors = { Color.RED, Color.BLUE, Color.GREEN };
    private String[] labels = {"A", "B", "C", "D"};
    private String centerText = "チャート図";
    private float holeRatio = 0.6f;

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.paint.setAntiAlias(true); // アンチエイリアスオン
        // 中央テキスト
        this.centerTextPaint.setAntiAlias(true);
        this.centerTextPaint.setColor(context.getColor(R.color.normal_text_color));
        this.centerTextPaint.setTextAlign(Paint.Align.CENTER);
        this.centerTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                18,
                getResources().getDisplayMetrics()
        ));
        // 真ん中を塗りつぶしてドーナッツ型にするためのペイント
        this.holePaint.setAntiAlias(true);
        this.holePaint.setColor(context.getColor(R.color.base_background));
        this.holePaint.setStyle(Paint.Style.FILL);
        // 扇の境界線
        this.borderLinePaint.setAntiAlias(true);
        this.borderLinePaint.setStrokeWidth(8f);
        this.borderLinePaint.setColor(context.getColor(R.color.base_background));
        this.borderLinePaint.setStyle(Paint.Style.STROKE);
        // ラベル名
        this.labelTextPaint.setAntiAlias(true);
        this.labelTextPaint.setColor(context.getColor(R.color.normal_text_color));
        this.labelTextPaint.setTextAlign(Paint.Align.CENTER);
        this.labelTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14,
                getResources().getDisplayMetrics()
        ));
        // ラベルの枠線用
        this.labelTextStrokePaint.setAntiAlias(true);
        this.labelTextStrokePaint.setColor(context.getColor(R.color.base_background));
        this.labelTextStrokePaint.setStyle(Paint.Style.STROKE);
        this.labelTextStrokePaint.setStrokeWidth(4f);
        this.labelTextStrokePaint.setTextAlign(Paint.Align.CENTER);
        this.labelTextStrokePaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14,
                getResources().getDisplayMetrics()
        ));

    }


    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //
        // Paintは初めに描画したものの上に重ねて図形を表示させるため，生成する順番が結構大事．
        //
        // 中心と半径を計算
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        int cx = width / 2;
        int cy = height / 2;
        // データ合計
        float total = 0f;
        for (float val : this.data) total += val;

        // 扇の描画
        float currentAngle = -90f; // 上から開始（12時方向）
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        for (int i = 0; i < this.data.length; i++) {
            float sweepAngle = 360f * (data[i] / total);
            if (i < this.colors.length) {
                this.paint.setColor(this.colors[i]);
            } else {
                this.paint.setColor(Color.GRAY);
            }
            canvas.drawArc(oval, currentAngle, sweepAngle, true, this.paint);

            double rad = Math.toRadians(currentAngle + sweepAngle);

            currentAngle += sweepAngle;
        }
        currentAngle = -90f;

        for (int i = 0; i < this.data.length; i++) {
            // 扇を作るループと内容同じだけど，先に描いたものの上に後から描いた物を描画する関係で，扇と境界線は
            // 別々で生成しないと見た目が崩れるので，ループを統合してない．
            float sweepAngle = 360f * (data[i] / total);

            double rad = Math.toRadians(currentAngle + sweepAngle);
            float x = cx + (float)(radius * Math.cos(rad));
            float y = cy + (float)(radius * Math.sin(rad));

            canvas.drawLine(cx, cy, x, y, this.borderLinePaint);

            currentAngle += sweepAngle;
        }
        // ドーナツ型にするための穴を描画
        float holeRadius = radius * this.holeRatio;
        canvas.drawCircle(cx, cy, holeRadius, this.holePaint);
        // カテゴリ名
        currentAngle = -90f;
        float textRadius = radius * 0.80f;
        for (int i = 0; i < data.length; i++) {
            float sweepAngle = 360f * (data[i] / total);
            if (sweepAngle < MIN_ANGLE) {
                currentAngle += sweepAngle;
                continue;
            }
            float midAngle = currentAngle + sweepAngle / 2f;
            double rad = Math.toRadians(midAngle);
            float x = cx + (float) (textRadius * Math.cos(rad));
            float y = cy + (float) (textRadius * Math.sin(rad));
            String label = this.labels[i] +" " + String.format(Locale.JAPANESE, "%3.1f%%", (data[i] / total) * 100f);
            canvas.drawText(label, x, y, this.labelTextStrokePaint);
            canvas.drawText(label, x, y, this.labelTextPaint);
            currentAngle += sweepAngle;
        }
        // 中央の文字
        if (!this.centerText.isEmpty()) {
            Paint.FontMetrics fm = this.centerTextPaint.getFontMetrics();
            float textHeight = (fm.bottom - fm.top) / 2 - fm.bottom;
            canvas.drawText(this.centerText, cx, cy + textHeight, this.centerTextPaint);
        }
    }

    public void setData(float[] newData) {
        this.data = newData;
        invalidate(); // 再描画
    }
    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }
    public void setLabelData(String[] labels) {
        this.labels = labels;
        invalidate();
    }
    public void setLabelTextColor(int color) {
        this.labelTextPaint.setColor(color);
        invalidate();
    }
    public void setLabelTextSize(int sp) {
        float textSizeSp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
        );
        this.labelTextPaint.setTextSize(textSizeSp);
        this.labelTextStrokePaint.setTextSize(textSizeSp);
        invalidate();
    }
    public void setCenterText(String text) {
        this.centerText = text;
        invalidate();
    }
    public void setCenterTextColor(int color) {
        this.centerTextPaint.setColor(color);
        invalidate();
    }
    public void setCenterTextSize(int sp) {
        this.centerTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
        ));
        invalidate();
    }
    public void setBackgroundColor(int color) {
        this.holePaint.setColor(color);
        this.borderLinePaint.setColor(color);
        this.labelTextStrokePaint.setColor(color);
        invalidate();
    }
}
