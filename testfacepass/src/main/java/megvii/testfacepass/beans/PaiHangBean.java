package megvii.testfacepass.beans;

import java.util.Comparator;

/**
 * Created by Administrator on 2018/7/31.
 */

public class PaiHangBean implements Comparator<PaiHangBean> {
    private long id;
    private String yanzhi;
    private String time;
    private String bianhao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getYanzhi() {
        return yanzhi;
    }

    public void setYanzhi(String yanzhi) {
        this.yanzhi = yanzhi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    @Override
    public int compare(PaiHangBean o1, PaiHangBean o2) {
        return (o1.getYanzhi()).compareTo(o2.getYanzhi());
    }
}
