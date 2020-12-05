package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PickActivity  extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_response;
   String code;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);



        Button refresh =findViewById(R.id.refresh);
        Button back=findViewById(R.id.back);
        refresh.setOnClickListener(this);
        back.setOnClickListener(this);
        this.tv_response = findViewById(R.id.response);
        Intent intent=getIntent();
        code=intent.getStringExtra("value");
        tv_response.setText(transString(FindWeather(code)));
        ToastUtils.showToast(PickActivity.this, "由存储得到");
    }
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.back:
                    Intent intent=new Intent(PickActivity.this,ShowActivity.class);
                    startActivity(intent);
                    break;
                case R.id.refresh:

                    OkHttpClient client1 = new OkHttpClient();
                    //构造Request对象
                    //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                    Request request1 = new Request.Builder().get()
                            .url("https://restapi.amap.com/v3/weather/weatherInfo?city="+code+"&key=7f4c7b2696eced7d5f25a562a641674a")
                            .build();
                    Call call1 = client1.newCall(request1);
                    call1.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //失败处理
                            e.printStackTrace();
                            ToastUtils.showToast(PickActivity.this, "Get请求失败");
                        }
                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            //返回结果处理
                            final String responseStr = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    tv_response.setText(transString(responseStr));
                                    SaveWeather(code,responseStr);
                                    ToastUtils.showToast(PickActivity.this, "已刷新");


                                }

                            });
                        }
                    });
                    break;
            }
        }
    public String FindWeather(String code){
        SharedPreferences sharedPreferences = getSharedPreferences("weather",MODE_PRIVATE);
        String str=sharedPreferences.getString(code,"");
        return str;
    }
    public String transString(String res){
        res=res.substring(res.indexOf('[')+1,res.indexOf(']'));
        Content ct= JSON.parseObject(res,Content.class);
        return ct.toString();
    }
    public void SaveWeather(String code,String responseStr){
        SharedPreferences sharedPreferences = getSharedPreferences("weather",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(code,responseStr);
        editor.commit();
    }
}

