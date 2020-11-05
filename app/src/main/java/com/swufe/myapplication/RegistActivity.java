package com.swufe.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button register;
    private boolean isOpen = false;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
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
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
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
            case R.id.register:
                boolean isTrue = true;
                if(PublicFunction.isPhoneNumberValid(username.getText().toString()) == false){
                    isTrue = false;
                    Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(PublicFunction.isEmpty(password.getText().toString())){
                    isTrue = false;
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isTrue = true){
                    //call DBOpenHelper
                    DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor c = db.query("user_tb",null,"userID=?",new String[]{username.getText().toString()},null,null,null);
                    if(c!=null && c.getCount() >= 1){
                        Toast.makeText(this, "该用户已存在", Toast.LENGTH_SHORT).show();
                        c.close();
                    }
                    else{
                        //insert data
                        ContentValues values= new ContentValues();
                        values.put("userID",username.getText().toString());
                        values.put("pwd",password.getText().toString());
                        long rowid = db.insert("user_tb",null,values);
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                    db.close();
                }else{
                    return;
                }
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
