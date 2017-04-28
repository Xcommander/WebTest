package com.example.xulinchao.webviewtest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckExpressActivity extends AppCompatActivity {
    private TextView dest;
    private SharedPreferences.Editor editor;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_express);
        dest=(TextView)findViewById(R.id.dest);
        sf=getSharedPreferences("config",MODE_PRIVATE);
        editor=sf.edit();
        load();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    public void save(){
        editor.putString("com",((EditText) findViewById(R.id.express_com)).getText().toString());
        editor.putString("num",((EditText) findViewById(R.id.express_num)).getText().toString());
        editor.apply();
    }
    public void load(){
        String com=sf.getString("com","");
        String num=sf.getString("num","");
        ((EditText) findViewById(R.id.express_com)).setText(com);
        ((EditText) findViewById(R.id.express_num)).setText(num);

    }

    public void CheckOnClick(View view) {
        switch (view.getId()) {
            case R.id.chaxun:
                String com = ((EditText) findViewById(R.id.express_com)).getText().toString();
                String num = ((EditText) findViewById(R.id.express_num)).getText().toString();
                if (!TextUtils.isEmpty(com)
                        &&
                        !TextUtils.isEmpty(num)) {
                    String uri = "http://www.kuaidi100.com/query?type=" + com + "&postid=" + num;

                    sendRequestWithOkhttp(uri);
                }else{
                    Toast.makeText(CheckExpressActivity.this,"公司或者订单号不能为空",Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    public void sendRequestWithOkhttp(final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(uri).build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    parseGson(data);
//                    parseJsonWithJSONObect(data);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void parseJsonWithJSONObect(String data) {
        String message = "";
        String data1 = "";
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder context = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(data);
            message = jsonObject.getString("message");
            data1 = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data1)) {
                stringBuilder = parseData(data1);
                if("ok".equals(message)){
                    message="已签收";

                }else{
                    message="未签收";

                }
                context.append(message+"\n");
                context.append(stringBuilder.toString());
                updateContext(context.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public StringBuilder parseData(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String time = jsonObject.getString("time");
                String content = jsonObject.getString("context");
                stringBuilder.append(time + "    " + content + "\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuilder;


    }
    public void updateContext(final String context){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dest.setText(context);
            }
        });
    }
    public void parseGson(String data){
        MainMessage mainMessage=new Gson().fromJson(data,MainMessage.class);
        StringBuilder stringBuilder=new StringBuilder();
        List<TwoMessage> twoMessages=mainMessage.getData();
        if("ok".equals(mainMessage.getMessage())){
            mainMessage.setMessage("已签收");

        }else{
            mainMessage.setMessage("未签收");

        }
        stringBuilder.append(mainMessage.getMessage()+"\n");
        for (TwoMessage t:
                twoMessages
             ) {
            stringBuilder.append(t.getTime()+"    "+t.getContext()+"\n");

        }
        updateContext(stringBuilder.toString());



    }

}
