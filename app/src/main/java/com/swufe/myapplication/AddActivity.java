package com.swufe.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private int type = 0;//0:income   1:payout
    final static int EDIT_MODE = 2;

    private String[] str = null;
    private String[] accountId = null;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePicker = null;
    private AlertDialog dialog = null;
    private ArrayAdapter<String> adapter;
    private List<String> list = null;

    private TextView title_tv = null;
    private RadioGroup trans_type_tab_rg = null;
    private RadioButton rb1=null;
    private RadioButton rb2=null;

    private FrameLayout corporation_fl = null;
    private FrameLayout empty_fl = null;
    private EditText cost = null;
    private String  value="0";
    private Spinner first_level_category_spn = null;
    private Spinner sub_category_spn = null;
    private int type_sub_id = 0;
    private Spinner account_spn = null;
    private Spinner corporation_spn = null;
    private Button trade_time_btn = null;
    private Spinner project_spn = null;
    private Button memo_btn = null;
    private Button save_btn = null;
    private Button cancel_btn = null;

    private EditText edit = null;
    private int isInitOnly = 0;
    private Context context;

    //类别
    private static String[] bigCategoryList = { "" };
    private static String[] defaultSubCategory_info = { "" };
    //子类别
    private static String[][] subCategory_info = new String[][] {{ "" }, { "" }};
    //账户
    private static String[] accountList = { "" };
    //商家
    private static String[] shopList = { "" };
    //备注
    private static String[] noteList = { "" };

    private TextView txtBigCategory_view;
    private Spinner BigCategory_spinner;
    private ArrayAdapter<String> BigCategory_adapter;

    private TextView txtSubCategory_view;
    private Spinner subCategory_spinner;
    private ArrayAdapter<String> subCategory_adapter;

    private TextView txtAccount_view;
    private Spinner account_spinner;
    private ArrayAdapter<String> account_adapter;

    private TextView txtShop_view;
    private Spinner shop_spinner;
    private ArrayAdapter<String> shop_adapter;

    private TextView txtNote_view;
    private Spinner note_spinner;
    private ArrayAdapter<String> note_adapter;

    private String txtBigCategory = "";
    private String txtSubCategory = "";
    private String txtAccount = "";
    private String txtShop = "";
    private String txtNote = "";

    private TextView txtDate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        //接收传递过来的参数
        final Intent intent = getIntent();
        type = intent.getIntExtra("strType", 0);

        context = this;

        initSpinner();

        loadingFormation();

        trade_time_btn.setText(PublicFunction.format(calendar.getTime()));

        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String i = cost.getText().toString().trim();  //获得文本框中的内容
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        trade_time_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                openDate();
            }
        });
    }

    private void loadingFormation(){
        cost=(EditText) findViewById(R.id.cost);
        trade_time_btn=(Button)findViewById(R.id.trade_time_btn);
    }

    private void openDate() {
        datePicker = new DatePickerDialog(this, mDateSetListenerSatrt,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    /**
     * return money
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            Bundle extras = data.getExtras();
            value = extras.getString("value");
            cost.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
        }
    }

    /**
     * return date
     */
    private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR, year);
            trade_time_btn.setText(PublicFunction.format(calendar.getTime()));
        }
    };

    /**
     * 初始化spinner
     */
    private void initSpinner(){
        if(type == 0){
            //类别
            bigCategoryList = new String[] { "职业收入", "其他收入" };
            defaultSubCategory_info = new String[] { "兼职收入", "投资收入" ,"奖金收入","加班收入","利息收入","工资收入" };
            //子类别
            subCategory_info = new String[][] {
                    { "工资","奖金","兼职","利息","其他" },
                    { "红包", "生活费", "投资收入" ,"报销","其他" }};
            //账户
            accountList = new String[] { "现金", "银行卡","微信","支付宝","其他" };
            //商家
            shopList = new String[] { "银行", "公司","长辈","其他" };
            //备注
            noteList = new String[] { "报销","奖励","其他" };
        }else{
            bigCategoryList = new String[] { "衣", "食","住","行","教育","娱乐","人情往来","医疗保健","其他" };
            defaultSubCategory_info = new String[] { "衣服裤子", "鞋帽包包" ,"化妆饰品" };
            //子类别
            subCategory_info = new String[][] {
                    { "服饰", "鞋帽" ,"包包" ,"化妆品","饰品","其他" },
                    { "早餐","午餐","晚餐", "水果","零食" ,"烟酒","其他" },
                    { "房租", "物业" , "水电煤气" ,"其他" },
                    { "公共交通", "出租车" ,"私家车","其他" },
                    { "书籍", "文具" ,"考试","辅导班","其他"  },
                    { "休闲", "旅游" ,"宠物", "聚会" ,"健身" ,"其他" },
                    { "红包", "请客" ,"还钱", "孝敬" ,"其他" },
                    { "治疗", "美容" ,"保健" ,"药品" ,"其他" },
                    { "丢失" ,"其他" }};
            //账户
            accountList = new String[] { "银行卡", "微信","支付宝","现金","其他" };
            //商家
            shopList = new String[] { "银行","公交","饭店","商场","超市","其他" };
            //备注
            noteList = new String[] { "公司报销","出差","其他" };
        }

        /**
         * 1、定义类别下拉菜单
         */
        txtBigCategory_view = (TextView) findViewById(R.id.txtBigCategory);
        BigCategory_spinner = (Spinner) findViewById(R.id.BigCategory_spinner);
        //将可选内容与ArrayAdapter连接起来
        BigCategory_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bigCategoryList);
        //设置下拉列表的风格
        BigCategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到spinner中
        BigCategory_spinner.setAdapter(BigCategory_adapter);
        // 添加事件Spinner事件监听
        BigCategory_spinner.setOnItemSelectedListener(new BigCategory_spinnerSelectedListener());
        // 设置默认值
        BigCategory_spinner.setVisibility(View.VISIBLE);

        /**
         * 2、定义子类别下拉菜单
         */
        txtSubCategory_view = (TextView) findViewById(R.id.txtSubCategory);
        subCategory_spinner = (Spinner) findViewById(R.id.subCategory_spinner);
        subCategory_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, defaultSubCategory_info);
        subCategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategory_spinner.setAdapter(subCategory_adapter);
        subCategory_spinner.setOnItemSelectedListener(new subCategory_spinnerSelectedListener());
        subCategory_spinner.setVisibility(View.VISIBLE);

        /**
         * 3、定义账户下拉菜单
         */
        txtAccount_view = (TextView)findViewById(R.id.txtAccount);
        account_spinner = (Spinner) findViewById(R.id.account_spinner);
        account_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountList);
        account_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(account_adapter);
        account_spinner.setOnItemSelectedListener(new account_spinnerSelectedListener());
        account_spinner.setVisibility(View.VISIBLE);

        /**
         * 4、定义商家下拉菜单
         */
        txtShop_view = (TextView)findViewById(R.id.txtShop);
        shop_spinner = (Spinner) findViewById(R.id.shop_spinner);
        shop_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shopList);
        shop_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shop_spinner.setAdapter(shop_adapter);
        shop_spinner.setOnItemSelectedListener(new shop_spinnerSelectedListener());
        shop_spinner.setVisibility(View.VISIBLE);

        /**
         * 5、定义备注下拉菜单
         */
        txtNote_view = (TextView)findViewById(R.id.txtNote);
        note_spinner = (Spinner) findViewById(R.id.note_spinner);
        note_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, noteList);
        note_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        note_spinner.setAdapter(note_adapter);
        note_spinner
                .setOnItemSelectedListener(new note_spinnerSelectedListener());
        note_spinner.setVisibility(View.VISIBLE);
    }

    /**
     * 选择 类别 事件 监听器
     */
    class BigCategory_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtBigCategory = bigCategoryList[arg2];
            int pos = BigCategory_spinner.getSelectedItemPosition();
            subCategory_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, subCategory_info[pos]);
            subCategory_spinner.setAdapter(subCategory_adapter);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 子类别 事件 监听器
     */
    class subCategory_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtSubCategory = (String) subCategory_spinner
                    .getItemAtPosition(arg2);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 账户事件 监听器
     */
    class account_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtAccount = accountList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }

    /**
     * 选择 商家事件 监听器
     */
    class shop_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtShop = shopList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 商家事件 监听器
     */
    class note_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtNote = noteList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public void OnMySaveClick(View v) {
        saveInfo();
    }
    public void OnMyCancelClick(View v) {
        exit();
    }

    /**
     * cancel event
     */
    private void exit() {
        if(type != EDIT_MODE){
            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.setResult(RESULT_OK, getIntent());
            this.finish();
        }
    }

    /**
     * save event
     */
    private void saveInfo() {
        //Save之前先判断用户是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("nav_setting",Activity.MODE_PRIVATE);
        String userID =sharedPreferences.getString("userID", "");
        Log.i("info", "此次登录的用户是" + userID);

        if(userID.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您还未登录，请点击确定按钮进行登录！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);
                            Intent intent=new Intent(AddActivity.this,LoginActivity.class);
                            AddActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }else{
            if(value.equals("") || value == null || Double.parseDouble(value) <= 0){
                return;
            }

            //调用DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
            SQLiteDatabase db = helper.getWritableDatabase();
            //插入数据
            ContentValues values= new ContentValues();
            values.put("userID",userID);
            values.put("Type",type);
            values.put("incomeWay",txtAccount);
            values.put("incomeBy",txtShop);
            values.put("category",txtBigCategory);
            values.put("item",txtSubCategory);
            values.put("cost", value);
            values.put("note", txtNote);
            values.put("makeDate",PublicFunction.format(calendar.getTime()));
            long rowid = db.insert("basicCode_tb",null,values);
            Cursor c = db.query("basicCode_tb",null,"userID=?",new String[]{userID},null,null,null);
            if(c!=null && c.getCount() >= 1){
                String[] cols = c.getColumnNames();
                while(c.moveToNext()){
                    for(String ColumnName:cols){
                        Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
                    }
                }
                c.close();
            }
            db.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }
    }
}
