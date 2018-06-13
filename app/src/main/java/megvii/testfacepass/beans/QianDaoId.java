package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Administrator on 2017/12/27.
 */
@Entity
public class QianDaoId {
    @Id
    @NotNull
    private Long id;
    private String name;
    private boolean isQd;
    @Generated(hash = 207389244)
    public QianDaoId(@NotNull Long id, String name, boolean isQd) {
        this.id = id;
        this.name = name;
        this.isQd = isQd;
    }
    @Generated(hash = 2019086784)
    public QianDaoId() {
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
    public boolean getIsQd() {
        return this.isQd;
    }
    public void setIsQd(boolean isQd) {
        this.isQd = isQd;
    }



}
