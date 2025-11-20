package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;

public class ColorPaletteCustomView extends ConstraintLayout {
    public interface OnColorSelectedListener {
        void onColorSelected(int colorInt);
    }

    private OnColorSelectedListener listener;
    private GridLayout gridLayout;
    private ImageButton selectedButton = null;
    private int columnCount = 4; // デフォルト列数
    String[] colorCodes = {
            "#e60012", "#e83820", "#ea5532", "#ed6d46", "#ef845c", "#f29b76", "#f5b090", "#f8c5ac",
            "#f39800", "#f5a21b", "#f6ad3c", "#f8b856", "#f9c270", "#facd89", "#FCD7A1", "#FCE2BA",
            "#FFF100", "#FFF200", "#FFF33F", "#FFF462", "#FFF67F", "#FFF799", "#FFF9B1", "#FFFBC7",
            "#8FC31F", "#9DC93A", "#AACF52", "#B6D56A", "#C1DB81", "#CCE198", "#D7E7AF", "#E2EEC5",
            "#009944", "#00A051", "#00A95F", "#3EB370", "#69BD83", "#89C997", "#A5D4AD", "#BEDFC2",
            "#009E96", "#00A59F", "#00ADA9", "#2BB7B3", "#61C1BE", "#84CCC9", "#A2D7D4", "#BCE1DF",

            "#00A0E9", "#00A7EA", "#00AFEC", "#00B9EF", "#54C3F1", "#7ECEF4", "#9FD9F6", "#BAE3F9",
            "#0068B7", "#0073BD", "#187FC4", "#4C8DCB", "#6C9BD2", "#88ABDA", "#A3BCE2", "#BBCCE9",
            "#1D2088", "#36318F", "#4D4398", "#6356A3", "#796BAF", "#8F82BC", "#A59ACA", "#BBB3D8",
            "#920783", "#9C308D", "#A64A97", "#B062A3", "#BA79B1", "#C490BF", "#CFA7CD", "#DBBEDA",
            "#E4007F", "#E62E8B", "#E85298", "#EB6EA5", "#EE87B4", "#F19EC2", "#F4B4D0", "#F7C9DD",
            "#E5004F", "#E7355C", "#E9546B", "#EC6D7B", "#EF858C", "#F29C9F", "#F5B2B2", "#F7C7C6",

            "#a44a0a", "#af5617", "#bb6325", "#c67134", "#d18045", "#dd8f58", "#e8a06c", "#f3b181",
            "#E5E5e5", "#d1d1d1", "#bdbdbd", "#aaaaaa", "#969696", "#828282", "#6e6e6e", "#5a5a5a",
    };
//    String[] colorCodes = {
//            // 赤系
//            "#FF0000", "#FF1A1A", "#FF3333", "#FF4D4D", "#FF6666", "#FF8080", "#FF9999", "#FFB3B3",
//            // ピンク系
//            "#FF1493", "#FF3399", "#FF66B2", "#FF85C1", "#FF99CC", "#FFADD6", "#FFC1E0", "#FFD6EB",
//            // 紫系
//            "#800080", "#9932CC", "#A64DFF", "#B266FF", "#BF80FF", "#CC99FF", "#D9B3FF", "#E6CCFF",
//            // 青系
//            "#0000FF", "#1A1AFF", "#3333FF", "#4D4DFF", "#6666FF", "#8080FF", "#9999FF", "#B3B3FF",
//            // 水色系
//            "#00CED1", "#20B2AA", "#40E0D0", "#48D1CC", "#5FDDE0", "#70E1F5", "#87F0FF", "#A0F8FF",
//            // 緑系
//            "#008000", "#009933", "#00B34D", "#00CC66", "#00E680", "#1AFF99", "#4DFFB3", "#80FFCC",
//            // 黄緑系
//            "#66FF00", "#76FF1A", "#85FF33", "#94FF4D", "#A3FF66", "#B2FF80", "#C1FF99", "#D0FFB3",
//            // 黄色系
//            "#FFD700", "#FFDF33", "#FFE733", "#FFEF66", "#FFF799", "#FFFAC1", "#FFFDD6", "#FFFFE0",
//            // オレンジ系
//            "#FF8C00", "#FFA500", "#FFB733", "#FFC966", "#FFDB99", "#FFE0B3", "#FFE6CC", "#FFF0E0",
//            // 茶系
//            "#8B4513", "#A0522D", "#B65C3A", "#C6714D", "#D98C66", "#E6A87F", "#F2C2A0", "#FFE0CC",
//            // コーラル系
//            "#FF6347", "#FF7256", "#FF8266", "#FF9180", "#FFA199", "#FFB1B3", "#FFC1CC", "#FFD1E6",
//            // 青緑系
//            "#008B8B", "#009999", "#00A3A3", "#00B2B2", "#00C0C0", "#00D1D1", "#00E3E3", "#00F5F5",
//            // インディゴ系
//            "#4B0082", "#5A00A3", "#6A00CC", "#7A1AFF", "#8C33FF", "#9F66FF", "#B399FF", "#C7CCFF",
//            // ティール系
//            "#008080", "#009999", "#00B3B3", "#00CCCC", "#00E6E6", "#1AFFFF", "#4DFFFF", "#80FFFF",
//            // マゼンタ系
//            "#FF00FF", "#FF1AFF", "#FF33FF", "#FF4DFF", "#FF66FF", "#FF80FF", "#FF99FF", "#FFB3FF",
//            // ライム系
//            "#BFFF00", "#CCFF1A", "#D9FF33", "#E6FF4D", "#F2FF66", "#F5FF99", "#F8FFC1", "#FBFFE0"
//    };

    public ColorPaletteCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ColorPaletteCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorPaletteCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_color_palette, this, true);
        gridLayout = findViewById(R.id.color_grid_layout);
        gridLayout.setColumnCount(columnCount);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        gridLayout.setUseDefaultMargins(true);
//        this.addView(gridLayout);
        int[] colorsInt = new int[colorCodes.length];
        for (int i = 0; i < colorsInt.length; i++) {
            colorsInt[i] = Color.parseColor(colorCodes[i]);
        }
        setColors(colorsInt);
    }

    /**
     * カラーパレットを作成
     *
     * @param colors 背景色の配列
     */
    public void setColors(int[] colors) {
        gridLayout.removeAllViews();

        for (int color : colors) {
            ImageButton btn = new ImageButton(getContext());
            btn.setBackgroundResource(R.drawable.color_palette_button); // 枠用Drawable
            btn.setScaleType(ImageButton.ScaleType.CENTER_CROP);

            LayerDrawable ld = (LayerDrawable) btn.getBackground();
            Drawable background = ld.findDrawableByLayerId(R.id.background);
            ((GradientDrawable) background).setColor(color);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = MyStdlib.dpToPx(getContext(), 48);
            params.height = MyStdlib.dpToPx(getContext(), 48);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            btn.setLayoutParams(params);
            // カラーコードをタグに埋め込む
            btn.setTag(color);

            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectButton((ImageButton) view);
                    if (listener != null) {
                        listener.onColorSelected(getSelectedColor());
                    }
                }
            });

            gridLayout.addView(btn);
        }
    }

    private void selectButton(ImageButton button) {
        if (selectedButton != null) {
            selectedButton.setSelected(false);
        }
        button.setSelected(true);
        selectedButton = button;
    }

    /**
     * 現在選択されているボタンの背景色を取得
     *
     * @return 選択色、未選択ならnull
     */
    public Integer getSelectedColor() {
        if (selectedButton != null) {
            // Tagに埋め込まれたカラーコードを取得
            return (int) selectedButton.getTag();
        }
        return null;
    }

    /**
     * 選択されているかどうか
     *
     * @return 選択されているtrue，されていないfalse
     */
    public boolean isSelected() {
        if (selectedButton != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 列数を設定
     */
    public void setColumnCount(int count) {
        this.columnCount = count;
        gridLayout.setColumnCount(count);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }
}
