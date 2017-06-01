package com.smartu.utilidades;

/**
 * Created by Emilio Chica Jiménez on 06/10/2015.
 */
import android.content.Context;
import android.util.AttributeSet;

public class ImageViewCuadradaMenu extends android.support.v7.widget.AppCompatImageView {
    public ImageViewCuadradaMenu(Context context) {
        super(context);
    }

    public ImageViewCuadradaMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewCuadradaMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Snap to width
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
