package megvii.testfacepass.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
import java.util.List;

import megvii.testfacepass.MyApplication;

import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BaoCunBeanDao;
import megvii.testfacepass.dialog.MoBanDialog;
import megvii.testfacepass.dialog.XiuGaiWenZiDialog;
import megvii.testfacepass.dialog.XiuGaiXinXiDialog;
import megvii.testfacepass.dialog.YuLanDialog;
import megvii.testfacepass.dialog.YuYingDialog;
import megvii.testfacepass.utils.FileUtil;


public class SheZhiActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11;
    private List<Button> sheZhiBeanList;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private int dw,dh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dw = FileUtil.getDisplaySize(SheZhiActivity.this).x;
        dh = FileUtil.getDisplaySize(SheZhiActivity.this).y;

        baoCunBeanDao= MyApplication.myApplication.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        if (baoCunBean.getWenzi()==null){
            baoCunBean.setWenzi("中国电信");
        }
        if (baoCunBean.getTouxiangzhuji()==null){
            baoCunBean.setTouxiangzhuji("http://121.46.3.20");
        }
        baoCunBeanDao.update(baoCunBean);
        baoCunBean=baoCunBeanDao.load(123456L);

        setContentView(R.layout.activity_she_zhi);

        if (dw>dh){
            /**
             * 设置为横屏
             */
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

        }else {
            /**
             * 设置为竖屏
             */
            if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

        }


        bt1= (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        bt1.setOnFocusChangeListener(this);
        bt2= (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        bt2.setOnFocusChangeListener(this);
        bt3= (Button) findViewById(R.id.bt3);
        bt3.setOnClickListener(this);
        bt3.setOnFocusChangeListener(this);
        bt4= (Button) findViewById(R.id.bt4);
        bt4.setOnClickListener(this);
        bt4.setOnFocusChangeListener(this);
        bt5= (Button) findViewById(R.id.bt5);
        bt5.setOnClickListener(this);
        bt5.setOnFocusChangeListener(this);
        bt6= (Button) findViewById(R.id.bt6);
        bt6.setOnClickListener(this);
        bt6.setOnFocusChangeListener(this);
        bt7= (Button) findViewById(R.id.bt7);
        bt7.setOnClickListener(this);
        bt7.setOnFocusChangeListener(this);
        bt8= (Button) findViewById(R.id.bt8);
        bt8.setOnClickListener(this);
        bt8.setOnFocusChangeListener(this);
        bt9= (Button) findViewById(R.id.bt9);
        bt9.setOnClickListener(this);
        bt9.setOnFocusChangeListener(this);
        bt10= (Button) findViewById(R.id.bt10);
        bt10.setOnClickListener(this);
        bt10.setOnFocusChangeListener(this);
        bt11= (Button) findViewById(R.id.bt11);
        bt11.setOnClickListener(this);
        bt11.setOnFocusChangeListener(this);
        bt1.requestFocus();

        sheZhiBeanList = new ArrayList<>();
        sheZhiBeanList.add(bt1);
        sheZhiBeanList.add(bt2);
        sheZhiBeanList.add(bt3);
        sheZhiBeanList.add(bt4);
        sheZhiBeanList.add(bt5);
        sheZhiBeanList.add(bt6);
        sheZhiBeanList.add(bt7);
        sheZhiBeanList.add(bt8);
        sheZhiBeanList.add(bt9);
        sheZhiBeanList.add(bt10);
        sheZhiBeanList.add(bt11);


}

    @Override
    protected void onPause() {
        //开启Activity
        //  Log.d("SheZhiActivity", "baoCunBean.getMoban():" + baoCunBean.getMoban());
        switch (baoCunBean.getMoban()){
            case 1:
                startActivity(new Intent(SheZhiActivity.this,YiDongNianHuiActivity.class));
                SystemClock.sleep(1600);
                Log.d("SheZhiActivity", "ddddddddd");

                break;

            case 2:

                break;
            case 3:

                break;
            case 4:


                break;

        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("SheZhiActivity", "停止");

        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                ChongsZHI();
                bt1.requestFocus();
                bt1.setTextColor(Color.WHITE);
                bt1.setBackgroundResource(R.drawable.zidonghuoqu1);
                  AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt1,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt1,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet.setDuration(300);
                    animatorSet.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {
                            final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                            if (baoCunBean.getShipingIP()==null){
                                dialog.setContents("设置网络摄像头IP","192.166.2.101");
                            }else {
                                dialog.setContents("设置网络摄像头IP",baoCunBean.getShipingIP());
                            }

                            dialog.setOnQueRenListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    baoCunBean.setShipingIP(dialog.getContents());
                                    baoCunBeanDao.update(baoCunBean);
                                    baoCunBean=baoCunBeanDao.load(123456L);
                                    dialog.dismiss();
                                }
                            });
                            dialog.setQuXiaoListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                    animatorSet.start();
                break;
            case R.id.bt2:
                ChongsZHI();
                bt2.requestFocus();
                bt2.setTextColor(Color.WHITE);
                bt2.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt2,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt2,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet2.setDuration(300);
                animatorSet2.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                        if (baoCunBean.getZhujiDiZhi()==null){
                            dialog.setContents("设置主机地址","ws://192.166.2.240:9000/video");
                        }else {
                            dialog.setContents("设置主机地址",baoCunBean.getZhujiDiZhi());
                        }
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setZhujiDiZhi(dialog.getContents());
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();
                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });
                animatorSet2.start();
                break;
            case R.id.bt3:
                ChongsZHI();
                bt3.requestFocus();
                bt3.setTextColor(Color.WHITE);
                bt3.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet3 = new AnimatorSet();
                animatorSet3.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt3,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt3,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet3.setDuration(300);
                animatorSet3.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
//                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
//                        if (baoCunBean.getTuisongDiZhi()==null){
//                            dialog.setContents("设置推送地址","http://192.168.2.50");
//                        }else {
//                            dialog.setContents("设置推送地址",baoCunBean.getTuisongDiZhi());
//                        }
//                        dialog.setOnQueRenListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                baoCunBean.setTuisongDiZhi(dialog.getContents());
//                                baoCunBeanDao.update(baoCunBean);
//                                baoCunBean=baoCunBeanDao.load(123456L);
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.setQuXiaoListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show();
                        YuLanDialog yuLanDialog=new YuLanDialog(SheZhiActivity.this);
                            yuLanDialog.show();
                    }
                });
                animatorSet3.start();

                break;
            case R.id.bt4:
                ChongsZHI();
                bt4.requestFocus();
                bt4.setTextColor(Color.WHITE);
                bt4.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet4 = new AnimatorSet();
                animatorSet4.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt4,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt4,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet4.setDuration(300);
                animatorSet4.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {

                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                        if (baoCunBean.getTouxiangzhuji()==null){
                            dialog.setContents("设置头像主机地址","http://121.46.3.20");
                        }else {
                            dialog.setContents("设置头像主机地址",baoCunBean.getTouxiangzhuji());
                        }
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setTouxiangzhuji(dialog.getContents());
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();
                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });
                animatorSet4.start();
                break;
            case R.id.bt5:
                ChongsZHI();
                bt5.requestFocus();
                bt5.setTextColor(Color.WHITE);
                bt5.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet5 = new AnimatorSet();
                animatorSet5.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt5,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt5,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet5.setDuration(300);
                animatorSet5.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        if (baoCunBean.getIsShowMoshengren()){ //false为 竖屏
                            baoCunBean.setIsShowMoshengren(false);
                            baoCunBeanDao.update(baoCunBean);
                            baoCunBean=baoCunBeanDao.load(123456L);
                            bt5.setText("已设置为不弹");
                            TastyToast.makeText(SheZhiActivity.this,"已设置为不弹",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();

                        }else {
                            baoCunBean.setIsShowMoshengren(true);
                            baoCunBeanDao.update(baoCunBean);
                            baoCunBean=baoCunBeanDao.load(123456L);
                            bt5.setText("已设置为弹出");
                            TastyToast.makeText(SheZhiActivity.this,"已设置为弹出",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                        }


                    }
                });
                animatorSet5.start();
                break;
            case R.id.bt6:
                ChongsZHI();
                bt6.requestFocus();
                bt6.setTextColor(Color.WHITE);
                bt6.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet6 = new AnimatorSet();
                animatorSet6.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt6,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt6,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet6.setDuration(300);
                animatorSet6.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                       //语音设置
                        YuYingDialog yuYingDialog=new YuYingDialog(SheZhiActivity.this);
                        yuYingDialog.show();

                    }
                });
                animatorSet6.start();

                break;

            case R.id.bt7:
                ChongsZHI();
                bt7.requestFocus();
                bt7.setTextColor(Color.WHITE);
                bt7.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet7 = new AnimatorSet();
                animatorSet7.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt7,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt7,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet7.setDuration(300);
                animatorSet7.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        //弹窗
                        MoBanDialog dialog=new MoBanDialog(SheZhiActivity.this,baoCunBeanDao);
                        dialog.show();
                        bt7.setEnabled(true);
                    }
                });
                animatorSet7.start();
                bt7.setEnabled(false);

                break;

            case R.id.bt8:
                ChongsZHI();
                bt8.requestFocus();
                bt8.setTextColor(Color.WHITE);
                bt8.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet8 = new AnimatorSet();
                animatorSet8.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt8,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt8,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet8.setDuration(300);
                animatorSet8.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        //弹窗
                        final XiuGaiWenZiDialog dialog=new XiuGaiWenZiDialog(SheZhiActivity.this);
                        dialog.setContents(baoCunBean.getWenzi()+"",baoCunBean.getSize()==0? "30":String.valueOf(baoCunBean.getSize()),
                                baoCunBean.getWenzi1()+"",baoCunBean.getSize1()==0? "60":String.valueOf(baoCunBean.getSize1()));
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setWenzi(dialog.getContents());
                                baoCunBean.setSize(Integer.valueOf(dialog.getSize()));
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();

                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        bt8.setEnabled(true);
                    }
                });
                animatorSet8.start();
                bt8.setEnabled(false);

                break;
            case R.id.bt9:

                ChongsZHI();
                bt9.requestFocus();
                bt9.setTextColor(Color.WHITE);
                bt9.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet9 = new AnimatorSet();
                animatorSet9.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt9,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt9,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet9.setDuration(300);
                animatorSet9.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        //弹窗
                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                        if (baoCunBean.getHoutaiDiZhi()==null){
                            dialog.setContents("设置后台地址","http://192.168.1.1");
                        }else {
                            dialog.setContents("设置后台地址",baoCunBean.getHoutaiDiZhi());
                        }
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setHoutaiDiZhi(dialog.getContents());
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();
                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        bt9.setEnabled(true);
                    }
                });
                animatorSet9.start();
                bt9.setEnabled(false);

                break;
            case R.id.bt10:

                ChongsZHI();
                bt10.requestFocus();
                bt10.setTextColor(Color.WHITE);
                bt10.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet10 = new AnimatorSet();
                animatorSet10.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt10,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt10,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet10.setDuration(300);
                animatorSet10.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        //弹窗
                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                        if (baoCunBean.getZhanghuId()==null){
                            dialog.setContents("设置账户id","");
                        }else {
                            dialog.setContents("设置账户id",baoCunBean.getZhanghuId());
                        }
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setZhanghuId(dialog.getContents());
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();
                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        bt10.setEnabled(true);
                    }
                });
                animatorSet10.start();
                bt10.setEnabled(false);
                break;
            case R.id.bt11:
                ChongsZHI();
                bt11.requestFocus();
                bt11.setTextColor(Color.WHITE);
                bt11.setBackgroundResource(R.drawable.zidonghuoqu1);
                AnimatorSet animatorSet11 = new AnimatorSet();
                animatorSet11.playTogether(
                        //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                        ObjectAnimator.ofFloat(bt11,"scaleX",1.0f,1.2f,1.0f),
                        ObjectAnimator.ofFloat(bt11,"scaleY",1.0f,1.2f,1.0f)
                );
                //animatorSet.setInterpolator(new DescelerateInterpolator());
                animatorSet11.setDuration(300);
                animatorSet11.addListener(new AnimatorListenerAdapter(){
                    @Override public void onAnimationEnd(Animator animation) {
                        //弹窗
                        final XiuGaiXinXiDialog dialog=new XiuGaiXinXiDialog(SheZhiActivity.this);
                        if (baoCunBean.getHuiyiId()==null){
                            dialog.setContents("设置会议id","");
                        }else {
                            dialog.setContents("设置会议id",baoCunBean.getHuiyiId());
                        }
                        dialog.setOnQueRenListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                baoCunBean.setHuiyiId(dialog.getContents());
                                baoCunBeanDao.update(baoCunBean);
                                baoCunBean=baoCunBeanDao.load(123456L);
                                dialog.dismiss();
                            }
                        });
                        dialog.setQuXiaoListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        bt11.setEnabled(true);
                    }
                });
                animatorSet11.start();
                bt11.setEnabled(false);

                break;

        }

    }

    private void  ChongsZHI(){
        if (sheZhiBeanList!=null){
        for (int i=0;i<sheZhiBeanList.size();i++){
            sheZhiBeanList.get(i).setBackgroundResource(R.drawable.zidonghuoqu2);
            sheZhiBeanList.get(i).setTextColor(Color.parseColor("#1b37d6"));
          }
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.bt1:
              //  Log.d("SheZhiActivity", "hasFocus:1" + hasFocus);
                if (hasFocus){
                    ChongsZHI();
                    bt1.setTextColor(Color.WHITE);
                    bt1.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt1,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt1,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
                break;
            case R.id.bt2:
             //   Log.d("SheZhiActivity", "hasFocus:2" + hasFocus);
                if (hasFocus){
                    ChongsZHI();
                    bt2.setTextColor(Color.WHITE);
                    bt2.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt2,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt2,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
                break;
            case R.id.bt3:
                if (hasFocus){
                    ChongsZHI();
                    bt3.setTextColor(Color.WHITE);
                    bt3.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt3,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt3,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
               // Log.d("SheZhiActivity", "hasFocus:3" + hasFocus);
                break;
            case R.id.bt4:
                if (hasFocus){
                    ChongsZHI();
                    bt4.setTextColor(Color.WHITE);
                    bt4.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt4,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt4,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
               // Log.d("SheZhiActivity", "hasFocus:4" + hasFocus);
                break;
            case R.id.bt5:
                if (hasFocus){
                    ChongsZHI();
                    bt5.setTextColor(Color.WHITE);
                    bt5.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt5,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt5,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
              //  Log.d("SheZhiActivity", "hasFocus:5" + hasFocus);
                break;
            case R.id.bt6:
                if (hasFocus){
                    ChongsZHI();
                    bt6.setTextColor(Color.WHITE);
                    bt6.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt6,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt6,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }

                break;
            case R.id.bt7:
                if (hasFocus){
                    ChongsZHI();
                    bt7.setTextColor(Color.WHITE);
                    bt7.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt7,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt7,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
              //  Log.d("SheZhiActivity", "hasFocus7:" + hasFocus);
                break;
            case R.id.bt8:
                if (hasFocus){
                    ChongsZHI();
                    bt8.setTextColor(Color.WHITE);
                    bt8.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt8,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt8,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
                //  Log.d("SheZhiActivity", "hasFocus7:" + hasFocus);
                break;
            case R.id.bt9:
                if (hasFocus){
                    ChongsZHI();
                    bt9.setTextColor(Color.WHITE);
                    bt9.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet9 = new AnimatorSet();
                    animatorSet9.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt9,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt9,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet9.setDuration(300);
                    animatorSet9.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet9.start();
                }
                //  Log.d("SheZhiActivity", "hasFocus7:" + hasFocus);
                break;
            case R.id.bt10:
                if (hasFocus){
                    ChongsZHI();
                    bt10.setTextColor(Color.WHITE);
                    bt10.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet10 = new AnimatorSet();
                    animatorSet10.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt10,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt10,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet10.setDuration(300);
                    animatorSet10.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet10.start();
                }
                //  Log.d("SheZhiActivity", "hasFocus7:" + hasFocus);
                break;
            case R.id.bt11:
                if (hasFocus){
                    ChongsZHI();
                    bt11.setTextColor(Color.WHITE);
                    bt11.setBackgroundResource(R.drawable.zidonghuoqu1);
                    AnimatorSet animatorSet6 = new AnimatorSet();
                    animatorSet6.playTogether(
                            //	ObjectAnimator.ofFloat(manager.getChildAt(1),"translationY",-1000,0),
                            ObjectAnimator.ofFloat(bt11,"scaleX",1.0f,1.2f,1.0f),
                            ObjectAnimator.ofFloat(bt11,"scaleY",1.0f,1.2f,1.0f)
                    );
                    //animatorSet.setInterpolator(new DescelerateInterpolator());
                    animatorSet6.setDuration(300);
                    animatorSet6.addListener(new AnimatorListenerAdapter(){
                        @Override public void onAnimationEnd(Animator animation) {

                        }
                    });
                    animatorSet6.start();
                }
                //  Log.d("SheZhiActivity", "hasFocus7:" + hasFocus);
                break;
        }
    }


}
