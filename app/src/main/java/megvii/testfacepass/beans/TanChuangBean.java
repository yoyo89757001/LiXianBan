package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by chenjun on 2017/5/16.
 */

@Entity
public class TanChuangBean {
    @Id
    @NotNull
    private Long id;
    private String name;
    private byte[] touxiang;
    private String remark;
    private byte[] bytes;
    private int type;
    private long idid;
    private String bumen;
    private boolean isLight;
    private String gonghao;
    private String zhiwei;
    @Generated(hash = 1463779143)
    public TanChuangBean(@NotNull Long id, String name, byte[] touxiang,
            String remark, byte[] bytes, int type, long idid, String bumen,
            boolean isLight, String gonghao, String zhiwei) {
        this.id = id;
        this.name = name;
        this.touxiang = touxiang;
        this.remark = remark;
        this.bytes = bytes;
        this.type = type;
        this.idid = idid;
        this.bumen = bumen;
        this.isLight = isLight;
        this.gonghao = gonghao;
        this.zhiwei = zhiwei;
    }
    @Generated(hash = 884899701)
    public TanChuangBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getTouxiang() {
        return this.touxiang;
    }
    public void setTouxiang(byte[] touxiang) {
        this.touxiang = touxiang;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public byte[] getBytes() {
        return this.bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getIdid() {
        return this.idid;
    }
    public void setIdid(long idid) {
        this.idid = idid;
    }
    public String getBumen() {
        return this.bumen;
    }
    public void setBumen(String bumen) {
        this.bumen = bumen;
    }
    public boolean getIsLight() {
        return this.isLight;
    }
    public void setIsLight(boolean isLight) {
        this.isLight = isLight;
    }
    public String getGonghao() {
        return this.gonghao;
    }
    public void setGonghao(String gonghao) {
        this.gonghao = gonghao;
    }
    public String getZhiwei() {
        return this.zhiwei;
    }
    public void setZhiwei(String zhiwei) {
        this.zhiwei = zhiwei;
    }
  
  
}
