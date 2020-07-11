package com.example.matikids;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2Nivel6 extends AppCompatActivity {
    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_chances, iv_signo;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    int score, numAleatorio_uno, numAleatorio_dos, resultado, chances = 3;
    String nombre_jugador, string_score, string_chances;

    /*NOMBRES DE NUMEROS EN IMAGENES*/
    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_nivel5);
        Toast.makeText(this, "NIVEL 6 - SUMAS, RESTAS Y MULTIPLICACIONES", Toast.LENGTH_LONG).show();

        tv_nombre = (TextView)findViewById(R.id.textView_nombre);
        tv_score = (TextView)findViewById(R.id.textView_score);
        iv_chances = (ImageView)findViewById(R.id.imageView_chances);
        iv_Auno = (ImageView)findViewById(R.id.imageView_numUno);
        iv_Ados = (ImageView)findViewById(R.id.imageView_numDos);
        iv_signo = (ImageView)findViewById(R.id.imageView_signo);
        et_respuesta = (EditText)findViewById(R.id.editText_resultado);

        /*JALA NOMBRE JUGADOR*/
        nombre_jugador = getIntent().getStringExtra("jugador");  ///// Recibe el valor de jugador
        tv_nombre.setText("Jugador: "+nombre_jugador);                  ///// Muestra nombre de jugador

        /*JALA SCORE DEL NIVEL ANTERIOR*/
        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        /*JALA VIDAS DEL NIVEL ANTERIOR*/
        string_chances = getIntent().getStringExtra("chances");
        chances = Integer.parseInt(string_chances);
        if(chances == 3) {
            iv_chances.setImageResource(R.drawable.treschances);
        }else if(chances == 2){
            iv_chances.setImageResource(R.drawable.doschances);
        }else if(chances == 1){
            iv_chances.setImageResource(R.drawable.unachance);
        }

        /*ICONO*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);          ///// Activa icono en ActionBar
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);            ///// Determina que icono usar

        /*AUDIO DE FONDO*/
        mp = MediaPlayer.create(this, R.raw.musicanivel);       ///// determina musica de fondo
        mp.start();                     //----------- reproduce el audio
        mp.setLooping(true);            //----------- repite audio

        /*AUDIO ✓ ó X */
        mp_great = MediaPlayer.create(this, R.raw.buena);    ///// acertado
        mp_bad = MediaPlayer.create(this, R.raw.error);         ///// equivocado

        numAleatorio();     /////   numeros con imagenes aleatorios al operar
    }
    public void numAleatorio() {
        if (score <= 59) {
            numAleatorio_uno = (int)(Math.random() * 10);   ///// aleatorios para el primer número en la operacion
            numAleatorio_dos = (int)(Math.random() * 10);   ///// aleatorios para el segundo número
            if(numAleatorio_uno >= 0 && numAleatorio_uno <=3){
                resultado = numAleatorio_uno + numAleatorio_dos;  /// resultado de operacion
                iv_signo.setImageResource(R.drawable.adicion);     // signo de SUMA
            }else if(numAleatorio_uno >= 4 && numAleatorio_uno <=7) {
                resultado = numAleatorio_uno - numAleatorio_dos;  /// resultado de operacion
                iv_signo.setImageResource(R.drawable.resta);       // signo de RESTA
            }else{
                resultado = numAleatorio_uno * numAleatorio_dos;  /// resultado de operacion
                iv_signo.setImageResource(R.drawable.multiplicacion);// signo de MULTIPLICACION
            }
            resultado = numAleatorio_uno * numAleatorio_dos;  /// resultado de operacion
            if(resultado >= 0) {
                for (int i = 0; i < numero.length; i++) {    ///// busca elementos de numero[]
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    /*elemento con imagen i <<==>> valor aleatorio generado*/
                    if (numAleatorio_uno == i) {
                        iv_Auno.setImageResource(id);
                    }
                    if (numAleatorio_dos == i) {
                        iv_Ados.setImageResource(id);
                    }
                }
            }else{
                numAleatorio();
            }
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            //////*YA NO HAY SGTE ACTIVITY ASI QUE NO LLEVA NINGUN VALOR*///////

            Toast.makeText(this,"¡WWoow ERES UN ÉXITO!!", Toast.LENGTH_LONG).show();
            startActivity(intent);                  /////  inicia Nivel 2
            finish();                               /////  finaliza Nivel 1
            mp.stop();   //----limpia audio
            mp.release();
        }
    }
    public void comparar(View view) {
        String respuesta = et_respuesta.getText().toString();       ///// Obtiene el valor en et_respuesta
        if (!respuesta.equals("")) {                             // Si respuesta NO está vacía
            int respuesta_jugador = Integer.parseInt(respuesta);    ///// Se convierte en entero
            if (resultado == respuesta_jugador) {                   ///// Si la respuesta es correcta
                mp_great.start();                                   ///// Audio de acertado
                score++;                                            ///// Incrementa score
                tv_score.setText("Score: " + score);                ///// Actualiza el texto del score
                et_respuesta.setText("");                           ///// Limpia el contenido de et_respuesta
                basdat();
            } else {
                mp_bad.start();             ///// Audio de equivocado
                chances--;                  ///// Disminuye chance
                basdat();
                switch (chances) {          ///// Actualiza imagen de chances
                    case 3:
                        iv_chances.setImageResource(R.drawable.treschances); break;
                    case 2:
                        Toast.makeText(this, "Te quedan dos flotadores", Toast.LENGTH_LONG).show();
                        iv_chances.setImageResource(R.drawable.doschances); break;
                    case 1:
                        Toast.makeText(this, "Cuidado!Te quedan un flotador", Toast.LENGTH_LONG).show();
                        iv_chances.setImageResource(R.drawable.unachance); break;
                    case 0:
                        Toast.makeText(this, "Has perdido todas tus flotadores", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);          ///// Devuelve al activity de Bienvenida
                        finish();                       ///// Finaliza Nivel
                        mp.stop();                      ///// Detiene el audio
                        mp.release(); break;            ///// Limpia recursos
                }
                et_respuesta.setText("");               ///// Limpia el contenido de et_respuesta
            }
            numAleatorio();         ///// actualiza siguientes números aleatorios
        } else {   // Si respuesta SI está vacía
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }
    public void basdat(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "bd", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();                             /////   apertura bd
        Cursor consulta = bd.rawQuery("select * from puntaje where score = " +
                "(select max(score) from puntaje)", null);       ///// consulta score max
        if(consulta.moveToFirst()){                                     ///// SÍ encuentra datos
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            int bestScore = Integer.parseInt(temp_score);
            if(score > bestScore){                                      ///// si score > dato guardado
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);
                bd.update("puntaje", modificacion, "score= " + bestScore, null);
            }
            bd.close();
        }else {                                                         ///// NO encuentra datos
            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);
            bd.insert("puntaje", null, insertar);
            bd.close();
        }
    }
    @Override
    public void onBackPressed() {
    }
}