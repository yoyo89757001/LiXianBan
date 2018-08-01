package megvii.testfacepass.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/8/1.
 */
@Entity
public class Ceshi {
    @Id
    @NotNull
    private Long id;
    private byte[] bytes;
    @Generated(hash = 1900831402)
    public Ceshi(@NotNull Long id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }
    @Generated(hash = 1352712445)
    public Ceshi() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public byte[] getBytes() {
        return this.bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    

}
