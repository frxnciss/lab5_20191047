package com.example.lab5_20191047;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20191047.Adapter.PlatoAdapter;
import com.example.lab5_20191047.Dto.Plato;

import java.util.ArrayList;

public class ResultadoActivity extends AppCompatActivity {

    private TextView tvName, tvObj, tvKcal, tvKcalcon, tvKcalFal;
    private ImageView Ivgen;
    private Button btnAdd, btn_recomendaciones;

    private RecyclerView recyclerViewPlatos;
    private PlatoAdapter platoAdapter;
    private ArrayList<Plato> listaPlatos = new ArrayList<>();
    private double totalCaloriasConsumidas = 0;
    ArrayList<String> datosUsuario = getIntent().getStringArrayListExtra("datosUsuario");

    private static final String CHANNEL_ID = "motivacion_channel";
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        createNotificationChannelMotivacional();
        iniciarNotificacionMotivacionalPeriodica();

        setContentView(R.layout.activity_home);

        tvName = findViewById(R.id.tvName);
        tvObj = findViewById(R.id.tvObj);
        tvKcal = findViewById(R.id.tvKcal);

        Ivgen = findViewById(R.id.imgGenero);
        btnAdd = findViewById(R.id.btn_login);
        btn_recomendaciones = findViewById(R.id.btn_recomendaciones);
        recyclerViewPlatos = findViewById(R.id.recyclerViewPlatos);
        recyclerViewPlatos.setLayoutManager(new LinearLayoutManager(this));
        platoAdapter = new PlatoAdapter(this, listaPlatos);
        recyclerViewPlatos.setAdapter(platoAdapter);



        if (datosUsuario != null && datosUsuario.size() >= 4) {
            tvName.setText(datosUsuario.get(0));  // Nombre
            tvObj.setText(datosUsuario.get(2));   // Objetivo
            tvKcal.setText(datosUsuario.get(3));  // Calorías a consumir
            if (datosUsuario.get(1).equals("Femenino")) {
                Ivgen.setImageResource(R.drawable.avatar_mujer);

            }else{
                Ivgen.setImageResource(R.drawable.avatar_hombre);
            }
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultadoActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_recomendaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultadoActivity.this, RecomendacionesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarNotificacionMotivacionalPeriodica() {

        int tiempo = 0;
        tiempo = Integer.parseInt(datosUsuario.get(4))*60000; //tiempo en milisegundos
        int finalTiempo = tiempo;
        runnable = new Runnable() {
            @Override
            public void run() {
                mostrarNotificacionMotivacional();
                handler.postDelayed(this, finalTiempo);
            }
        };
        handler.postDelayed(runnable, tiempo);
    }
    @SuppressLint("MissingPermission")
    private void mostrarNotificacionMotivacional() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.avatar_comida)
                .setContentTitle("¡Mantente motivado!")
                .setContentText("Recuerda mantener una dieta balanceada y saludable.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, builder.build());
    }

    private void createNotificationChannelMotivacional() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones de Motivación",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canal para notificaciones de motivación");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene la ejecución del Runnable cuando se destruye la actividad
        handler.removeCallbacks(runnable);
    }

    //Apoyo de AI  pero en mi dto Plato esta definido como String, entonces tuve que cambiar ese tipo
    // de dato, ademas para el conteo de calerias consumidas y parsearlo.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String nombrePlato = data.getStringExtra("nombrePlato");
            String kcalPlato = data.getStringExtra("kcalPlato");

            double caloriasRecomendadas = 0.0;
            caloriasRecomendadas = Double.parseDouble(datosUsuario.get(3));

            tvKcalcon = findViewById(R.id.tvKcalCon);
            tvKcalFal = findViewById(R.id.tvKcalFal);

            listaPlatos.add(new Plato(nombrePlato, kcalPlato));
            totalCaloriasConsumidas += Double.parseDouble(kcalPlato);
            tvKcalcon.setText("Consumidas: " + totalCaloriasConsumidas + " kcal");
            Double calorias_faltantes = (caloriasRecomendadas - totalCaloriasConsumidas);
            tvKcalFal.setText("Por consumir: " + calorias_faltantes + " kcal");

            if(calorias_faltantes <= 0 ){
                tvKcalcon.setText("Ya alcanzo su objetivo");
            }


            if (totalCaloriasConsumidas > caloriasRecomendadas) {
                lanzarNotificacion(totalCaloriasConsumidas, caloriasRecomendadas);
            }
            platoAdapter.notifyDataSetChanged();

        }
    }

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ResultadoActivity.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }
    String channelId = "channelDefaultPri";
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }
    public void lanzarNotificacion(double totalCaloriasConsumidas, double caloriasRecomendadas) {
        Intent intent = new Intent(this, ResultadoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.avatar_comida)
                .setContentTitle("¡HAZ SUPERADO EL LIMITE DE CALORIAS !")
                .setContentText("Se ha consumido (" + totalCaloriasConsumidas + "/" + caloriasRecomendadas + ") calorias." )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

}
