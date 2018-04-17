package com.strive.rich.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.strive.rich.http.HttpClientUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 2/27/17.
 */
@Component
public class SynchronizeAssetDataSchedule {
//    @Autowired
//    private AssetInfoDao assetInfoDao;

    @Value("${result.output}")
    private String output;


    @Value("${gifts.input}")
    private String giftsInput;


    @Value("${weapons.url}")
    private String url;

    @Value("${connect.proxySwitch}")
    private boolean proxySwitch;

    @Value("${connect.proxyIp}")
    private String proxyIp;

    @Value("${connect.proxyPort}")
    private int proxyPort;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeAssetDataSchedule.class);

    private HashMap<String, Double> initGiftMap = new HashMap<String, Double>();
    FileOutputStream outputStream = null;

    @Scheduled(fixedRate = 50000)
    public void loadAssetInfo() {
        try {
            Map<String, Double> giftMap = getInitGiftMap();

            /*fileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/rich/chiji.txt"));
            //返回全部内容
            final String schemaString = IOUtils.toString(fileInputStream, "UTF-8");
            JSONArray weaponsArray = JSON.parseArray(schemaString);
            System.out.println(weaponsArray.toJSONString());
            IOUtils.closeQuietly(fileInputStream);*/

            double averagePrice = 0;

            String[] urls = url.split(",");

            for (String url : urls) {
                String content = HttpClientUtil.get(url, proxySwitch, proxyIp, proxyPort);

                JSONObject weaponsPage = JSON.parseObject(content);
                JSONObject data = weaponsPage.getJSONObject("data");
                JSONArray weaponitems = data.getJSONArray("items");

                for (Object weaponitem : weaponitems) {
                    JSONObject weaponJson = (JSONObject) weaponitem;
                    String id = weaponJson.getString("id");

                    if (giftMap.containsKey(id)) {
                        Double probability = giftMap.get(id);
                        String sellMinPrice = ((JSONObject) weaponitem).getString("sell_min_price");
                        averagePrice = averagePrice + Double.parseDouble(sellMinPrice) * probability;
                    }
                }
            }

            outputStream = getInitOutputStream();

            String now = simpleDateFormat.format(new Date().getTime());
            String str = now + " ====== " + averagePrice;

            IOUtils.write(str, outputStream);
            IOUtils.write("\r\n", outputStream);

            System.out.println(averagePrice);

        } catch (Exception e) {
            logger.info("the third-tools-backend server get asset info failed:{}", e);
        }
    }

    private FileOutputStream getInitOutputStream() throws FileNotFoundException {
        if (outputStream == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            outputStream = new FileOutputStream(new File(output + format.format(new Date().getTime()) + ".txt"));
        }
        return outputStream;
    }

    public Map<String, Double> getInitGiftMap() throws IOException {
        if (CollectionUtils.isEmpty(initGiftMap.entrySet())) {
            FileInputStream fileInputStream = new FileInputStream(new File(giftsInput));
            //返回全部内容
            final String schemaString = IOUtils.toString(fileInputStream, "UTF-8");
            JSONObject jsonObject = JSON.parseObject(schemaString);

            IOUtils.closeQuietly(fileInputStream);

            Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                initGiftMap.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
            }
        }

        return initGiftMap;
    }


}