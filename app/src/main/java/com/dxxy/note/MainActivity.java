package com.dxxy.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dxxy.note.adapter.NoteAdapter;
import com.dxxy.note.db.DatabaseManager;
import com.dxxy.note.entity.NoteInfo;
import com.dxxy.note.service.MusicService;
import com.dxxy.note.util.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NoteInfo> mNoteInfos = new ArrayList<>();
    private DatabaseManager manager;
    private NoteAdapter adapter;

    private ListView listview;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private ImageView iv_music;
    private boolean isPlay = true;
    private MusicService.MusicPlayBinder playBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listview);
        tvEmpty = findViewById(R.id.tv_empty);
        fabAdd = findViewById(R.id.fab_add);
        iv_music = findViewById(R.id.iv_music);

        Toolbar home_toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(home_toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }

        //创建数据库管理者操作增删改查
        manager = new DatabaseManager();

        //第三步：准备数据
        mNoteInfos.addAll(manager.getAllRecord(MainActivity.this));

        //第四步：定义适配器 设置列表控件数据
        adapter=new NoteAdapter(MainActivity.this, R.layout.personal_note_item, mNoteInfos);
        listview.setAdapter(adapter);

        //空页面显示或隐藏
        tvEmpty.setVisibility(adapter.getCount()==0? View.VISIBLE:View.GONE);

        //点击事件跳转编辑页面
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteInfo info, int position) {
                startActivityForResult(new Intent(MainActivity.this, EditNoteActivity.class)
                        .putExtra("data", info), 998);
            }

            @Override
            public void onItemDelClick(NoteInfo info, int position) {
                //删除记录
                deleteRecord(info);
            }
        });
        //点击事件跳转编辑页面
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                NoteInfo info = mNoteInfos.get(position);
//                startActivityForResult(new Intent(MainActivity.this, EditNoteActivity.class)
//                        .putExtra("data", info), 998);
//            }
//        });
//        //长按事件弹框提示删除记录
//        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                NoteInfo info = mNoteInfos.get(position);
//
//                deleteRecord(info);
//                return true;
//            }
//        });

        //添加记录
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, EditNoteActivity.class), 998);
            }
        });

        //背景音乐的设置
        //bind服务的连接类
        final ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                playBinder = (MusicService.MusicPlayBinder) iBinder; //获取代理人对象
                if (playBinder != null) {
                    playBinder.play();  //调用播放方法
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                //断开服务连接
            }
        };
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);   //绑定服务
        iv_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    isPlay = false;
                    if (playBinder != null) {
                        playBinder.stop();  //调用暂停方法
                    }
                    iv_music.setBackgroundResource(R.mipmap.icon_play);
                }else{
                    isPlay = true;
                    if (playBinder != null) {
                        playBinder.play();  //调用播放方法
                    }
                    iv_music.setBackgroundResource(R.mipmap.icon_pause);
                }
            }
        });
    }

    private void deleteRecord(NoteInfo info) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("温馨提示")
                .setMessage("确定要删除该记录吗？")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除记录数据保存
                        if (manager.deleteRecord(MainActivity.this, info.getId())) {
                            ToastUtil.showToast(MainActivity.this, "删除记录成功！");

                            //更新列表
                            mNoteInfos.remove(info);
                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(adapter.getCount()==0?View.VISIBLE:View.GONE);
                        } else {
                            ToastUtil.showToast(MainActivity.this, "删除记录失败！");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 998 && resultCode == Activity.RESULT_OK) {
            mNoteInfos.clear();
            mNoteInfos.addAll(manager.getAllRecord(MainActivity.this));
            adapter.notifyDataSetChanged();
            tvEmpty.setVisibility(adapter.getCount()==0? View.VISIBLE:View.GONE);
        }
    }

    /**
     * 右上角退出登录按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.more_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
    }

}