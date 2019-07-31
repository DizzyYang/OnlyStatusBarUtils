package dizzy.only.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Dizzy
 * 2019/6/6 16:16
 * 简介：OnlyStatusBarUtils
 */
public class OnlyStatusBarUtils {

    private static int ONLY_STATUSBAR_VIEW_ID = R.id.only_statusbar_view;

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, true);
    }

    public static void setColor(Activity activity, @ColorInt int color, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isDark) {
                color = getStatusBarColor(color);
            }
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addOnlyStatusBarView(activity, color, isDark);
            setRootView(activity);
        }
    }

    public static void setImageView(Activity activity, @ColorInt int color) {
        setImageView(activity, color, null);
    }

    public static void setImageView(Activity activity, @ColorInt int color, View view) {
        setImageView(activity, color, true, view);
    }

    public static void setImageView(Activity activity, @ColorInt int color, boolean isDark, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setStatusBarTransparent(activity);
        addOnlyStatusBarView(activity, color, isDark);
        if (view != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            view.setLayoutParams(layoutParams);
        }
    }

    private static void addOnlyStatusBarView(Activity activity, @ColorInt int color, boolean isDark) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View onlyStatusBarView = decorView.findViewById(ONLY_STATUSBAR_VIEW_ID);
        if (onlyStatusBarView != null) {
            if (onlyStatusBarView.getVisibility() == View.GONE) {
                onlyStatusBarView.setVisibility(View.VISIBLE);
            }
            if (isDark) {
                color = getStatusBarColor(color);
            }
            onlyStatusBarView.setBackgroundColor(color);
        } else {
            decorView.addView(getOnlyStatusBarView(activity, color, isDark));
        }
    }

    private static View getOnlyStatusBarView(Activity activity, @ColorInt int color, boolean isDark) {
        View onlyStatusBarView = new View(activity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        onlyStatusBarView.setLayoutParams(layoutParams);
        if (isDark) {
            color = getStatusBarColor(color);
        }
        onlyStatusBarView.setBackgroundColor(color);
        onlyStatusBarView.setId(ONLY_STATUSBAR_VIEW_ID);
        return onlyStatusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup rootView = activity.findViewById(android.R.id.content);
        int childCount = rootView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = rootView.getChildAt(i);
            if (view instanceof ViewGroup) {
                view.setFitsSystemWindows(true);
                ((ViewGroup) view).setClipToPadding(true);
            }
        }
    }

    public static void setOnlyStatusBarIcon(Activity activity, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                int visibility = decorView.getSystemUiVisibility();
                if (isDark) {
                    visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    visibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(visibility);
            }
        }
    }

    public static boolean setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return true;
        }
        return false;
    }

    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getStatusBarColor(@ColorInt int color) {
        int alpha = color >> 24 & 0xff;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) Math.floor(red * 0.8);
        green = (int) Math.floor(green * 0.8);
        blue = (int) Math.floor(blue * 0.8);
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

}
