package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

public class ColorItemView extends ConstraintLayout implements SelectableItem<Integer> {
    private boolean isSelected;
    private int colorCode;
    private GradientDrawable backgroundColor;
    public ColorItemView(Context context) {
        super(context);
        init(context);
    }

    public ColorItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_color, this, true);
        LayerDrawable layer = (LayerDrawable) findViewById(R.id.background).getBackground();
        this.backgroundColor = (GradientDrawable) layer.findDrawableByLayerId(R.id.background_layer);
    }
    public void setData(int colorCode) {
        this.colorCode = colorCode;
        this.backgroundColor.setColor(this.colorCode);
    }

    @Override
    public void setSelectedState(boolean selected) {
        this.isSelected = selected;
        this.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    public long getEigenvalue() {
        return this.colorCode;
    }

    @Override
    public Integer getData() {
        return this.colorCode;
    }
    public static String[] COLOR_CODES = {
            // 赤
            "#e60012", "#e83820", "#ea5532", "#ed6d46",
            "#ef845c", "#f29b76", "#f5b090", "#f8c5ac",
            //オレンジ
            "#f39800", "#f5a21b", "#f6ad3c", "#f8b856",
            "#f9c270", "#facd89", "#FCD7A1", "#FCE2BA",
            //黄色
            "#FFF100", "#FFF200", "#FFF33F", "#FFF462",
            "#FFF67F", "#FFF799", "#FFF9B1", "#FFFBC7",
            // 黄緑
            "#8FC31F", "#9DC93A", "#AACF52", "#B6D56A",
            "#C1DB81", "#CCE198", "#D7E7AF", "#E2EEC5",
            // 緑
            "#009944", "#00A051", "#00A95F", "#3EB370",
            "#69BD83", "#89C997", "#A5D4AD", "#BEDFC2",
            // 青緑
            "#009E96", "#00A59F", "#00ADA9", "#2BB7B3",
            "#61C1BE", "#84CCC9", "#A2D7D4", "#BCE1DF",
            // 水色
            "#00A0E9", "#00A7EA", "#00AFEC", "#00B9EF",
            "#54C3F1", "#7ECEF4", "#9FD9F6", "#BAE3F9",
            // 青
            "#0068B7", "#0073BD", "#187FC4", "#4C8DCB",
            "#6C9BD2", "#88ABDA", "#A3BCE2", "#BBCCE9",
            // 群青
            "#1D2088", "#36318F", "#4D4398", "#6356A3",
            "#796BAF", "#8F82BC", "#A59ACA", "#BBB3D8",
            // 紫
            "#920783", "#9C308D", "#A64A97", "#B062A3",
            "#BA79B1", "#C490BF", "#CFA7CD", "#DBBEDA",
            // ピンク
            "#E4007F", "#E62E8B", "#E85298", "#EB6EA5",
            "#EE87B4", "#F19EC2", "#F4B4D0", "#F7C9DD",
            // 赤紫
            "#E5004F", "#E7355C", "#E9546B", "#EC6D7B",
            "#EF858C", "#F29C9F", "#F5B2B2", "#F7C7C6",
            // 茶色
            "#a44a0a", "#af5617", "#bb6325", "#c67134",
            "#d18045", "#dd8f58", "#e8a06c", "#f3b181",
            // モノクロ
            "#E5E5e5", "#d1d1d1", "#bdbdbd", "#aaaaaa",
            "#969696", "#828282", "#6e6e6e", "#5a5a5a",
    };
}
