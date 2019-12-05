package br.com.marzinhogas.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import br.com.marzinhogas.Controlers.MainActivity;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.R;

public class FirebasePushMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        final Map<String, String> map = remoteMessage.getData();

        if (map == null || map.get("id") == null) return;

        final Intent i_entregadores = new Intent(this, MainActivity.class);

        FirebaseFirestore.getInstance().collection("notifications")
                .document(Objects.requireNonNull(map.get("id")))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        Pedido pedido = Objects.requireNonNull(documentSnapshot).toObject(Pedido.class);

                        i_entregadores.putExtra("pedido", pedido);

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                0, i_entregadores, 0);

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        String notificacionid = "channel_id";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            NotificationChannel notificationChannel =
                                    new NotificationChannel(notificacionid, "notificacion",
                                            NotificationManager.IMPORTANCE_DEFAULT);

                            notificationChannel.setDescription("Channel Description");
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.GREEN);
                            notificationChannel.setVibrationPattern(new long[]{0, 1000, 0, 1000});
                            notificationChannel.getSound();
                            notificationManager.createNotificationChannel(notificationChannel);

                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notificacionid);

                        String pedidos = "Nome: " + map.get("nome") + "\nEndereço: " +
                                map.get("endereco") + "\nProduto: " + map.get("produto");

                        builder.setColor(Color.WHITE)
                                .setSmallIcon(R.drawable.logo_entrada)
                                .setContentTitle("Você tem uma entrega para fazer")
                                .setContentText(pedidos)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(pedidos))
                                .setContentIntent(pendingIntent);

                        notificationManager.notify(1, builder.build());

                    }
                });
    }
}
