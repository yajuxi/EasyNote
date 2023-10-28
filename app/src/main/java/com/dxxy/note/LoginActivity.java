package com.dxxy.note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dxxy.note.db.DatabaseManager;
import com.dxxy.note.entity.User;
import androidx.appcompat.app.AppCompatActivity;

//登录
public class LoginActivity extends AppCompatActivity{
    //定义对象
    private EditText et_phone,et_pwd;
    private Button btn_login;
    private TextView btn_newregister;
    private CheckBox cb;

    private DatabaseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定控件
        initView();
        //登录按钮
        btnLogin();
        //新用户注册按钮
        btnNewRegister();
    }
    //绑定控件
    private void initView() {
        et_phone= (EditText) findViewById(R.id.et_phone);
        et_pwd= (EditText) findViewById(R.id.et_pwd_lg);
        btn_newregister= (TextView) findViewById(R.id.bt_newregister_lg);
        btn_login= (Button) findViewById(R.id.bt_login_lg);
        cb= (CheckBox) findViewById(R.id.cb);

        mManager=new DatabaseManager();

        //从缓存中取出用户名，密码，回填到编辑框
        SharedPreferences sp=getSharedPreferences("userinfo",0);
        String account = sp.getString("name","");
        String pwd = sp.getString("pwd", "");
        boolean remember = sp.getBoolean("remember",false);
        if (remember) {
            cb.setChecked(true);
            et_phone.setText(account);
            et_pwd.setText(pwd);
        }
    }
    //登录按钮功能
    private void btnLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                String pwd = et_pwd.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = mManager.queryUser(LoginActivity.this, phone, pwd);
                if (user != null) {
                    Toast.makeText(LoginActivity.this,"登录成功！", Toast.LENGTH_SHORT).show();
                    //如果输入正确，将用户+密码保存到一个键值对中以供后边使用。0是覆盖模式只保存最后一次
                    SharedPreferences.Editor editor=getSharedPreferences("userinfo",0).edit();
                    editor.putString("name",user.getName());
                    editor.putString("pwd", user.getPassword());
                    editor.putBoolean("remember",cb.isChecked());
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //新用户注册按钮功能
    private void btnNewRegister() {
        btn_newregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
