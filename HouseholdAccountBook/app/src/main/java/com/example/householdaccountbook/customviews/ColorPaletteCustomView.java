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
    private GridLayout gridLayout;
    private ImageButton selectedButton = null;
    private int columnCount = 4; // デフォルト列数
    private final String[] colorCodes = {
            "#FF6B6B", "#FF8E53", "#FFD166", "#06D6A0", "#1B9AAA", "#118AB2", "#3A86FF", "#5A4FCF",
            "#9D4EDD", "#C77DFF", "#FF5D8F", "#FF9F1C", "#F4A261", "#2EC4B6", "#8AC926", "#FFD60A",
            "#E36414", "#9A031E", "#E53838", "#6A4C93", "#4361EE", "#4CC9F0", "#80FFDB", "#72EFDD",
            "#FFCB77", "#FFC6FF", "#B5179E", "#7209B7", "#3A0CA3", "#4895EF"
    };

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
     * @return 選択色、未選択なら -1
     */
    public int getSelectedColor() {
        if (selectedButton != null) {
            // Tagに埋め込まれたカラーコードを取得
            return (int) selectedButton.getTag();
        }
        return -1;
    }

    /**
     * 列数を設定
     */
    public void setColumnCount(int count) {
        this.columnCount = count;
        gridLayout.setColumnCount(count);
    }
}
