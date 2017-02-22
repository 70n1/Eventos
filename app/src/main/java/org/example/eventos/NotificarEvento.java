package org.example.eventos;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMARTIN on 22/02/2017.
 */
public class NotificarEvento extends AppCompatActivity {

    static final String URL_SERVIDOR = "http://www.tiramillas.es/notificaciones/";

    @BindView(R.id.textMensaje)
    EditText mensaje;

    static Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificar_evento);

        ButterKnife.bind(this);
        contexto = this;

    }

    public void enviar_mensaje(View view) {
        if (mensaje.getText().toString().equals("")){
            //mostrar error
            Toast.makeText(contexto
                    , "Debe de rellenar el campo mensaje."
                    , Toast.LENGTH_LONG).show();

            /*runOnUiThread(new Runnable(){

                @Override
                public void run(){
                    Toast.makeText(contexto
                            , "Debe de rellenar el campo mensaje."
                            , Toast.LENGTH_LONG).show();
                }
            });*/

        } else {
            //enviar mensaje

            enviarMensajeWebTask tarea =
                    new enviarMensajeWebTask();
            tarea.contexto=this;
            tarea.mensaje=mensaje.getText().toString();
            tarea.execute();
        }
    }

    public static class enviarMensajeWebTask
            extends AsyncTask<Void, Void, String> {
        String response="error";
        Context contexto;
        String mensaje ="";
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            try{
                Uri.Builder constructorParametros = new Uri.Builder()
                        .appendQueryParameter("mensaje", mensaje);  //el idapp no es necesario para enviar a nuestro server, pero lo mantenemos para mantener la compatibilidad con el server del curso
                String parametros =
                        constructorParametros.build().getEncodedQuery();
                String url = URL_SERVIDOR + "notificar.php";
                URL direccion = new URL(url);
                HttpURLConnection conexion = (HttpURLConnection)
                        direccion.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("Accept-Language", "UTF-8");
                conexion.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new
                        OutputStreamWriter(conexion.getOutputStream());
                outputStreamWriter.write(parametros.toString());
                outputStreamWriter.flush();
                int respuesta = conexion.getResponseCode();
                if (respuesta==200){
                    response="ok";
                } else {
                    response="error";
                }
            } catch (IOException e) {
                response= "error";
            }
            return response;
        }
        public void onPostExecute(String res) {
            if (res == "ok") {
                //mensaje enviado correctamente

                Handler handler =  new Handler(contexto.getMainLooper());
                handler.post(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(contexto
                                , "Mensaje enviado correctamente."
                                , Toast.LENGTH_LONG).show();
                    }
                });

            }
            else {

                Handler handler =  new Handler(contexto.getMainLooper());
                handler.post(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(contexto
                                , "Error al enviar el mensaje."
                                , Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
