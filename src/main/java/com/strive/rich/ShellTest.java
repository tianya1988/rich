package com.strive.rich;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;

/**
 * Created by jason on 18-4-9.
 */
public class ShellTest {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/chiji.txt"));
        //返回全部内容
        final String schemaString = IOUtils.toString(fileInputStream, "UTF-8");
        JSONArray weaponsArray = JSON.parseArray(schemaString);
        System.out.println(weaponsArray.toJSONString());
        IOUtils.closeQuietly(fileInputStream);

        HashMap<Integer, Double> giftMap = new HashMap<Integer, Double>();
        giftMap.put(756042, 10 * 0.01);
        giftMap.put(756044, 10 * 0.01);
        giftMap.put(756035, 10 * 0.01);
        giftMap.put(756026, 15 * 0.01);
        giftMap.put(756046, 1.3 * 0.01);
        giftMap.put(756037, 5 * 0.01);
        giftMap.put(756027, 4.5 * 0.01);
        giftMap.put(756056, 0.16 * 0.01);
        giftMap.put(756041, 1.3 * 0.01);
        giftMap.put(756047, 0.32 * 0.01);
        giftMap.put(756051, 0.6 * 0.01);
        giftMap.put(756053, 0.32 * 0.01);
        giftMap.put(756049, 2.5 * 0.01);
        giftMap.put(756040, 10 * 0.01);
        giftMap.put(756030, 4.5 * 0.01);
        giftMap.put(756039, 5 * 0.01);
        giftMap.put(756032, 4.5 * 0.01);
        giftMap.put(756029, 15 * 0.01);

        double averagePrice = 0;

        for (Object o : weaponsArray) {

            JSONObject weaponsPage = (JSONObject) o;

            JSONObject data = weaponsPage.getJSONObject("data");
            JSONArray weaponitems = data.getJSONArray("items");

            for (Object weaponitem : weaponitems) {
                JSONObject weaponJson = (JSONObject) weaponitem;
                Integer id = weaponJson.getInteger("id");

                if (giftMap.containsKey(id)) {
                    Double probability = giftMap.get(id);
                    String sellMinPrice = ((JSONObject) weaponitem).getString("sell_min_price");
                    averagePrice = averagePrice + Double.parseDouble(sellMinPrice) * probability;
                }
            }
        }

        System.out.println(averagePrice);
    }
}
