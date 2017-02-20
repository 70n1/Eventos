package org.example.eventos;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by AMARTIN on 20/02/2017.
 */

public class EventosFCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String evento="";
            evento ="Evento: "+remoteMessage.getData().get("evento")+ "\n";
            evento = evento + "Día: "+ remoteMessage.getData().get("dia")+ "\n";
            evento = evento +"Ciudad: "+
                    remoteMessage.getData().get("ciudad")+"\n";
            evento = evento +"Comentario: "
                    +remoteMessage.getData().get("comentario");
            mostrarDialogo(getApplicationContext(), evento);
        } else {
            if (remoteMessage.getNotification() != null) {
                mostrarDialogo(getApplicationContext(),
                        remoteMessage.getNotification().getBody());
            }
        }
    }
    static void mostrarDialogo (final Context context, final String mensaje){
        Intent intent = new Intent(context, Dialogo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mensaje", mensaje);
        context.startActivity(intent);
    }
}
