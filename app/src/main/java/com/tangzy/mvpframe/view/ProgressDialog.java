package com.tangzy.mvpframe.view;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.manager.Constant;

/**
 * Created by Administrator on 2018/5/31.
 */

public class ProgressDialog extends Dialog {

    Context context;
    private static ProgressDialog dialog = null;
    private static TextView tv_message;
    private static ImageView imageView;

    public ProgressDialog(Context context) {
        this(context, R.style.MyDialogStyle);
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public static ProgressDialog getInstance(Context context){
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            init(context);
        }
        return dialog;
    }

    @Override
    public void show() {
        super.show();
        Glide.with(imageView.getContext()).load(R.mipmap.ico_progress_gif).into(imageView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        imageView = null;
        tv_message = null;
        dialog = null;
    }

    private static void init(Context context) {
        dialog.setContentView(R.layout.progress_custom);
        tv_message = dialog.findViewById(R.id.message);
        imageView = dialog.findViewById(R.id.imageView);
        int width = Constant.widthScreen/10;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
    }
    public ProgressDialog setMessage(String message){
        if (tv_message != null && message != null){
            tv_message.setText(message);
        }
        return dialog;
    }
}
