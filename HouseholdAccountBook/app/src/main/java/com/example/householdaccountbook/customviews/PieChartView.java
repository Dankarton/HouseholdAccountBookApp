package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    private Paint paint = new Paint();
    private float[] data = {40f, 30f, 20f, 10f};  // 表示するデータ
    private int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    public PieChartView(Context context) {
        super(context);
    }
    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        paint.setAntiAlias(true); // ギザギザ防止
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 中心と半径を計算
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20; // 少し余白を持たせる
        int cx = width / 2;
        int cy = height / 2;

        // データ合計
        float total = 0f;
        for (float val : data) total += val;

        // 円弧を描画
        float startAngle = -90f; // 上から開始（12時方向）
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        for (int i = 0; i < data.length; i++) {
            float sweepAngle = 360f * (data[i] / total);
            paint.setColor(colors[i % colors.length]);
            canvas.drawArc(oval, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }

    // データ更新用メソッド
    public void setData(float[] newData) {
        this.data = newData;
        invalidate(); // 再描画
    }
}
