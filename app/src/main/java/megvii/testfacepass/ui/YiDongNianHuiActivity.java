package megvii.testfacepass.ui;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.github.florent37.viewanimator.ViewAnimator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.CharsetUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.jpush.android.service.AlarmReceiver;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassAddFaceResult;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;
import megvii.facepass.types.FacePassGroupSyncDetail;
import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;
import megvii.facepass.types.FacePassSyncResult;
import megvii.testfacepass.FaceView;
import megvii.testfacepass.MainActivity2;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.SettingActivity;
import megvii.testfacepass.SettingVar;
import megvii.testfacepass.adapter.FaceTokenAdapter;
import megvii.testfacepass.adapter.GroupNameAdapter;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BaoCunBeanDao;
import megvii.testfacepass.beans.MoShengRenBean;
import megvii.testfacepass.beans.MoShengRenBeanDao;
import megvii.testfacepass.beans.QianDaoIdDao;
import megvii.testfacepass.beans.ShiBieBean;
import megvii.testfacepass.beans.TanChuangBean;
import megvii.testfacepass.beans.TanChuangBeanDao;
import megvii.testfacepass.beans.WBBean;
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;
import megvii.testfacepass.donghua.ExplosionField;
import megvii.testfacepass.interfaces.RecytviewCash;
import megvii.testfacepass.network.ByteRequest;
import megvii.testfacepass.tts.control.InitConfig;
import megvii.testfacepass.tts.control.MySyntherizer;
import megvii.testfacepass.tts.control.NonBlockSyntherizer;
import megvii.testfacepass.tts.listener.UiMessageListener;
import megvii.testfacepass.tts.util.OfflineResource;
import megvii.testfacepass.utils.DateUtils;
import megvii.testfacepass.utils.DividerItemDecoration;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GlideRoundTransform;
import megvii.testfacepass.utils.GsonUtil;
import megvii.testfacepass.utils.JiaZaiDialog;
import megvii.testfacepass.utils.WrapContentLinearLayoutManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class YiDongNianHuiActivity extends Activity implements RecytviewCash,CameraManager.CameraListener, View.OnClickListener {
	private final static String TAG = "WebsocketPushMsg";
//	private IjkVideoView ijkVideoView;
	private MyReceiver myReceiver=null;
	//private SurfaceView surfaceview;
	private RecyclerView recyclerView;
	private MyAdapter adapter=null;
	private RecyclerView recyclerView2;
	private MyAdapter2 adapter2=null;
	private MoShengRenBeanDao daoSession=null;
	//private SpeechSynthesizer mSpeechSynthesizer;
	private WrapContentLinearLayoutManager manager;
	private WrapContentLinearLayoutManager manager2;
	private static WebSocketClient webSocketClient=null;
	//private ExplosionField mExplosionField;
//	private MediaPlayer mediaPlayer=null;
//	private IVLCVout vlcVout=null;
//	private IVLCVout.Callback callback;
//	private LibVLC libvlc;
//	private Media media;
//	private SurfaceHolder mSurfaceHolder;
	private String zhuji=null;
	private static final String zhuji2="http://121.46.3.20";
	private  static Vector<TanChuangBean> tanchuangList=null;
	private  static Vector<TanChuangBean> yuangongList=null;
	private int dw,dh;
	private ImageView dabg;
	private BaoCunBeanDao baoCunBeanDao=null;
	private BaoCunBean baoCunBean=null;
	private NetWorkStateReceiver netWorkStateReceiver=null;
	private TextView wangluo;
	private boolean isLianJie=false;
	private TanChuangBeanDao tanChuangBeanDao=null;
	private Typeface typeFace1;
	private String zhanghuID=null,huiyiID=null;
	protected Handler mainHandler;
	private String appId = "10588094";
	private String appKey = "dfudSSFfNNhDCDoK7UG9G5jn";
	private String secretKey = "9BaCHNSTw3TGRgTKht4ZZvPEb2fjKEC8";
	// TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
	private TtsMode ttsMode = TtsMode.MIX;
	// 离线发音选择，VOICE_FEMALE即为离线女声发音。
	// assets目录下bd_etts_speech_female.data为离线男声模型；bd_etts_speech_female.data为离线女声模型
	private String offlineVoice = OfflineResource.VOICE_FEMALE;
	// 主控制类，所有合成控制方法从这个类开始
	private MySyntherizer synthesizer;
	private TextView shi,riqi,xingqi,tianqi,wendu,tishi_tv33,jian,dian;
	private QianDaoIdDao qianDaoIdDao=null;
	private ImageView bao;
	//private MyReceiverFile myReceiverFile;


	private enum FacePassSDKMode {
		MODE_ONLINE,
		MODE_OFFLINE
	};

	private static FacePassSDKMode SDK_MODE = FacePassSDKMode.MODE_OFFLINE;

	private static final String DEBUG_TAG = "FacePassDemo";

	private static final int MSG_SHOW_TOAST = 1;

	private static final int DELAY_MILLION_SHOW_TOAST = 2000;

    /* 识别服务器IP */

	private static final String serverIP_offline = "10.104.44.50";//offline

	private static final String serverIP_online = "10.199.1.14";

	private static String serverIP;

	//   private static final String authIP = "https://api-cn.faceplusplus.com";
	private static final String authIP = "https://api-cn.faceplusplus.com";
	private static final String apiKey = "CKbSYQqAuc5AzCMoOK-kbo9KaabtEciQ";
	private static final String apiSecret = "HeZgW5ILE83nKkqF-QO5IqEEmeRxPgeI";

	private static String recognize_url;

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

	//入库弹窗
	JiaZaiDialog dialog=null;
	//入库状态
	private static boolean isRuku=false;
	private static final Object lock = new Object();
	public static String usbPath=null;
	private int si=0;

	/* SDK 实例对象 */
	FacePassHandler mFacePassHandler;

	/* 相机实例 */
	private CameraManager caneraManager;

	/* 显示人脸位置角度信息 */
	private TextView faceBeginTextView;

	/* 显示faceId */
	private TextView faceEndTextView;

	/* 相机预览界面 */
	private CameraPreview cameraView;

	private boolean isLocalGroupExist = false;

	/* 在预览界面圈出人脸 */
	//private FaceView faceView;

	private ScrollView scrollView;

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

	/* 网络请求队列*/
	RequestQueue requestQueue;

	FacePassModel trackModel;
	FacePassModel poseModel;
	FacePassModel blurModel;
	FacePassModel livenessModel;
	FacePassModel searchModel;
	FacePassModel detectModel;

	Button visible;
	LinearLayout ll;
	FrameLayout frameLayout;
	private int buttonFlag = 0;
	private Button settingButton;

	/*Toast 队列*/
	LinkedBlockingQueue<Toast> mToastBlockQueue;

	/*DetectResult queue*/
	ArrayBlockingQueue<byte[]> mDetectResultQueue;

	/*recognize thread*/
	RecognizeThread mRecognizeThread;


	/*底库同步*/
	private ImageView mSyncGroupBtn;
	private AlertDialog mSyncGroupDialog;

	private ImageView mFaceOperationBtn;
	/*图片缓存*/
	private FaceImageCache mImageCache;

	private Handler mAndroidHandler;


	private Button mSDKModeBtn;


	public  Handler handler=new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(final Message msg) {

			//识别
			if (msg.arg1==1){

				ShiBieBean.PersonBeanSB dataBean= (ShiBieBean.PersonBeanSB) msg.obj;

				try {

					final TanChuangBean bean=new TanChuangBean();
					bean.setBytes(null);
					bean.setIdid(dataBean.getId());
					bean.setType(dataBean.getSubject_type());
					bean.setName(dataBean.getName());
					bean.setTouxiang(dataBean.getAvatar());
					switch (dataBean.getSubject_type()){
						case 0: //员工
							Log.d(TAG, "员工");
							yuangongList.add(bean);
							adapter2.notifyItemInserted(yuangongList.size());
							manager2.scrollToPosition(yuangongList.size()-1);
							new Thread(new Runnable() {
								@Override
								public void run() {

									try {

										Thread.sleep(20000);
										Message message=Message.obtain();
										message.what=110;
										handler.sendMessage(message);

									} catch (InterruptedException e) {
										e.printStackTrace();
									}


								}
							}).start();

							break;
						case 1: //普通访客
							Log.d(TAG, "普通访客");

						//	mExplosionField.explode(bao);

							tanchuangList.add(bean);
							adapter.notifyItemInserted(tanchuangList.size());
							manager.scrollToPosition(tanchuangList.size()-1);
							new Thread(new Runnable() {
								@Override
								public void run() {

									try {
										Thread.sleep(20000);

										Message message=Message.obtain();
										message.what=999;
										handler.sendMessage(message);

									} catch (InterruptedException e) {
										e.printStackTrace();
									}


								}
							}).start();

							break;
						case 2:  //VIP访客
							Log.d(TAG, "VIP");
//								if (!fllowerAnimation.isRunings()){
//									fllowerAnimation.startAnimation();
//								}
							//mExplosionField.explode(bao);
							//int z=tanchuangList.size();
							int a=0;
							for (int i2=0;i2<tanchuangList.size();i2++){
								if (tanchuangList.get(i2).getIdid()==bean.getIdid()){
									a=1;
								}
							}
							if (a==0){
								tanchuangList.add(bean);
								adapter.notifyItemInserted(tanchuangList.size());
								manager.scrollToPosition(tanchuangList.size()-1);
								new Thread(new Runnable() {
									@Override
									public void run() {

										try {
											Thread.sleep(20000);

											Message message=Message.obtain();
											message.what=999;
											handler.sendMessage(message);


										} catch (InterruptedException e) {
											e.printStackTrace();
										}


									}
								}).start();
							}

							break;

					}


				} catch (Exception e) {
					//Log.d("WebsocketPushMsg", e.getMessage());
					e.printStackTrace();
				}

			}

			switch (msg.what){
				case 111:
					//更新地址

					break;
				case 110:
					//员工弹窗消失

					if (yuangongList.size()>2) {
						yuangongList.remove(2);

						adapter2.notifyItemRemoved(2);
						//adapter.notifyItemChanged(1);
						//adapter.notifyItemRangeChanged(1,tanchuangList.size());
						//adapter.notifyDataSetChanged();
						manager2.scrollToPosition(yuangongList.size() - 1);
						//Log.d(TAG, "tanchuangList.size():" + tanchuangList.size());

					}


					break;
				case 999:
					//访客弹窗消失

					if (tanchuangList.size()>1) {
						tanchuangList.remove(1);

						adapter.notifyItemRemoved(1);
						//adapter.notifyItemChanged(1);
						//	adapter.notifyItemRangeChanged(1,tanchuangList.size());
						//adapter.notifyDataSetChanged();
						manager.scrollToPosition(tanchuangList.size() - 1);
						//Log.d(TAG, "tanchuangList.size():" + tanchuangList.size());

					}


					break;
				case 19: //更新识别记录
					int size=yuangongList.size();

					//adapter2.notifyItemInserted(size-1);
					//manager2.smoothScrollToPosition(recyclerView2,null,size-1);

					break;

			}


			return false;
		}
	});


	@Override
	public void reset() {

		//数据重置
		chongzhi();
	}



	private void chongzhi(){
		//yuangongList.clear();
		//tanchuangList.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						yuangongList.clear();
						tanchuangList.clear();

						TanChuangBean bean=new TanChuangBean();
						bean.setBytes(null);
						bean.setName(null);
						bean.setType(-2);
						bean.setTouxiang(null);
						tanchuangList.add(bean);

						TanChuangBean bean3=new TanChuangBean();
						bean3.setBytes(null);
						bean3.setName(null);
						bean3.setType(-2);
						bean3.setTouxiang(null);
						yuangongList.add(bean3);

						TanChuangBean bean4=new TanChuangBean();
						bean4.setBytes(null);
						bean4.setName(null);
						bean4.setType(-2);
						bean4.setTouxiang(null);
						yuangongList.add(bean4);

						if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!recyclerView.isComputingLayout())) {
							adapter.notifyDataSetChanged();
						}
					}
				});

			}
		}).start();

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		//DisplayMetrics dm = getResources().getDisplayMetrics();
		dw = FileUtil.getDisplaySize(YiDongNianHuiActivity.this).x;
		dh = FileUtil.getDisplaySize(YiDongNianHuiActivity.this).y;
		//	Log.d(TAG, "创建111");
		mImageCache = new FaceImageCache();
		mToastBlockQueue = new LinkedBlockingQueue<>();
		mDetectResultQueue = new ArrayBlockingQueue<byte[]>(5);
		initAndroidHandler();

		if (SDK_MODE == FacePassSDKMode.MODE_ONLINE) {
			recognize_url = "http://" + serverIP_online + ":8080/api/service/recognize/v1";
			serverIP = serverIP_online;
		} else {
			serverIP = serverIP_offline;
		}

        /* 初始化界面 */
		initView();
        /* 申请程序所需权限 */
		if (!hasPermission()) {
			requestPermission();
		} else {
			initFacePassSDK();
		}

		initFaceHandler();
        /* 初始化网络请求库 */
		requestQueue = Volley.newRequestQueue(getApplicationContext());

		mRecognizeThread = new RecognizeThread();
		mRecognizeThread.start();


		qianDaoIdDao= MyApplication.myApplication.getDaoSession().getQianDaoIdDao();
		tanChuangBeanDao=MyApplication.myApplication.getDaoSession().getTanChuangBeanDao();
		baoCunBeanDao = MyApplication.myApplication.getDaoSession().getBaoCunBeanDao();
		baoCunBean = baoCunBeanDao.load(123456L);
		if (baoCunBean == null) {
			BaoCunBean baoCunBea = new BaoCunBean();
			baoCunBea.setId(123456L);
			baoCunBeanDao.insert(baoCunBea);
			baoCunBean = baoCunBeanDao.load(123456L);
		}
		//setContentView(R.layout.yidongnianhuiactivity);


		bao= (ImageView) findViewById(R.id.bao);
		wangluo = (TextView) findViewById(R.id.wangluo);
		//typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/xk.TTF");

		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/led.ttf");
		shi= (TextView) findViewById(R.id.shi);
		jian= (TextView) findViewById(R.id.jian);
		dian= (TextView) findViewById(R.id.dian);
		riqi= (TextView) findViewById(R.id.riqi);
		xingqi= (TextView) findViewById(R.id.xingqi);

		//使用字体
		riqi.setTypeface(typeFace);
		shi.setTypeface(typeFace);
		dian.setTypeface(typeFace);
		jian.setTypeface(typeFace);

		final String time=(System.currentTimeMillis()/1000)+"";
		shi.setText(DateUtils.timeShi(time));
		jian.setText(DateUtils.timeJian(time));
		riqi.setText(DateUtils.timesTwo(time));
		Animation animation = AnimationUtils.loadAnimation(YiDongNianHuiActivity.this, R.anim.alpha_anim);
		animation.setRepeatCount(-1);
		dian.setAnimation(animation);

		tanchuangList=new Vector<>();
		yuangongList = new Vector<>();
		TanChuangBean bean=new TanChuangBean();
		bean.setBytes(null);
		bean.setName(null);
		bean.setType(-2);
		bean.setTouxiang(null);
		tanchuangList.add(bean);

		TanChuangBean bean3=new TanChuangBean();
		bean3.setBytes(null);
		bean3.setName(null);
		bean3.setType(-2);
		bean3.setTouxiang(null);
		yuangongList.add(bean3);

		TanChuangBean bean4=new TanChuangBean();
		bean4.setBytes(null);
		bean4.setName(null);
		bean4.setType(-2);
		bean4.setTouxiang(null);
		yuangongList.add(bean4);

		Button button = (Button) findViewById(R.id.dddk);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chongzhi();

				startActivity(new Intent(YiDongNianHuiActivity.this, MainActivity2.class));
				finish();
			}
		});


		IntentFilter filter = null;


//		myReceiverFile=new MyReceiverFile();
//		filter = new IntentFilter();
//		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //接受外媒挂载过滤器
//		filter.addAction(Intent.ACTION_MEDIA_REMOVED);   //接受外媒挂载过滤器
//		filter.addDataScheme("file");
//		registerReceiver(myReceiverFile, filter);

		//实例化过滤器并设置要过滤的广播
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("duanxianchonglian");
		intentFilter.addAction("gxshipingdizhi");
		intentFilter.addAction("shoudongshuaxin");
		intentFilter.addAction("updateGonggao");
		intentFilter.addAction("updateTuPian");
		intentFilter.addAction("updateShiPing");
		intentFilter.addAction("delectShiPing");
		intentFilter.addAction("guanbi");
		intentFilter.addAction(Intent.ACTION_TIME_TICK);

		// 注册广播
		registerReceiver(myReceiver, intentFilter);

		dabg= (ImageView) findViewById(R.id.dabg);

		daoSession = MyApplication.getAppContext().getDaoSession().getMoShengRenBeanDao();
		daoSession.deleteAll();
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
//		recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
//			//用来标记是否正在向最后一个滑动
//			boolean isSlidingToLast = false;
//
//			@Override
//			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//				super.onScrollStateChanged(recyclerView, newState);
//				LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//				// 当不滚动时
//				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//					//获取最后一个完全显示的ItemPosition
//					int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
//					int totalItemCount = manager.getItemCount();
//
//					// 判断是否滚动到底部，并且是向右滚动
//					if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
//						//加载更多功能的代码
//						manager2.smoothScrollToPosition(recyclerView2,null,0);
//					}
//
//					if (lastVisibleItem==4 && !isSlidingToLast){
//						manager2.smoothScrollToPosition(recyclerView2,null,shiBieJiLuList.size()-1);
//					}
//
//				}
//			}
//
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//
//				//dx用来判断横向滑动方向，dy用来判断纵向滑动方向
//				//大于0表示正在向下滚动
//				//小于等于0表示停止或向上滚动
//				isSlidingToLast = dy > 0;
//			}
//		});
		//	mSurfaceView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


		manager = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false,this);
		recyclerView.setLayoutManager(manager);

		manager2 = new WrapContentLinearLayoutManager(YiDongNianHuiActivity.this,LinearLayoutManager.HORIZONTAL, false,this);
		manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView2.setLayoutManager(manager2);

		adapter = new MyAdapter(R.layout.tanchuang_item2,tanchuangList);

		adapter2 = new MyAdapter2(R.layout.shibiejilu_item,yuangongList);

		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

		recyclerView2.setAdapter(adapter2);
		recyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

		mainHandler = new Handler() {
			/*
             * @param msg
             */

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				//Log.d(TAG, "msg:" + msg);
			}

		};

		//Utils.initPermission(YiDongNianHuiActivity.this);
		initialTts();



		new Thread(new Runnable() {
			@Override
			public void run() {

				SystemClock.sleep(10000);
				sendBroadcast(new Intent(YiDongNianHuiActivity.this,AlarmReceiver.class));
				Log.d(TAG, "开启监听");

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					Thread.sleep(2000);
					Glide.get(YiDongNianHuiActivity.this).clearDiskCache();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							Glide.with(MyApplication.getAppContext())
									.load(FileUtil.createTmpDir(YiDongNianHuiActivity.this)+"dgx.jpg")
									.into(dabg);

						}
					});

				} catch (InterruptedException e) {
					e.printStackTrace();
				}


			}
		}).start();


//		File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"qiandao.txt");
//		// Make sure log file is exists
//		if (!logFile.exists()) {
//
//			try {
//				logFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//
//			}
//
//		}
//
//		FileOutputStream outputStream;
//
//		try {
//			outputStream = openFileOutput(logFile.getName(), Context.MODE_APPEND);
//			outputStream.write(builder.toString().getBytes());
//			outputStream.flush();
//			outputStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}



	}

	/**
	 * 初始化引擎，需要的参数均在InitConfig类里
	 * <p>
	 * DEMO中提供了3个SpeechSynthesizerListener的实现
	 * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
	 * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
	 * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
	 */
	protected void initialTts() {
		// 设置初始化参数
		SpeechSynthesizerListener listener = new UiMessageListener(mainHandler); // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
		Map<String, String> params = getParams();
		// appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
		InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
		synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程


	}

	/**
	 * 合成的参数，可以初始化时填写，也可以在合成前设置。
	 *
	 * @return
	 */
	protected Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		// 以下参数均为选填
		params.put(SpeechSynthesizer.PARAM_SPEAKER, baoCunBean.getBoyingren()+""); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
		params.put(SpeechSynthesizer.PARAM_VOLUME, "8"); // 设置合成的音量，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_SPEED, baoCunBean.getYusu()+"");// 设置合成的语速，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_PITCH, baoCunBean.getYudiao()+"");// 设置合成的语调，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);         // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
		// MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());

		return params;
	}
    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
           // toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }


	public  class MyAdapter extends BaseQuickAdapter<TanChuangBean,BaseViewHolder> {
		private View view;
		//	private List<TanChuangBean> d;
		private int p;
		private int vid;


		private MyAdapter(int layoutResId, List<TanChuangBean> data) {
			super(layoutResId, data);
			vid=layoutResId;
			//d=data;
		}



		@Override
		protected void convert(final BaseViewHolder helper, TanChuangBean item) {
			//Log.d(TAG, "动画执行");

			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(helper.itemView,"scaleY",0f,1f),
					ObjectAnimator.ofFloat(helper.itemView,"scaleX",0f,0f)
					//	ObjectAnimator.ofFloat(helper.itemView,"alpha",0f,1f)
			);
			animatorSet.setDuration(200);
			animatorSet.setInterpolator(new AccelerateInterpolator());
			animatorSet.addListener(new AnimatorListenerAdapter(){
				@Override public void onAnimationEnd(Animator animation) {

					AnimatorSet animatorSet2 = new AnimatorSet();
					animatorSet2.playTogether(
							ObjectAnimator.ofFloat(helper.itemView,"scaleX",0f,1f)
							//ObjectAnimator.ofFloat(helper.itemView,"alpha",0f,1f)
							//	ObjectAnimator.ofFloat(helper.itemView,"scaleY",1f,0.5f,1f)
					);
					animatorSet2.setInterpolator(new AccelerateInterpolator());
					animatorSet2.setDuration(400);
					animatorSet2.start();

				}
			});
			animatorSet.start();


//			ViewAnimator
//					.animate(helper.itemView)
//				//	.scale(0,1)
//					.alpha(0,1)
//					.duration(1000)
//					.start();

			RelativeLayout toprl= helper.getView(R.id.ffflll);


			TextView tishi_tv= helper.getView(R.id.tishi_tv);
			TextView tishi_tv2= helper.getView(R.id.ddd);
			ImageView imageView= helper.getView(R.id.touxiang);

			if (helper.getAdapterPosition()==0 ){
				toprl.setBackgroundColor(Color.parseColor("#00000000"));
				tishi_tv.setText("");
				tishi_tv2.setText("");
				imageView.setImageBitmap(null);

			}else {

			switch (item.getType()){
				case -1:
					//陌生人
					//	toprl.setBackgroundResource(R.drawable.tanchuang);


					break;
				case 0:
					//员工
					//	toprl.setBackgroundResource(R.drawable.tanchuang);
					//	tishi_tv.setText("欢迎老板");
					//	tishi_im.setBackgroundResource(R.drawable.tike);

					break;

				case 1:
					//访客

					view=toprl;
					toprl.setBackgroundResource(R.drawable.fgfgfg);
					String sa="热烈欢迎"+item.getName()+"莅临参观指导";
					StringBuilder sb=new StringBuilder();
					for(int i=0;i<sa.length();i++){
						sb.append((sa.charAt(i)));//依次加入sb中
						if((i+1)%(8)==0 &&((i+1)!=sa.length())){
							sb.append("\n");
						}
					}

					tishi_tv.setText(sb.toString());

					break;
				case 2:
					//VIP访客
					view=toprl;
					toprl.setBackgroundResource(R.drawable.fgfgfg);
					String sa1="热烈欢迎"+item.getName()+"莅临参观指导";
					StringBuilder sb1=new StringBuilder();
					for(int i=0;i<sa1.length();i++){
						sb1.append((sa1.charAt(i)));//依次加入sb中
						if((i+1)%(8)==0 &&((i+1)!=sa1.length())){
							sb1.append("\n");
						}
					}
					synthesizer.speak("热烈欢迎"+item.getName()+"莅临参观指导");
					tishi_tv.setText(sb1.toString());

					break;

			}


			if (item.getTouxiang()!=null){

				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					mFacePassHandler.getFaceImage(item.getTouxiang()).compress(Bitmap.CompressFormat.JPEG, 50, baos);
					byte[] datas = baos.toByteArray();
					Glide.with(MyApplication.getAppContext())
                            .load(datas)
                            //.transform(new GlideCircleTransform(MyApplication.getAppContext(),2,Color.parseColor("#ffffffff")))
                            .transform(new GlideRoundTransform(MyApplication.getAppContext(), 6))
                            .into(imageView);
				//	imageView.setImageBitmap();
				} catch (FacePassException e) {
					e.printStackTrace();
				}
			}else {
				Glide.with(MyApplication.getAppContext())
						.load(item.getBytes())
						//.transform(new GlideCircleTransform(MyApplication.getAppContext(),2,Color.parseColor("#ffffffff")))
						.transform(new GlideRoundTransform(MyApplication.getAppContext(), 6))
						.into(imageView);
			}
		}
		}


	}


	private  class MyAdapter2 extends BaseQuickAdapter<TanChuangBean,BaseViewHolder> {

		private MyAdapter2(int layoutResId, List<TanChuangBean> data) {
			super(layoutResId, data);

		}


		@Override
		protected void convert(BaseViewHolder helper, TanChuangBean item) {
			ViewAnimator
					.animate(helper.itemView)
					.scale(0,1)
//					.alpha(0,1)
					.duration(1000)
					.start();

			RelativeLayout toprl= helper.getView(R.id.ffflll);

			ImageView imageView= helper.getView(R.id.touxiang22);
			//ImageView tishi_im= helper.getView(R.id.tishi_im);
			TextView tishi_tv= helper.getView(R.id.tishi_tv);

			if (helper.getAdapterPosition()==0 || helper.getAdapterPosition()==1){
				toprl.setBackgroundColor(Color.parseColor("#00000000"));
				tishi_tv.setText("");
				imageView.setImageBitmap(null);

			}else {

				switch (item.getType()) {
					case -1:
						//陌生人
						//	toprl.setBackgroundResource(R.drawable.tanchuang);

						break;
					case 0:
						//员工

						toprl.setBackgroundResource(R.drawable.gfgfgf);
						tishi_tv.setText("员工\n" + item.getName());
						//Log.d("SheZhiActivity", "名字0"+item.getName());

						break;

					case 1:
						//访客
						//	toprl.setBackgroundResource(R.drawable.tanchuang);

						//richeng.setText("");
						//name.setText(item.getName());
						//autoScrollTextView.setText("欢迎你来本公司参观指导。");
						//Log.d("SheZhiActivity", "名字1"+item.getName());
						break;
					case 2:
						//VIP访客
						//toprl.setBackgroundResource(R.drawable.tanchuang);
						//	richeng.setText("");
						//	name.setText(item.getName());
						//autoScrollTextView.setText("欢迎VIP访客 "+item.getName()+" 来本公司指导工作。");
						//Log.d("SheZhiActivity", "名字2"+item.getName());
						break;


				}
				if (item.getTouxiang() != null) {

					Glide.with(MyApplication.getAppContext())
							.load(baoCunBean.getTouxiangzhuji() + item.getTouxiang())
							.transform(new GlideRoundTransform(MyApplication.getAppContext(), 6))
							//	.transform(new GlideCircleTransform(MyApplication.getAppContext()))
							.into((ImageView) helper.getView(R.id.touxiang22));
				} else{
					Glide.with(MyApplication.getAppContext())
							.load(item.getBytes())
							//.transform(new GlideCircleTransform(MyApplication.getAppContext()))
							.into((ImageView) helper.getView(R.id.touxiang22));

				}

			}


		}
	}

//	/**
//	 * 生成二维码
//	 * @param string 二维码中包含的文本信息
//	 * @param mBitmap logo图片
//	 * @param format  编码格式
//	 * [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
//	 * @throws WriterException
//	 */
//	private static final int IMAGE_HALFWIDTH = 1;//宽度值，影响中间图片大小
//	public Bitmap createCode(String string,Bitmap mBitmap, BarcodeFormat format)
//			throws WriterException {
//		Matrix m = new Matrix();
//		float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
//		float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
//		m.setScale(sx, sy);//设置缩放信息
//		//将logo图片按martix设置的信息缩放
//		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
//				mBitmap.getWidth(), mBitmap.getHeight(), m, false);
//		MultiFormatWriter writer = new MultiFormatWriter();
//		Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
//		hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
//		BitMatrix matrix = writer.encode(string, format, 600, 600, hst);//生成二维码矩阵信息
//		int width = matrix.getWidth();//矩阵高度
//		int height = matrix.getHeight();//矩阵宽度
//		int halfW = width/2;
//		int halfH = height/2;
//		int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
//		for (int y = 0; y < height; y++) {//从行开始迭代矩阵
//			for (int x = 0; x < width; x++) {//迭代列
//				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
//						&& y > halfH - IMAGE_HALFWIDTH
//						&& y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//			//记录图片每个像素信息
//					pixels[y * width + x] = mBitmap.getPixel(x - halfW
//							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);              } else {
//					if (matrix.get(x, y)) {//如果有黑块点，记录信息
//						pixels[y * width + x] = 0xff000000;//记录黑块信息
//					}
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		// 通过像素数组生成bitmap
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}

//	public class MyReceiverFile  extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, final Intent intent) {
//			String action = intent.getAction();
//
//			if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
//				//USB设备移除，更新UI
//				Log.d(TAG, "设备被移出");
//				TastyToast.makeText(YiDongNianHuiActivity.this,"设备被移出",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
//
//
//			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
//				//USB设备挂载，更新UI
//
//				String usbPath = intent.getDataString();//（usb在手机上的路径）
//				Log.d(TAG, "设备插入"+usbPath);
//				try {
//				//	file:///mnt/usb_storage/USB_DISK1
//					Bitmap bitmap = BitmapFactory.decodeFile(usbPath.split("file:///")[1]+File.separator+"dgx.png");
//
//					try {
//
//						File file = new File(FileUtil.createTmpDir(YiDongNianHuiActivity.this)+"dgx.jpg");
//						FileOutputStream out = new FileOutputStream(file);
//						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//						out.flush();
//						out.close();
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}catch (Exception e){
//					Log.d(TAG, e.getMessage()+"");
//				}
//
//
//			}
//
//		}
//	}


	private class MyReceiver  extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, final Intent intent) {
			//Log.d(TAG, "intent:" + intent.getAction());

			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				//ijkVideoView.requestFocus();
				String time=(System.currentTimeMillis()/1000)+"";
				shi.setText(DateUtils.timeShi(time));
				jian.setText(DateUtils.timeJian(time));
				riqi.setText(DateUtils.timesTwo(time));
				xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));


			}
			if (intent.getAction().equals("duanxianchonglian")) {

				//断线重连
				if (webSocketClient!=null) {

					//	Log.d(TAG, "进来1");

					if (!isLianJie) {
						//	Log.d(TAG, "进来2");
						try {
							baoCunBean = baoCunBeanDao.load(123456L);
							WebsocketPushMsg websocketPushMsg = new WebsocketPushMsg();
							websocketPushMsg.close();
							if (baoCunBean.getZhujiDiZhi() != null && baoCunBean.getShipingIP() != null) {
								websocketPushMsg.startConnection(baoCunBean.getZhujiDiZhi(), "rtsp://" + baoCunBean.getShipingIP() + ":554/user=admin_password=tlJwpbo6_channel=1_stream=0.sdp?real_stream");
							}

						} catch (Exception e) {
							Log.d(TAG, e.getMessage() + "aaa");

						}
					}
				}}
		}
	}

	// 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
	public static void getAllFiles(File root,String nameaaa){
		File files[] = root.listFiles();

		if(files != null){
			for (File f : files){
				if(f.isDirectory()){
					getAllFiles(f,nameaaa);
				}else{
					String name=f.getName();
					if (name.equals(nameaaa)){
						Log.d(TAG, "视频文件删除:" + f.delete());
					}
				}
			}
		}
	}

	private void link_fasong(String discernPhoto,int timestamp,long id,String name,String weizhi) {
		//Log.d(TAG, DateUtils.time(timestamp + "000"));
		OkHttpClient okHttpClient= MyApplication.getOkHttpClient();
		RequestBody body = new FormBody.Builder()
				.add("accountId",baoCunBean.getZhanghuId())
                .add("snapshotPhoto","")
                .add("discernPhoto",discernPhoto)
				.add("timestamp2",DateUtils.time(timestamp+"000"))
				.add("subjectId",id+"")
				.add("subjectName",name)
				.add("screenPosition",weizhi)
				.add("conference_id",baoCunBean.getHuiyiId())
				.build();
		Request.Builder requestBuilder = new Request.Builder()
				//.header("Content-Type", "application/json")
				.post(body)
				.url(baoCunBean.getHoutaiDiZhi()+"/appSave.do");
		//Log.d(TAG, baoCunBean.getHoutaiDiZhi() + "/appSave.do");
		// step 3：创建 Call 对象
		Call call = okHttpClient.newCall(requestBuilder.build());

		//step 4: 开始异步请求
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

				Log.d("AllConnects", "请求获取二维码失败"+e.getMessage());

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//Log.d("AllConnects", "请求获取二维码成功"+call.request().toString());
				//获得返回体
				//List<YongHuBean> yongHuBeanList=new ArrayList<>();
				//List<MoShengRenBean2> intlist=new ArrayList<>();
			//	intlist.addAll(moShengRenBean2List);
				try {


				ResponseBody body = response.body();
				String ss=body.string();
				  Log.d("AllConnects", "aa   "+ss);

//					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//					Gson gson=new Gson();
//						int code=jsonObject.get("resultCode").getAsInt();
//						if (code==0){
//					JsonArray array=jsonObject.get("data").getAsJsonArray();
//					int a=array.size();
//					for (int i=0;i<a;i++){
//						YongHuBean zhaoPianBean=gson.fromJson(array.get(i),YongHuBean.class);
//						moShengRenBean2List.add(zhaoPianBean);
//						//Log.d("VlcVideoActivity", zhaoPianBean.getSubjectId());
//					}

				//	}

				}catch (Exception e){
					Log.d("WebsocketPushMsg", e.getMessage());
				}

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if( KeyEvent.KEYCODE_MENU == keyCode ){  //如果按下的是菜单
			Log.d(TAG, "按下菜单键 ");
			chongzhi();
			//isTiaoZhuang=false;
			startActivity(new Intent(YiDongNianHuiActivity.this, SheZhiActivity.class));
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {

	//	mExplosionField = ExplosionField.attach2Window(YiDongNianHuiActivity.this);
	//	mExplosionField.expandExplosionBound(900,1200);

		if (netWorkStateReceiver == null) {
			netWorkStateReceiver = new NetWorkStateReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(netWorkStateReceiver, filter);
		}

		baoCunBean=baoCunBeanDao.load(123456L);


		checkGroup();
		initToast();
        /* 打开相机 */
		if (hasPermission()) {
			Log.d(TAG, "打开相机" + caneraManager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight));
		}
		adaptFrameLayout();

		super.onResume();
	}


	@Override
	public void onPause() {

		Log.d(TAG, "暂停");

		super.onPause();
	}



	@Override
	protected void onDestroy() {
		if (webSocketClient!=null){
			webSocketClient.close();
			webSocketClient=null;
		}

		Intent intent1=new Intent("guanbi333"); //关闭监听服务
		sendBroadcast(intent1);
		synthesizer.release();
		handler.removeCallbacksAndMessages(null);
		if (myReceiver != null)
			unregisterReceiver(myReceiver);
		unregisterReceiver(netWorkStateReceiver);
		//unregisterReceiver(myReceiverFile);

		mRecognizeThread.isInterrupt = true;

		mRecognizeThread.interrupt();
		if (requestQueue != null) {
			requestQueue.cancelAll("upload_detect_result_tag");
			requestQueue.cancelAll("handle_sync_request_tag");
			requestQueue.cancelAll("load_image_request_tag");
			requestQueue.stop();
		}

		if (caneraManager != null) {
			caneraManager.release();
		}
		if (mToastBlockQueue != null) {
			mToastBlockQueue.clear();
		}
		if (mAndroidHandler != null) {
			mAndroidHandler.removeCallbacksAndMessages(null);
		}

		if (mFacePassHandler != null) {
			mFacePassHandler.release();
		}


		super.onDestroy();

	}


//	private void changeSurfaceSize() {
//		// get screen size
//		int dw = Utils.getDisplaySize(getApplicationContext()).x;
//		int dh = Utils.getDisplaySize(getApplicationContext()).y;
//
////		RelativeLayout.LayoutParams re1 = (RelativeLayout.LayoutParams)surfaceview.getLayoutParams();
////
////		  re1.width=dw/3;
////		  re1.height = dh/5;
////
////		surfaceview.setLayoutParams(re1);
////		surfaceview.invalidate();
//		Log.d(TAG, baoCunBean.getShipingIP()+"hhhhh");
//		if (mediaPlayer != null) {
//			Log.d(TAG, baoCunBean.getShipingIP()+"gggg");
//
//			media = new Media(libvlc, Uri.parse("rtsp://"+baoCunBean.getShipingIP()+"/user=admin&password=&channel=1&stream=0.sdp"));
//			mediaPlayer.setMedia(media);
//			mediaPlayer.play();
//
//		}
//
//	}
//	/**
//	 * websocket接口返回face.image
//	 * image为base64编码的字符串
//	 * 将字符串转为可以识别的图片
//	 * @param imgStr
//	 * @return
//	 */
//	public Bitmap generateImage(String imgStr, int cont, WBWeiShiBieDATABean dataBean, Context context) throws Exception {
//		// 对字节数组字符串进行Base64解码并生成图片
//		if (imgStr == null) // 图像数据为空
//			return null;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			// Base64解码
//			final byte[][] b = {decoder.decodeBuffer(imgStr)};
//			for (int i = 0; i < b[0].length; ++i) {
//				if (b[0][i] < 0) {// 调整异常数据
//					b[0][i] += 256;
//				}
//			}
//			MoShengRenBean2 moShengRenBean2=new MoShengRenBean2();
//			moShengRenBean2.setId(dataBean.getTrack());
//			moShengRenBean2.setBytes(b[0]);
//			moShengRenBean2.setUrl("dd");
//
//			moShengRenBean2List.add(moShengRenBean2);
//
//				adapter.notifyDataSetChanged();
//
//
//
//
//
//			//   Bitmap bitmap= BitmapFactory.decodeByteArray(b[0],0, b[0].length);
//
//			//  Log.d("WebsocketPushMsg", "bitmap.getHeight():" + bitmap.getHeight());
//
//			// 生成jpeg图片
//			//  OutputStream out = new FileOutputStream(imgFilePath);
//			//   out.write(b);
//			//  out.flush();
//			//  out.close();
//
//
////			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////				@Override
////				public void onDismiss(DialogInterface dialog) {
////					Log.d("VlcVideoActivity", "Dialog销毁2");
////					b[0] =null;
////				}
////			});
//			//dialog.show();
//
//
//			return null;
//		} catch (Exception e) {
//			throw e;
//
//		}
//	}

	public  int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	public  int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	/**
	 * 识别消息推送
	 * 主机盒子端API ws://[主机ip]:9000/video
	 * 通过 websocket 获取 识别结果
	 * @author Wangshutao
	 */
	private class WebsocketPushMsg {
		/** * 识别消息推送
		 * @param wsUrl websocket接口 例如 ws://192.168.1.50:9000/video
		 * @param rtspUrl 视频流地址 门禁管理-门禁设备-视频流地址
		 *                例如 rtsp://192.168.0.100/live1.sdp
		 *                或者 rtsp://admin:admin12345@192.168.1.64/live1.sdp
		 *                或者 rtsp://192.168.1.103/user=admin&password=&channel=1&stream=0.sdp
		 *                或者 rtsp://192.168.1.100/live1.sdp
		 *                       ?__exper_tuner=lingyun&__exper_tuner_username=admin
		 *                       &__exper_tuner_password=admin&__exper_mentor=motion
		 *                       &__exper_levels=312,1,625,1,1250,1,2500,1,5000,1,5000,2,10000,2,10000,4,10000,8,10000,10
		 *                       &__exper_initlevel=6
		 * @throws URISyntaxException
		 * @throws
		 * @throws
		 *
		 *  ://192.168.2.52/user=admin&password=&channel=1&stream=0.sdp
		 *
		 *   rtsp://192.166.2.55:554/user=admin_password=tljwpbo6_channel=1_stream=0.sdp?real_stream
		 */
		private void startConnection(String wsUrl, String rtspUrl) throws URISyntaxException {
			//当视频流地址中出现&符号时，需要进行进行url编码
			if (rtspUrl.contains("&")){
				try {
					//Log.d("WebsocketPushMsg", "dddddttttttttttttttt"+rtspUrl);
					rtspUrl = URLEncoder.encode(rtspUrl,"UTF-8");

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					//Log.d("WebsocketPushMsg", e.getMessage());
				}
			}

			URI uri = URI.create(wsUrl + "?url=" + rtspUrl);
		//	Log.d("WebsocketPushMsg", "url="+uri);
			  webSocketClient = new WebSocketClient(uri) {
			//	private Vector vector=new Vector();

				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					isLianJie=true;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!YiDongNianHuiActivity.this.isFinishing())
								wangluo.setVisibility(View.GONE);
						}
					});
				}

				@Override
				public void onMessage(String ss) {

					JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
					Gson gson=new Gson();
					WBBean wbBean= gson.fromJson(jsonObject, WBBean.class);

					//Log.d("WebsocketPushMsg", wbBean.getType());
					if (wbBean.getType().equals("recognized")) {


						//识别//Log.d("WebsocketPushMsg", "识别出了")
						//String s = ss.replace("\\\\\\", "").replace("\"tag\": \"{\"", "\"tag\": {\"").replace("jpg\"}\"", "jpg\"}");
						//	JsonObject jsonObject5 = GsonUtil.parse(ss).getAsJsonObject();

						//	JsonObject jsonObject1 = jsonObject.get("data").getAsJsonObject();
						//JsonObject jsonObject2 = jsonObject5.get("person").getAsJsonObject();
						//   JsonObject jsonObject3=jsonObject.get("screen").getAsJsonObject();
						final ShiBieBean dataBean = gson.fromJson(jsonObject, ShiBieBean.class);

						try {
							//Log.d("WebsocketPushMsg", dataBean.getPerson().getSrc()+"kkkk");

							//	final WBShiBiePersonBean personBean = gson.fromJson(jsonObject2, WBShiBiePersonBean.class);
							//Log.d("WebsocketPushMsg", "personBean.getSubject_type():" + personBean.getSubject_type());

//						if (dataBean.getPerson().getSubject_type() == 2) {
//
//							//Log.d("WebsocketPushMsg", personBean.getAvatar());
//							runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//
//									stringVector.add("欢迎VIP访客 "+dataBean.getPerson().getName()+" 来访！ 来访时间: "+DateUtils.getCurrentTime_Today());
//									Collections.reverse(stringVector);
//
//									delet();
//
//									runOnUiThread(new Runnable() {
//										@Override
//										public void run() {
//											marqueeView.stopFlipping();
//											marqueeView.startWithList(stringVector);
//
//										}
//									});
////									VipDialog dialog=new VipDialog(VlcVideoActivity.this,personBean.getAvatar(),R.style.dialog_style,personBean.getName());
////									Log.d("WebsocketPushMsg", "vip");
////									dialog.show();
//								}
//							});
//
//
//						}else {
							if (dataBean.getPerson().getSubject_type()==2 || dataBean.getPerson().getSubject_type()==1){

								synthesizer.speak("热烈欢迎"+dataBean.getPerson().getName()+"莅临参观指导");

							}

							MoShengRenBean bean = new MoShengRenBean(dataBean.getPerson().getId(), "sss");

							daoSession.insert(bean);
							//更新右边上下滚动列表
							//shiBieJiLuBeanDao.insert(shiBieJiLuBean);
//								yuangongList.add(shiBieJiLuBean);
//								Message message = Message.obtain();
//								message.what = 19;
//								handler.sendMessage(message);

//								if (vector2.size()>30){
//									vector2.clear();
//									vector2.add("欢迎 "+dataBean.getPerson().getName()+" 签到:"+DateUtils.getCurrentTime_Today());
//								}
//
//							vector2.add("欢迎 "+dataBean.getPerson().getName()+" 签到:"+DateUtils.getCurrentTime_Today());
//								Collections.reverse(vector2);


//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										marqueeView2.stopFlipping();
//										marqueeView2.startWithList(vector2);
//
//									}
//								});


							//异步保存今天刷脸的人数

							Message message = new Message();
							message.arg1 = 1;
							message.obj = dataBean.getPerson();
							handler.sendMessage(message);

						}catch (Exception e){
							Log.d("WebsocketPushMsg", e.getMessage());
						}finally {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							try {
								daoSession.deleteByKey(dataBean.getPerson().getId());
								//	Log.d("WebsocketPushMsg", "删除");
							}catch (Exception e){
								Log.d("WebsocketPushMsg", e.getMessage());
							}
						}
					}

				}

				@Override
				public void onClose(int i, String s, boolean b) {
					isLianJie=false;

					Log.d("WebsocketPushMsg", "onClose"+i);
					runOnUiThread( new Runnable() {
						@Override
						public void run() {
							if (!YiDongNianHuiActivity.this.isFinishing()){
								wangluo.setVisibility(View.GONE);
								wangluo.setText("连接识别主机失败,重连中...");
							}

						}
					});
//
//					if (conntionHandler==null && runnable==null){
//						Looper.prepare();
//						conntionHandler=new Handler();
//						runnable=new Runnable() {
//							@Override
//							public void run() {
//
//								Intent intent=new Intent("duanxianchonglian");
//								sendBroadcast(intent);
//							}
//						};
//						conntionHandler.postDelayed(runnable,13000);
//						Looper.loop();
//					}

				}

				@Override
				public void onError(Exception e) {
					Log.d("WebsocketPushMsg", "onError"+e.getMessage());

				}
			};

			webSocketClient.connect();
		}
		private void close(){
//
//			if (conntionHandler!=null && runnable!=null){
//				conntionHandler.removeCallbacks(runnable);
//				conntionHandler=null;
//				runnable=null;
//
//			}
			if (webSocketClient!=null){
				webSocketClient.close();
				webSocketClient=null;
				System.gc();

			}

		}

	}



	private void creatUser(byte[] bytes, Long tt, String age) {
		//Log.d("WebsocketPushMsg", "创建用户");
		String fileName="tong"+System.currentTimeMillis()+".jpg";
		//通过bytes数组创建图片文件
		createFileWithByte(bytes,fileName,tt,age);
		//上传
	//	addPhoto(fileName);
	}

	/**
	 * 根据byte数组生成文件
	 *
	 * @param bytes
	 *            生成文件用到的byte数组
	 * @param age
	 */
	private void createFileWithByte(byte[] bytes, String filename, Long tt, String age) {
		/**
		 * 创建File对象，其中包含文件所在的目录以及文件的命名
		 */
		File file=null;
		String	sdDir = this.getFilesDir().getAbsolutePath();//获取跟目录
		makeRootDirectory(sdDir);

		// 创建FileOutputStream对象
		FileOutputStream outputStream = null;
		// 创建BufferedOutputStream对象
		BufferedOutputStream bufferedOutputStream = null;

		try {
			file = new File(sdDir +File.separator+ filename);
			// 在文件系统中根据路径创建一个新的空文件
		//	file2.createNewFile();
		//	Log.d(TAG, file.createNewFile()+"");

			// 获取FileOutputStream对象
			outputStream = new FileOutputStream(file);
			// 获取BufferedOutputStream对象
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			// 往文件所在的缓冲输出流中写byte数据
			bufferedOutputStream.write(bytes);
			// 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
			bufferedOutputStream.flush();
			//上传文件


		} catch (Exception e) {
			// 打印异常信息
			//Log.d(TAG, "ssssssssssssssssss"+e.getMessage());
		} finally {
			// 关闭创建的流对象
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}






	public class NetWorkStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			//检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

				//获得ConnectivityManager对象
				ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

				//获取ConnectivityManager对象对应的NetworkInfo对象
				//以太网
				NetworkInfo wifiNetworkInfo1 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
				//获取WIFI连接的信息
				NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				//获取移动数据连接的信息
				NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (wifiNetworkInfo1.isConnected() || wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()){
					wangluo.setVisibility(View.GONE);

				}else {
					isLianJie=false;

					wangluo.setVisibility(View.VISIBLE);
				}


//				if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
//				} else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//				}
//API大于23时使用下面的方式进行网络监听
			}else {

				Log.d(TAG, "API23");
				//获得ConnectivityManager对象
				ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

				//获取所有网络连接的信息
				Network[] networks = connMgr.getAllNetworks();
				//用于存放网络连接信息
				StringBuilder sb = new StringBuilder();
				//通过循环将网络信息逐个取出来
				Log.d(TAG, "networks.length:" + networks.length);
				if (networks.length==0){
					isLianJie=false;
					wangluo.setVisibility(View.VISIBLE);
				}
				for (int i=0; i < networks.length; i++){
					//获取ConnectivityManager对象对应的NetworkInfo对象
					NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

					if (networkInfo.isConnected()){
						wangluo.setVisibility(View.GONE);

					}
				}

			}
		}
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
		Log.d("WJY", FacePassHandler.getVersion());
	}

	private void initFaceHandler() {

		new Thread() {
			@Override
			public void run() {
				while (true && !isFinishing()) {
					if (FacePassHandler.isAvailable()) {
//                        Log.d(DEBUG_TAG, "start to build FacePassHandler");
                         /* FacePass SDK 所需模型， 模型在assets目录下 */
						trackModel = FacePassModel.initModel(getApplicationContext().getAssets(), "tracker.DT1.4.1.dingding.20180315.megface2.9.bin");
						poseModel = FacePassModel.initModel(getApplicationContext().getAssets(), "pose.alfa.tiny.170515.bin");
						blurModel = FacePassModel.initModel(getApplicationContext().getAssets(), "blurness.v5.l2rsmall.bin");
						livenessModel = FacePassModel.initModel(getApplicationContext().getAssets(), "panorama.facepass.offline.180312.bin");
						searchModel = FacePassModel.initModel(getApplicationContext().getAssets(), "feat.small.facepass.v2.9.bin");
						detectModel = FacePassModel.initModel(getApplicationContext().getAssets(), "detector.mobile.v5.fast.bin");
                        /* SDK 配置 */
//                        float searchThreshold = 75f;
//                        float livenessThreshold = 70f;
//                        boolean livenessEnabled = true;
//                        int faceMinThreshold = 150;
//                        FacePassPose poseThreshold = new FacePassPose(30f, 30f, 30f);
//                        float blurThreshold = 0.2f;
//                        float lowBrightnessThreshold = 70f;
//                        float highBrightnessThreshold = 210f;
//                        float brightnessSTDThreshold = 60f;
//                        int retryCount = 2;
						float searchThreshold = 75f;
						float livenessThreshold = 70f;
						boolean livenessEnabled = true;
						int faceMinThreshold = 50;
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
									trackModel, poseModel, blurModel, livenessModel, searchModel, detectModel);
                            /* 创建SDK实例 */
							mFacePassHandler = new FacePassHandler(config);
							checkGroup();
						} catch (FacePassException e) {
							e.printStackTrace();
							Log.d(DEBUG_TAG, "FacePassHandler is null");
							return;
						}

						try {
							if (mFacePassHandler!=null) {
								boolean isSuccess = mFacePassHandler.createLocalGroup(group_name);

								Log.d("ffffffff", "循环完了？创建组状态:" + isSuccess);
							}
						} catch (FacePassException e) {
							Log.d("ffffffff", e.getMessage()+"创建组异常");

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




	private void checkGroup() {
		if (mFacePassHandler == null) {
			return;
		}
		String[] localGroups = mFacePassHandler.getLocalGroups();
		isLocalGroupExist = false;
		if (localGroups == null || localGroups.length == 0) {
//			faceView.post(new Runnable() {
//				@Override
//				public void run() {
//					toast("请创建" + group_name + "底库");
//				}
//			});
			return;
		}
		for (String group : localGroups) {
			if (group_name.equals(group)) {
				isLocalGroupExist = true;
			}
		}
		if (!isLocalGroupExist) {
//			faceView.post(new Runnable() {
//				@Override
//				public void run() {
//					toast("请创建" + group_name + "底库");
//				}
//			});
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

         /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
		FacePassDetectionResult detectionResult = null;
		try {
			detectionResult = mFacePassHandler.feedFrame(image);
		} catch (FacePassException e) {
			e.printStackTrace();
		}

		if (detectionResult == null || detectionResult.faceList.length == 0) {
            /* 当前帧没有检出人脸 */
		//	faceView.clear();
		//	faceView.invalidate();
		} else {
            /* 将识别到的人脸在预览界面中圈出，并在上方显示人脸位置及角度信息 */
			showFacePassFace(detectionResult.faceList);
		}

		if (SDK_MODE == FacePassSDKMode.MODE_ONLINE) {
             /*抓拍版模式*/
			if (detectionResult != null && detectionResult.message.length != 0) {
            /* 构建http请求 */
				FacePassRequest request = new FacePassRequest(recognize_url, detectionResult, new com.android.volley.Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(DEBUG_TAG, String.format("%s", response));
						try {
							JSONObject jsresponse = new JSONObject(response);
							int code = jsresponse.getInt("code");
							if (code != 0) {
								Log.e(DEBUG_TAG, String.format("error code: %d", code));
								return;
							}
                        /* 将服务器返回的结果交回SDK进行处理来获得识别结果 */
							FacePassRecognitionResult[] result = null;
							try {
								Log.i("lengthlength", "length is " + jsresponse.getString("data").getBytes().length);
								result = mFacePassHandler.decodeResponse(jsresponse.getString("data").getBytes());
							} catch (FacePassException e) {
								e.printStackTrace();
								return;
							}
							if (result == null || result.length == 0) {
								return;
							}

							for (FacePassRecognitionResult res : result) {
								String faceToken = new String(res.faceToken);
								if (FacePassRecognitionResultType.RECOG_OK == res.facePassRecognitionResultType) {
									getFaceImageByFaceToken(res.trackId, faceToken);
								}
								showRecognizeResult(res.trackId, res.detail.searchScore, res.detail.livenessScore, FacePassRecognitionResultType.RECOG_OK == res.facePassRecognitionResultType);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new com.android.volley.Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(DEBUG_TAG, "volley error response");
						if (error.networkResponse != null) {
							faceEndTextView.append(String.format("network error %d", error.networkResponse.statusCode));
						} else {
							String errorMessage = error.getClass().getSimpleName();
							faceEndTextView.append("network error" + errorMessage);
						}
						faceEndTextView.append("\n");
					}
				});
				request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				Log.d(DEBUG_TAG, "request add");
				request.setTag("upload_detect_result_tag");
				requestQueue.add(request);
			}
		} else {
			//Log.d("MainActivity", "离线");
               /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
			if (detectionResult != null && detectionResult.message.length != 0) {
				//Log.d("ffffff", "mDetectResultQueue.offer");
				mDetectResultQueue.offer(detectionResult.message);
				Log.d("ffffff", "队列中的数量" + mDetectResultQueue.size());
				Log.d("ffffff", "队列中的数量" + mDetectResultQueue.size());
			}
		}
	}

	private class RecognizeThread extends Thread {

		boolean isInterrupt;

		@Override
		public void run() {
			while (!isInterrupt) {
				try {
					Log.d("ffffff", "获取队列中的数量 " + mDetectResultQueue.size());
					byte[] detectionResult = mDetectResultQueue.take();
					Log.d("fffff", "detectionResult.length:" + detectionResult.length);
					//Log.d("ffffff", "mDetectResultQueue.isLocalGroupExist");
					if (isLocalGroupExist) {

						FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult);
						if (recognizeResult != null && recognizeResult.length > 0) {
							for (FacePassRecognitionResult result : recognizeResult) {
								String faceToken = new String(result.faceToken);
								Log.d("ffffff", "result.facePassRecognitionResultType:" + result.facePassRecognitionResultType);
								if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
									//
									getFaceImageByFaceToken(result.trackId, faceToken);

								}
								showRecognizeResult(result.trackId, result.detail.searchScore, result.detail.livenessScore, !TextUtils.isEmpty(faceToken));

							}
						}
					}
				} catch (InterruptedException e) {
					Log.d("ffffff", e.getMessage()+"4545");
				} catch (FacePassException e) {
					Log.d("ffffff", e.getMessage()+"yyyy");
				}
			}
		}

		@Override
		public void interrupt() {
			isInterrupt = true;
			super.interrupt();
		}
	}


	private void showRecognizeResult(final long trackId, final float searchScore, final float livenessScore, final boolean isRecognizeOK) {
		mAndroidHandler.post(new Runnable() {
			@Override
			public void run() {

				faceEndTextView.append("ID = " + trackId + (isRecognizeOK ? "识别成功" : "识别失败") + "\n");
				faceEndTextView.append("识别分 = " + searchScore + "\n");
				faceEndTextView.append("活体分 = " + livenessScore + "\n");
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);

			}
		});

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


		int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * Surface.ROTATION_90;
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
		setContentView(R.layout.yidongnianhuiactivity);

		final TextView ruku= (TextView) findViewById(R.id.txt_facepass_sdk_name);
		ruku.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, usbPath+"ddddddd ");
				dialog=new JiaZaiDialog(YiDongNianHuiActivity.this);
				dialog.show();

				if (!isRuku) {

					if (mFacePassHandler==null){
						toast("mFacePassHandler为空");
						dialog.setTiShi("     读取数据失败");
						return;
					}

					if (usbPath==null){
						dialog.setTiShi("     读取数据失败");
						toast("请插拔一下USB");
						return;
					}

					isRuku=true;

					new Thread(new Runnable() {
						@Override
						public void run() {

							try {

								List<String> strings=new ArrayList<>();
								final StringBuilder stringBuffer=new StringBuilder();

								final List<String> jsonArray= FileUtil.getAllFiles(usbPath+ File.separator + "入库照片",strings);

								if (jsonArray!=null){
									final int size= jsonArray.size();
									Log.d("ffffff", "size:" + size);

									for (int i=0;i<size;i++){
										final String pSte=jsonArray.get(i);

										synchronized (lock) {

											final int finalI = i;
											new Thread(new Runnable() {
												@Override
												public void run() {
													try {

														Bitmap bitmap = BitmapFactory.decodeFile(pSte);
														if (bitmap!=null) {
															FacePassAddFaceResult result=null;
															try {
																result = mFacePassHandler.addFace(bitmap);

															}catch (Exception e){
																synchronized (lock) {
																	lock.notify();
																}
																return;
															}

															if (result != null) {
																runOnUiThread(new Runnable() {
																	@Override
																	public void run() {
																		if (dialog!=null)
																			dialog.setProgressBar(((finalI / (float) size) * 100));

																	}
																});

																if (result.result != 0) {
																	//失败的记录起来
																	stringBuffer.append(jsonArray.get(finalI)).append("\n");
																	si++;
																} else {

																	boolean b = mFacePassHandler.bindGroup(group_name, result.faceToken);
																	Log.d("ffffffffffff", "绑定状态" + b);
																	//一些后续操作...

																}
															}
														}
														synchronized (lock) {
															lock.notify();
														}


													} catch (FacePassException e) {
														e.printStackTrace();

														synchronized (lock) {
															lock.notify();
														}
													}
												}
											}).start();

											lock.wait();

										}

									}
									if (stringBuffer.length()>0){
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												String ss=stringBuffer.toString();
												dialog.setTiShi("以下数据导入失败:\n共"+si+"条,已保存到本地根目录\n"+ss);
												try {
													FileUtil.savaFileToSD("失败记录"+System.currentTimeMillis(),ss);
												} catch (Exception e) {
													e.printStackTrace();
												}

												si=0;
											}
										});

									}else {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												dialog.setTiShi("全部入库成功");
											}
										});

									}

									isRuku=false;
									//循环完了


								}else {
									isRuku=false;
									dialog.setTiShi("    读取数据失败");
								}

//                                File file = new File(FileUtil.createTmpDir(YiDongNianHuiActivity.this) + "dgx.jpg");
//                                FileOutputStream out = new FileOutputStream(file);
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                                out.flush();
//                                out.close();

							} catch (Exception e) {
								Log.d("ffffff", e.getMessage() + "");

							}

						}
					}).start();

				}
			}
		});

		mSyncGroupBtn = (ImageView) findViewById(R.id.btn_group_name);
		mSyncGroupBtn.setOnClickListener(this);

		mFaceOperationBtn = (ImageView) findViewById(R.id.btn_face_operation);
		mFaceOperationBtn.setOnClickListener(this);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		heightPixels = displayMetrics.heightPixels;
		widthPixels = displayMetrics.widthPixels;
		SettingVar.mHeight = heightPixels;
		SettingVar.mWidth = widthPixels;
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		AssetManager mgr = getAssets();
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
        /* 初始化界面 */
		faceEndTextView = (TextView) this.findViewById(R.id.tv_meg2);
		faceEndTextView.setTypeface(tf);
		//faceView = (FaceView) this.findViewById(R.id.fcview);
		settingButton = (Button) this.findViewById(R.id.settingid);
		settingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long curTime = System.currentTimeMillis();
				long durTime = curTime - mLastClickTime;
				mLastClickTime = curTime;
				if (durTime < CLICK_INTERVAL) {
					++mSecretNumber;
					if (mSecretNumber == 5) {
						Intent intent = new Intent(YiDongNianHuiActivity.this, SettingActivity.class);
						startActivity(intent);
						YiDongNianHuiActivity.this.finish();
					}
				} else {
					mSecretNumber = 0;
				}
			}
		});
		SettingVar.cameraSettingOk = false;
		ll = (LinearLayout) this.findViewById(R.id.ll);
		ll.getBackground().setAlpha(100);
		visible = (Button) this.findViewById(R.id.visible);
		visible.setBackgroundResource(R.drawable.debug);
		visible.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonFlag == 0) {
					ll.setVisibility(View.VISIBLE);
					if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
						visible.setBackgroundResource(R.drawable.down);
					} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
						visible.setBackgroundResource(R.drawable.right);
					}
					buttonFlag = 1;
				} else if (buttonFlag == 1) {
					buttonFlag = 0;
					if (SettingVar.isButtonInvisible)
						ll.setVisibility(View.INVISIBLE);
					else
						ll.setVisibility(View.GONE);
					visible.setBackgroundResource(R.drawable.debug);
				}

			}
		});
		caneraManager = new CameraManager();
		cameraView = (CameraPreview) findViewById(R.id.preview);
		caneraManager.setPreviewDisplay(cameraView);
		frameLayout = (FrameLayout) findViewById(R.id.frame);
        /* 注册相机回调函数 */
		caneraManager.setListener(this);

		mSDKModeBtn=(Button)findViewById(R.id.btn_mode_switch);
		mSDKModeBtn.setText(SDK_MODE.toString());
		mSDKModeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SDK_MODE == FacePassSDKMode.MODE_OFFLINE) {
					SDK_MODE = FacePassSDKMode.MODE_ONLINE;
					recognize_url = "http://" + serverIP_online + ":8080/api/service/recognize/v1";
					serverIP = serverIP_online;
					mSDKModeBtn.setText(SDK_MODE.toString());
				} else {
					SDK_MODE = FacePassSDKMode.MODE_OFFLINE;
					serverIP = serverIP_offline;
					mSDKModeBtn.setText(SDK_MODE.toString());
				}
			}
		});

	}


	@Override
	protected void onStop() {
		SettingVar.isButtonInvisible = false;
		mToastBlockQueue.clear();
		mDetectResultQueue.clear();
		if (caneraManager != null) {
			caneraManager.release();
		}
		if (dialog!=null){
			dialog.dismiss();
			dialog=null;
		}
		super.onStop();
	}

	@Override
	protected void onRestart() {
		//faceView.clear();
		//faceView.invalidate();
		super.onRestart();
	}



	private void showFacePassFace(FacePassFace[] detectResult) {
		//faceView.clear();
		for (FacePassFace face : detectResult) {
			boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
			StringBuilder faceIdString = new StringBuilder();
			faceIdString.append("ID = ").append(face.trackId);
			SpannableString faceViewString = new SpannableString(faceIdString);
			faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			StringBuilder faceRollString = new StringBuilder();
			faceRollString.append("旋转: ").append((int) face.pose.roll).append("°");
			StringBuilder facePitchString = new StringBuilder();
			facePitchString.append("上下: ").append((int) face.pose.pitch).append("°");
			StringBuilder faceYawString = new StringBuilder();
			faceYawString.append("左右: ").append((int) face.pose.yaw).append("°");
			String blur = String.valueOf(face.blur);
			StringBuilder faceBlurString = new StringBuilder();
			faceBlurString.append("模糊: ").append(blur.substring(0, 4));
			Matrix mat = new Matrix();
			int w = cameraView.getMeasuredWidth();
			int h = cameraView.getMeasuredHeight();

			int cameraHeight = caneraManager.getCameraheight();
			int cameraWidth = caneraManager.getCameraWidth();

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
			RectF srect = new RectF(left, top, right, bottom);

			mat.mapRect(drect, srect);
//			faceView.addRect(drect);
//			faceView.addId(faceIdString.toString());
//			faceView.addRoll(faceRollString.toString());
//			faceView.addPitch(facePitchString.toString());
//			faceView.addYaw(faceYawString.toString());
//			faceView.addBlur(faceBlurString.toString());
		}
		//faceView.invalidate();
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

	private static final int REQUEST_CODE_CHOOSE_PICK = 1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_group_name:
				showSyncGroupDialog();
				break;
			case R.id.btn_face_operation:
				showAddFaceDialog();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			//从相册选取照片后读取地址
			case REQUEST_CODE_CHOOSE_PICK:
				if (resultCode == RESULT_OK) {
					String path = "";
					Uri uri = data.getData();
					String[] pojo = {MediaStore.Images.Media.DATA};
					CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null, null, null);
					Cursor cursor = cursorLoader.loadInBackground();
					if (cursor != null) {
						cursor.moveToFirst();
						path = cursor.getString(cursor.getColumnIndex(pojo[0]));
					}
					if (!TextUtils.isEmpty(path) && "file".equalsIgnoreCase(uri.getScheme())) {
						path = uri.getPath();
					}
					if (TextUtils.isEmpty(path)) {
						try {
							path = FileUtil.getPath(getApplicationContext(), uri);
						} catch (Exception e) {
						}
					}
					if (TextUtils.isEmpty(path)) {
						toast("图片选取失败！");
						return;
					}
					if (!TextUtils.isEmpty(path) && mFaceOperationDialog != null && mFaceOperationDialog.isShowing()) {
						EditText imagePathEdt = (EditText) mFaceOperationDialog.findViewById(R.id.et_face_image_path);
						imagePathEdt.setText(path);
					}
				}
				break;
		}
	}

	private void getFaceImageByFaceToken(final long trackId, String faceToken) {
		if (TextUtils.isEmpty(faceToken)) {
			return;
		}

		final String faceUrl = "http://" + serverIP + ":8080/api/image/v1/query?face_token=" + faceToken;

		final Bitmap cacheBmp = mImageCache.getBitmap(faceUrl);
		if (cacheBmp != null) {
			mAndroidHandler.post(new Runnable() {
				@Override
				public void run() {
					Log.i(DEBUG_TAG, "getFaceImageByFaceToken cache not null");
					showToast("ID = " + String.valueOf(trackId), Toast.LENGTH_SHORT, true, cacheBmp);
				}
			});
			return;
		} else {
			try {


			//	final Bitmap bitmap = mFacePassHandler.getFaceImage(faceToken.getBytes());
				Log.i(DEBUG_TAG, "11111111111111111111111");
				ShiBieBean.PersonBeanSB pb=new ShiBieBean.PersonBeanSB();
				pb.setName("测试");
				pb.setId(System.currentTimeMillis());
				pb.setSubject_type(2);
				pb.setAvatar(faceToken.getBytes());

				Message message = new Message();
				message.arg1 = 1;
				message.obj = pb;
				handler.sendMessage(message);


//				mAndroidHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						Log.i(DEBUG_TAG, "getFaceImageByFaceToken cache is null");
//						showToast("ID = " + String.valueOf(trackId), Toast.LENGTH_SHORT, true, bitmap);
//					}
//				});
//				if (bitmap != null) {
//					return;
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		ByteRequest request = new ByteRequest(com.android.volley.Request.Method.GET, faceUrl, new com.android.volley.Response.Listener<byte[]>() {
			@Override
			public void onResponse(byte[] response) {

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0, response.length, options);
				mImageCache.putBitmap(faceUrl, bitmap);
				showToast("ID = " + String.valueOf(trackId), Toast.LENGTH_SHORT, true, bitmap);
				Log.i(DEBUG_TAG, "getFaceImageByFaceToken response ");
			}
		}, new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i(DEBUG_TAG, "image load failed ! ");
			}
		});
		request.setTag("load_image_request_tag");
		requestQueue.add(request);
	}


	/*同步底库操作*/
	private void showSyncGroupDialog() {

		if (mSyncGroupDialog != null && mSyncGroupDialog.isShowing()) {
			mSyncGroupDialog.hide();
			requestQueue.cancelAll("handle_sync_request_tag");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_sync_groups, null);

		final EditText groupNameEt = (EditText) view.findViewById(R.id.et_group_name);
		final TextView syncDataTv = (TextView) view.findViewById(R.id.tv_show_sync_data);

		Button obtainGroupsBtn = (Button) view.findViewById(R.id.btn_obtain_groups);
		Button createGroupBtn = (Button) view.findViewById(R.id.btn_submit);
		ImageView closeWindowIv = (ImageView) view.findViewById(R.id.iv_close);

		final Button handleSyncDataBtn = (Button) view.findViewById(R.id.btn_handle_sync_data);
		final ListView groupNameLv = (ListView) view.findViewById(R.id.lv_group_name);
		final ScrollView syncScrollView = (ScrollView) view.findViewById(R.id.sv_handle_sync_data);

		final GroupNameAdapter groupNameAdapter = new GroupNameAdapter();

		builder.setView(view);
		closeWindowIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSyncGroupDialog.dismiss();
			}
		});

		obtainGroupsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String[] groups = mFacePassHandler.getLocalGroups();
				if (groups != null && groups.length > 0) {
					List<String> data = Arrays.asList(groups);
					syncScrollView.setVisibility(View.GONE);
					groupNameLv.setVisibility(View.VISIBLE);
					groupNameAdapter.setData(data);
					groupNameLv.setAdapter(groupNameAdapter);
				} else {
					toast("groups is null !");
				}
			}
		});

		createGroupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String groupName = groupNameEt.getText().toString();
				if (TextUtils.isEmpty(groupName)) {
					toast("please input group name ！");
					return;
				}
				boolean isSuccess = false;
				try {
					isSuccess = mFacePassHandler.createLocalGroup(groupName);
				} catch (FacePassException e) {
					e.printStackTrace();
				}
				toast("create group " + isSuccess);
				if (isSuccess && group_name.equals(groupName)) {
					isLocalGroupExist = true;
				}

			}
		});

		handleSyncDataBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String requestData = mFacePassHandler.getSyncRequestData();
				getHandleSyncGroupData(requestData);
			}

			private void getHandleSyncGroupData(final String paramsValue) {


				ByteRequest request = new ByteRequest(com.android.volley.Request.Method.POST, "http://" + serverIP + ":8080/api/service/sync/v1", new com.android.volley.Response.Listener<byte[]>() {
					@Override
					public void onResponse(byte[] response) {
						if (mFacePassHandler == null) {

							return;
						}
						FacePassSyncResult result3 = null;
						try {
							result3 = mFacePassHandler.handleSyncResultData(response);
						} catch (FacePassException e) {
							e.printStackTrace();
						}

						if (result3 == null || result3.facePassGroupSyncDetails == null) {
							toast("handle sync result is failed!");
							return;
						}

						StringBuilder builder = new StringBuilder();
						for (FacePassGroupSyncDetail detail : result3.facePassGroupSyncDetails) {
							builder.append("========" + detail.groupName + "==========" + "\r\n");
							builder.append("groupName :" + detail.groupName + " \r\n");
							builder.append("facetokenadded :" + detail.faceAdded + " \r\n");
							builder.append("facetokendeleted :" + detail.faceDeleted + " \r\n");
							builder.append("resultcode :" + detail.result + " \r\n");
						}
						syncDataTv.setText(builder);
						syncScrollView.setVisibility(View.VISIBLE);
						groupNameLv.setVisibility(View.GONE);
					}
				}, new com.android.volley.Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				}) {
					@Override
					public byte[] getBody() throws AuthFailureError {

						return paramsValue.getBytes();
					}
				};
				request.setTag("handle_sync_request_tag");
				requestQueue.add(request);
			}
		});

		groupNameAdapter.setOnItemDeleteButtonClickListener(new GroupNameAdapter.ItemDeleteButtonClickListener() {
			@Override
			public void OnItemDeleteButtonClickListener(int position) {
				List<String> groupNames = groupNameAdapter.getData();
				if (groupNames == null) {
					return;
				}
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String groupName = groupNames.get(position);
				boolean isSuccess = false;
				try {
					isSuccess = mFacePassHandler.deleteLocalGroup(groupName);
				} catch (FacePassException e) {
					e.printStackTrace();
				}
				if (isSuccess) {
					String[] groups = mFacePassHandler.getLocalGroups();

					if (group_name.equals(groupName)) {
						isLocalGroupExist = false;
					}
					if (groups != null) {
						groupNameAdapter.setData(Arrays.asList(groups));
						groupNameAdapter.notifyDataSetChanged();
					}
					toast("删除成功!");
				} else {
					toast("删除失败!");

				}
			}

		});

		mSyncGroupDialog = builder.create();

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高

		WindowManager.LayoutParams attributes = mSyncGroupDialog.getWindow().getAttributes();
		attributes.height = d.getHeight();
		attributes.width = d.getWidth();
		mSyncGroupDialog.getWindow().setAttributes(attributes);

		mSyncGroupDialog.show();

	}

	private AlertDialog mFaceOperationDialog;

	private void showAddFaceDialog() {

		if (mFaceOperationDialog != null && !mFaceOperationDialog.isShowing()) {
			mFaceOperationDialog.show();
			return;
		}
		if (mFaceOperationDialog != null && mFaceOperationDialog.isShowing()) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_face_operation, null);
		builder.setView(view);

		final EditText faceImagePathEt = (EditText) view.findViewById(R.id.et_face_image_path);
		final EditText faceTokenEt = (EditText) view.findViewById(R.id.et_face_token);
		final EditText groupNameEt = (EditText) view.findViewById(R.id.et_group_name);

		Button choosePictureBtn = (Button) view.findViewById(R.id.btn_choose_picture);
		Button addFaceBtn = (Button) view.findViewById(R.id.btn_add_face);
		Button getFaceImageBtn = (Button) view.findViewById(R.id.btn_get_face_image);
		Button deleteFaceBtn = (Button) view.findViewById(R.id.btn_delete_face);
		Button bindGroupFaceTokenBtn = (Button) view.findViewById(R.id.btn_bind_group);
		Button getGroupInfoBtn = (Button) view.findViewById(R.id.btn_get_group_info);

		ImageView closeIv = (ImageView) view.findViewById(R.id.iv_close);

		final ListView groupInfoLv = (ListView) view.findViewById(R.id.lv_group_info);

		final FaceTokenAdapter faceTokenAdapter = new FaceTokenAdapter();

		groupNameEt.setText(group_name);

		closeIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFaceOperationDialog.dismiss();
			}
		});

		choosePictureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
				try {
					startActivityForResult(intentFromGallery, REQUEST_CODE_CHOOSE_PICK);
				} catch (ActivityNotFoundException e) {
					toast("请安装相册或者文件管理器");
				}
			}
		});

		addFaceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String imagePath = faceImagePathEt.getText().toString();
				if (TextUtils.isEmpty(imagePath)) {
					toast("请输入正确的图片路径！");
					return;
				}

				File imageFile = new File(imagePath);
				if (!imageFile.exists()) {
					toast("图片不存在 ！");
					return;
				}

				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

				try {
					FacePassAddFaceResult result = mFacePassHandler.addFace(bitmap);
					if (result != null) {
						if (result.result == 0) {
							toast("成功！");
							faceTokenEt.setText(new String(result.faceToken));
						} else if (result.result == 1) {
							toast("没脸！");
						} else {
							toast("质量不行！");
						}
					}
				} catch (FacePassException e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
			}
		});

		getFaceImageBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				try {
					byte[] faceToken = faceTokenEt.getText().toString().getBytes();
					Bitmap bmp = mFacePassHandler.getFaceImage(faceToken);
					final ImageView iv = (ImageView) findViewById(R.id.imview);
					iv.setImageBitmap(bmp);
					iv.setVisibility(View.VISIBLE);
					iv.postDelayed(new Runnable() {
						@Override
						public void run() {
							iv.setVisibility(View.GONE);
							iv.setImageBitmap(null);
						}
					}, 2000);
					mFaceOperationDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
			}
		});

		deleteFaceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				boolean b = false;
				try {
					byte[] faceToken = faceTokenEt.getText().toString().getBytes();
					b = mFacePassHandler.deleteFace(faceToken);
					if (b) {
						String groupName = groupNameEt.getText().toString();
						if (TextUtils.isEmpty(groupName)) {
							toast("group name  is null ！");
							return;
						}
						byte[][] faceTokens = mFacePassHandler.getLocalGroupInfo(groupName);
						List<String> faceTokenList = new ArrayList<>();
						if (faceTokens != null && faceTokens.length > 0) {
							for (int j = 0; j < faceTokens.length; j++) {
								if (faceTokens[j].length > 0) {
									faceTokenList.add(new String(faceTokens[j]));
								}
							}

						}
						faceTokenAdapter.setData(faceTokenList);
						groupInfoLv.setAdapter(faceTokenAdapter);
					}
				} catch (FacePassException e) {
					e.printStackTrace();
					toast(e.getMessage());
				}

				String result = b ? "success " : "failed";
				toast("delete face " + result);
				Log.d(DEBUG_TAG, "delete face  " + result);

			}
		});

		bindGroupFaceTokenBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				Log.d("ffffff", faceTokenEt.getText().toString());
				byte[] faceToken = faceTokenEt.getText().toString().getBytes();

				String groupName = groupNameEt.getText().toString();

				if (faceToken == null || faceToken.length == 0 || TextUtils.isEmpty(groupName)) {
					toast("params error！");
					return;
				}
				try {
					boolean b = mFacePassHandler.bindGroup(groupName, faceToken);
					String result = b ? "成功" : "失败";
					toast("bind  " + result);
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("ffffff", e.getMessage()+"绑定组异常");
					toast(e.getMessage());
				}


			}
		});

		getGroupInfoBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String groupName = groupNameEt.getText().toString();
				if (TextUtils.isEmpty(groupName)) {
					toast("group name  is null ！");
					return;
				}
				try {
					byte[][] faceTokens = mFacePassHandler.getLocalGroupInfo(groupName);
					List<String> faceTokenList = new ArrayList<>();
					if (faceTokens != null && faceTokens.length > 0) {
						for (int j = 0; j < faceTokens.length; j++) {
							if (faceTokens[j].length > 0) {
								faceTokenList.add(new String(faceTokens[j]));
							}
						}

					}
					faceTokenAdapter.setData(faceTokenList);
					groupInfoLv.setAdapter(faceTokenAdapter);
				} catch (Exception e) {
					e.printStackTrace();
					toast("get local group info error!");
				}

			}
		});

		faceTokenAdapter.setOnItemButtonClickListener(new FaceTokenAdapter.ItemButtonClickListener() {
			@Override
			public void onItemDeleteButtonClickListener(int position) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}

				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}
				String groupName = groupNameEt.getText().toString();
				if (TextUtils.isEmpty(groupName)) {
					toast("group name  is null ！");
					return;
				}
				try {
					byte[] faceToken = faceTokenAdapter.getData().get(position).getBytes();
					boolean b = mFacePassHandler.deleteFace(faceToken);
					String result = b ? "success " : "failed";
					toast("delete face " + result);
					if (b) {
						byte[][] faceTokens = mFacePassHandler.getLocalGroupInfo(groupName);
						List<String> faceTokenList = new ArrayList<>();
						if (faceTokens != null && faceTokens.length > 0) {
							for (int j = 0; j < faceTokens.length; j++) {
								if (faceTokens[j].length > 0) {
									faceTokenList.add(new String(faceTokens[j]));
								}
							}

						}
						faceTokenAdapter.setData(faceTokenList);
						groupInfoLv.setAdapter(faceTokenAdapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}

			}

			@Override
			public void onItemUnbindButtonClickListener(int position) {
				if (mFacePassHandler == null) {
					toast("FacePassHandle is null ! ");
					return;
				}

				String groupName = groupNameEt.getText().toString();
				if (TextUtils.isEmpty(groupName)) {
					toast("group name  is null ！");
					return;
				}
				try {
					byte[] faceToken = faceTokenAdapter.getData().get(position).getBytes();
					boolean b = mFacePassHandler.unBindGroup(groupName, faceToken);
					String result = b ? "success " : "failed";
					toast("unbind " + result);
					if (b) {
						byte[][] faceTokens = mFacePassHandler.getLocalGroupInfo(groupName);
						List<String> faceTokenList = new ArrayList<>();
						if (faceTokens != null && faceTokens.length > 0) {
							for (int j = 0; j < faceTokens.length; j++) {
								if (faceTokens[j].length > 0) {
									faceTokenList.add(new String(faceTokens[j]));
								}
							}

						}
						faceTokenAdapter.setData(faceTokenList);
						groupInfoLv.setAdapter(faceTokenAdapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
					toast("unbind error!");
				}

			}
		});


		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
		mFaceOperationDialog = builder.create();
		WindowManager.LayoutParams attributes = mFaceOperationDialog.getWindow().getAttributes();
		attributes.height = d.getHeight();
		attributes.width = d.getWidth();
		mFaceOperationDialog.getWindow().setAttributes(attributes);
		mFaceOperationDialog.show();
	}

	private void toast(String msg) {
		Toast.makeText(YiDongNianHuiActivity.this, msg, Toast.LENGTH_SHORT).show();
	}


	/**
	 * 根据facetoken下载图片缓存
	 */
	private static class FaceImageCache implements ImageLoader.ImageCache {

		private static final int CACHE_SIZE = 6 * 1024 * 1024;

		LruCache<String, Bitmap> mCache;

		public FaceImageCache() {
			mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}
	}

	private class FacePassRequest extends com.android.volley.Request<String> {

		HttpEntity entity;

		FacePassDetectionResult mFacePassDetectionResult;
		private com.android.volley.Response.Listener<String> mListener;

		public FacePassRequest(String url, FacePassDetectionResult detectionResult, com.android.volley.Response.Listener<String> listener, com.android.volley.Response.ErrorListener errorListener) {
			super(Method.POST, url, errorListener);
			mFacePassDetectionResult = detectionResult;
			mListener = listener;
		}

		@Override
		protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
			String parsed;
			try {
				parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			} catch (UnsupportedEncodingException e) {
				parsed = new String(response.data);
			}
			return com.android.volley.Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
		}

		@Override
		protected void deliverResponse(String response) {
			mListener.onResponse(response);
		}

		@Override
		public String getBodyContentType() {
			return entity.getContentType().getValue();
		}

		@Override
		public byte[] getBody() throws AuthFailureError {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//        beginRecogIdArrayList.clear();

			for (FacePassImage passImage : mFacePassDetectionResult.images) {
                /* 将人脸图转成jpg格式图片用来上传 */
				YuvImage img = new YuvImage(passImage.image, ImageFormat.NV21, passImage.width, passImage.height, null);
				Rect rect = new Rect(0, 0, passImage.width, passImage.height);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				img.compressToJpeg(rect, 95, os);
				byte[] tmp = os.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(tmp, String.valueOf(passImage.trackId) + ".jpg");
//            beginRecogIdArrayList.add(passImage.trackId);
				entityBuilder.addPart("image_" + String.valueOf(passImage.trackId), bab);
			}
			StringBody sbody = null;
			try {
				sbody = new StringBody(group_name, ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			entityBuilder.addPart("group_name", sbody);
			StringBody data = null;
			try {
				data = new StringBody(new String(mFacePassDetectionResult.message), ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			entityBuilder.addPart("face_data", data);
			entity = entityBuilder.build();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				entity.writeTo(bos);
			} catch (IOException e) {
				VolleyLog.e("IOException writing to ByteArrayOutputStream");
			}
			byte[] result = bos.toByteArray();
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
	}

	public static class  UsbBroadCastReceiver extends BroadcastReceiver{

		public UsbBroadCastReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction()!=null && intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")){
				usbPath = intent.getData().getPath();
				List<String> sss=  FileUtil.getMountPathList();
				int size=sss.size();
				for (int i=0;i<size;i++){

					if (sss.get(i).contains(usbPath)){
						usbPath=sss.get(i);
					}

				}

				Log.d("UsbBroadCastReceiver", usbPath);
				Log.d("UsbBroadCastReceiver", "ddddddddd");
				Log.d("UsbBroadCastReceiver", usbPath+"ll");
			}


		}
	}

}
