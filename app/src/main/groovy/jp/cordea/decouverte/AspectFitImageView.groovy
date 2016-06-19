package jp.cordea.decouverte

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class AspectFitImageView extends ImageView {

    AspectFitImageView(Context context, AttributeSet attrs) {
        super(context, attrs)
    }

    private float aspect

    def setAspect(float aspect) {
        this.aspect = aspect
        invalidate()
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (getMeasuredWidth() == 0 || aspect == 0f) {
            return
        }
        int width = getMeasuredWidth()
        setMeasuredDimension(width, Math.floor(width * aspect) as int)
    }

}