package rupik.com.bengali_calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

/**
 * Created by BoomPc on 17-10-2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        Uri notificationR = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(context, notificationR);
        r.play();
//        Vibrator vibrator = (Vibrator)context
//                .getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(5000);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        String dateStr = arg1.getStringExtra("engDate");
        String occasionStr = arg1.getStringExtra("occasionName");

        Notification notification = builder.setContentTitle("Bengali Calendar")
                .setContentText(occasionStr)
                .setTicker(dateStr)
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
