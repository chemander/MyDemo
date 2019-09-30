package com.chemander.story.offline.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.chemander.story.R;
import com.chemander.story.data.database.AppDatabase;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.GetChapterInformation;
import com.chemander.story.data.model.GetChaptersInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.utils.ApiUtils;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "Download";
    private static final String CHANNEL_NAME = "Download";
    private static final String CHANNEL_DESCRIPTION = "Download a story";
    String GROUP_KEY_WORK_EMAIL = "NotificationService";
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    AppDatabase appDatabase;
    StoryService storyService;
    StoryInformation storyInformation;
    List<ChapterInformation> chapters = new ArrayList<>();
    int serviceID = new Random().nextInt(100);
//    int serviceID = 100;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        appDatabase = AppDatabase.getAppDatabase(getApplication());
        storyService = ApiUtils.getStoryService();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
//        storyInformation = (StoryInformation) get
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            stopSelf();
        }else {
            storyInformation = (StoryInformation) intent.getSerializableExtra(SettingsManager.STORY_INFORMATION);
            Log.d("Hung", "Total downloaded chapter-" + appDatabase.recentDao().loadAllChapterDetails(storyInformation.getStoryID()).length);
            if (storyInformation == null) {
                stopSelf();
            }
            loadChapters();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        notificationBuilder.setOngoing(false);
//        notificationManager.notify(100, notificationBuilder.build());
        super.onDestroy();
    }

    public void loadChapters(){
        Toast.makeText(this, "Đang tải...", Toast.LENGTH_SHORT).show();
        Log.d("Hung", "getStoryID - "+storyInformation.getStoryID());
        storyService.getChapters(storyInformation.getStoryID(), 1,10000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
                    List<ChapterInformation> temp = response.body().getData();
                    Collections.sort(temp, new Comparator<ChapterInformation>() {
                        @Override
                        public int compare(ChapterInformation chapterInformation, ChapterInformation t1) {
                            if(chapterInformation.getChapterNum() > t1.getChapterNum()){
                                return 1;
                            }else return -1;
                        }
                    });
                    chapters.addAll(temp);
                    showNotification();
                    startDownload();
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
//
            }
        });
    }

    public void startDownload(){
        DownloadChapters downloadChapters = new DownloadChapters();
        downloadChapters.execute();
        Log.d("Hung", "Start download");
    }

    public void showNotification(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
//        }else {
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
//        }

        notificationBuilder.setSmallIcon(R.drawable.ic_download_content)
                .setContentTitle(storyInformation.getStoryName())
                .setContentText("0/"+chapters.size())
                .setOngoing(true)
                .setProgress(chapters.size(), 0, false)
                .build();
        Notification notification = notificationBuilder.build();
        notificationManager.notify(serviceID, notification);
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
        startForeground(serviceID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notificationBuilder behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    class DownloadChapters extends AsyncTask<Void, Integer, Void>{
        int progress = 0;
        boolean isEnd = false;
        @Override
        protected Void doInBackground(Void... voids) {
            Callback<GetChapterInformation> callback = new Callback<GetChapterInformation>() {
                @Override
                public synchronized void onResponse(Call<GetChapterInformation> call, Response<GetChapterInformation> response) {
                    if(response.isSuccessful() && !isEnd){
                        if(response.body().getChapterDetail()!=null){
//                            Log.d("Hung", progress+"- Insert - "+appDatabase.recentDao().insertChapterDetail(response.body().getChapterDetail())+"-into-"+response.body().getChapterDetail().getChapterNum());
                            appDatabase.recentDao().insertChapterDetail(response.body().getChapterDetail());
                            if(progress == chapters.size()) {
                            isEnd = true;
                            }
                            publishProgress(progress);
                        }
                    }else{
                        return;
                    }
                }

                @Override
                public void onFailure(Call<GetChapterInformation> call, Throwable t) {
                }
            };

             for (int i = 0; i < chapters.size(); i++){
                final ChapterInformation chapter = chapters.get(i);
                progress = i+1;
                 storyService.getChapterDetail(chapter.getId()).enqueue(callback);
                 SystemClock.sleep(10);
                /*if(i > (chapters.size() - 5)){
                    SystemClock.sleep(500);
                }*/
            }
            return null;
        }

        @Override
        protected synchronized void onProgressUpdate(Integer... values) {
//            notificationBuilder
            super.onProgressUpdate(values);
            notificationBuilder.setProgress(chapters.size(), values[0], false);
            notificationBuilder.setContentText(values[0]+"/"+chapters.size());
            notificationManager.notify(serviceID, notificationBuilder.build());
//            Log.d("Hung", "values[0] -" + values[0]);
            if(values[0] == chapters.size()){
                Log.d("Hung", "values[0] -" + values[0]);
//                notificationBuilder.setOngoing(false).setProgress(0, 0, false).setContentText("Tải về thành công!");
                onPostExecute(null);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isEnd) {
                notificationBuilder.setOngoing(false).setProgress(0, 0, false).setContentText("Tải về thành công!");
                Notification notification = notificationBuilder.build();
                notificationManager.notify(serviceID, notification);
                Log.d("Hung", "getChapters - "+chapters.size());
                Log.d("Hung", "Total chapter-" + appDatabase.recentDao().loadAllChapterDetails(storyInformation.getStoryID()).length);
//            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
                stopForeground(false);
            stopSelf();
            }
        }
    }
}
