package com.tangzy.mvpframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.LoginActivity;
import com.tangzy.mvpframe.fragment.base.BaseFragment;
import com.tangzy.mvpframe.util.PrefUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BannerItemFragment extends BaseFragment{
    @BindView(R.id.image)
    ImageView image;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView(R.layout.fragment_baner);
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            int index = bundle.getInt("index");
            if(index==0){
                image.setImageResource(R.mipmap.guidepage_1);
            }else{
                image.setImageResource(R.mipmap.guidepage_2);
                image.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if(event.getAction()==MotionEvent.ACTION_DOWN){
                            PrefUtils.setPrefBoolean(getActivity(), "isFirst", false);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        return true;
                    }
                });
            }
        }
    }
}
