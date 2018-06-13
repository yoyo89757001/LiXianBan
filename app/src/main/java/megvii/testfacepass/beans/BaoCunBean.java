package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Administrator on 2017/9/15.
 */
@Entity
public class BaoCunBean {
    @Id
    @NotNull
    private Long id;
    private String shipingIP;
    private String zhujiDiZhi;
    private int moban;
    private String tuisongDiZhi;
    private String gonggao;
    private boolean isShowMoshengren;
    private boolean isShowShiPingLiu;
    private boolean isHengOrShu;
    private int yusu;
    private int yudiao;
    private int boyingren;
    private String zhanghuId;
    private String wenzi;
    private int size;
    private String touxiangzhuji;
    private String houtaiDiZhi;
    private String huiyiId;
    private String wenzi1;
    private int size1;
    @Generated(hash = 1899331884)
    public BaoCunBean(@NotNull Long id, String shipingIP, String zhujiDiZhi,
            int moban, String tuisongDiZhi, String gonggao,
            boolean isShowMoshengren, boolean isShowShiPingLiu, boolean isHengOrShu,
            int yusu, int yudiao, int boyingren, String zhanghuId, String wenzi,
            int size, String touxiangzhuji, String houtaiDiZhi, String huiyiId,
            String wenzi1, int size1) {
        this.id = id;
        this.shipingIP = shipingIP;
        this.zhujiDiZhi = zhujiDiZhi;
        this.moban = moban;
        this.tuisongDiZhi = tuisongDiZhi;
        this.gonggao = gonggao;
        this.isShowMoshengren = isShowMoshengren;
        this.isShowShiPingLiu = isShowShiPingLiu;
        this.isHengOrShu = isHengOrShu;
        this.yusu = yusu;
        this.yudiao = yudiao;
        this.boyingren = boyingren;
        this.zhanghuId = zhanghuId;
        this.wenzi = wenzi;
        this.size = size;
        this.touxiangzhuji = touxiangzhuji;
        this.houtaiDiZhi = houtaiDiZhi;
        this.huiyiId = huiyiId;
        this.wenzi1 = wenzi1;
        this.size1 = size1;
    }
    @Generated(hash = 1469853663)
    public BaoCunBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getShipingIP() {
        return this.shipingIP;
    }
    public void setShipingIP(String shipingIP) {
        this.shipingIP = shipingIP;
    }
    public String getZhujiDiZhi() {
        return this.zhujiDiZhi;
    }
    public void setZhujiDiZhi(String zhujiDiZhi) {
        this.zhujiDiZhi = zhujiDiZhi;
    }
    public int getMoban() {
        return this.moban;
    }
    public void setMoban(int moban) {
        this.moban = moban;
    }
    public String getTuisongDiZhi() {
        return this.tuisongDiZhi;
    }
    public void setTuisongDiZhi(String tuisongDiZhi) {
        this.tuisongDiZhi = tuisongDiZhi;
    }
    public String getGonggao() {
        return this.gonggao;
    }
    public void setGonggao(String gonggao) {
        this.gonggao = gonggao;
    }
    public boolean getIsShowMoshengren() {
        return this.isShowMoshengren;
    }
    public void setIsShowMoshengren(boolean isShowMoshengren) {
        this.isShowMoshengren = isShowMoshengren;
    }
    public boolean getIsShowShiPingLiu() {
        return this.isShowShiPingLiu;
    }
    public void setIsShowShiPingLiu(boolean isShowShiPingLiu) {
        this.isShowShiPingLiu = isShowShiPingLiu;
    }
    public boolean getIsHengOrShu() {
        return this.isHengOrShu;
    }
    public void setIsHengOrShu(boolean isHengOrShu) {
        this.isHengOrShu = isHengOrShu;
    }
    public int getYusu() {
        return this.yusu;
    }
    public void setYusu(int yusu) {
        this.yusu = yusu;
    }
    public int getYudiao() {
        return this.yudiao;
    }
    public void setYudiao(int yudiao) {
        this.yudiao = yudiao;
    }
    public int getBoyingren() {
        return this.boyingren;
    }
    public void setBoyingren(int boyingren) {
        this.boyingren = boyingren;
    }
    public String getZhanghuId() {
        return this.zhanghuId;
    }
    public void setZhanghuId(String zhanghuId) {
        this.zhanghuId = zhanghuId;
    }
    public String getWenzi() {
        return this.wenzi;
    }
    public void setWenzi(String wenzi) {
        this.wenzi = wenzi;
    }
    public int getSize() {
        return this.size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getTouxiangzhuji() {
        return this.touxiangzhuji;
    }
    public void setTouxiangzhuji(String touxiangzhuji) {
        this.touxiangzhuji = touxiangzhuji;
    }
    public String getHoutaiDiZhi() {
        return this.houtaiDiZhi;
    }
    public void setHoutaiDiZhi(String houtaiDiZhi) {
        this.houtaiDiZhi = houtaiDiZhi;
    }
    public String getHuiyiId() {
        return this.huiyiId;
    }
    public void setHuiyiId(String huiyiId) {
        this.huiyiId = huiyiId;
    }
    public String getWenzi1() {
        return this.wenzi1;
    }
    public void setWenzi1(String wenzi1) {
        this.wenzi1 = wenzi1;
    }
    public int getSize1() {
        return this.size1;
    }
    public void setSize1(int size1) {
        this.size1 = size1;
    }


}
