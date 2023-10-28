package com.dxxy.note;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dxxy.note.db.DatabaseManager;
import com.dxxy.note.entity.User;
import androidx.appcompat.app.AppCompatActivity;

//注册
public class RegisterActivity extends AppCompatActivity {
    //定义对象
    private EditText et_name,et_pwd, et_pwd_again;
    private Button btn_register;

    private DatabaseManager mManager;//创建一个数据库类文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //绑定控件
        initView();
        //注册按钮
        btnRegister();
    }
    //绑定控件
    private void initView() {
        et_name= (EditText) findViewById(R.id.et_name_rg);
        et_pwd= (EditText) findViewById(R.id.et_pwd_rg);
        et_pwd_again= (EditText) findViewById(R.id.et_pwd_rg_again);
        btn_register= (Button) findViewById(R.id.bt_ok_rg);

        mManager=new DatabaseManager();
    }

    //注册按钮功能
    private void btnRegister() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String pwd = et_pwd.getText().toString();
                String pwd_again = et_pwd_again.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd_again)) {
                    Toast.makeText(RegisterActivity.this,"请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(pwd_again, pwd)) {
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User();
                user.setName(name);
                user.setPassword(pwd);

                //将封装好的一行数据保存到数据库的user表中
                if (mManager.insertUser(RegisterActivity.this, user)) {
                    Toast.makeText(RegisterActivity.this,"注册新用户成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,"注册新用户失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
