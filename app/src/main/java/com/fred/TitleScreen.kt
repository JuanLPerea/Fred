package com.fred

import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
            SharedApp.prefs.nivelInicio = 1
            jugar()
        }


    }

    private fun jugar() {
        timer.cancel()
        SharedApp.myMediaPlayer.stopPlayer()
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun scrollCreditos() {
        timer = Timer()
        val ticks = object : TimerTask() {
            override fun run() {

                if (scrollY < creditos.height - altoCreditos - 5) {

                    val scroll = Bitmap.createBitmap(creditos, 0, scrollY, creditos.width, altoCreditos)
                    scrollY = scrollY + 5

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

                        var bitmapRecords = Bitmap.createBitmap(3017, 1488,  Bitmap.Config.ARGB_8888)
                        var lienzo = Canvas(bitmapRecords)

                        val rectDestino = Rect()
                        rectDestino.set(0, 0, 3017, 1488)
                        lienzo.drawBitmap(records, null, rectDestino, null)

                       Log.d("Miapp" , "ancho: ${records.width} alto: ${records.height}")


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


        popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.seleccionar_velocidad -> {
                        val popupMenuVelocidad = PopupMenu(applicationContext, view)
                        popupMenuVelocidad.inflate(R.menu.menu_velocidad)

                        popupMenuVelocidad.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.velolento -> {
                                    SharedApp.prefs.velocidadJuego = 200L
                                }
                                R.id.velonormal -> {
                                    SharedApp.prefs.velocidadJuego = 150L
                                }
                                R.id.velorapida -> {
                                    SharedApp.prefs.velocidadJuego = 100L
                                }
                                R.id.veloabsurda -> {
                                    SharedApp.prefs.velocidadJuego = 50L
                                }
                            }

                            true
                        }
                        popupMenuVelocidad.show()
                    }

                    R.id.secreto -> {

                        if (SharedApp.prefs.secreto) {
                            val popupMenuSecreto = PopupMenu(applicationContext, view)
                            popupMenuSecreto.inflate(R.menu.menu_secreto)

                            popupMenuSecreto.setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.fredoriginal -> {
                                        SharedApp.prefs.tipoFred = false
                                    }
                                    R.id.fredcolor -> {
                                        SharedApp.prefs.tipoFred = true
                                    }
                                }

                                true
                            }
                            popupMenuSecreto.show()
                        } else {
                            Toast.makeText(applicationContext, "Para desbloquear tienes que pasar del nivel 4", Toast.LENGTH_LONG).show()
                        }

                    }

                    R.id.seleccionar_nivel -> {
                        val popupMenuNiveles = PopupMenu(applicationContext, view)
                        popupMenuNiveles.inflate(R.menu.menu_niveles)

                        popupMenuNiveles.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.niveltut -> {
                                    // lanzamos el nivel de tut
                                    if (SharedApp.prefs.reto1) {
                                        SharedApp.prefs.nivelInicio = 6
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, "Este nivel todavía no está desbloqueado", Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.nivelhuesos -> {
                                    if (SharedApp.prefs.reto2) {
                                        SharedApp.prefs.nivelInicio = 7
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, "Este nivel todavía no está desbloqueado", Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.niveldracula -> {
                                    if (SharedApp.prefs.reto3) {
                                        SharedApp.prefs.nivelInicio = 8
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, "Este nivel todavía no está desbloqueado", Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.nivelegipcio -> {
                                    if (SharedApp.prefs.reto4) {
                                        SharedApp.prefs.nivelInicio = 9
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, "Este nivel todavía no está desbloqueado", Toast.LENGTH_LONG).show()
                                    }
                                }

                            }

                            true
                        }
                        popupMenuNiveles.show()
                    }

                    R.id.elegirenemigos -> {
                        // TODO Dialog seleccionando los enemigos y pasarlos al juego
                    }


                }
        true
        }


        popupMenu.show()
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("Miapp" , "Restart")
        scrollCreditos()
    }

}