package com.chemander.story.data;

import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadJSON {
    /*public static List<Chapter> readChapterFromJSONFile(Context context) {
        try {
            String jsonText = readText(context, R.raw.data);
            JSONObject jsonObject = new JSONObject(jsonText);
            JSONArray jsonArray = jsonObject.getJSONArray("story");
            List<Chapter> chapters = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String chapter = jsonObject1.getString("chapter");
                String content = jsonObject1.getString("content");
                Chapter newChapter = new Chapter(chapter, content);
                chapters.add(newChapter);
                chapters.add(newChapter);
            }
            return chapters;
        } catch (JSONException e) {
            throw new RuntimeException("JSONException" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("IOException" + e.getMessage());
        }finally {
//            return null;
        }
    }*/

    private static String readText(Context context, int data) throws IOException {
        InputStream is = context.getResources().openRawResource(data);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s=br.readLine())!= null){
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

}
