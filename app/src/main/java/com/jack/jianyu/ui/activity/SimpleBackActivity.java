package com.jack.jianyu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.SimpleBackPage;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * author:S.jack
 * data:2016-01-13 11:19
 */


public class SimpleBackActivity extends BaseActivity {

    public static final String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    protected int mPageValue = -1;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void setToobarTitle(int res) {
        if (res != 0) {
            toolbar.setTitle(res);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    private void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException("you must provide a page info to display");
        }
        SimpleBackPage page = SimpleBackPage.getPageValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);

        }
        try {
            Fragment fragment = (Fragment) page.getCls().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_PAGE);
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragmentContent, fragment);
            trans.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
