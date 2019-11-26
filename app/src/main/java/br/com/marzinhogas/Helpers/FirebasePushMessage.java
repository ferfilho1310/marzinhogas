package br.com.marzinhogas.Helpers;

import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.marzinhogas.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebasePushMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        MostrarNotificacao(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }

    public void MostrarNotificacao(String title, String mensagem) {

        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(FirebasePushMessage.this, "Notificacoes")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo_entrada)
                .setAutoCancel(true)
                .setContentText(mensagem);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(FirebasePushMessage.this);
        managerCompat.notify(999, notificacion.build());
    }
}
