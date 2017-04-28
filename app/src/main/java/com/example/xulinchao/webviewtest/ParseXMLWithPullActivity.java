package com.example.xulinchao.webviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ParseXMLWithPullActivity extends AppCompatActivity {
    private List<Food> foodList = new ArrayList<>();
    private Food food;
    private String name = null;
    private String price = null;
    private String description = null;
    private RecyclerView recyclerView;
    FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_xmlwith_pull);
        recyclerView = (RecyclerView) findViewById(R.id.get_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ParseXMLWithPullActivity.this);
        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodAdapter);
    }

    public void ParseOnClick(View view) {
        switch (view.getId()) {
            case R.id.parse_xml:
                foodList.clear();
                sendRequestWithOkHttp();
                break;
        }
    }

    /************************启动Okhttp来访问----start********************************/
    public void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://www.w3school.com.cn/example/xmle/simple.xml").build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
//                    parseXMLWithPull(data);
                    parseXMLWithSax(data);
                    updateList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /************************启动Okhttp来访问----end********************************/
    /************************pull解析xml----start********************************/
    // 分析xml,最主要是记得标签属性和标签的值
    // pull分析是一个树结构。一个个节点来分析的。
    public void parseXMLWithPull(String data) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(data));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("price".equals(nodeName)) {
                            price = xmlPullParser.nextText();

                        } else if ("description".equals(nodeName)) {
                            description = xmlPullParser.nextText();

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (("food").equals(nodeName)) {
                            if (name != null && price != null && description != null) {
                                food = new Food();
                                food.setName(name);
                                food.setPrice(price);
                                food.setDescription(description);
                                foodList.add(food);
                                name = null;
                                price = null;
                                description = null;
                            }


                        }
                        break;

                }
                eventType = xmlPullParser.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /************************pull解析xml----end********************************/
    /************************sax解析xml----start********************************/
    public void parseXMLWithSax(String data) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            MyHander hander = new MyHander();
            xmlReader.setContentHandler(hander);
            Log.e("xulinchao", "parseXMLWithSax: ");
            xmlReader.parse(new InputSource(new StringReader(data)));


            for (Food f:hander.getFood()
                 ) {
                foodList.add(f);
                Log.e("xulinchao", "name = : "+f.getName());

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /************************sax解析xml----end********************************/
    /*************************更新recyclerView---start*************************/
    public void updateList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("xulinchao", "run: " +foodList.size());
                //更新recyclerview，先更新adapter，再更新recyclerview，这样看起来有效果
                foodAdapter.notifyItemChanged(foodList.size()-1);
                recyclerView.scrollToPosition(foodList.size()-1);
//                FoodAdapter foodAdapter=new FoodAdapter(foodList);
//                recyclerView.setAdapter(foodAdapter);

            }
        });
    }
    /*************************更新recyclerView---end*************************/
}
