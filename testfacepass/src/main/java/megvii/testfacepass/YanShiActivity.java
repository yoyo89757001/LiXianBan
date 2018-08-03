package megvii.testfacepass;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.ImageFormat;
import android.graphics.Matrix;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import android.graphics.YuvImage;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import android.util.LongSparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;

import megvii.facepass.types.FacePassAddFaceResult;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;

import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;
import megvii.testfacepass.adapter.MyRecyclerAdapter;
import megvii.testfacepass.beans.Ceshi;
import megvii.testfacepass.beans.CeshiDao;
import megvii.testfacepass.beans.DIKu;
import megvii.testfacepass.beans.DIKuDao;
import megvii.testfacepass.beans.YanZhiBean;
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;

import megvii.testfacepass.cookies.CookiesManager;
import megvii.testfacepass.utils.DateUtils;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GsonUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class YanShiActivity extends Activity implements CameraManager.CameraListener {
    private ValueAnimator animator;
    private static final String DEBUG_TAG = "FacePassDemo";
    private DIKuDao diKuDao=MyApplication.myApplication.getDaoSession().getDIKuDao();
    private static final int MSG_SHOW_TOAST = 1;
    private int dw,dh;
    private static final int DELAY_MILLION_SHOW_TOAST = 2000;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter yanZhiadapter;
    /* 识别服务器IP */
  //  private static final String serverIP_offline = "10.104.44.50";//offline
   // private static final String serverIP_online = "10.199.1.14";
  //  private static String serverIP;

    private static final String authIP = "https://api-cn.faceplusplus.com";
    private static final String apiKey = "CKbSYQqAuc5AzCMoOK-kbo9KaabtEciQ";
    private static final String apiSecret = "HeZgW5ILE83nKkqF-QO5IqEEmeRxPgeI";

    private static long lingshiTokenid=-1;

    /* 人脸识别Group */
    private static final String group_name = "face-pass-test-x";

    /* 程序所需权限 ：相机 文件存储 网络访问 */
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE, PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE};

    private String [] duanyuString=new String[10];
    private String jiebang=null;
    /* SDK 实例对象 */
    FacePassHandler mFacePassHandler;
    /* 相机实例 */
    private CameraManager manager;
    /* 相机预览界面 */
    private CameraPreview cameraView;

    private boolean isLocalGroupExist = false;

    /* 在预览界面圈出人脸 */
    private FaceView faceView;

    /* 相机是否使用前置摄像头 */
    private static boolean cameraFacingFront = true;
    /* 相机图片旋转角度，请根据实际情况来设置
     * 对于标准设备，可以如下计算旋转角度rotation
     * int windowRotation = ((WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
     * Camera.CameraInfo info = new Camera.CameraInfo();
     * Camera.getCameraInfo(cameraFacingFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK, info);
     * int cameraOrientation = info.orientation;
     * int rotation;
     * if (cameraFacingFront) {
     *     rotation = (720 - cameraOrientation - windowRotation) % 360;
     * } else {
     *     rotation = (windowRotation - cameraOrientation + 360) % 360;
     * }
     */
    private int cameraRotation;

    private static final int cameraWidth = 1920;
    private static final int cameraHeight = 1080;
    private int mSecretNumber = 0;
    private static final long CLICK_INTERVAL = 600;
    private long mLastClickTime;
    private int heightPixels;
    private int widthPixels;
    int screenState = 0;// 0 横 1 竖
    private final int TIMEOUT=1000*10;
    FacePassModel trackModel;
    FacePassModel poseModel;
    FacePassModel blurModel;
    FacePassModel livenessModel;
    FacePassModel searchModel;
    FacePassModel detectModel;
    FacePassModel ageGenderModel;
    private static int count=1;
    private static boolean isLink=true;

    private static List<YanZhiBean> yanZhiBeans=new ArrayList<>(5);
    private LongSparseArray<Bitmap> bitmapSparseArray=new LongSparseArray<>();
    private static Vector<DIKu> diKuVector=new Vector<>();

    FrameLayout frameLayout;
    private int buttonFlag = 0;
    private Button settingButton;

    /*Toast 队列*/
    LinkedBlockingQueue<Toast> mToastBlockQueue;

    LinkedBlockingQueue<YanZhiBean> yanzhengBlockQueue;
    /*DetectResult queue*/
    ArrayBlockingQueue<byte[]> mDetectResultQueue;

    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;

    /*recognize thread*/
    RecognizeThread mRecognizeThread;

    FeedFrameThread mFeedFrameThread;


    /*底库同步*/
    private ImageView ceshi;
    private AlertDialog mSyncGroupDialog;

    private Handler mAndroidHandler;

   // private  static ValueAnimator animator = ValueAnimator.ofFloat(1f, 10f);
    //排行榜
    private List<DIKu> paiHangLists=new ArrayList<>();
    private static int gz=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToastBlockQueue = new LinkedBlockingQueue<>();
        yanzhengBlockQueue = new LinkedBlockingQueue<>();
        mDetectResultQueue = new ArrayBlockingQueue<>(5);
        mFeedFrameQueue = new ArrayBlockingQueue<>(1);
        initAndroidHandler();

        CeshiDao dd=MyApplication.myApplication.getDaoSession().getCeshiDao();
        Ceshi ceshi=new Ceshi();
        ceshi.setId(12345L);
        ceshi.setBytes(bitmabToBytes(BitmapFactory.decodeResource(getResources(),R.drawable.fugui)));
        dd.update(ceshi);

        Log.d("YanShiActivity", "dd.load(1234L).getBytes():" + dd.load(12345L).getBytes().length);

        /* 初始化界面 */
        initView();
        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {
            initFacePassSDK();
        }
        duanyuString[0]="我愿意先颠沛流离再遇见温暖的你";
        duanyuString[1]="年少轻狂的好日子、一懂事就结束 !";
        duanyuString[2]="最珍贵的财富是时间,最大的浪费是虚度流年.";
        duanyuString[3]="做事重在坚持，为人重在有爱。";
        duanyuString[4]="得不到的付出，最好学会适可而止";
        duanyuString[5]="不要等待机会,而要创造机会";
        duanyuString[6]="勤奋是你生命的密码,能译出你一部壮丽的史诗";
        duanyuString[7]="人之所以能，是相信能";
        duanyuString[8]="懂得低头，才能出头。";
        duanyuString[9]="含泪播种的人，一定能含笑收获";


        initFaceHandler();
        mRecognizeThread = new RecognizeThread();
        mRecognizeThread.start();

        mFeedFrameThread = new FeedFrameThread();
        mFeedFrameThread.start();

//        animator.setDuration(1000L);
//        animator.setRepeatCount(-1);
//        animator.start();

        animator = ValueAnimator.ofFloat(0, 1f);
        //动画时长，让进度条在CountDown时间内正好从0-360走完，
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//表示不循环，-1表示无限循环
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("YanShiActivity", "eeeeeeeee");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("YanShiActivity", "fffffffffffffffff");



            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                gz=1;

            }
        });
        animator.start();

    }

    private void initAndroidHandler() {

        mAndroidHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SHOW_TOAST:
                        if (mToastBlockQueue.size() > 0) {
                            Toast toast = mToastBlockQueue.poll();
                            if (toast != null) {
                                toast.show();
                            }
                        }
                        if (mToastBlockQueue.size() > 0) {
                            removeMessages(MSG_SHOW_TOAST);
                            sendEmptyMessageDelayed(MSG_SHOW_TOAST, DELAY_MILLION_SHOW_TOAST);
                        }
                        break;
                }
            }
        };
    }

    private void initFacePassSDK() {
        FacePassHandler.getAuth(authIP, apiKey, apiSecret);
        FacePassHandler.initSDK(getApplicationContext());

    }

    private void initFaceHandler() {

        new Thread() {
            @Override
            public void run() {
                while (true && !isFinishing()) {
                    if (FacePassHandler.isAvailable()) {

                        Log.d(DEBUG_TAG, "start to build FacePassHandler");
                         /* FacePass SDK 所需模型， 模型在assets目录下 */
                        trackModel = FacePassModel.initModel(getApplicationContext().getAssets(), "tracker.DT1.4.1.dingding.20180315.megface2.9.bin");
                        poseModel = FacePassModel.initModel(getApplicationContext().getAssets(), "pose.alfa.tiny.170515.bin");
                        blurModel = FacePassModel.initModel(getApplicationContext().getAssets(), "blurness.v5.l2rsmall.bin");
                        livenessModel = FacePassModel.initModel(getApplicationContext().getAssets(), "panorama.facepass.offline.180312.bin");
                        searchModel = FacePassModel.initModel(getApplicationContext().getAssets(), "feat.small.facepass.v2.9.bin");
                        detectModel = FacePassModel.initModel(getApplicationContext().getAssets(), "detector.mobile.v5.fast.bin");
                        ageGenderModel = FacePassModel.initModel(getApplicationContext().getAssets(), "age_gender.bin");
                        /* SDK 配置 */
                        float searchThreshold = 72f;
                        float livenessThreshold = 48f;
                        boolean livenessEnabled = true;
                        int faceMinThreshold = 80;
                        FacePassPose poseThreshold = new FacePassPose(30f, 30f, 30f);
                        float blurThreshold = 0.2f;
                        float lowBrightnessThreshold = 70f;
                        float highBrightnessThreshold = 210f;
                        float brightnessSTDThreshold = 60f;
                        int retryCount = 2;
                        int rotation = cameraRotation;
                        String fileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        FacePassConfig config;
                        try {
                            /* 填入所需要的配置 */
                            config = new FacePassConfig(searchThreshold, livenessThreshold, livenessEnabled,
                                    faceMinThreshold, poseThreshold, blurThreshold,
                                    lowBrightnessThreshold, highBrightnessThreshold, brightnessSTDThreshold,
                                    retryCount, rotation, fileRootPath,
                                    trackModel, poseModel, blurModel, livenessModel, searchModel, detectModel, ageGenderModel);
                            /* 创建SDK实例 */
                            mFacePassHandler = new FacePassHandler(config);

                            float searchThreshold2 = 75f;
                            float livenessThreshold2 = 48f;
                            boolean livenessEnabled2 = true;
                            int faceMinThreshold2 = 60;
                            float blurThreshold2 = 0.6f;
                            float lowBrightnessThreshold2 = 50f;
                            float highBrightnessThreshold2 = 210f;
                            float brightnessSTDThreshold2 = 90f;
                            FacePassConfig config1=new FacePassConfig(faceMinThreshold2,30f,30f,30f,blurThreshold2,
                                    lowBrightnessThreshold2,highBrightnessThreshold2,brightnessSTDThreshold2);

                            Log.d("YanShiActivity", "设置入库质量配置" + mFacePassHandler.setAddFaceConfig(config1));

                            //建库
                            mFacePassHandler.createLocalGroup(group_name);
                            Bitmap bb=BitmapFactory.decodeResource(getResources(),R.drawable.zf3);
                           FacePassAddFaceResult ddd= mFacePassHandler.addFace(bb);
                           jiebang=new String(ddd.faceToken);
                            Log.d("YanShiActivity", "入库状态" + mFacePassHandler.bindGroup(group_name, jiebang.getBytes()));

                            checkGroup();

                        } catch (FacePassException e) {
                            e.printStackTrace();
                            Log.d(DEBUG_TAG, "FacePassHandler is null");
                            return;
                        }
                        return;
                    }
                    try {
                        /* 如果SDK初始化未完成则需等待 */
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        checkGroup();
        initToast();
        /* 打开相机 */
        if (hasPermission()) {
            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);
        }
        adaptFrameLayout();
        super.onResume();
    }


    private void checkGroup() {
        if (mFacePassHandler == null) {
            return;
        }
        String[] localGroups = mFacePassHandler.getLocalGroups();
        isLocalGroupExist = false;
        if (localGroups == null || localGroups.length == 0) {
            faceView.post(new Runnable() {
                @Override
                public void run() {
                    toast("请创建" + group_name + "底库");
                }
            });
            return;
        }
        for (String group : localGroups) {
            if (group_name.equals(group)) {
                isLocalGroupExist = true;

            }
        }
        if (!isLocalGroupExist) {
            faceView.post(new Runnable() {
                @Override
                public void run() {
                    toast("请创建" + group_name + "底库");
                }
            });
        }
    }


    /* 相机回调函数 */
    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {

        /* 如果SDK实例还未创建，则跳过 */
        if (mFacePassHandler == null) {
            return;
        }
         /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */

        FacePassImage image;
        try {
            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
        } catch (FacePassException e) {
            e.printStackTrace();
            return;
        }

     //   ceshi.setImageBitmap();

        mFeedFrameQueue.offer(image);
    }

    private class FeedFrameThread extends Thread {
        boolean isIterrupt;

        @Override
        public void run() {
            while (!isIterrupt) {
                try {
                    FacePassImage image = mFeedFrameQueue.take();
                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
                    FacePassDetectionResult detectionResult = null;
                    detectionResult = mFacePassHandler.feedFrame(image);
                    if (detectionResult == null || detectionResult.faceList.length == 0) {
                        faceView.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                faceView.invalidate();
                            }
                        });
                    } else {
                       // Log.d("bitmapSparseArray", "image:");
                        showFacePassFace(detectionResult,image);
                    }


                    /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                        if (detectionResult != null && detectionResult.message.length != 0) {
                          //  Log.d(DEBUG_TAG, "mDetectResultQueue.offer");
                            mDetectResultQueue.offer(detectionResult.message);
                            Log.d("bitmapSparseArray", "image222:");
                        }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (FacePassException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RecognizeThread extends Thread {

        boolean isInterrupt;

        @Override
        public void run() {
            while (!isInterrupt) {
                try {
                  //  Log.d(DEBUG_TAG, "2 mDetectResultQueue.size = " + mDetectResultQueue.size());
                    byte[] detectionResult = mDetectResultQueue.take();

                    Log.d("YanShiActivity", "isLocalGroupExist:" + isLocalGroupExist);
                    if (isLocalGroupExist) {

                        FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult);

                        if (recognizeResult != null && recognizeResult.length > 0) {
                            for (FacePassRecognitionResult result : recognizeResult) {

                                String faceToken = new String(result.faceToken);
                                if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
                                    SystemClock.sleep(2500);
                                    //弹窗
                                    //识别出来的trackId跟diKuVector里面的比对


                                    for (int i=0;i<diKuVector.size();i++){
                                        if (result.trackId==diKuVector.get(i).getTrackId()){
                                          DIKu diKu=diKuVector.get(i);
                                          DIKu diKu1=diKuDao.queryBuilder().where(DIKuDao.Properties.TeZhengMa.eq(faceToken)).unique();
                                            Log.d("YanShiActivitytttttt", "查询出来的:" + diKu1);
                                            if (diKu1==null)
                                                return;
                                          diKu1.setTrackId(diKu.getTrackId());
                                          diKu1.setXingbie(diKu.getXingbie());
                                          diKu1.setNianl(diKu.getNianl());
                                          diKu1.setBytes(diKu.getBytes());
                                          diKu1.setGuanzhu(diKu.getGuanzhu());
                                          diKu1.setBiaoqing(diKu.getBiaoqing());
                                          diKu1.setFuzhi(diKu.getFuzhi());
                                          diKu1.setCishu(diKu.getCishu()+1);
                                          diKu1.setPaihang(diKu.getPaihang());
                                          diKu1.setYanzhi(diKu.getYanzhi());
                                          //  Log.d("YanShiActivitytttttt", "diKu1.getBytes().length:" + diKu1.getBytes().length);
                                          diKuDao.update(diKu1);
                                          //排序
                                            final LazyList<DIKu> diKus=diKuDao.queryBuilder().orderAsc(DIKuDao.Properties.Yanzhi).where(DIKuDao.Properties.Bytes.isNotNull()).listLazy();
                                           // Log.d("YanShiActivitytttttt", "diKus.size():" + diKus.size());
                                            paiHangLists.clear();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for (int j=0;j<15;j++){
                                                        if (j<diKus.size()){
                                                           // Log.d("YanShiActivitytttttt", "diKus.get(j).getBytes().length:" + diKus.get(j).getBytes().length);
                                                            paiHangLists.add(diKus.get(j));

                                                        }else {
                                                            break;
                                                        }

                                                    }
                                                    Collections.reverse(paiHangLists); // 倒序排列
                                                    yanZhiadapter.notifyDataSetChanged();
                                                }
                                            });




                                        }
                                    }


                                }else {
                                    if (result.detail.livenessScore > 48) {

                                    if (lingshiTokenid != result.trackId) {
                                        lingshiTokenid = result.trackId;
                                        //不会重复
                                        final Bitmap bb = bitmapSparseArray.get(result.trackId);
                                        Log.d("YanShiActivity", "获取图片的:" + result.trackId);

                                        if (bb != null) {
                                            FacePassAddFaceResult result33=null;
                                            try {
                                                 result33 = mFacePassHandler.addFace(bb);
                                            }catch (Exception e){
                                                Log.d("RecognizeThread", e.getMessage()+"回收的位图");
                                                return;
                                            }

                                            if (result33 != null) {
                                                if (result33.result == 0) {
                                                    mFacePassHandler.bindGroup(group_name, result33.faceToken);
                                                    DIKu diKu = new DIKu();
                                                    diKu.setId(System.currentTimeMillis());
                                                    diKu.setXingbie("");
                                                    diKu.setTeZhengMa(new String(result33.faceToken));
                                                    diKu.setTrackId(result.trackId);
                                                    diKu.setTime(DateUtils.time(System.currentTimeMillis() + ""));
                                                    diKu.setCishu(1);
                                                    diKuDao.insert(diKu);

                                                    Log.d("YanShiActivitytttttt", "入库成功" + result.trackId +
                                                            "    " + new String(result33.faceToken));

                                                } else {

                                                    Log.d("YanShiActivitytttttt", "入库失败质量不行");
                                                    lingshiTokenid = -1;

                                                }
                                            }else {
                                                lingshiTokenid=-1;
                                            }

                                        } else {
                                            lingshiTokenid=-1;
                                            Log.d("YanShiActivitytttttt", "入库图片为空");
                                        }
                                    }

                                }else {
                                        lingshiTokenid=-1;
                                        Log.d("YanShiActivitytttttt", "非活体");
                                }



                                }

                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d("RecognizeThread", e.getMessage()+"插入");
                } catch (FacePassException e) {
                    Log.d("RecognizeThread", e.getMessage()+"插入2");
                }
            }
        }

        @Override
        public void interrupt() {
            isInterrupt = true;
            super.interrupt();
        }
    }




    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /* 请求程序所需权限 */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Permission, PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED)
                    granted = false;
            }
            if (!granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA)
                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_WRITE_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_INTERNET)
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)) {
                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
                    }
            } else {
                initFacePassSDK();
            }
        }
    }

    private void adaptFrameLayout() {
        SettingVar.isButtonInvisible = false;
        SettingVar.iscameraNeedConfig = false;
    }

    private void initToast() {
        SettingVar.isButtonInvisible = false;
    }

    private void initView() {

        @SuppressLint("WrongConstant")
        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
        if (windowRotation == 0) {
            cameraRotation = FacePassImageRotation.DEG90;
        } else if (windowRotation == 90) {
            cameraRotation = FacePassImageRotation.DEG0;
        } else if (windowRotation == 270) {
            cameraRotation = FacePassImageRotation.DEG180;
        } else {
            cameraRotation = FacePassImageRotation.DEG270;
        }
        Log.i(DEBUG_TAG, "cameraRation: " + cameraRotation);
        cameraFacingFront = true;
        SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
        SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
        SettingVar.isCross = preferences.getBoolean("isCross", SettingVar.isCross);
        SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
        SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
        SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);


        if (SettingVar.isSettingAvailable) {
            cameraRotation = SettingVar.faceRotation;
            cameraFacingFront = SettingVar.cameraFacingFront;
        }


        Log.i("orientation", String.valueOf(windowRotation));
        final int mCurrentOrientation = getResources().getConfiguration().orientation;

        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }
        setContentView(R.layout.activity_yanshi);
        recyclerView= (RecyclerView) findViewById(R.id.reclelist);
        LinearLayoutManager manager2=new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager2);
        yanZhiadapter=new MyRecyclerAdapter(YanShiActivity.this,paiHangLists);
        recyclerView.setAdapter(yanZhiadapter);

        ceshi= (ImageView) findViewById(R.id.ceshi);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
        /* 初始化界面 */
        faceView = (FaceView) this.findViewById(R.id.fcview);

        settingButton = (Button) this.findViewById(R.id.settingid);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YanShiActivity.this, SettingActivity.class);
                startActivity(intent);
                YanShiActivity.this.finish();
            }
        });
        SettingVar.cameraSettingOk = false;
        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        /* 注册相机回调函数 */
        manager.setListener(this);

        dw = getDisplaySize(YanShiActivity.this).x;
        dh = getDisplaySize(YanShiActivity.this).y;
        faceView.setWH(dw,dh);

    }

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    protected void onStop() {
        SettingVar.isButtonInvisible = false;
        mToastBlockQueue.clear();
        mDetectResultQueue.clear();
        mFeedFrameQueue.clear();
        if (manager != null) {
            manager.release();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        faceView.clear();
        faceView.invalidate();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mRecognizeThread.isInterrupt = true;

        mRecognizeThread.interrupt();

        if (manager != null) {
            manager.release();
        }
        if (mToastBlockQueue != null) {
            mToastBlockQueue.clear();
        }
        if (mAndroidHandler != null) {
            mAndroidHandler.removeCallbacksAndMessages(null);
        }

        if (mFacePassHandler != null) {
            try {
                if (jiebang!=null){
                    mFacePassHandler.unBindGroup(group_name,jiebang.getBytes());
                    mFacePassHandler.deleteFace(jiebang.getBytes());
                }

            } catch (FacePassException e) {
                e.printStackTrace();
            }
            mFacePassHandler.release();
        }
        if (mFeedFrameQueue != null) {
            mFeedFrameQueue.clear();
        }



        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //结束时把tokenid置为-1；

        isLink=true;
        count=1;

        super.onPause();
    }

    private void showFacePassFace(final FacePassDetectionResult detectResult, final FacePassImage image) {
        final FacePassFace[] result = detectResult.faceList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceView.clear();

                for (final FacePassFace face : result) {

                    boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
                    // SpannableString faceViewString = new SpannableString(faceIdString);
                    // faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        StringBuilder faceGenderString = new StringBuilder();
//                        switch (lingshi.getFacePassFace().gender) {
//                            case 0:
//                                faceGenderString.append("男");
//                                break;
//                            case 1:
//                                faceGenderString.append("女");
//                                break;
//                            default:
//                                faceGenderString.append("未知");
//                        }
                    Matrix mat = new Matrix();
                    int w = cameraView.getMeasuredWidth();
                    int h = cameraView.getMeasuredHeight();

                    int cameraHeight = manager.getCameraheight();
                    int cameraWidth = manager.getCameraWidth();

                    float left = 0;
                    float top = 0;
                    float right = 0;
                    float bottom = 0;
                    switch (cameraRotation) {
                        case 0:
                            left = face.rect.left;
                            top = face.rect.top;
                            right = face.rect.right;
                            bottom = face.rect.bottom;
                            mat.setScale(mirror ? -1 : 1, 1);
                            mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                            mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                            break;
                        case 90:
                            mat.setScale(mirror ? -1 : 1, 1);
                            mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                            mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                            left = face.rect.top;
                            top = cameraWidth - face.rect.right;
                            right = face.rect.bottom;
                            bottom = cameraWidth - face.rect.left;
                            break;
                        case 180:
                            mat.setScale(1, mirror ? -1 : 1);
                            mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                            mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                            left = face.rect.right;
                            top = face.rect.bottom;
                            right = face.rect.left;
                            bottom = face.rect.top;
                            break;
                        case 270:
                            mat.setScale(mirror ? -1 : 1, 1);
                            mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                            mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                            left = cameraHeight - face.rect.bottom;
                            top = face.rect.left;
                            right = cameraHeight - face.rect.top;
                            bottom = face.rect.right;
                    }

                    RectF drect = new RectF();
                    final RectF srect = new RectF(left, top, right, bottom);
                    mat.mapRect(drect, srect);

                    //裁图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (face.pose.pitch<20 && face.pose.pitch>-20 && face.pose.roll<20 && face.pose.roll>-20 && face.pose.yaw<20 && face.pose.yaw>-20 && face.blur<0.2){
                                try{
                                    //获取图片
                                    //  Log.d("YanShiActivityrrrr", "bitmapSparseArray.size()22222222:" );
                                    YuvImage image2 = new YuvImage(image.image, ImageFormat.NV21, image.width, image.height, null);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    image2.compressToJpeg(new Rect(0, 0, image.width, image.height), 100, stream);
                                    final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                    stream.close();

                                    int x1,y1,x2,y2=0;
                                    x1= (int) (srect.left - 34);
                                    y1= (int) (srect.top - 140);
                                    x2= (int) ((srect.right+34)-x1);
                                    y2= (int) ((srect.bottom+180)-(srect.top));

                                    Bitmap bitmap = Bitmap.createBitmap(bmp,x1<=0?0:x1,y1<=0?0:y1,(x1+x2)>=bmp.getWidth()?bmp.getWidth()-x1:x2,
                                            (y1+y2)>=bmp.getHeight()?-y1:y2);

                                    bitmapSparseArray.put(face.trackId,bitmap);
                                  //  Log.d("YanShiActivity", "插入图片:" + face.trackId);
                                    if (bitmapSparseArray.size()>14){
                                        Log.d("YanShiActivitytttttt", bitmapSparseArray.size()+"删除key" + bitmapSparseArray.keyAt(0));
                                        bitmapSparseArray.removeAt(0);
                                    }
                                    int p=0;
                                    for (int i=0;i<diKuVector.size();i++){
                                        if (face.trackId==diKuVector.get(i).getTrackId()){
                                            p=1;
                                            break;
                                        }else {
                                            p=0;
                                        }
                                    }

                                    if (p==0 && isLink){
                                        isLink=false;
                                        getOkHttpClient2(bitmap,face.trackId);
                                    }
                                   // Log.d("YanShiActivity", isLink+"p:" + p);

                                }catch(Exception ex){
                                    Log.e("Sys","Error:"+ex.getMessage());
                                }
                            }

                        }
                    }).start();



                    int size=diKuVector.size();
                    if (size>8){
                        diKuVector.remove(0);
                        size-=1;
                    }
                    int ppp=0;
                    DIKu diKu=null;
                    for (int j=0;j<size;j++){
                        if (face.trackId==diKuVector.get(j).getTrackId()){
                            //相等就去取存起来的
                            diKu=diKuVector.get(j);
                            ppp=1;
                            break;
                        }else {
                            ppp=0;
                        }

                    }


                    if (ppp==1){
                        //相等就去取存起来的

                        faceView.addRect(drect);
                        faceView.addId("ID = " + face.trackId);


                        if (diKu!=null){

                            faceView.addAge(diKu.getNianl()+"岁");
                            faceView.addGenders(diKu.getXingbie());
                            //颜值
                            DecimalFormat df = new DecimalFormat("#.00");
                            faceView.addBlur(df.format(diKu.getYanzhi()));
                            //肤质
                            faceView.addYaw(diKu.getFuzhi());
                            //  Log.d("YanShiActivity", "diKu.getBitmapBytes():" + diKu.getBitmapBytes().length);
                            //图片
                            if (diKu.getBytes()!=null && diKu.getBytes().length>0){
                                //  Log.d("YanShiActivity", "diKu.getBitmapBytes():" + diKu.getBytes().length);
                                faceView.addBitmaps(BitmapFactory.decodeByteArray(diKu.getBytes(), 0, diKu.getBytes().length));
                            }else {
                                faceView.addBitmaps(null);
                            }

                            //表情
                            if (diKu.getBiaoqing()!=null){
                                faceView.addPitch(diKu.getBiaoqing());
                            }else {
                                faceView.addPitch("平静");
                            }
                            //时长
                            diKu.setGuanzhu(diKu.getGuanzhu()+gz);
                           // Log.d("YanShiActivity", "gz:" + gz);

                            gz=0;
                            faceView.addTimes(diKu.getGuanzhu()+"");
                            //排行
                            faceView.addRoll("你的排行是");

                        }else {

                            faceView.addAge("" );
                            faceView.addGenders("性别: 请正对屏幕" );
                            faceView.addBlur("" );
                            faceView.addYaw("");
                            faceView.addPitch("");
                            faceView.addRoll("");
                            faceView.addBitmaps(null);
                            faceView.addTimes("");

                        }
                    }else {

                        faceView.addRect(drect);
                        faceView.addId("ID = " + face.trackId);
                        faceView.addAge("" );
                        faceView.addGenders("" );
                        faceView.addBlur("" );
                        faceView.addYaw("");
                        faceView.addPitch("");
                        faceView.addRoll("");
                        faceView.addBitmaps(null);
                        faceView.addTimes("");

                    }

                  //      faceView.addRoll("旋转: " + (int) lingshi.getFacePassFace().pose.roll + "°");
                    //    faceView.addPitch("上下: " + (int) lingshi.getFacePassFace().pose.pitch + "°");
                     //   faceView.addYaw("左右: " + (int) lingshi.getFacePassFace().pose.yaw + "°");
                    //   faceView.addBlur("模糊: " + String.format("%.2f", face.blur));

                }
                faceView.invalidate();
            }
        });



    }

    public void showToast(CharSequence text, int duration, boolean isSuccess, Bitmap bitmap) {
        LayoutInflater inflater = getLayoutInflater();
        View toastView = inflater.inflate(R.layout.toast, null);
        LinearLayout toastLLayout = (LinearLayout) toastView.findViewById(R.id.toastll);
        if (toastLLayout == null) {
            return;
        }
        toastLLayout.getBackground().setAlpha(100);
        ImageView imageView = (ImageView) toastView.findViewById(R.id.toastImageView);
        TextView idTextView = (TextView) toastView.findViewById(R.id.toastTextView);
        TextView stateView = (TextView) toastView.findViewById(R.id.toastState);
        SpannableString s;
        if (isSuccess) {
            s = new SpannableString("验证成功");
            imageView.setImageResource(R.drawable.success);
        } else {
            s = new SpannableString("验证失败");
            imageView.setImageResource(R.drawable.success);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        stateView.setText(s);
        idTextView.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(toastView);

        if (mToastBlockQueue.size() == 0) {
            mAndroidHandler.removeMessages(MSG_SHOW_TOAST);
            mAndroidHandler.sendEmptyMessage(MSG_SHOW_TOAST);
            mToastBlockQueue.offer(toast);
        } else {
            mToastBlockQueue.offer(toast);
        }
    }


    //首先登录-->获取所有主机-->创建或者删除或者更新门禁
    private void getOkHttpClient2(final Bitmap bitmap, final long trackId){
         String batt = null;
        try {
            batt=FileUtil.bitmapToBase64(bitmap);

        }catch (Exception e){
            isLink=true;
            Log.d("YanShiActivityttttttt", e.getMessage()+"bitmap回收");
            return;
        }

      //  Base64.decode(printData.getBytes(), Base64.DEFAULT);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("api_key", "BBDRR-nwJM38qGHUiBV0k4eMUZ2jDsa1")
                .add("api_secret", "YDhcIhcjc5OVnDVQvspwSoSQnjM-fWYn")
                .add("image_base64", batt)
                .add("return_attributes", "gender,age,emotion,eyestatus,beauty,skinstatus")
                .build();

        Request.Builder requestBuilder = new Request.Builder();
        //requestBuilder.header("User-Agent", "Koala Admin");
        //requestBuilder.header("Content-Type","application/json");
        requestBuilder.post(body);
        requestBuilder.url("https://api-cn.faceplusplus.com/facepp/v3/detect");
        final Request request = requestBuilder.build();

        Call mcall= okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("YanShiActivitytttttt", "请求失败"+e.getMessage());
                isLink=true;

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    String s=response.body().string();
                    Log.d("YanShiActivitytttttt", "检测"+s);
                    JsonObject jsonObject = GsonUtil.parse(s).getAsJsonObject();
                    Gson gson = new Gson();
                    YanZhiBean menBean = gson.fromJson(jsonObject, YanZhiBean.class);
                    if (menBean.getFaces()!=null && menBean.getFaces().get(0)!=null ){

//                        DIKu diKu=diKuDao.queryBuilder().where(DIKuDao.Properties.TeZhengMa.eq(tezhengma)).unique();
                        DIKu diKu=new DIKu();
                            //更新
                            YanZhiBean.FacesBean.AttributesBean.SkinstatusBean nn=menBean.getFaces().get(0).getAttributes().getSkinstatus();
                                HashMap<Double,String> kk=new HashMap<>();
                                double[] a=new double[4];
                                a[0]=nn.getAcne();
                                a[1]=nn.getDark_circle();
                                a[2]=nn.getHealth();
                                a[3]=nn.getStain();
                                kk.put(a[0],"青春痘");
                                kk.put(a[1],"黑眼圈");
                                kk.put(a[2],"健康");
                                kk.put(a[3],"暗淡");
                                Arrays.sort(a);  //进行排序

                         YanZhiBean.FacesBean.AttributesBean.EmotionBean nn2= menBean.getFaces().get(0).getAttributes().getEmotion();
                        HashMap<Double,String> kk2=new HashMap<>();
                        double[] a2=new double[7];
                        a2[0]=nn2.getAnger();
                        a2[1]=nn2.getDisgust();
                        a2[2]=nn2.getFear();
                        a2[3]=nn2.getHappiness();
                        a2[4]=nn2.getNeutral();
                        a2[5]=nn2.getSadness();
                        a2[6]=nn2.getSurprise();
                        kk2.put(a2[0],"愤怒");
                        kk2.put(a2[1],"厌恶");
                        kk2.put(a2[2],"害怕");
                        kk2.put(a2[3],"高兴");
                        kk2.put(a2[4],"平静");
                        kk2.put(a2[5],"悲伤");
                        kk2.put(a2[6],"惊讶");
                        Arrays.sort(a2);  //进行排序

                            String xb="";
                            if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")){
                                xb="男";
                            }else {
                                xb="女";
                            }
                          //  Log.d("YanShiActivityttttt", "bitmabToBytes(bitmap):" + bitmabToBytes(bitmap).length);
                            diKu.setTrackId(trackId);
                            diKu.setCishu(diKu.getCishu()+1);
                            diKu.setFuzhi(kk.get(a[3]));
                            diKu.setNianl(menBean.getFaces().get(0).getAttributes().getAge().getValue());
                            diKu.setXingbie(xb);
                            diKu.setYanzhi((xb.equals("女")?menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score():menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score()));
                            diKu.setBiaoqing(kk2.get(a2[6]));
                            diKu.setBytes(bitmabToBytes(bitmap));
                    //    Log.d("YanShiActivitytttttt", "diKu.getBytes().length:" + diKu.getBytes().length);

                            diKuVector.add(diKu);
                            for (int i=0;i<diKuVector.size();i++){
                                if (diKu.getTrackId()==diKuVector.get(i).getTrackId()){
                                    //替换
                                    diKuVector.set(i,diKu);
                                    Log.d("YanShiActivitytttttt", "替换成功" );
                                    break;
                                }

                            }

                            if (diKuVector.size()>10){
                                diKuVector.remove(0);
                            }

                            Log.d("YanShiActivitytttttt", "更新成功" );

//                        final PaiHangBean paiHangBean=new PaiHangBean();
                     //   paiHangBean.setBianhao(menBean.getFacePassFace().trackId+"");
                      //  (lingshi.getFacePassFace().gender==1?lingshi.getFaces().get(0).getAttributes().getBeauty().getFemale_score():lingshi.getFaces().get(0).getAttributes().getBeauty().getMale_score()
                     //   paiHangBean.setYanzhi((menBean.getFacePassFace().gender==1?menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score():menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score())+"");
                      //  paiHangBean.setTime(megvii.testfacepass.utils.DateUtils.time(System.currentTimeMillis()+""));

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                paiHangLists.add(paiHangBean);
//                                Log.d("YanShiActivity", "paiHangLists.size():" + paiHangLists.size());
//                                yanZhiadapter.notifyDataSetChanged();
//                            }
//                        });

                    }else {

//                        count++;
//                        if (count<7){
//                            getOkHttpClient2(bitmap,trackId);
//                        }else {
//                            count=1;
//
//                        }

                    }


                }catch (Exception e){
                    Log.d("YanShiActivity", e.getMessage()+"");

                }finally {
                    isLink=true;
                }

            }
        });

    }


    //图片转为二进制数据
    public byte[] bitmabToBytes(Bitmap bitmap){
        //将图片转化为位图
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            return baos.toByteArray();
        }catch (Exception ignored){
        }finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }


    private void getFaceImageByFaceToken(final long trackId, String faceToken) {
        if (TextUtils.isEmpty(faceToken)) {
            return;
        }

            try {
                final Bitmap bitmap = mFacePassHandler.getFaceImage(faceToken.getBytes());
                mAndroidHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(DEBUG_TAG, "getFaceImageByFaceToken cache is null");
                        showToast("ID = " + String.valueOf(trackId), Toast.LENGTH_SHORT, true, bitmap);
                    }
                });

            } catch (FacePassException e) {
                e.printStackTrace();
            }

    }





    private void toast(String msg) {
        Toast.makeText(YanShiActivity.this, msg, Toast.LENGTH_SHORT).show();
    }



}
