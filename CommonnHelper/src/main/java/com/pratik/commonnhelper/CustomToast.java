package com.pratik.commonnhelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class CustomToast extends Toast {

    public static final int TYPE_INFO = 0;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_WARNING = 2;
    public static final int TYPE_ERROR = 3;

    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    Context mContext;
    private View mView;
    private int mType;

    public CustomToast(Context context) {
        super(context);
        mContext = context;
    }

    public static CustomToast makeText(Context context, String message) {
        return makeText(context, message, LENGTH_SHORT, TYPE_INFO);
    }

    public static CustomToast makeText(Context context, String message, int duration) {
        return makeText(context, message, duration, TYPE_INFO);
    }

    public static CustomToast makeText(Context context, String message, int duration, int type) {
        CustomToast customToast = new CustomToast(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_container, null);

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView text = (TextView) view.findViewById(R.id.text);

        switch (type) {
            case TYPE_SUCCESS:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_white_24dp));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_success_background));
                } else
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccess));
                break;
            case TYPE_WARNING:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_warning_white_24dp));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_warn_background));
                } else
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWarning));
                break;
            case TYPE_ERROR:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error_white_24dp));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_error_background));
                } else view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorError));
                break;
            default:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info_white_24dp));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_info_background));
                } else view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInfo));
                break;
        }

        text.setText(message);
        customToast.setDuration(duration);
        customToast.setView(view);

        customToast.mView = view;
        customToast.mType = type;
        return customToast;
    }

    @Override
    public void setText(@StringRes int resId) {
        setText(mContext.getString(resId));
    }

    @Override
    public void setText(CharSequence s) {
        if (mView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mView.findViewById(R.id.text);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }


    public void setIcon(@DrawableRes int iconId) {
        setIcon(ContextCompat.getDrawable(mContext, iconId));
    }

    public void setIcon(Drawable icon) {
        if (mView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        ImageView iv = (ImageView) mView.findViewById(R.id.icon);
        if (iv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        iv.setImageDrawable(icon);
    }


    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }
}