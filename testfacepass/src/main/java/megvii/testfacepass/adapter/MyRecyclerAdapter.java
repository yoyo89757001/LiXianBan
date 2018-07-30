package megvii.testfacepass.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import megvii.testfacepass.R;

/**
 * Created by Administrator on 2018/7/30.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<ContactsContract.Contacts.Data> dataList;

    private LayoutInflater layoutInflater;

    public MyRecyclerAdapter(Context context, List<ContactsContract.Contacts.Data> dataList) {
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
        holder.xuhao.setText((position+1)+"");
       // holder.shijian.setText(dataList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView xuhao,bianhao,shijian,yanzhi;
        private ImageView touxiang;



        MyViewHolder(View itemView) {
            super(itemView);
            xuhao = (TextView) itemView.findViewById(R.id.xuhao);
            bianhao = (TextView) itemView.findViewById(R.id.bianhao);
            shijian = (TextView) itemView.findViewById(R.id.shijian);
            yanzhi = (TextView) itemView.findViewById(R.id.yanzhi);
        }
    }

}


