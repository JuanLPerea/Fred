package com.fred

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import java.util.*

class TitleScreen : AppCompatActivity() {

    lateinit var imagenCreditos : ImageView
    lateinit var layoutCreditos : LinearLayout
    lateinit var creditos : Bitmap
    lateinit var records : Bitmap
    lateinit var timer : Timer
    lateinit var mediaPlayer: MediaPlayer
    var musica = false
    var altoCreditos = 0
    var anchoCreditos = 0
    var scrollY = 0
    var crono = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)


        imagenCreditos = findViewById(R.id.imageViewTitulo)
        layoutCreditos = findViewById(R.id.layoutCreditos)

        mediaPlayer = MediaPlayer.create(this, R.raw.marchafunebre)




        records = BitmapFactory.decodeResource(resources , R.drawable.todaysgreatest)

        layoutCreditos.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
            altoCreditos = view.height
            anchoCreditos = view.width
            Log.d("Miapp", "Alto: ${altoCreditos}")
            creditos = BitmapFactory.decodeResource(resources , R.drawable.tituloscredito)
            scrollCreditos()
        }


        if (SharedApp.prefs.record1 == "") {
            SharedApp.prefs.record1 = "000000"
            SharedApp.prefs.record1Name = "FRED"

            SharedApp.prefs.record2 = "000000"
            SharedApp.prefs.record2Name = "FRED"

            SharedApp.prefs.record3 = "000000"
            SharedApp.prefs.record3Name = "FRED"

            SharedApp.prefs.record4 = "000000"
            SharedApp.prefs.record4Name = "FRED"
        }


        val botonJugar = findViewById(R.id.botonJugarBTN) as Button

        botonJugar.setOnClickListener {
            timer.cancel()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun scrollCreditos() {
        timer = Timer()
        val ticks = object : TimerTask() {
            override fun run() {

                if (scrollY < creditos.height - altoCreditos - 2) {

                    val scroll = Bitmap.createBitmap(creditos, 0, scrollY, creditos.width, altoCreditos)
                    scrollY = scrollY + 2

                    runOnUiThread {
                        imagenCreditos.setImageBitmap(scroll)
                    }


                } else {
                    // Mostramos los records durante 10 segundos
                    if (musica == false) {
                        mediaPlayer.start()
                        crono = System.currentTimeMillis() + 10000
                        musica = true
                        var pintura = Paint()
                        pintura.style = Paint.Style.FILL
                        pintura.textSize = 82F
                        pintura.strokeWidth = 4F
                        pintura.setARGB(255,255,100,0)

                        var bitmapRecords = Bitmap.createBitmap(records.width, records.height,  Bitmap.Config.ARGB_8888)
                        var lienzo = Canvas(bitmapRecords)

                        val rectDestino = Rect()
                        rectDestino.set(0, 0, records.width, records.height)
                        lienzo.drawBitmap(records, null, rectDestino, null)

                        Log.d("Miapp" , "ancho: ${records.width} alto: ${records.height}")

                        // Mostramos los records
                        lienzo.drawText(SharedApp.prefs.record1Name, 350F, 800F, pintura)
                        lienzo.drawText(SharedApp.prefs.record1, 350F, 1400F, pintura)

                        lienzo.drawText(SharedApp.prefs.record2Name, 1050F, 800F, pintura)
                        lienzo.drawText(SharedApp.prefs.record2, 1050F, 1400F, pintura)

                        lienzo.drawText(SharedApp.prefs.record3Name, 1750F, 800F, pintura)
                        lienzo.drawText(SharedApp.prefs.record3, 1750F, 1400F, pintura)

                        lienzo.drawText(SharedApp.prefs.record4Name, 2450F, 800F, pintura)
                        lienzo.drawText(SharedApp.prefs.record4, 2450F, 1400F, pintura)

                        runOnUiThread {
                            mediaPlayer.start()
                            imagenCreditos.setImageBitmap(bitmapRecords)
                        }
                    }


                if (crono < System.currentTimeMillis()) {
                    scrollY = 0
                    musica = false
                }


                }
            }


        }

        timer.schedule(ticks, 50L, 50L)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SCROLLY", scrollY)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        scrollY = savedInstanceState.getInt("SCROLLY")
        Log.d("Miapp" , "Valor Scroll: ${scrollY}")
    }


    fun menuOpciones(view: View) {
        val popupMenu = PopupMenu(applicationContext, view)
        popupMenu.inflate(R.menu.opciones_menu)
        popupMenu.show()


    }
}