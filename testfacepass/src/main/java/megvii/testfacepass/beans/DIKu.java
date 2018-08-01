package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/7/31.
 */

@Entity
public class DIKu {
    @Id
    @NotNull
    private Long id;
    private String teZhengMa;
    private double yanzhi;
    private String fuzhi;
    private String time;
    private long trackId;
    private int cishu;
    private int paihang;
    private int nianl;
    private String xingbie;
    private int guanzhu;
    private byte[] bytes;
    private String biaoqing;
    @Generated(hash = 210380642)
    public DIKu(@NotNull Long id, String teZhengMa, double yanzhi, String fuzhi,
            String time, long trackId, int cishu, int paihang, int nianl,
            String xingbie, int guanzhu, byte[] bytes, String biaoqing) {
        this.id = id;
        this.teZhengMa = teZhengMa;
        this.yanzhi = yanzhi;
        this.fuzhi = fuzhi;
        this.time = time;
        this.trackId = trackId;
        this.cishu = cishu;
        this.paihang = paihang;
        this.nianl = nianl;
        this.xingbie = xingbie;
        this.guanzhu = guanzhu;
        this.bytes = bytes;
        this.biaoqing = biaoqing;
    }
    @Generated(hash = 220588313)
    public DIKu() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTeZhengMa() {
        return this.teZhengMa;
    }
    public void setTeZhengMa(String teZhengMa) {
        this.teZhengMa = teZhengMa;
    }
    public double getYanzhi() {
        return this.yanzhi;
    }
    public void setYanzhi(double yanzhi) {
        this.yanzhi = yanzhi;
    }
    public String getFuzhi() {
        return this.fuzhi;
    }
    public void setFuzhi(String fuzhi) {
        this.fuzhi = fuzhi;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getTrackId() {
        return this.trackId;
    }
    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }
    public int getCishu() {
        return this.cishu;
    }
    public void setCishu(int cishu) {
        this.cishu = cishu;
    }
    public int getPaihang() {
        return this.paihang;
    }
    public void setPaihang(int paihang) {
        this.paihang = paihang;
    }
    public int getNianl() {
        return this.nianl;
    }
    public void setNianl(int nianl) {
        this.nianl = nianl;
    }
    public String getXingbie() {
        return this.xingbie;
    }
    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }
    public int getGuanzhu() {
        return this.guanzhu;
    }
    public void setGuanzhu(int guanzhu) {
        this.guanzhu = guanzhu;
    }
    public byte[] getBytes() {
        return this.bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getBiaoqing() {
        return this.biaoqing;
    }
    public void setBiaoqing(String biaoqing) {
        this.biaoqing = biaoqing;
    }

    

}
