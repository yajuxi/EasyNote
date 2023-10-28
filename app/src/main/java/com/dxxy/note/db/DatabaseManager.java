package com.dxxy.note.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.dxxy.note.entity.NoteInfo;
import com.dxxy.note.entity.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 */
public class DatabaseManager {

    //查询所有记录
    @SuppressLint("Range")
    public List<NoteInfo> getAllRecord(Activity activity) {
        List<NoteInfo> infoList = new ArrayList<>();

        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //从数据库中查询所有记录（用rawQuery方法），查询到的结果用游标结果集cursor表示。
        Cursor cursor =db.rawQuery("select * from "+ DatabaseHelper.NOTE_TABLE + " order by id desc ", null);
        //根据查询到的结果进行判断
        while (cursor.moveToNext()) {//查询到时从结果集中取出数据
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
            String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));

            NoteInfo info = new NoteInfo();
            info.setId(id);
            info.setImage(image);
            info.setTitle(title);
            info.setContent(content);
            info.setCreateTime(createTime);
            info.setUpdateTime(updateTime);

            infoList.add(info);
        }
        //释放游标
        cursor.close();

        return infoList;
    }

    //模糊查询所有记录
    @SuppressLint("Range")
    public List<NoteInfo> getRecordByKeyword(Activity activity, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return getAllRecord(activity);
        }
        List<NoteInfo> infoList = new ArrayList<>();

        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //从数据库中查询所有记录（用rawQuery方法），查询到的结果用游标结果集cursor表示。
        Cursor cursor =db.rawQuery("SELECT * FROM "+ DatabaseHelper.NOTE_TABLE +" where title like '%"+keyword+"%' or content like '%"+keyword+"%'", null);
        //根据查询到的结果进行判断
        while (cursor.moveToNext()) {//查询到时从结果集中取出数据
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
            String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));

            NoteInfo info = new NoteInfo();
            info.setId(id);
            info.setImage(image);
            info.setTitle(title);
            info.setContent(content);
            info.setCreateTime(createTime);
            info.setUpdateTime(updateTime);

            infoList.add(info);
        }
        //释放游标
        cursor.close();

        return infoList;
    }

    //新增记录
    @SuppressLint("Range")
    public boolean insertRecord(Activity activity, NoteInfo info) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //创建一个对象，用来封装一行数据
        ContentValues values=new ContentValues();
        values.put("image",info.getImage());//将图片放到image列
        values.put("title",info.getTitle());//将输入的标题放到title列
        values.put("content",info.getContent());//将输入的内容放到content列
        values.put("createTime", getyMdHms(System.currentTimeMillis()));//将当前时间格式化放到time列

        //将封装好的一行数据保存到数据库的NOTE_TABLE表中
        long result = db.insert(DatabaseHelper.NOTE_TABLE,null,values);

        return result!=-1;
    }

    //更新记录
    @SuppressLint("Range")
    public boolean updateRecord(Activity activity, NoteInfo info) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //创建一个对象，用来封装一行数据
        ContentValues values=new ContentValues();
        values.put("image",info.getImage());//将图片放到image列
        values.put("title",info.getTitle());//将输入的标题放到title列
        values.put("content",info.getContent());//将输入的内容放到content列
        values.put("updateTime", getyMdHms(System.currentTimeMillis()));//将当前时间格式化放到time列

        //将封装好的一行数据保存到数据库的NOTE_TABLE表中
        long result = db.update(DatabaseHelper.NOTE_TABLE,values,"id=?",new String[]{info.getId()+""});

        return result!=-1;
    }

    //删除单条记录
    @SuppressLint("Range")
    public boolean deleteRecord(Activity activity, int id) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //将封装好的一行数据保存到数据库的NOTE_TABLE表中
        long result = db.delete(DatabaseHelper.NOTE_TABLE, "id=?", new String[]{id + ""});

        return result!=-1;
    }

    //删除所有记录
    @SuppressLint("Range")
    public boolean deleteAllRecord(Activity activity) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //将封装好的一行数据保存到数据库的NOTE_TABLE表中
        long result =  db.delete(DatabaseHelper.NOTE_TABLE, null, null);

        return result!=-1;
    }

    //新增用户
    @SuppressLint("Range")
    public boolean insertUser(Activity activity, User user) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //创建一个对象，用来封装一行数据
        ContentValues values=new ContentValues();
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        values.put("avatar",user.getAvatar());

        //将封装好的一行数据保存到数据库的USER_TABLE表中
        long result = db.insert(DatabaseHelper.USER_TABLE,null,values);

        return result!=-1;
    }

    //查询用户
    @SuppressLint("Range")
    public User queryUser(Activity activity, String accountQ, String pwdQ) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        User user = null;
        //将封装好的一行数据保存到数据库的USER_TABLE表中
        Cursor cursor =db.rawQuery("select * from " + DatabaseHelper.USER_TABLE + " where name=? and password=? ",new String[]{accountQ,pwdQ});
        if (cursor.moveToNext()) {
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String account=cursor.getString(cursor.getColumnIndex("name"));
            String pwd=cursor.getString(cursor.getColumnIndex("password"));
            String avatar=cursor.getString(cursor.getColumnIndex("avatar"));

            user = new User();
            user.setId(id);
            user.setName(account);
            user.setPassword(pwd);
            user.setAvatar(avatar);
        }

        return user;
    }

    //查询所有用户
    @SuppressLint("Range")
    public List<User> queryUserAll(Activity activity) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        List<User> userList = new ArrayList<>();
        //将封装好的一行数据保存到数据库的USER_TABLE表中
        Cursor cursor =db.rawQuery("select * from " + DatabaseHelper.USER_TABLE, null);
        while (cursor.moveToNext()) {
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String account=cursor.getString(cursor.getColumnIndex("name"));
            String pwd=cursor.getString(cursor.getColumnIndex("password"));
            String avatar=cursor.getString(cursor.getColumnIndex("avatar"));

            User user = new User();
            user.setId(id);
            user.setName(account);
            user.setPassword(pwd);
            user.setAvatar(avatar);

            userList.add(user);
        }

        return userList;
    }

    /**
     * 更新用户信息
     */
    public boolean updateUser(Activity activity, User user) {
        if (user==null) return false;

        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        values.put("avatar",user.getAvatar());

        int result = db.update(DatabaseHelper.USER_TABLE, values, "id=?", new String[]{user.getId() + ""});

        if (result != -1) {
            SharedPreferences.Editor editor=activity.getSharedPreferences("userinfo",0).edit();
            editor.putString("name",user.getName());
            editor.putString("pwd",user.getPassword());
            editor.commit();
        }

        return result!=-1;
    }

    //删除单个用户
    @SuppressLint("Range")
    public boolean deleteUser(Activity activity, int uId) {
        //实例化数据库操作类
        DatabaseHelper mhelper=new DatabaseHelper(activity);
        //实例化数据库对象
        SQLiteDatabase db=mhelper.getWritableDatabase();

        //将封装好的一行数据保存到数据库的USER_TABLE表中
        long result =  db.delete(DatabaseHelper.USER_TABLE, "id=?", new String[]{uId+""});

        return result!=-1;
    }

    //获取当前登录用户
    @SuppressLint("Range")
    public User getLoginUser(Activity activity) {
        SharedPreferences sp=activity.getSharedPreferences("userinfo",0);

        String account = sp.getString("name","");
        String pwd = sp.getString("pwd", "");

        return queryUser(activity, account, pwd);
    }

    /*时间戳转时间格式化*/
    public static String getyMdHms(long time){
        if (time == 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromTime = sdf.format(time);
        return fromTime;
    }
}
