package com.fred

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
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

     //   SharedApp.prefs.reto6 = true

        imagenCreditos = findViewById(R.id.imageViewTitulo)
        layoutCreditos = findViewById(R.id.layoutCreditos)

        records = BitmapFactory.decodeResource(resources , R.drawable.todaysgreatest)

        val idioma = Locale.getDefault().language
        Log.d("Miapp", idioma)
        if (idioma == "en") {
            creditos = BitmapFactory.decodeResource(resources , R.drawable.tituloscredito_english)
        } else {
            creditos = BitmapFactory.decodeResource(resources , R.drawable.tituloscredito)
        }


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


        val botonJugar = findViewById(R.id.botonJugarBTN) as ImageButton

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

                      // Log.d("Miapp" , "ancho: ${records.width} alto: ${records.height}")


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

        if (SharedApp.prefs.secreto) {
            popupMenu.menu.getItem(1).setTitle(getString(R.string.colorFred))
        }


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
                            Toast.makeText(applicationContext, getString(R.string.desbloquearsecreto), Toast.LENGTH_LONG).show()
                        }

                    }

                    R.id.seleccionar_nivel -> {
                        val popupMenuNiveles = PopupMenu(applicationContext, view)
                        popupMenuNiveles.inflate(R.menu.menu_niveles)

                        popupMenuNiveles.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.nivelbidon -> {
                                    if (SharedApp.prefs.reto1) {
                                        SharedApp.prefs.nivelInicio = 5
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                                    }
                                }

                                R.id.niveltut -> {
                                    // lanzamos el nivel de tut
                                    if (SharedApp.prefs.reto2) {
                                        SharedApp.prefs.nivelInicio = 6
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.nivelhuesos -> {
                                    if (SharedApp.prefs.reto3) {
                                        SharedApp.prefs.nivelInicio = 7
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.niveldracula -> {
                                    if (SharedApp.prefs.reto4) {
                                        SharedApp.prefs.nivelInicio = 8
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                                    }
                                }
                                R.id.nivelegipcio -> {
                                    if (SharedApp.prefs.reto5) {
                                        SharedApp.prefs.nivelInicio = 9
                                        jugar()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                                    }
                                }

                            }

                            true
                        }
                        popupMenuNiveles.show()
                    }

                    R.id.elegirenemigos -> {
                        if (SharedApp.prefs.reto6) {

                            val dialogenemigos = Dialog(this)
                            dialogenemigos.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogenemigos.setCancelable(false)
                            dialogenemigos.setContentView(R.layout.dialogo_enemigos)

                            val numeroGotas = dialogenemigos.findViewById(R.id.editTextNumbergotas) as EditText
                            val numeroEspinetes = dialogenemigos.findViewById(R.id.editTextNumber_espinetes) as EditText
                            val numeroFantasmas = dialogenemigos.findViewById(R.id.editTextNumber_Fantasmas) as EditText
                            val numeroLagartijas = dialogenemigos.findViewById(R.id.editTextNumber_Lagartijas) as EditText
                            val numeroMomias = dialogenemigos.findViewById(R.id.editTextNumber_Momias) as EditText
                            val numeroVampiros = dialogenemigos.findViewById(R.id.editTextNumber_Vampiros) as EditText
                            val numeroEsqueletos = dialogenemigos.findViewById(R.id.editTextNumber_Esqueletos) as EditText
                            val numeroBalas = dialogenemigos.findViewById(R.id.editTextNumber_Balas) as EditText

                            val botonmasgotas = dialogenemigos.findViewById(R.id.buttongotas) as Button
                            val botonmasespinetes = dialogenemigos.findViewById(R.id.button_espinetes) as Button
                            val botonmasfantasmas = dialogenemigos.findViewById(R.id.button_Fantasmas) as Button
                            val botonmaslagartijas = dialogenemigos.findViewById(R.id.button_Lagartijas) as Button
                            val botonmasmomias = dialogenemigos.findViewById(R.id.button_Momias) as Button
                            val botonmasvampiros = dialogenemigos.findViewById(R.id.button_Vampiros) as Button
                            val botonmasesqueletos = dialogenemigos.findViewById(R.id.button_Esqueletos) as Button
                            val botonmasbalas = dialogenemigos.findViewById(R.id.button_Balas) as Button

                            val botonmenosgotas = dialogenemigos.findViewById(R.id.button2gotas) as Button
                            val botonmenosespinetes = dialogenemigos.findViewById(R.id.button2_espinetes) as Button
                            val botonmenosfantasmas = dialogenemigos.findViewById(R.id.button2_Fantasmas) as Button
                            val botonmenoslagartijas = dialogenemigos.findViewById(R.id.button2_Lagartijas) as Button
                            val botonmenosmomias = dialogenemigos.findViewById(R.id.button2_Momias) as Button
                            val botonmenosvampiros = dialogenemigos.findViewById(R.id.button2_Vampiros) as Button
                            val botonmenosesqueletos = dialogenemigos.findViewById(R.id.button2_Esqueletos) as Button
                            val botonmenosbalas = dialogenemigos.findViewById(R.id.button2_Balas) as Button


                            botonmasgotas.setOnClickListener {botonmas(numeroGotas)}
                            botonmasespinetes.setOnClickListener {botonmas(numeroEspinetes)}
                            botonmasfantasmas.setOnClickListener {botonmas(numeroFantasmas)}
                            botonmaslagartijas.setOnClickListener {botonmas(numeroLagartijas)}
                            botonmasmomias.setOnClickListener {botonmas(numeroMomias)}
                            botonmasvampiros.setOnClickListener {botonmas(numeroVampiros)}
                            botonmasesqueletos.setOnClickListener {botonmas(numeroEsqueletos)}
                            botonmasbalas.setOnClickListener {botonmas(numeroBalas)}

                            botonmenosgotas.setOnClickListener {botonmenos(numeroGotas)}
                            botonmenosespinetes.setOnClickListener {botonmenos(numeroEspinetes)}
                            botonmenosfantasmas.setOnClickListener {botonmenos(numeroFantasmas)}
                            botonmenoslagartijas.setOnClickListener {botonmenos(numeroLagartijas)}
                            botonmenosmomias.setOnClickListener {botonmenos(numeroMomias)}
                            botonmenosvampiros.setOnClickListener {botonmenos(numeroVampiros)}
                            botonmenosesqueletos.setOnClickListener {botonmenos(numeroEsqueletos)}
                            botonmenosbalas.setOnClickListener {botonmenos(numeroBalas)}
                            
                            
                            val botonOK = dialogenemigos.findViewById(R.id.botonokenemigos) as Button

                            botonOK.setOnClickListener {

                                if (numeroGotas.text.toString() == "") numeroGotas.setText("0")
                                if (numeroEspinetes.text.toString() == "") numeroEspinetes.setText("0")
                                if (numeroFantasmas.text.toString() == "") numeroFantasmas.setText("0")
                                if (numeroLagartijas.text.toString() == "") numeroLagartijas.setText("0")
                                if (numeroMomias.text.toString() == "") numeroMomias.setText("0")
                                if (numeroVampiros.text.toString() == "") numeroVampiros.setText("0")
                                if (numeroEsqueletos.text.toString() == "") numeroEsqueletos.setText("0")
                                if (numeroBalas.text.toString() == "") numeroBalas.setText("0")

                                SharedApp.prefs.numerodegotas = numeroGotas.text.toString().toInt()
                                SharedApp.prefs.numerodeespinetes = numeroEspinetes.text.toString().toInt()
                                SharedApp.prefs.numerodefantasmas = numeroFantasmas.text.toString().toInt()
                                SharedApp.prefs.numerodelagartijas = numeroLagartijas.text.toString().toInt()
                                SharedApp.prefs.numerodemomias = numeroMomias.text.toString().toInt()
                                SharedApp.prefs.numerodevampiros = numeroVampiros.text.toString().toInt()
                                SharedApp.prefs.numerodeesqueletos = numeroEsqueletos.text.toString().toInt()
                                SharedApp.prefs.numerodebalas = numeroBalas.text.toString().toInt()
                                SharedApp.prefs.nivelInicio = 999
                                dialogenemigos.dismiss()
                                jugar()
                            }


                            
                            dialogenemigos.show()
                            
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.nodesbloqueado), Toast.LENGTH_LONG).show()
                        }
                    }

                    R.id.acercade -> {
                        val dialogoacercade = Dialog(this)
                        dialogoacercade.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialogoacercade.setCancelable(true)
                        dialogoacercade.setContentView(R.layout.dialogo_acercade)

                        val botonok = dialogoacercade.findViewById(R.id.buttonokacercade) as Button

                        botonok.setOnClickListener {
                            dialogoacercade.dismiss()
                        }

                        dialogoacercade.show()
                    }


                }
        true
        }


        popupMenu.show()
    }

    fun botonmas (view: View) {
        if (view is EditText) {
            val texto = view.text.toString()
            var valor = 0
            if (texto != "") {
                 valor = view.text.toString().toInt()
            }
            valor++
            if (valor > 999) valor = 999
            view.setText(valor.toString())
        }
    }

    fun botonmenos (view: View) {
        if (view is EditText) {
            val texto = view.text.toString()
            var valor = 0
            if (texto != "") {
                valor = view.text.toString().toInt()
            }
            valor--
            if (valor < 0) valor = 0
            view.setText(valor.toString())
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("Miapp" , "Restart")
        scrollCreditos()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
        finish()
    }

}