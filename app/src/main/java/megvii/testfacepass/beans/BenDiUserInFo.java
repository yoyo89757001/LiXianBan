package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/25.
 */
@Entity
public class BenDiUserInFo {
    @Id
    @NotNull
    private Long id;
    private String name;
    private int xingbie;
    private String beizhu;
    @Generated(hash = 355698400)
    public BenDiUserInFo(@NotNull Long id, String name, int xingbie,
            String beizhu) {
        this.id = id;
        this.name = name;
        this.xingbie = xingbie;
        this.beizhu = beizhu;
    }
    @Generated(hash = 2095391012)
    public BenDiUserInFo() {
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
    public int getXingbie() {
        return this.xingbie;
    }
    public void setXingbie(int xingbie) {
        this.xingbie = xingbie;
    }
    public String getBeizhu() {
        return this.beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    


}
