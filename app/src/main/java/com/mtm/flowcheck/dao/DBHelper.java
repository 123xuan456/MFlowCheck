package com.mtm.flowcheck.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.LinkBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.bean.dao.OrganBean;
import com.mtm.flowcheck.bean.dao.RegionBean;
import com.mtm.flowcheck.dao.base.CheckBeanDao;
import com.mtm.flowcheck.dao.base.DaoMaster;
import com.mtm.flowcheck.dao.base.DaoSession;
import com.mtm.flowcheck.dao.base.LinkBeanDao;
import com.mtm.flowcheck.dao.base.OrganBeanDao;
import com.mtm.flowcheck.dao.base.RegionBeanDao;
import com.mtm.flowcheck.dao.base.UserBeanDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By WangYanBin On 2020\03\18 13:12.
 * <p>
 * （DBHelper）
 * 参考：
 * 描述：
 */
public class DBHelper {
    // -------------------------------------------------- 单例 --------------------------------------------------

    private static DBHelper mDBHelper;

    public static DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DBHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DBHelper(context);
                }
            }
        }
        return mDBHelper;
    }
    // -------------------------------------------------- 构造器 --------------------------------------------------

    private static final String DB_NAME = "file.db";// 数据库名称

    private Context mContext;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DBHelper(Context context) {
        this.mContext = context;
        MyContextWrapper mWrapper = new MyContextWrapper(context);
        mHelper = new DaoMaster.DevOpenHelper(mWrapper, DB_NAME, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 保存登录人员信息
     *
     * @param user
     * @return
     */
    public long saveUser(UserBean user) {
        return mDaoSession.insert(user);
    }

    /**
     * 删除登录人员信息
     */
    public void deleteUser() {
        mDaoSession.deleteAll(UserBean.class);
    }

    public UserBean getUser(String name) {
        UserBean user = mDaoSession.getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.UserName.eq(name)).unique();
        return user;
    }

    public UserBean getUser() {
        List<UserBean> userList = mDaoSession.getUserBeanDao().loadAll();
        if (null != userList && userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 保存下载的流转调查任务，保存48小时
     *
     * @param checkBeanInfo
     * @return
     */
    public long saveTask(CheckBean checkBeanInfo) {
        return mDaoSession.insert(checkBeanInfo);
    }

    /**
     * 更新流转调查任务
     *
     * @param checkBeanInfo
     */
    public void updateTask(CheckBean checkBeanInfo) {
        mDaoSession.update(checkBeanInfo);
    }

    /**
     * 删除流转调查任务
     *
     * @param checkBeanInfo
     */
    public void deleteTask(CheckBean checkBeanInfo) {
        mDaoSession.update(checkBeanInfo);
    }

    /**
     * 查询所有调查任务
     *
     * @return
     */
    public List<CheckBean> getTaskList() {
        List<CheckBean> taskList = mDaoSession.getCheckBeanDao().loadAll();
        return taskList;
    }

    /**
     * 查询调查任务
     * value 0未完成  1已完成
     *
     * @return
     */
    public List<CheckBean> getCheckTaskList(int value) {
        List<CheckBean> linkBeanList = mDaoSession.getCheckBeanDao().queryBuilder()
                .where(CheckBeanDao.Properties.IsDone.eq(value)).list();
        return linkBeanList;
    }

    /**
     * 根据主键查询调查任务
     */
    public CheckBean getTaskById(String id) {
        CheckBean checkBeanInfo = mDaoSession.load(CheckBean.class, id);
        return checkBeanInfo;
    }

    /**
     * 获取任务信息
     *
     * @param taskId
     * @return
     */
    public CheckBean getTaskByCase(String taskId) {
        CheckBean checkBean = mDaoSession.getCheckBeanDao().queryBuilder().where(CheckBeanDao.Properties.CaseId.eq(taskId)).unique();
        return checkBean;
    }
    // -------------------------------------------------- 环节信息 --------------------------------------------------

    /**
     * 获取环节信息 通过 任务ID
     *
     * @param taskId
     * @return
     */
    public List<LinkBean> getLinkInfoByTaskId(String taskId) {
        return mDaoSession.getLinkBeanDao().queryBuilder().where(LinkBeanDao.Properties.TaskId.eq(taskId)).orderAsc(LinkBeanDao.Properties.LinkType).list();
    }

    /**
     * 保存调查任务对应的环节信息
     *
     * @param linkInfo
     * @return
     */
    public long saveTaskLinkData(LinkBean linkInfo) {
        return mDaoSession.insert(linkInfo);
    }

    /**
     * 更新调查任务对应的环节信息
     *
     * @param linkInfo
     */
    public void updateTaskLinkInfo(LinkBean linkInfo) {
        mDaoSession.update(linkInfo);
    }

    /**
     * 删除调查任务对应的环节信息
     *
     * @param taskId
     */
    public void deleteTaskLinkInfo(String taskId, int linkType) {
        mDaoSession.getLinkBeanDao().queryBuilder().where(LinkBeanDao.Properties.TaskId.eq(taskId), LinkBeanDao.Properties.LinkType.eq(linkType))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 获取任务环节信息
     *
     * @param taskId
     * @param linkType
     * @return
     */
    public LinkBean getTaskLinkInfo(String taskId, int linkType) {
        LinkBean linkBean = mDaoSession.getLinkBeanDao().queryBuilder().where(LinkBeanDao.Properties.TaskId.eq(taskId),
                LinkBeanDao.Properties.LinkType.eq(linkType)).unique();
        return linkBean;
    }

    /**
     * 获取任务环节信息
     *
     * @param taskId
     * @param linkValue
     * @return
     */
    public List<LinkBean> getTaskLinkList(String taskId, String linkValue) {
        List<LinkBean> linkBean = mDaoSession.getLinkBeanDao().queryBuilder().where(LinkBeanDao.Properties.TaskId.eq(taskId),
                LinkBeanDao.Properties.LinkValue.eq(linkValue)).list();
        return linkBean;
    }

    public void close() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        if (null != mDBHelper) {
            mDBHelper.close();
            mDBHelper = null;
        }
    }

    // -------------------------------------------------- 地区 --------------------------------------------------

    /**
     * 插入地区信息
     *
     * @param regionList
     */
    public void insertRegion(List<RegionBean> regionList) {
        mDaoSession.getRegionBeanDao().insertOrReplaceInTx(regionList);
    }

    /**
     * 查询地区信息 通过 ID
     *
     * @param id
     * @return
     */
    public RegionBean queryRegionById(String id) {
        return mDaoSession.getRegionBeanDao().queryBuilder().where(RegionBeanDao.Properties.ID.eq(id)).build().unique();
    }

    /**
     * 查询地区信息 通过 PID
     *
     * @param pid
     * @return
     */
    public List<RegionBean> queryRegionByPid(String pid) {
        List<RegionBean> regionList = new ArrayList<>();
        regionList.addAll(mDaoSession.getRegionBeanDao().queryBuilder().where(RegionBeanDao.Properties.PID.eq(pid)).list());
        return regionList;
    }

    // -------------------------------------------------- 机构 --------------------------------------------------

    /**
     * 插入机构信息
     *
     * @param regionList
     */
    public void insertOrgan(List<OrganBean> regionList) {
        mDaoSession.getOrganBeanDao().insertOrReplaceInTx(regionList);
    }

    /**
     * 查询机构信息 通过 ID
     *
     * @param id
     * @return
     */
    public OrganBean queryOrganById(String id) {
        return mDaoSession.getOrganBeanDao().queryBuilder().where(OrganBeanDao.Properties.ID.eq(id)).build().unique();
    }

    /**
     * 查询机构信息 通过 PID
     *
     * @param pid
     * @return
     */
    public List<OrganBean> queryOrganByPid(String pid) {
        List<OrganBean> regionList = new ArrayList<>();
        regionList.addAll(mDaoSession.getOrganBeanDao().queryBuilder().where(OrganBeanDao.Properties.PID.eq(pid)).list());
        return regionList;
    }

    // -------------------------------------------------- 其他 --------------------------------------------------

    public class MyContextWrapper extends ContextWrapper {

        public MyContextWrapper(Context mContext) {
            super(mContext);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getPath(), factory, errorHandler);
        }

        @Override
        public File getDatabasePath(String dbName) {
            try {
                // 目录
                String dbDirPath = Environment.getExternalStorageDirectory().toString() + "/data/com.mtm.flowcheck/db";
                File dbDir = new File(dbDirPath);
                if (!dbDir.exists()) {
                    dbDir.mkdirs();
                }
                // 文件
                String dbFilePath = dbDirPath + File.separator + dbName;
                File dbFile = new File(dbFilePath);
                if (!dbFile.exists()) {
                    dbFile.createNewFile();
                    return dbFile;
                } else {
                    return dbFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return super.getDatabasePath(dbName);
            }
        }
    }
}
