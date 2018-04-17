package com.strive.rich.controller;

import com.alibaba.fastjson.JSONObject;
import com.strive.rich.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by developer on 4/13/18.
 */
@RestController
@RequestMapping("/goods")
public class GoodController {
    @Value("${connect.proxySwitch}")
    private boolean proxySwitch;

    @Value("${connect.proxyIp}")
    private String proxyIp;

    @Value("${connect.proxyPort}")
    private int proxyPort;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    static String baseUri = "https://buff.163.com/api/market/goods/price_history?game=pubg&goods_id=";

    @RequestMapping(value = "/{id}/prices", method = RequestMethod.GET)
    public String getGoodPrices(@PathVariable String id) {
        String result = HttpClientUtil.get(baseUri + id, proxySwitch, proxyIp, proxyPort);
        logger.info(result);
        return result;
    }

    @RequestMapping(value = "/{id}/gift-id", method = RequestMethod.GET)
    public String getGiftIds(@PathVariable String id) {

        String result = HttpClientUtil.get("https://buff.163.com/market/goods?goods_id=" + id, proxySwitch, proxyIp, proxyPort);
        String strArr[] = result.split("\n");
        String ids[] = new String[3];
        int num = 0;
        String findStr = "/market/goods?goods_id=";
        for (String str : strArr) {
            if (num < 3 && str.contains(findStr)) {
                String giftId = str.substring(str.indexOf(findStr) + findStr.length(), str.indexOf(findStr) + findStr.length() + 7);
                giftId = giftId.replace("\"", "");
                ids[num] = giftId;
                num = num + 1;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ids", ids);
        return jsonObject.toString();
    }

    public static void main(String[] args) {
/*        GoodController goodController = new GoodController();
        String prices = goodController.getGoodPrices("756056");
        System.out.println(prices);*/
/*
        String content = HttpClientUtil.get("https://buff.163.com/api/market/goods?game=pubg&page_num=1&category_group=weapons&sort_by=price.desc&_=1523254920175", true, "10.22.98.21", 8080);
        System.out.println(content);*/
    }
}
