package com.codido.nymeria.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.codido.nymeria.R;
import com.codido.nymeria.util.DownLoadAppFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载service
 */
public class DownLoadAppUpdateService extends Service {

    public static final String Install_Apk = "Install_Apk";
    private static final int down_step_custom = 1;

    private static final int TIMEOUT = 10 * 1000;
    private static String down_url;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private RemoteViews contentView;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            app_name = intent.getStringExtra("Key_App_Name");
            down_url = intent.getStringExtra("Key_Down_Url");

            DownLoadAppFileUtil.createFile(app_name);

            if (DownLoadAppFileUtil.isCreateFileSucess == true) {
                createNotification();
                createThread();
            } else {
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 打开通知
     */
    public void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        contentView = new RemoteViews(getPackageName(), R.layout.notifycation);
        contentView.setTextViewText(R.id.notificationTitle, app_name + "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        builder.setContent(contentView);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        notification = builder.build();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.notifycation, notification);
    }


    /**
     * 初始化下载线程
     */
    public void createThread() {
        new DownLoadThread().start();
    }


    /**
     * 处理handler
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    File file = DownLoadAppFileUtil.updateFile;
                    Uri uri = Uri.fromFile(DownLoadAppFileUtil.updateFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(DownLoadAppUpdateService.this, "com.codido.nymeria.fileProvider", file);
                        // 给目标应用一个临时授权
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    pendingIntent = PendingIntent.getActivity(DownLoadAppUpdateService.this, 0, intent, 0);

                    notification.flags = Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL;
                    //notification.setLatestEventInfo(DownLoadAppUpdateService.this, app_name, "下载成功", pendingIntent);
                    PendingIntent pendingIntent = PendingIntent.getActivity(DownLoadAppUpdateService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    notification.contentIntent = pendingIntent;
                    notificationManager.notify(R.layout.notifycation, notification);

                    installApk();

                    /*** stop service *****/
                    stopSelf();
                    break;

                case DOWN_ERROR:
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    //notification.setLatestEventInfo(DownLoadAppUpdateService.this, app_name, "下载失败", null);

                    /*** stop service *****/
                    stopSelf();
                    break;

                default:
                    /****** Stop service ******/
                    break;
            }
        }
    };

    /**
     * 下载线程
     */
    private class DownLoadThread extends Thread {
        @Override
        public void run() {
            Message message = new Message();
            try {
                long downloadSize = downloadUpdateFile(down_url,
                        DownLoadAppFileUtil.updateFile.toString());
                if (downloadSize > 0) {
                    // down success
                    message.what = DOWN_OK;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 安装apk
     */
    private void installApk() {
        Uri uri = Uri.fromFile(DownLoadAppFileUtil.updateFile);
        File file = DownLoadAppFileUtil.updateFile;
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(DownLoadAppUpdateService.this, "com.codido.nymeria.fileProvider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        DownLoadAppUpdateService.this.startActivity(intent);
    }


    /***
     * down file
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file) throws Exception {
        int down_step = down_step_custom;
        int totalSize;
        int downloadCount = 0;
        int updateCount = 0;

        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        totalSize = httpURLConnection.getContentLength();

        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);

        byte buffer[] = new byte[1024];
        int readsize = 0;

        while ((readsize = inputStream.read(buffer)) != -1) {

            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                notification.contentView = contentView;
                notificationManager.notify(R.layout.notifycation, notification);
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;
    }

}