package com.example.xulinchao.webviewtest;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulinchao on 2017/4/27.
 */

public class MyHander extends DefaultHandler {
    private List<Food> foodList;
    private StringBuilder name ;
    private StringBuilder price;
    private StringBuilder description;
    private String localName;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("name".equals(localName)) {
            name.append(ch, start, length);
        } else if ("price".equals(localName)) {
            price.append(ch, start, length);
        } else if ("description".equals(localName)) {
            description.append(ch, start, length);

        }
    }

    @Override
    public void startDocument() throws SAXException {
        foodList = new ArrayList<>();
        name = new StringBuilder();
        price = new StringBuilder();
        description = new StringBuilder();
        localName = null;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.localName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!TextUtils.isEmpty(name.toString().trim()) &&
                !TextUtils.isEmpty(price.toString().trim())
                && !TextUtils.isEmpty(description.toString().trim())) {
            Log.e("xulinchao", "endElement: "+name.toString().trim());
            Food food=new Food();
            food.setName(name.toString().trim());
            food.setPrice(price.toString().trim());
            food.setDescription(description.toString().trim());
            foodList.add(food);
            name.setLength(0);
            price.setLength(0);
            description.setLength(0);
        }
    }
    public List<Food> getFood(){
        return foodList;
    }
}
