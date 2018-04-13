package com.strive.rich.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 2/27/17.
 */
@Component
public class SynchronizeAssetDataSchedule {
//    @Autowired
//    private AssetInfoDao assetInfoDao;

//    @Value("${asset.pageSize}")
//    private int pageSize;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeAssetDataSchedule.class);

    private HashMap<Integer, Double> initGiftMap = new HashMap<Integer, Double>();
    FileOutputStream outputStream = null;

    @Scheduled(fixedRate = 50000)
    public void loadAssetInfo() {
        try {
            Map<Integer, Double> giftMap = getInitGiftMap();

            FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/rich/chiji.txt"));
            //返回全部内容
            final String schemaString = IOUtils.toString(fileInputStream, "UTF-8");
            JSONArray weaponsArray = JSON.parseArray(schemaString);
            System.out.println(weaponsArray.toJSONString());
            IOUtils.closeQuietly(fileInputStream);

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
            outputStream = new FileOutputStream(new File("/home/jason/Desktop/rich/rich-" +
                    format.format(new Date().getTime()) + ".txt"));
        }
        return outputStream;
    }

    public Map<Integer, Double> getInitGiftMap() {
        if (CollectionUtils.isEmpty(initGiftMap.entrySet())) {
            initGiftMap.put(756042, 10 * 0.01);
            initGiftMap.put(756044, 10 * 0.01);
            initGiftMap.put(756035, 10 * 0.01);
            initGiftMap.put(756026, 15 * 0.01);
            initGiftMap.put(756046, 1.3 * 0.01);
            initGiftMap.put(756037, 5 * 0.01);
            initGiftMap.put(756027, 4.5 * 0.01);
            initGiftMap.put(756056, 0.16 * 0.01);
            initGiftMap.put(756041, 1.3 * 0.01);
            initGiftMap.put(756047, 0.32 * 0.01);
            initGiftMap.put(756051, 0.6 * 0.01);
            initGiftMap.put(756053, 0.32 * 0.01);
            initGiftMap.put(756049, 2.5 * 0.01);
            initGiftMap.put(756040, 10 * 0.01);
            initGiftMap.put(756030, 4.5 * 0.01);
            initGiftMap.put(756039, 5 * 0.01);
            initGiftMap.put(756032, 4.5 * 0.01);
            initGiftMap.put(756029, 15 * 0.01);
        }

        return initGiftMap;
    }


}