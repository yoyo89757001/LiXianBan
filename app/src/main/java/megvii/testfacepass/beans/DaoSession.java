package megvii.testfacepass.beans;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import megvii.testfacepass.beans.BenDiUserInFo;

import megvii.testfacepass.beans.BenDiUserInFoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig benDiUserInFoDaoConfig;

    private final BenDiUserInFoDao benDiUserInFoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        benDiUserInFoDaoConfig = daoConfigMap.get(BenDiUserInFoDao.class).clone();
        benDiUserInFoDaoConfig.initIdentityScope(type);

        benDiUserInFoDao = new BenDiUserInFoDao(benDiUserInFoDaoConfig, this);

        registerDao(BenDiUserInFo.class, benDiUserInFoDao);
    }
    
    public void clear() {
        benDiUserInFoDaoConfig.clearIdentityScope();
    }

    public BenDiUserInFoDao getBenDiUserInFoDao() {
        return benDiUserInFoDao;
    }

}
