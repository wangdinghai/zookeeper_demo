package com.wangdinghai.testdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangdinghai.testdemo.util.HttpClientUtil;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class TestDemo {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        String get = HttpClientUtil.doGet("http://localhost:8080/ok", map);
        System.out.println("get请求调用成功，返回数据是：" + get);


        /*for (int i = 0; i < 100; i++) {
            try{
                createOrder(i);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }*/
        int orderId = 1000000;
        String orderInfo = getOrder(orderId);
        JSONObject orderInfoJson = JSONObject.parseObject(orderInfo);
        BigDecimal price = orderInfoJson.getBigDecimal("price");

        String result =  pay(orderId,price);
        JSONObject resultJson = JSON.parseObject(result);
        Boolean success = resultJson.getBoolean("success");
        if (success) {
            updateOrderStatus(orderId, "paid_success");
        } else {
            updateOrderStatus(orderId, "paid_fail");
        }

    }

    /**
     * 创建订单
     * @param id 订单序号
     */
    public static String getOrder(int id){
        Map<String, String> map = new HashMap<String, String>();

        map.put("orderName", " 订单_" + id);
        map.put("orderStatus", "unpaid");
        map.put("price", "1.00");

        String post = HttpClientUtil.doPost("http://localhost:8080/queryOrder/" + id, map, Charset.forName("UTF-8"));
        System.out.println("post调用成功，返回数据是：" + post);
        return post;
    }

    /**
     * 创建订单
     * @param id 订单序号
     */
    public static void createOrder(int id){
        Map<String, String> map = new HashMap<String, String>();

        map.put("orderName", " 订单_" + id);
        map.put("orderStatus", "unpaid");
        map.put("price", "1.00");

        String post = HttpClientUtil.doPost("http://localhost:8080/saveOrder", map, Charset.forName("UTF-8"));
        System.out.println("post调用成功，返回数据是：" + post);
    }

    /**
     * 创建订单
     * @param orderId 订单序号
     */
    public static void updateOrderStatus(int orderId, String orderStatus){
        Map<String, String> map = new HashMap<>();

        map.put("orderId", orderId + "");
        map.put("orderStatus", orderStatus);

        String post = HttpClientUtil.doPost("http://localhost:8081/updateOrderStatus", map, Charset.forName("UTF-8"));
        System.out.println("post调用成功，返回数据是：" + post);
    }

    /**
     * 付款接口
     * @param orderId
     * @param payAmount
     */
    public static String pay(int orderId, BigDecimal payAmount) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("payAmount", payAmount.toString());
        map.put("orderId", orderId + "");
        map.put("payStatus", "success");


        String post = HttpClientUtil.doPost("http://localhost:8081/pay", map, Charset.forName("UTF-8"));
        System.out.println("post调用成功，返回数据是：" + post);
        return post;
    }
}
