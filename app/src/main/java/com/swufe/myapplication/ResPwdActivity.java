package com.swufe.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ResPwdActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username, password, password2;
    private Button bt_username_clear;
    private Button bt_pwd_clear,bt_pwd_clear2;
    private Button bt_pwd_eye,bt_pwd_eye2;
    private Button confirm;
    private boolean isOpen = false;
    private boolean isOpen2 = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_password);
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
        password2 = (EditText) findViewById(R.id.password2);
        // 监听文本框内容变化
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = password2.getText().toString().trim();  //获得文本框中的用户
                if ("".equals(pwd)) {
                    bt_pwd_clear2.setVisibility(View.INVISIBLE);  //用户名为空,设置按钮不可见
                } else {
                    bt_pwd_clear2.setVisibility(View.VISIBLE);   //用户名不为空，设置按钮可见
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
        bt_pwd_clear2 = (Button) findViewById(R.id.bt_pwd_clear2);
        bt_pwd_clear2.setOnClickListener(this);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        bt_pwd_eye2 = (Button) findViewById(R.id.bt_pwd_eye2);
        bt_pwd_eye2.setOnClickListener(this);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
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
            case R.id.bt_pwd_clear2:
                password2.setText("");  //清除密码
                break;
            case R.id.bt_pwd_eye:
                if (isOpen) {  //密码可见与不可见的切换
                    isOpen = false;
                } else {
                    isOpen = true;
                }
                changePwdOpenOrClose(isOpen);  //默认isOpen是false,密码不可见
                break;
            case R.id.bt_pwd_eye2:
                if (isOpen2) {  //密码可见与不可见的切换
                    isOpen2 = false;
                } else {
                    isOpen2 = true;
                }
                changePwdOpenOrClose(isOpen2);  //默认isOpen是false,密码不可见
                break;
            case R.id.confirm:
                confirmInfo();
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

    private void confirmInfo() {
        if(PublicFunction.isEmpty(username.getText().toString()) || PublicFunction.isEmpty(password.getText().toString()) || PublicFunction.isEmpty(password2.getText().toString())){
            Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(this, "输入密码不正确！", Toast.LENGTH_SHORT).show();
            return;
        }

        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("user_tb",null,"userID=?",new String[]{username.getText().toString()},null,null,null);
        if(c!=null && c.getCount() >= 1){
            ContentValues cv = new ContentValues();
            cv.put("pwd", password.getText().toString());
            String[] args = {String.valueOf(username.getText().toString())};
            long rowid = db.update("user_tb", cv, "userID=?",args);
            c.close();
            db.close();
            Toast.makeText(this, "密码重置成功！", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("该用户不存在，请到注册界面进行注册！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);
                            Intent intent=new Intent(ResPwdActivity.this,RegistActivity.class);
                            ResPwdActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }
    }
}