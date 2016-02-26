package com.jack.jianyu.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * author:S.jack
 * data:2016-01-13 10:46
 */


public class BaseActivity extends AppCompatActivity {
    protected LayoutInflater mInflater;
    private TextView mTvActionTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

    }



    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        mInflater = getLayoutInflater();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ActivityDestroy", "Activity Destroy");
    }
}
