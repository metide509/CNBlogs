package com.metide.cnblogs.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.metide.cnblogs.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFetures();
        setFlags();
        if(getContentView() > 0){
            setContentView(getContentView());
        }
        initData();
        initView(savedInstanceState);
    }

    protected void setFlags(){}
    protected void setFetures(){}

    protected abstract @LayoutRes int getContentView();

    protected abstract void initData();

    protected abstract void initView(Bundle saveInstanceState);
}
