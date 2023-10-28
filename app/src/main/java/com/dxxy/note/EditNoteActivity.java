package com.dxxy.note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dxxy.note.db.DatabaseManager;
import com.dxxy.note.engine.GlideEngine;
import com.dxxy.note.entity.NoteInfo;
import com.dxxy.note.util.ToastUtil;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private TextView tv_title;
    private EditText et_title, et_content;
    private ImageView iv_image;
    private RelativeLayout rlt_add;

    private DatabaseManager mManager;

    private NoteInfo info;
    private String image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_note);

        tv_title = findViewById(R.id.tv_title);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        iv_image = findViewById(R.id.iv_image);
        rlt_add = findViewById(R.id.rlt_add);

        info = (NoteInfo) getIntent().getSerializableExtra("data");
        //获取编辑携带的数据回填显示
        if (info != null) {
            et_title.setText(info.getTitle());
            et_content.setText(info.getContent());
            if (!TextUtils.isEmpty(info.getImage())) {
                image = info.getImage();
                Glide.with(EditNoteActivity.this).load(image).into(iv_image);
                rlt_add.setVisibility(View.GONE);
            }
        }
        //从缓存中取出用户名，设置标题
        SharedPreferences sp=getSharedPreferences("userinfo",0);
        String name = sp.getString("name","");
        tv_title.setText(name+"的" + getResources().getString(R.string.app_name));

        mManager = new DatabaseManager();

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast(EditNoteActivity.this, "请输入标题");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast(EditNoteActivity.this, "请输入内容");
                    return;
                }

                if (info == null) { //新增
                    info = new NoteInfo();
                    info.setTitle(title);
                    info.setContent(content);
                    info.setImage(image);
                    if (mManager.insertRecord(EditNoteActivity.this, info)) {
                        ToastUtil.showToast(EditNoteActivity.this, "新增记录成功！");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtil.showToast(EditNoteActivity.this, "新增记录失败！");
                    }
                } else { //修改
                    info.setTitle(title);
                    info.setContent(content);
                    info.setImage(image);
                    if (mManager.updateRecord(EditNoteActivity.this, info)) {
                        ToastUtil.showToast(EditNoteActivity.this, "修改记录成功！");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtil.showToast(EditNoteActivity.this, "修改记录失败！");
                    }
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void addImage(View view) {
        checkPicturePermission();
    }

    //使用相册功能的权限检查并设置
    private void checkPicturePermission() {
        // 进入相册
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine()) // 这里就是设置图片加载引擎
                .setMaxSelectNum(1)  //选1张
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 图片选择器回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                image = result.get(0).getRealPath();
                Glide.with(EditNoteActivity.this).load(image).into(iv_image);
                rlt_add.setVisibility(View.GONE);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i("TAG", "onActivityResult PictureSelector Cancel");
        }
    }
}
