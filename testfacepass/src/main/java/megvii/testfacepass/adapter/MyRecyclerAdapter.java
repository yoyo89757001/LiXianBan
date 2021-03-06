package megvii.testfacepass.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.text.DecimalFormat;
import java.util.List;

import megvii.testfacepass.R;
import megvii.testfacepass.beans.DIKu;
import megvii.testfacepass.beans.PaiHangBean;


/**
 * Created by Administrator on 2018/7/30.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<DIKu> dataList;

    private LayoutInflater layoutInflater;

    public MyRecyclerAdapter(Context context, List<DIKu> dataList) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.paihang_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.xuhao.setText("YOUKEY  "+dataList.get(position).getTrackId()+"");
      //  String t[]=dataList.get(position).getTime().split(" ");
      //  holder.shijian.setText(t[0]+"\n"+t[1]);
     //   DecimalFormat df = new DecimalFormat("#.00");
        holder.shijian.setText(dataList.get(position).getTime()+"");
        holder.guanzhudu.setText(dataList.get(position).getLingshiTime()+"");
        holder.cishi.setText(dataList.get(position).getCishu()+"次");
        if (dataList.get(position).getBytes().length>0)
        holder.touxiang.setImageBitmap(BitmapFactory.decodeByteArray(dataList.get(position).getBytes(),0,dataList.get(position).getBytes().length));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView guanzhudu,cishi,shijian,xuhao;
        private ImageView touxiang;



        MyViewHolder(View itemView) {
            super(itemView);
            xuhao = (TextView) itemView.findViewById(R.id.xuhao);
            guanzhudu = (TextView) itemView.findViewById(R.id.guanzhu);
            shijian = (TextView) itemView.findViewById(R.id.shijian);
            cishi = (TextView) itemView.findViewById(R.id.cishu);
            touxiang= (ImageView) itemView.findViewById(R.id.touxiang);
        }
    }

}


