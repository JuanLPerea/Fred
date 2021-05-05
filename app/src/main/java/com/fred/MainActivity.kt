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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var botonArriba: FloatingActionButton
    lateinit var botonIzquierda: FloatingActionButton
    lateinit var botonAbajo: FloatingActionButton
    lateinit var botonDerecha: FloatingActionButton
    lateinit var miLaberinto: Laberinto
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
        botonIzquierda = findViewById(com.fred.R.id.flecha_izquierda)
        botonAbajo = findViewById(R.id.flecha_abajo)
        botonDerecha = findViewById(com.fred.R.id.flecha_derecha)

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
        crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)

        establecerListeners()

        // Con este Timer se actualiza la pantalla cada 1/4 de segundo osea 4 fps!!
        // lo que le da ese toque tan rítmico que enganchaba en los 80's tic-tac-tic-tac-tic-tac
        // ¿fundiré el procesador del siglo 21??? sometiéndole a este terrible trabajo :D ???

        val timer = Timer()
        val ticks = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (pulsado_izquierda) moverFondo(Direcciones.IZQUIERDA)
                    if (pulsado_derecha) moverFondo(Direcciones.DERECHA)
                    if (pulsado_arriba) moverFondo(Direcciones.ARRIBA)
                    if (pulsado_abajo) moverFondo(Direcciones.ABAJO)
                    crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                }

            }
        }

        timer.schedule(ticks, 250, 250)
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
                }
            }
            true
        }
    }


    fun crearFondo(
        posX: Int,
        posY: Int,
        offsetInicialX: Int,
        offsetInicialY: Int,
        miLaberinto: Laberinto
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

        // Pintar a Fred
        val fred_quieto = BitmapFactory.decodeResource(resources, R.drawable.fred)


        rectDestino.offsetTo(384, 240)
        lienzo.drawBitmap(fred_quieto, null, rectDestino, null)

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
                // Mover fondo hacia arriba
                if (((miLaberinto.map[cX][cY - 1] == 0 || miLaberinto.map[cX][cY - 1] == 3) && pasoX == 0) || pasoY != -160) {
                    if (cY > 2) {
                        pasoY += 32
                        if (pasoY == 0) {
                            cY--
                            pasoY = -160
                        }
                        crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                    }
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
                        crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                    }

                }
            }

            Direcciones.DERECHA -> {
                // Derecha
                if ((miLaberinto.map[cX + 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                    if (cX < 32) {
                        pasoX -= 32
                        if (pasoX == -128) {
                            cX++
                            pasoX = 0
                        }
                        crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                    }
                }

            }

            Direcciones.IZQUIERDA -> {
                // Izquierda
                if ((miLaberinto.map[cX - 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                    if (cX > 2) {
                        pasoX += 32
                        if (pasoX == 128) {
                            cX--
                            pasoX = 0
                        }
                        crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                    }

                }
            }
        }
    }
}

enum class Direcciones {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA
}

/*

  // Mover fondo hacia arriba
                    if (((miLaberinto.map[cX][cY-1] == 0 || miLaberinto.map[cX][cY-1] == 3) && pasoX == 0) || pasoY != -160) {
                        if (cY > 2){
                            pasoY += 32
                            if (pasoY == 0) {
                                cY--
                                pasoY = -160
                            }
                            crearFondo(cX-4, cY-3, pasoX, pasoY, miLaberinto)
                        }


                    // Mover fondo hacia abajo
                    if ((miLaberinto.map[cX][cY + 1] == 0 && pasoX == 0) || pasoY != -160) {
                        if (cY < 35) {
                            pasoY -= 32
                            if (pasoY == -320) {
                                cY++
                                pasoY = -160
                            }
                            crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                        }

                    }

                    // Izquierda

                                     if ((miLaberinto.map[cX - 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                        if (cX > 2) {
                            pasoX += 32
                            if (pasoX == 128) {
                                cX--
                                pasoX = 0
                            }
                            crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                        }

                    }


                    // Derecha

                                    if ((miLaberinto.map[cX + 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                        if (cX < 32) {
                            pasoX -= 32
                            if (pasoX == -128) {
                                cX++
                                pasoX = 0
                            }
                            crearFondo(cX - 4, cY - 3, pasoX, pasoY, miLaberinto)
                        }
                    }

 */