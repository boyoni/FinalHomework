package com.swufe.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button login;
    private Button forgive_pwd;
    private Button register;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String user = username.getText().toString().trim();  //获得文本框中的用户
                if ("".equals(user)) {
                    bt_username_clear.setVisibility(View.INVISIBLE);  //用户名为空,设置按钮不可见
                } else {
                    bt_username_clear.setVisibility(View.VISIBLE);  //用户名不为空，设置按钮可见
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = password.getText().toString().trim();  //获得文本框中的用户
                if ("".equals(pwd)) {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);  //用户名为空,设置按钮不可见
                } else {
                    bt_pwd_clear.setVisibility(View.VISIBLE);   //用户名不为空，设置按钮可见
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
        forgive_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                username.setText("");  //清除登录名
                break;
            case R.id.bt_pwd_clear:
                password.setText("");  //清除密码
                break;
            case R.id.bt_pwd_eye:
                if (isOpen) {  //密码可见与不可见的切换
                    isOpen = false;
                } else {
                    isOpen = true;
                }
                changePwdOpenOrClose(isOpen);  //默认isOpen是false,密码不可见
                break;
            case R.id.login:
                if(PublicFunction.isEmpty(username.getText().toString()) || PublicFunction.isEmpty(password.getText().toString())){
                    Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //call DBOpenHelper
                DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.query("user_tb",null,"userID=? and pwd=?",new String[]{username.getText().toString(),password.getText().toString()},null,null,null);
                if(c!=null && c.getCount() >= 1){
                    String[] cols = c.getColumnNames();
                    while(c.moveToNext()){
                        for(String ColumnName:cols){
                            Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
                        }
                    }
                    c.close();
                    db.close();

                    //将登陆用户信息存储到SharedPreferences中
                    SharedPreferences mySharedPreferences= getSharedPreferences("nav_setting", Activity.MODE_PRIVATE); //实例化SharedPreferences对象
                    SharedPreferences.Editor editor = mySharedPreferences.edit();//实例化SharedPreferences.Editor对象
                    editor.putString("userID", username.getText().toString()); //用putString的方法保存数据
                    editor.commit(); //提交当前数据
                    this.finish();
                }
                else{
                    Toast.makeText(this, "手机号或密码输入错误！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                Intent intent1=new Intent(LoginActivity.this,RegistActivity.class);
                LoginActivity.this.startActivity(intent1);
                break;
            case R.id.forgive_pwd:
                Intent intent2=new Intent(LoginActivity.this,ResPwdActivity.class);
                LoginActivity.this.startActivity(intent2);
                break;
            default:
                break;
        }
    }

    //密码可见与不可见的切换
    private void changePwdOpenOrClose(boolean flag) {
        if (flag) {    //第一次过来是false，密码不可见
            bt_pwd_eye.setBackgroundResource(R.drawable.password_open);  //密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //设置EditText的密码可见
        } else {
            bt_pwd_eye.setBackgroundResource(R.drawable.password_close);  //密码不可见
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());  //设置EditText的密码隐藏
        }
    }
}