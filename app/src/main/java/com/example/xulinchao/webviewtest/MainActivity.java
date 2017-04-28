package com.example.xulinchao.webviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
// httpUrlConnection 步骤,先获取定义Url对象，从Url对象中获取httpUrlConnection对象，接下来就是设置获取数据流了

public class MainActivity extends AppCompatActivity {
    //    private WebView view;
    private TextView webText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webText = (TextView) findViewById(R.id.web_text);
//        view=(WebView)findViewById(R.id.web);
//        view.getSettings().setJavaScriptEnabled(true);
//        view.setWebViewClient(new WebViewClient());
//        view.loadUrl("http://www.baidu.com");
    }

    public void WebOnClick(View view) {
        switch (view.getId()) {
            case R.id.go_search:
//                sendRequestWithHttpUrlConnection();
                useOKhttp();
                break;
            case R.id.go_food:
                startActivity(new Intent(MainActivity.this,ParseXMLWithPullActivity.class));
                break;
            case R.id.go_check:
                startActivity(new Intent(MainActivity.this,CheckExpressActivity.class));
                break;
        }
    }
    // Okhttp步骤，先搞一个client ，再搞得request，根据client和request生出来一个response。
    public  void useOKhttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder().url("http://www.w3school.com.cn/example/xmle/simple.xml").build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    showResponse(responseData);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void sendRequestWithHttpUrlConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://www.baidu.com/");
                    Log.e("xulinchao", "run: start");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    Log.e("xulinchao", "run: 1");
                    while ((line = reader.readLine()) != null) {
                        Log.e("xulinchao", "run: 2");
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }

    public void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webText.setText(response);
            }
        });

    }
}
