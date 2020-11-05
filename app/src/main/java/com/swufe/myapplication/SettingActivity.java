package com.swufe.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView cil_change,cil_forget,cil_store,cil_export;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initView();
    }

    private void initView() {
        cil_change = (TextView) findViewById(R.id.cil_change);
        cil_change.setOnClickListener(this);
        cil_forget = (TextView) findViewById(R.id.cil_forget);
        cil_forget.setOnClickListener(this);
        cil_store = (TextView) findViewById(R.id.cil_store);
        cil_store.setOnClickListener(this);
        cil_export = (TextView) findViewById(R.id.cil_export);
        cil_export.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cil_change:
                //修改密码
                Intent intent1=new Intent(SettingActivity.this,ResPwdActivity.class);
                SettingActivity.this.startActivity(intent1);
                break;
            case R.id.cil_forget:
                //忘记密码
                Intent intent2=new Intent(SettingActivity.this,ResPwdActivity.class);
                SettingActivity.this.startActivity(intent2);
                break;
            case R.id.cil_store:
                //清除缓存
                break;
            case R.id.cil_export:
                //导出账单
                break;
            default:
                break;
        }
    }
}
