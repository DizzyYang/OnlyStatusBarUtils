package dizzy.only.statusbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dizzy
 * 2019/6/6 16:16
 * 简介：OnlyStatusBarView
 */
public class OnlyStatusBarView extends View {

    public OnlyStatusBarView(Context context) {
        this(context, null);
    }

    public OnlyStatusBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlyStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setVisibility(View.GONE);
    }

    public void openStatusBarTransparent(Activity activity) {
        boolean isTransparent = OnlyStatusBarUtils.setStatusBarTransparent(activity);
        if (isTransparent) {
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        OnlyStatusBarUtils.getStatusBarHeight(activity)
                );
            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = OnlyStatusBarUtils.getStatusBarHeight(activity);
            }
            this.setLayoutParams(layoutParams);
            this.setVisibility(View.VISIBLE);
        }
    }

    public void setBackgroundColor(@ColorInt int color, boolean isDark) {
        if (isDark) {
            color = OnlyStatusBarUtils.getStatusBarColor(color);
        }
        super.setBackgroundColor(color);
    }

    public void setBackgroundResource(Context context, @ColorRes int colorId, boolean isDark) {
        this.setBackgroundColor(ContextCompat.getColor(context, colorId), isDark);
    }

}
