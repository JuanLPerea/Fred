package com.fred

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import java.util.*

class TitleScreen : AppCompatActivity() {

    lateinit var imagenCreditos : ImageView
    var musica = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)


        if (SharedApp.prefs.record1 == "") {
            SharedApp.prefs.record1 = "000000"
            SharedApp.prefs.record1Name = "JUANLU"

            SharedApp.prefs.record2 = "000000"
            SharedApp.prefs.record2Name = "JUANLU"

            SharedApp.prefs.record3 = "000000"
            SharedApp.prefs.record3Name = "JUANLU"

            SharedApp.prefs.record4 = "000000"
            SharedApp.prefs.record4Name = "JUANLU"
        }

        imagenCreditos = findViewById(R.id.imageViewTitulo)

        val timer = Timer()

        val botonJugar = findViewById(R.id.botonJugarBTN) as Button

        botonJugar.setOnClickListener {
            timer.cancel()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }


        val creditos = BitmapFactory.decodeResource(resources , R.drawable.tituloscredito)


        val rectDestino = Rect()
        rectDestino.set(0, 0, 128, 160)
        var x = 0
        var y = 0

        val ticks = object : TimerTask() {
            override fun run() {
                val scroll = Bitmap.createBitmap(creditos, x,y, creditos.width, 480)


             //   Log.d("Miapp" , "Ancho: ${scroll.width}")

                if (y < creditos.height - 482) {
                    y = y + 2

                    runOnUiThread {
                        imagenCreditos.setImageBitmap(scroll)

                    }


                } else {
                    val crono = System.currentTimeMillis() + 5000

                    var pintura = Paint()
                    pintura.style = Paint.Style.FILL
                    pintura.textSize = 42F
                    pintura.strokeWidth = 2F
                    pintura.setARGB(255,0,0,0)
                    var bitmapFondo = Bitmap.createBitmap(creditos, x,y, creditos.width, 480)
                    var lienzo = Canvas(bitmapFondo)
                    // Mostramos los records
                    lienzo.drawText(SharedApp.prefs.record1Name, 135F, 100F, pintura)
                    lienzo.drawText(SharedApp.prefs.record1, 135F, 350F, pintura)

                    lienzo.drawText(SharedApp.prefs.record2Name, 450F, 100F, pintura)
                    lienzo.drawText(SharedApp.prefs.record2, 450F, 350F, pintura)

                    lienzo.drawText(SharedApp.prefs.record3Name, 755F, 100F, pintura)
                    lienzo.drawText(SharedApp.prefs.record3, 755F, 350F, pintura)

                    lienzo.drawText(SharedApp.prefs.record4Name, 1060F, 100F, pintura)
                    lienzo.drawText(SharedApp.prefs.record4, 1060F, 350F, pintura)


                    if (System.currentTimeMillis() > crono ) {
                        y = 0
                    }

                    runOnUiThread {
                        if (musica == false) {
                            musica = true
                            val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.fredfrito)
                            mediaPlayer.start()
                        }
                        imagenCreditos.setImageBitmap(bitmapFondo)
                    }


                }
            }


        }

        timer.schedule(ticks, 10L, 10L)

    }





}