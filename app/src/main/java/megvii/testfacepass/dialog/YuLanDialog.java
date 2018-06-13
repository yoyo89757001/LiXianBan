package megvii.testfacepass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BaoCunBeanDao;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class YuLanDialog extends Dialog  {

//    private MediaPlayer mediaPlayer=null;
//    private IVLCVout vlcVout=null;
//    private IVLCVout.Callback callback;
//    private LibVLC libvlc;
//    private Media media;
//    private SurfaceView surfaceview;
   private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;


    public YuLanDialog(Context context) {
        super(context, R.style.dialog_style2);
        Window window =  this.getWindow();
        if ( window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = LayoutParams.WRAP_CONTENT;
                attr.width = LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        baoCunBeanDao = MyApplication.myApplication.getDaoSession().getBaoCunBeanDao();
        baoCunBean = baoCunBeanDao.load(123456L);
        setCustomDialog(context);
    }

    private void setCustomDialog(Context c) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.yulan_dialog, null);
//        surfaceview = (SurfaceView) mView.findViewById(R.id.surfaceview);
//        if (baoCunBean != null) {
//        libvlc = new LibVLC(c);
//        mediaPlayer = new MediaPlayer(libvlc);
//        vlcVout = mediaPlayer.getVLCVout();
//
//        callback = new IVLCVout.Callback() {
//            @Override
//            public void onNewLayout(IVLCVout ivlcVout, int i, int i1, int i2, int i3, int i4, int i5) {
//
//            }
//
//            @Override
//            public void onSurfacesCreated(IVLCVout ivlcVout) {
//                try {
//
//                    if (mediaPlayer != null) {
//                       // Log.d("dddddd", baoCunBean.getShipingIP() + "gggg");
//
//                        media = new Media(libvlc, Uri.parse("rtsp://" + baoCunBean.getShipingIP() + "/user=admin&password=&channel=1&stream=0.sdp"));
//                        mediaPlayer.setMedia(media);
//                        mediaPlayer.play();
//
//                    }
//
//                } catch (Exception e) {
//                    Log.d("vlc-newlayout", e.toString());
//                }
//            }
//
//            @Override
//            public void onSurfacesDestroyed(IVLCVout ivlcVout) {
//
//            }
//
//            @Override
//            public void onHardwareAccelerationError(IVLCVout vlcVout) {
//
//            }
//        };
//
//        vlcVout.addCallback(callback);
//        vlcVout.setVideoView(surfaceview);
//        vlcVout.attachViews();

//    }
        super.setContentView(mView);
    }




    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("YuLanDialog", "dialog分离");


    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnQueRenListener(View.OnClickListener listener){
     //   l1.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setQuXiaoListener(View.OnClickListener listener){
       // l2.setOnClickListener(listener);
    }



}
