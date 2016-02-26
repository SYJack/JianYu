package com.jack.jianyu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.playutils.MusicPlayService;
import com.jack.jianyu.ui.fragment.AndroidFragment;
import com.jack.jianyu.ui.fragment.MusicFragment;
import com.jack.jianyu.ui.fragment.ZhiHuFragment;
import com.jack.jianyu.utils.ShareManager;
import com.jack.jianyu.widget.XCRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.FeedbackAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @InjectView(R.id.fl_fragmentContent)
    FrameLayout flFragmentContent;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.user_imageView)
    XCRoundImageView userImageView;

    private Fragment mContent;
    private ZhiHuFragment mZhiHuFragment;
    private MusicFragment mSongFragment;
    private AndroidFragment mAndroidFragment;

    private long firstTime = 0;

    private Bitmap logoBitmap;

    private Intent playerService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //启动 playerService
        playerService = new Intent(this, MusicPlayService.class);
        startService(playerService);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userImageView.setOnClickListener(this);
        //设置overflow永远在ToolBar右边显示
        setOverflowShowingAlways();
        initContent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (logoBitmap != null) {
            logoBitmap.recycle();
        }
    }

    public void setToolbarTitle(int res) {
        toolbar.setTitle(res);
    }

    /**
     * 初始化显示内容
     **/
    private void initContent() {

        mZhiHuFragment = new ZhiHuFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.fl_fragmentContent, mZhiHuFragment);
        mContent = mZhiHuFragment;
        transaction.commit();
        setToolbarTitle(R.string.str_menu_daily_paper);

        initUserImageView();

        ShareManager.getInstance().init(this);
        ShareManager.getInstance().addWechat(this);
        ShareManager.getInstance().addQQfriend(this);
        ShareManager.getInstance().addQQZone(this);
        ShareManager.getInstance().addSinaWeibo(this);
    }

    private void initUserImageView() {
        String photoName = Environment.getExternalStorageDirectory()
                + AppConstant.Paths.USER_IMAGE_PHOTO + "logo.jpg";
        File file = new File(photoName);
        if (file.exists()) {
            try {
                logoBitmap = BitmapFactory.decodeFile(photoName);
                userImageView.setImageBitmap(logoBitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fl_fragmentContent, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(flFragmentContent, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sb.show();
                firstTime = secondTime;
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                ToastHelper.showShort(MainActivity.this, "清理缓存成功");
//            } else {
//                ToastHelper.showShort(MainActivity.this, "清理缓存失败");
//            }
//        }
//    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "清理缓存成功", Toast.LENGTH_SHORT).show();
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
//            new Thread() {
//                @Override
//                public void run() {
//                    Message message = new Message();
//                    try {
//                        File file = new File(Environment.getExternalStorageDirectory()
//                                , AppConstant.Paths.IMAGE_LOADER_CACHE_PATH);
//                        DeleteFile(file);
//                        message.what = 1;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        message.what = -1;
//                    }
//                    handler.sendMessage(message);
//                }
//            }.start();
            return true;
        }
        if (id == R.id.action_playmusic) {
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

//    public void DeleteFile(File file) {
//        if (!file.exists()) {
//            handler.sendEmptyMessage(0);
//            return;
//        } else {
//            if (file.isFile()) {
//                file.delete();
//                return;
//            }
//            if (file.isDirectory()) {
//                File[] childFile = file.listFiles();
//                if (childFile == null || childFile.length == 0) {
//                    file.delete();
//                    return;
//                }
//                for (File f : childFile) {
//                    DeleteFile(f);
//                }
//                file.delete();
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_imageView:
                changeLogo();
                break;
        }
    }

    private void changeLogo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setIcon(R.mipmap.ic_launcher).setTitle("换个样子！");
        dialog.setMessage("选择一张图片改变头像");

        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("从相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                                + AppConstant.Paths.USER_IMAGE_PHOTO + "logo.jpg")));
                startActivityForResult(intent2, 2);
            }
        });
        dialog.setNegativeButton("从相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
            }
        }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                // after select from gallery
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());
                }
                break;
            case 2:
                // after taking a photo
                if (resultCode == RESULT_OK) {
                    File file = new File(Environment.getExternalStorageDirectory()
                            + AppConstant.Paths.USER_IMAGE_PHOTO, "logo.jpg");
                    cropPhoto(Uri.fromFile(file));
                }
                break;
            case 3:
                // after crop the picture
                if (data != null) {
                    Bundle extras = data.getExtras();
                    logoBitmap = extras.getParcelable("data");
                    if (logoBitmap != null) {
                        setPicToView(logoBitmap);
                        userImageView.setImageBitmap(logoBitmap);
                    }
                }
                break;
        }
        ShareManager.getInstance().setOnActivityResult(requestCode, resultCode, data);
    }

    // Crop a picture///////////////////////////////////////////////////////////////////////////////////
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX : aspectY
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY the height and width
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    // Storage a picture////////////////////////////////////////////////////////////////////////////////
    private void setPicToView(Bitmap mBitmap) {
        FileOutputStream b = null;
        File file = new File(Environment.getExternalStorageDirectory()
                + AppConstant.Paths.USER_IMAGE_PHOTO, "logo.jpg");
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        try {
            b = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);  // write the data to file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // close
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_zhihu) {
            if (mZhiHuFragment != null) {
                switchContent(mContent, mZhiHuFragment);
                setToolbarTitle(R.string.str_menu_daily_paper);
            }

        } else if (id == R.id.nav_music) {
            if (mSongFragment == null) {
                mSongFragment = new MusicFragment();
            }
            switchContent(mContent, mSongFragment);
            setToolbarTitle(R.string.str_menu_music);

        } else if (id == R.id.nav_communicate) {
            if (mAndroidFragment == null) {
                mAndroidFragment = new AndroidFragment();
            }
            switchContent(mContent, mAndroidFragment);
            setToolbarTitle(R.string.str_menuduihua);
        } else if (id == R.id.nav_share) {
            ShareManager.getInstance().popUmengShareDialog(this);
        } else if (id == R.id.nav_send) {
            FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
            agent.startFeedbackActivity();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //通过反射修改ViewConfiguration类中一个叫做sHasPermanentMenuKey的静态变量修改为false
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存MyTouchListener接口的列表
     */
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 回调接口
     */
    public interface MyTouchListener {
        public boolean onTouch(MotionEvent event);
    }

}

