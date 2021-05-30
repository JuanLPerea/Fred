package com.fred

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
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
    var musica = false
    var altoCreditos = 0
    var anchoCreditos = 0
    var scrollY = 0
    var crono = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)

        SharedApp.myMediaPlayer.playMedia(R.raw.beep)
        SharedApp.myMediaPlayer.stopPlayer()

        imagenCreditos = findViewById(R.id.imageViewTitulo)
        layoutCreditos = findViewById(R.id.layoutCreditos)

        records = BitmapFactory.decodeResource(resources , R.drawable.todaysgreatest)

        creditos = BitmapFactory.decodeResource(resources , R.drawable.tituloscredito)

        val orientation = resources.configuration.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                altoCreditos = creditos.height / 8
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                altoCreditos = creditos.height / 4
            }
        }

         //   Log.d("Miapp", "Alto: ${altoCreditos}")

            anchoCreditos = creditos.width
            scrollCreditos()



        if (SharedApp.prefs.record1 == "") {
            SharedApp.prefs.firstrecords()
        }


        val botonJugar = findViewById(R.id.botonJugarBTN) as Button

        botonJugar.setOnClickListener {
            timer.cancel()
            SharedApp.myMediaPlayer.stopPlayer()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun scrollCreditos() {
        timer = Timer()
        val ticks = object : TimerTask() {
            override fun run() {

                if (scrollY < creditos.height - altoCreditos - 10) {

                    val scroll = Bitmap.createBitmap(creditos, 0, scrollY, creditos.width, altoCreditos)
                    scrollY = scrollY + 10

                    runOnUiThread {
                        imagenCreditos.setImageBitmap(scroll)
                    }


                } else {
                    // Mostramos los records durante 10 segundos
                    if (musica == false) {
                        SharedApp.myMediaPlayer.playMedia(R.raw.marchafunebre)
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

                   //     Log.d("Miapp" , "ancho: ${records.width} alto: ${records.height}")


                        // Mostramos los records
                        lienzo.drawText(rellenarConEspacios(SharedApp.prefs.record1Name), 350F, 800F, pintura)
                        lienzo.drawText(rellenarConCeros(SharedApp.prefs.record1), 350F, 1400F, pintura)

                        lienzo.drawText(rellenarConEspacios(SharedApp.prefs.record2Name), 1050F, 800F, pintura)
                        lienzo.drawText(rellenarConCeros(SharedApp.prefs.record2), 1050F, 1400F, pintura)

                        lienzo.drawText(rellenarConEspacios(SharedApp.prefs.record3Name), 1750F, 800F, pintura)
                        lienzo.drawText(rellenarConCeros(SharedApp.prefs.record3), 1750F, 1400F, pintura)

                        lienzo.drawText(rellenarConEspacios(SharedApp.prefs.record4Name), 2450F, 800F, pintura)
                        lienzo.drawText(rellenarConCeros(SharedApp.prefs.record4), 2450F, 1400F, pintura)

                        runOnUiThread {
                            imagenCreditos.setImageBitmap(bitmapRecords)
                        }
                    }


                if (crono < System.currentTimeMillis()) {
                    scrollY = 0
                    musica = false
                    SharedApp.myMediaPlayer.stopPlayer()
                }


                }
            }


        }

        timer.schedule(ticks, 50L, 50L)
    }

    private fun rellenarConCeros(string: String): String {
        var espaciosderecha = (6 - string.length)/2
        val sb = StringBuffer(10)
        for (n in 0..espaciosderecha) {
            sb.append("0")
        }
        return sb.toString() + string
    }

    private fun rellenarConEspacios(string: String): String {
        var espaciosderecha = (6 - string.length) -1
        val sb = StringBuffer(10)
        for (n in 0..espaciosderecha) {
            sb.append(" ")
        }
        return sb.toString() + string
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timer.cancel()
        SharedApp.myMediaPlayer.stopPlayer()
        outState.putInt("SCROLLY", scrollY)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        scrollY = savedInstanceState.getInt("SCROLLY")
    }


    fun menuOpciones(view: View) {
        val popupMenu = PopupMenu(applicationContext, view)
        popupMenu.inflate(R.menu.opciones_menu)
        popupMenu.show()
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("Miapp" , "Restart")
        scrollCreditos()
    }

}