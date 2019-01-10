package ch.heigvd.iict.sym.sym_labo4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private static final int NOTIFICATION_SIMPLE_ID = 1;
    private static final int NOTIFICATION_ACTION_ID = 2;
    private static final int NOTIFICATION_ACTION_ID_OK = 21;
    private static final int NOTIFICATION_ACTION_ID_NO = 22;
    private static final int NOTIFICATION_ACTION_ID_MAYBE = 23;
    private static final int NOTIFICATION_WEARABLE_ID = 3;
    private static final int NOTIFICATION_WEARABLE_ID_END = 31;
    private static final int NOTIFICATION_WEARABLE_ID_CONTINUE = 32;
    private static final int NOTIFICATION_WEARABLE_ID_CALL = 33;


    private static final String CHANNEL_ID = "labo4";


    private Button BtnSimpleNotification = null;
    private Button BtnNotification = null;
    private Button BtnWearableNotification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if(getIntent() != null)
            onNewIntent(getIntent());

        /* A IMPLEMENTER */

        BtnSimpleNotification = findViewById(R.id.btSimpleNotif);
        BtnNotification = findViewById(R.id.btNotifBt);
        BtnWearableNotification = findViewById(R.id.btNotifWearOnly);

        //create a channel for notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Labo4";
            String description = "Channel pour les notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        BtnSimpleNotification.setOnClickListener(v -> {

            PendingIntent viewPendingIntent = createPendingIntentSimple(NOTIFICATION_SIMPLE_ID, "Il y un truc qui se passe!");

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_directions_bike_black_18dp)
                            .setContentTitle("wearTitle")
                            .setContentText("Fais Quelque chose!")
                            .setContentIntent(viewPendingIntent);

            // Get an instance of the NotificationManager service
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(getApplicationContext());

            // Issue the notification with notification manager.
            notificationManager.notify(NOTIFICATION_SIMPLE_ID, notificationBuilder.build());

        });

        BtnNotification.setOnClickListener(v -> {

            // Build intent for notification content
            PendingIntent oui = createPendingIntentActions(NOTIFICATION_ACTION_ID_OK, "look on your pocket");
            PendingIntent non = createPendingIntentActions(NOTIFICATION_ACTION_ID_NO, "Bravissimo !!");
            PendingIntent pe = createPendingIntentActions(NOTIFICATION_ACTION_ID_MAYBE, "Cool !");

            NotificationCompat.Action ouiAc = new NotificationCompat.Action(R.drawable.ic_message_bulleted_black_18dp, "OUI", oui);
            NotificationCompat.Action nonAc = new NotificationCompat.Action(R.drawable.ic_alert_white_18dp, "NON", non);
            NotificationCompat.Action peAc = new NotificationCompat.Action(R.drawable.ic_alert_black_18dp, "OUPS", pe);
            // Notification channel ID is ignored for Android 7.1.1
            // (API level 25) and lower.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_directions_car_black_18dp)
                    .setContentTitle("LOST")
                    .setContentText("Tu as pas oublié un truc?")
                    .addAction(ouiAc)
                    .addAction(nonAc)
                    .addAction(peAc);

            // Get an instance of the NotificationManager service
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(getApplicationContext());
            // Issue the notification with notification manager.
            notificationManager.notify(NOTIFICATION_ACTION_ID, notificationBuilder.build());
        });

        BtnWearableNotification.setOnClickListener(v -> {

            // Build intent for notification content
            PendingIntent continueIntent = createPendingIntentWearable(NOTIFICATION_WEARABLE_ID_CONTINUE, "aller on Continue");
            PendingIntent finishIntent = createPendingIntentWearable(NOTIFICATION_WEARABLE_ID_END, "Aller on rentre");
            PendingIntent callIntent = createPendingIntentWearable(NOTIFICATION_WEARABLE_ID_CALL, "Commandont une pizza");

            // Build action for notification action
            NotificationCompat.Action continueAction = new NotificationCompat.Action(R.drawable.ic_motorcycle_black_18dp, "Continuez", continueIntent);
            NotificationCompat.Action finishAction = new NotificationCompat.Action(R.drawable.ic_alert_grey600_18dp, "FINIR !!", finishIntent);
            NotificationCompat.Action callAction = new NotificationCompat.Action(R.drawable.ic_message_bulleted_grey600_18dp, "Appeler une ambulance", callIntent);

            // Notification channel ID is ignored for Android 7.1.1
            // (API level 25) and lower.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_motorcycle_black_18dp)
                    .setContentTitle("activity")
                    .setContentText("on continue?")
                    .extend(new NotificationCompat.WearableExtender()
                            .addAction(continueAction)
                            .addAction(finishAction)
                            .addAction(callAction));

            // Get an instance of the NotificationManager service
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            // Issue the notification with notification manager.
            notificationManager.notify(NOTIFICATION_WEARABLE_ID, notificationBuilder.build());
        });

    }

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        if(intent.getAction() == null) return;
        if (intent.getAction().startsWith(Constants.MY_PENDING_INTENT_ACTION)) {
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();

            int notificationToCancel = 0;

            if (Constants.MY_PENDING_INTENT_ACTION_SIMPLE.equals(intent.getAction())) {
                notificationToCancel = NOTIFICATION_SIMPLE_ID;
            } else if (Constants.MY_PENDING_INTENT_ACTION_ACTIONS.equals(intent.getAction())) {
                notificationToCancel = NOTIFICATION_ACTION_ID;
            } else if (Constants.MY_PENDING_INTENT_ACTION_WEARABLE.equals(intent.getAction())) {
                notificationToCancel = NOTIFICATION_WEARABLE_ID;
            }

            NotificationManagerCompat.from(this).cancel(notificationToCancel);
        }
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     * @param requestCode The request code
     * @param message The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message, String actionSet) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(actionSet);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createPendingIntentSimple(int requestCode, String message) {
        return createPendingIntent(requestCode, message, Constants.MY_PENDING_INTENT_ACTION_SIMPLE);
    }

    private PendingIntent createPendingIntentActions(int requestCode, String message) {
        return createPendingIntent(requestCode, message, Constants.MY_PENDING_INTENT_ACTION_ACTIONS);
    }

    private PendingIntent createPendingIntentWearable(int requestCode, String message) {
        return createPendingIntent(requestCode, message, Constants.MY_PENDING_INTENT_ACTION_WEARABLE);
    }

}
