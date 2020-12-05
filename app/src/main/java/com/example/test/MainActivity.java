package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import com.lljjcoder.citypickerview.widget.CityPicker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.alibaba.fastjson.JSON;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_response;
    private EditText edit;
    private CityPicker mCP;
    private TextView address;
    Content ct=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.send_request);
        Button refresh =findViewById(R.id.refresh);
        Button research=findViewById(R.id.research);
        Button go=findViewById(R.id.go);
        Button concern=findViewById(R.id.concern);
        Button back=findViewById(R.id.back);
       go.setOnClickListener(this);
        refresh.setOnClickListener(this);
        sendRequest.setOnClickListener(this);
        research.setOnClickListener(this);
        concern.setOnClickListener(this);
        back.setOnClickListener(this);
        this.tv_response = findViewById(R.id.response);
        this.edit = findViewById(R.id.code);
        this.address=findViewById(R.id.address);
    }
    public String transString(String res){
        res=res.substring(res.indexOf('[')+1,res.indexOf(']'));
         ct=JSON.parseObject(res,Content.class);
        return ct.toString();
    }
    public void SaveWeather(String code,String responseStr){
        SharedPreferences sharedPreferences = getSharedPreferences("weather",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(code,responseStr);
        editor.commit();
    }
    public String FindWeather(String code){
        SharedPreferences sharedPreferences = getSharedPreferences("weather",MODE_PRIVATE);
        String str=sharedPreferences.getString(code,"");
        return str;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send_request:
                if(!FindWeather(edit.getText().toString()).equals(""))
                {
                    tv_response.setText(transString(FindWeather(edit.getText().toString())));
                ToastUtils.showToast(MainActivity.this, "由存储得到");
                    break;}
                //初始化OKHttp客户端
                OkHttpClient client = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city="+edit.getText()+"&key=7f4c7b2696eced7d5f25a562a641674a")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败处理
                        e.printStackTrace();
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //返回结果处理
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(responseStr.length()<100){
                                    ToastUtils.showToast(MainActivity.this, "城市码输入有误");
                                }
                                else {
                                    tv_response.setText(transString(responseStr));
                                       SaveWeather(edit.getText().toString(),responseStr);
                                }

                            }

                        });
                    }
                });
                break;
             case R.id.refresh:

                OkHttpClient client1 = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request1 = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city="+edit.getText()+"&key=7f4c7b2696eced7d5f25a562a641674a")
                        .build();
                Call call1 = client1.newCall(request1);
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败处理
                        e.printStackTrace();
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //返回结果处理
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    tv_response.setText(transString(responseStr));
                                    SaveWeather(edit.getText().toString(),responseStr);
                                    ToastUtils.showToast(MainActivity.this, "已刷新");


                            }

                        });
                    }
                });
                break;
                case R.id.research:
                 initCityPicker();
                 mCP.show();
                 break;
            case R.id.go:
                     search();
                 break;
            case R.id.concern:
                if(ct.equals(null)) {ToastUtils.showToast(MainActivity.this, "无法关注");break;}
                SharedPreferences sharedPreferences = getSharedPreferences("concern",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ct.getProvince()+" "+ct.getCity(),ct.getAdcode());
                editor.commit();
                ToastUtils.showToast(MainActivity.this, "关注成功"+ct.getCity());
                break;
            case R.id.back:
                Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                startActivity(intent);
                break;
        }

    }
    public void search(){
        OkHttpClient client2 = new OkHttpClient();
        //构造Request对象
        //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
        Request request2 = new Request.Builder().get()
                .url("https://restapi.amap.com/v3/geocode/geo?address="+address.getText().toString()+"&output=XML&key=7f4c7b2696eced7d5f25a562a641674a")
                .build();
        Call call2 = client2.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败处理
                e.printStackTrace();
                ToastUtils.showToast(MainActivity.this, "Get请求失败");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //返回结果处理
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int index=responseStr.indexOf("adcode");
                        String str=responseStr.substring(index+7,index+13);
                        edit.setText(str);

                    }

                });
            }
        });
    }
    public void initCityPicker() {
        //滚轮文字的大小
        //滚轮文字的颜色
        //省份滚轮是否循环显示
        //城市滚轮是否循环显示
        //地区（县）滚轮是否循环显示
        //滚轮显示的item个数
        //滚轮item间距
        mCP = new CityPicker.Builder(MainActivity.this)
                .textSize(20)//滚轮文字的大小
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();

        //监听方法，获取选择结果
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];

               address.setText(province + city + district);

            }

            @Override
            public void onCancel() {

            }
        });
    }


}