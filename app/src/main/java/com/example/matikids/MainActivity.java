package com.example.matikids;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_bestScore;
    private MediaPlayer mp;                 ///////////////    para el fondo musical

    int num_aleatorio = (int)(Math.random()*10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //La actividad esta creada

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView)findViewById(R.id.imageView_personaje);
        tv_bestScore = (TextView)findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);     /////// determina el icono que se muestra

        int id;
        if(num_aleatorio == 0 || num_aleatorio == 11){
            id = getResources().getIdentifier("zorro","drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 1 || num_aleatorio == 10){
            id = getResources().getIdentifier("castor","drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 2 || num_aleatorio == 9) {
            id = getResources().getIdentifier("venado", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 3 || num_aleatorio == 8) {
            id = getResources().getIdentifier("conejo", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 4 || num_aleatorio == 7) {
            id = getResources().getIdentifier("mapache", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 5 || num_aleatorio == 6) {
            id = getResources().getIdentifier("espin", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        mp = MediaPlayer.create(this, R.raw.tambores);
        mp.start();             ///////////      inicia la reproduccion
        mp.setLooping(true);    ///////////      determina que se repita el audio

        /*----------------------------------------------------------
        -------------------------- BD ----------------------------*/
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "bd", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();                             /////   apertura bd
        Cursor consulta = bd.rawQuery("select * from puntaje where score = " +
                        "(select max(score) from puntaje)", null);       ///// consulta score max
        if(consulta.moveToFirst()){                                                  ///// si encuentra datos
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            bd.close();
        }else {
            bd.close();
        }
        /*----------------------------------------------------------*/
    }
    public void Jugar(View view) {
        String nombre = et_nombre.getText().toString();//////   obtiene valor ingresado en et_nombre
        if(!nombre.equals("")){ /////////   valida si nombre NO esta vacio
            mp.stop();              /////   detiene el audio
            mp.release();           /////   libera espacio
            Intent intent = new Intent(this,MainActivity2Nivel1.class);
                    /////   crea un objeto tipo intent para llamar a Activity Nivel 1
            intent.putExtra("jugador", nombre); /////   transfiere a "jugador" lo guardado en "nombre"
            startActivity(intent);  /////   inicia Activity Nivel 1
            finish();               /////   destruye el Activity Bienvenida
        }else{                  /////////   si nombre SI esta vacio
            Toast.makeText(this, "Antes de empezar dinos ¿Cómo te llamas?",Toast.LENGTH_LONG).show();
            et_nombre.requestFocus();////   devuelve el foco a et_nombre
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
                    /////   activa el teclado para ingresar el nombre
        }
    }
    @Override /*Bloquea el boton para RETROCEDER*/
    public void onBackPressed() {
        //sobreescribe el metodo para que realice ninguna funcion
    }
}