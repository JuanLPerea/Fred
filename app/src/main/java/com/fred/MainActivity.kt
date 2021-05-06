package com.fred

import android.app.Activity
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fred.entidades.Fred
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var botonArriba: FloatingActionButton
    lateinit var botonIzquierda: FloatingActionButton
    lateinit var botonAbajo: FloatingActionButton
    lateinit var botonDerecha: FloatingActionButton
    lateinit var miLaberinto: Laberinto
    lateinit var alturaTV : TextView
    lateinit var fred : Fred
    var cX = 0
    var cY = 0
    var pasoX = 0
    var pasoY = 0
    var pulsado_abajo = false
    var pulsado_arriba = false
    var pulsado_izquierda = false
    var pulsado_derecha = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        botonArriba = findViewById(R.id.flecha_arriba)
        botonIzquierda = findViewById(R.id.flecha_izquierda)
        botonAbajo = findViewById(R.id.flecha_abajo)
        botonDerecha = findViewById(R.id.flecha_derecha)
        alturaTV = findViewById(R.id.alturaTV)

        // Creamos a nuestro protagonista
        fred = Fred()

        // Necesitamos un laberinto para jugar
        miLaberinto = Laberinto()
        miLaberinto.generarLaberinto()

        // Variables para la posición
        cX = 0
        cY = 0
        pasoX = 0
        pasoY = -160

        // Situar al personaje en un punto aleatorio en la fila de mas abajo
        cY = 34
        do {
            cX = (3..29).shuffled().last()
        } while (miLaberinto.map[cX][cY] != 0)
        crearFondo(cX - 4, cY - 3, pasoX, pasoY)

        establecerListeners()

        // Con este Timer se actualiza la pantalla cada 200ms osea 5 fps!!
        // lo que le da ese toque tan rítmico que enganchaba en los 80's tic-tac-tic-tac-tic-tac
        // ¿fundiré un procesador del siglo 21??? sometiéndole a este terrible trabajo :D ???

        val timer = Timer()
        val ticks = object : TimerTask() {
            override fun run() {
                if (pulsado_izquierda) {
                    if (fred.lado == 1) moverFondo(Direcciones.IZQUIERDA) else fred.lado = 1
                }
                if (pulsado_derecha)  {
                    if (fred.lado == 0) moverFondo(Direcciones.DERECHA) else fred.lado = 0
                }
                if (pulsado_arriba) moverFondo(Direcciones.ARRIBA)
                if (pulsado_abajo) moverFondo(Direcciones.ABAJO)
                runOnUiThread {
                    crearFondo(cX - 4, cY - 3, pasoX, pasoY)
                    alturaTV.text = "Altura: $cY + Posicion X : $cX"
                }

            }
        }

        timer.schedule(ticks, 200, 200)
    }


    private fun establecerListeners() {
        botonArriba.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsado_arriba = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsado_arriba = false
                }

            }
            true
        }


        botonAbajo.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsado_abajo = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsado_abajo = false
                }
            }
            true
        }


        botonDerecha.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsado_derecha = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsado_derecha = false
                    fred.animacion = 0
                }
            }
            true
        }

        botonIzquierda.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsado_izquierda = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsado_izquierda = false
                    fred.animacion = 0
                }
            }



            true
        }
    }


    fun crearFondo(
        posX: Int,
        posY: Int,
        offsetInicialX: Int,
        offsetInicialY: Int
    ) {

        var offsetx = offsetInicialX - 128
        var offsety = offsetInicialY - 80

        val fondo = findViewById(R.id.imagen_fondo) as ImageView
        val roca1 = BitmapFactory.decodeResource(resources, R.drawable.roca1)
        val roca2 = BitmapFactory.decodeResource(resources, R.drawable.roca2)
        val roca3 = BitmapFactory.decodeResource(resources, R.drawable.roca3)
        val fin_cuerda = BitmapFactory.decodeResource(resources, R.drawable.finalcuerda)
        val cuerda = BitmapFactory.decodeResource(resources, R.drawable.mediocuerda)
        val inicio_cuerda = BitmapFactory.decodeResource(resources, R.drawable.arribacuerda)
        val tierra = BitmapFactory.decodeResource(resources, R.drawable.tierra)
        val trampilla = BitmapFactory.decodeResource(resources, R.drawable.trampilla)
        val cielo = BitmapFactory.decodeResource(resources, R.drawable.cielo)
        val pasillo = BitmapFactory.decodeResource(resources, R.drawable.pasillo_negro)

        var bitmapFondo = Bitmap.createBitmap(896, 800, Bitmap.Config.ARGB_8888)
        var lienzo = Canvas(bitmapFondo)
        val rectDestino = Rect()
        rectDestino.set(0, 0, 128, 160)

        // La pantalla en horizontal está dividida en 6 espacios completos y está centrado de forma que se ven 1/2 por cada lado. En horizontal hace scroll en 4 pasos cada uno
        // en vertical son 4 completos y se ve 2 pasos del siguiente. Hace scroll con 5 pasos en vertical
        for (y in 0..7) {
            for (x in 0..8) {
                rectDestino.offsetTo(offsetx, offsety)
                // en los bordes dibujar la tierra o el cielo
                if (posY == 2 && y < 1) {
                    lienzo.drawBitmap(cielo, null, rectDestino, null)
                } else {

                    if (posY == 1 && y < 2) {
                        lienzo.drawBitmap(cielo, null, rectDestino, null)
                    } else {

                        if (posX + x <= 2 || posX + x > 35 || posY + y > 35) {
                            lienzo.drawBitmap(tierra, null, rectDestino, null)
                        } else {
                            if (miLaberinto.map[posX + x][posY + y] == 2) {
                                var muro_azar = 0
                                if (x % 3 == 0) muro_azar = 1
                                if (y % 3 == 0) muro_azar = 2
                                when (muro_azar) {
                                    0 -> lienzo.drawBitmap(roca1, null, rectDestino, null)
                                    1 -> lienzo.drawBitmap(roca2, null, rectDestino, null)
                                    2 -> lienzo.drawBitmap(roca3, null, rectDestino, null)
                                }
                            } else {
                                if (miLaberinto.map[posX + x][posY + y] == 3) {
                                    lienzo.drawBitmap(trampilla, null, rectDestino, null)
                                } else {
                                    // si estamos en un pasillo dibujar las cuerdas o fondo negro
                                    if (miLaberinto.map[posX + x][posY + y - 1] == 2 && miLaberinto.map[posX + x][posY + y + 1] != 2) {
                                        lienzo.drawBitmap(inicio_cuerda, null, rectDestino, null)
                                    } else {
                                        if (miLaberinto.map[posX + x][posY + y + 1] == 2 && miLaberinto.map[posX + x][posY + y - 1] != 2) {
                                            lienzo.drawBitmap(fin_cuerda, null, rectDestino, null)
                                        } else {
                                            if (miLaberinto.map[posX + x][posY + y + 1] != 2 && miLaberinto.map[posX + x][posY + y - 1] != 2) {
                                                lienzo.drawBitmap(cuerda, null, rectDestino, null)
                                            } else {
                                                lienzo.drawBitmap(pasillo, null, rectDestino, null)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                offsetx += 128
            }
            offsetx = offsetInicialX - 128
            offsety += 160
        }


        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Pintar a Fred
        val fredd = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar)
        val fredd1 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar2)
        val fredd2 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar3)
        val fredi = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar6)
        val fredi1 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar5)
        val fredi2 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar4)
        val fredSaltandoI = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_1)
        val fredSaltandoD = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_2)
        val fredCuerda1D = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda1)
        val fredCuerda2D = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda2)
        val fredCuerda1I = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda3)
        val fredCuerda2I = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda4)


        rectDestino.offsetTo(384, 240)

        // comprobamos si fred mira a la derecha o la izquierda
        if (fred.lado == 0) {
            // Fred mira a la derecha
            // comprobar si está saltando
            if (fred.saltando && fred.animacionSalto <= 4) {
                fred.saltar()
                lienzo.drawBitmap(fredSaltandoI, null, rectDestino, null)

                if (fred.animacionSalto == 4) {
                    // Comprobar si hay cuerda y agarrarse si la hay
                    if (miLaberinto.map[cX][cY-1] == 0 && pasoX == 0) {
                        fred.cuerda = true
                        fred.animacionSalto = 0
                        fred.saltando = false
                    }
                }

            } else {
                if (fred.animacionSalto != 0) fred.animacionSalto++
                if (fred.animacionSalto == 6) {
                    fred.animacionSalto = 0
                    fred.saltando = false
                }


                // comprobamos si está en una cuerda
                if (fred.cuerda) {
                    if ((pulsado_arriba || pulsado_abajo) ) {
                        when (fred.animacionCuerda) {
                            0-> lienzo.drawBitmap(fredCuerda1D, null, rectDestino, null)
                            1-> lienzo.drawBitmap(fredCuerda2D, null, rectDestino, null)
                        }
                        fred.animarCuerda()
                    } else {
                        lienzo.drawBitmap(fredCuerda1D, null, rectDestino, null)
                    }



                } else {
                    // fred por tanto está de pie
                    when (fred.animacion) {
                        0 -> lienzo.drawBitmap(fredd, null, rectDestino, null)
                        1 -> lienzo.drawBitmap(fredd1, null, rectDestino, null)
                        2 -> lienzo.drawBitmap(fredd2, null, rectDestino, null)
                        3 -> lienzo.drawBitmap(fredd1, null, rectDestino, null)
                    }
                }
            }

        } else {
            // Fred mira a la izquierda
            if (fred.saltando && fred.animacionSalto <= 4) {
                fred.saltar()
                lienzo.drawBitmap(fredSaltandoD, null, rectDestino, null)

                if (fred.animacionSalto == 4) {
                    // Comprobar si hay cuerda y agarrarse si la hay
                    if (miLaberinto.map[cX][cY-1] == 0 && pasoX == 0) {
                        fred.cuerda = true
                        fred.animacionSalto = 0
                        fred.saltando = false
                    }
                }

            }else {
                if (fred.animacionSalto != 0) fred.animacionSalto++
                if (fred.animacionSalto == 6) {
                    fred.animacionSalto = 0
                    fred.saltando = false
                }

                // comprobamos si está en una cuerda
                if (fred.cuerda) {
                    if ((pulsado_arriba || pulsado_abajo) ) {
                        when (fred.animacionCuerda) {
                            0 -> lienzo.drawBitmap(fredCuerda1I, null, rectDestino, null)
                            1 -> lienzo.drawBitmap(fredCuerda2I, null, rectDestino, null)
                        }
                        fred.animarCuerda()
                    }else {
                        lienzo.drawBitmap(fredCuerda1I, null, rectDestino, null)
                    }


                } else {
                    // fred por tanto está de pie
                    when (fred.animacion) {
                        0 -> lienzo.drawBitmap(fredi, null, rectDestino, null)
                        1 -> lienzo.drawBitmap(fredi1, null, rectDestino, null)
                        2 -> lienzo.drawBitmap(fredi2, null, rectDestino, null)
                        3 -> lienzo.drawBitmap(fredi1, null, rectDestino, null)
                    }

                }
            }
        }



        //pintamos finalmente el fondo compuesto con todos los bitmaps
        fondo.setImageBitmap(bitmapFondo)
    }


    private fun dibujarLaberintoTexto(miLaberinto: Laberinto) {
        var fila = ""
        for (y in 0..35) {
            for (x in 0..36) {
                when (miLaberinto.map[x][y]) {
                    0 -> fila += " "
                    2 -> fila += "X"
                }
            }
            fila += "\n"
            Log.d("Miapp", fila)
        }
    }

    private fun moverFondo(direccion: Direcciones) {
        when (direccion) {
            Direcciones.ARRIBA -> {
                // Comprobar si estamos en una cuerda o no
                if (fred.cuerda) {
                    // Estamos en una cuerda, por lo tanto movemos a Fred arriba si se puede
                    // Mover fondo hacia arriba
                    if (((miLaberinto.map[cX][cY - 1] == 0 || miLaberinto.map[cX][cY - 1] == 3) && pasoX == 0) || pasoY != -160) {
                        if (cY > 2) {
                            pasoY += 32
                            if (pasoY == 0) {
                                cY--
                                pasoY = -160
                            }
                        }
                    }

                } else {
                    // saltar
                        if (fred.animacionSalto == 0) fred.saltando = true

                }

            }

            Direcciones.ABAJO -> {
                // Mover fondo hacia abajo
                if ((miLaberinto.map[cX][cY + 1] == 0 && pasoX == 0) || pasoY != -160) {
                    if (cY < 35) {
                        pasoY -= 32
                        if (pasoY == -320) {
                            cY++
                            pasoY = -160
                        }
                    }

                }
            }

            Direcciones.DERECHA -> {
                // Derecha
                if (fred.lado == 1) fred.cambiarLado()

                if ((miLaberinto.map[cX + 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                    if (cX < 34) {
                        pasoX -= 32
                        if (pasoX == -128) {
                            cX++
                            pasoX = 0
                        }

                        // Animación del personaje
                        if (pulsado_derecha) {
                            fred.animar()
                        } else {
                            fred.animacion = 0
                        }
                    }
                } else {
                    fred.animacion = 0
                }

            }

            Direcciones.IZQUIERDA -> {
                // Izquierda
                if (fred.lado == 0) fred.cambiarLado()

                if ((miLaberinto.map[cX - 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                    if (cX > 2) {
                        pasoX += 32
                        if (pasoX == 128) {
                            cX--
                            pasoX = 0
                        }

                        // Animación del personaje
                        if (pulsado_izquierda) {
                            fred.animar()
                        } else {
                            fred.animacion = 0
                        }
                    }

                } else {
                    fred.animacion = 0
                }
            }
        }
    }
}

enum class Direcciones {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA
}
