package net.http;


import android.util.Log;

import net.entity.DataEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class NetUtils {


    // 读取整个文件内容
    public static String getUrlContent(String urlStr) {
        String ret = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer buffer = new StringBuffer();
            char[] buff = new char[1024 * 4];
            int len;
            while ((len = reader.read(buff)) > 0) {
                buffer.append(buff, 0, len);
            }
            is.close();
            reader.close();
            ret = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ret;
    }

    public static ArrayList<DataEntity> get(String json) {
        ArrayList<DataEntity> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String next = iterator.next();
                JSONObject jsonObject1 = jsonObject.getJSONObject(next);
                DataEntity entity = new DataEntity();
                entity.setTitle(jsonObject1.getString("title"));
                entity.setLat(jsonObject1.getDouble("lat"));
                entity.setLng(jsonObject1.getDouble("lng"));
                entity.setKey(next);
                entity.setPanoid(jsonObject1.getString("panoid"));
                if (entity.getPanoid().equals("LiAWseC5n46JieDt9Dkevw")) {
                    continue;
                }
                if (jsonObject1.has("isFife")) {
                    entity.setFife(jsonObject1.getBoolean("isFife"));
                    continue;
                }
                if (entity.getFife()) {
                    entity.setImageUrl("https://lh4.googleusercontent.com/" + entity.getPanoid() + "/w400-h300-fo90-ya0-pi0/");
                    continue;
                } else {
                    entity.setImageUrl("https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + jsonObject1.getString("panoid"));
                }

                result.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("xxxxxxH", "e = " + e.toString());
        }
        return result;
    }

    public static ArrayList<DataEntity> getSmall(DataEntity big, String json) {
        ArrayList<DataEntity> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String next = iterator.next();
                JSONObject jsonObject1 = jsonObject.getJSONObject(next);
                DataEntity entity = new DataEntity();
                entity.setTitle(jsonObject1.getString("title"));
                entity.setLat(jsonObject1.getDouble("lat"));
                entity.setLng(jsonObject1.getDouble("lng"));
                entity.setPannoId(jsonObject1.getString("panoid"));
                if (big.getFife()) {
                    entity.setImageUrl("https://lh4.googleusercontent.com/" + entity.getPannoId() + "/w400-h300-fo90-ya0-pi0/");

                } else {
                    entity.setImageUrl("https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + jsonObject1
                            .getString("panoid"));
                }
                result.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("xxxxxxH", "e = " + e.toString());
        }

        return result;
    }

}
