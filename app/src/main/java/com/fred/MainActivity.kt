package com.fred

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val botonArriba = findViewById(R.id.flecha_arriba) as FloatingActionButton
        val botonIzquierda = findViewById(R.id.flecha_izquierda) as FloatingActionButton
        val botonAbajo = findViewById(R.id.flecha_abajo) as FloatingActionButton
        val botonDerecha = findViewById(R.id.flecha_derecha) as FloatingActionButton

        val miLaberinto = Laberinto()
        miLaberinto.generarLaberinto()

        var cX = 0
        var cY = 0
        var pasoX = 0
        var pasoY = -160

        // Variables para la posición
        cY = 34
        cX = 4
        crearFondo(cX-3, cY-3, pasoX, pasoY, miLaberinto)

        dibujarLaberintoTexto(miLaberinto)
        /*
        do {
            cX = (3..29).shuffled().last()
        } while (miLaberinto.map[cX][cY] != 0)
        crearFondo(cX-1, cY-1, pasoX, pasoY, miLaberinto)

         */
        botonIzquierda.setOnClickListener {

            if ((miLaberinto.map[cX-1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                if (cX > 2) {
                    pasoX += 32
                    if (pasoX == 128) {
                        cX--
                        pasoX = 0
                    }
                    crearFondo(cX-3, cY-3, pasoX, pasoY, miLaberinto)
                }

            }

            Log.d("MyApp", "Pasox: " + pasoX + " PasoY: " + pasoY + "  cX: " + cX + "  cY:" + cY)
        }

        botonDerecha.setOnClickListener {
            if ((miLaberinto.map[cX + 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
                if (cX < 32) {
                    pasoX -= 32
                    if (pasoX == -128){
                        cX++
                        pasoX = 0
                    }
                    crearFondo(cX-3, cY-3, pasoX, pasoY, miLaberinto)
                }
            }
            Log.d("MyApp", "Pasox: " + pasoX + " PasoY: " + pasoY + "  cX: " + cX + "  cY:" + cY)
        }

        botonArriba.setOnClickListener {
            if (((miLaberinto.map[cX][cY-1] == 0 || miLaberinto.map[cX][cY-1] == 3) && pasoX == 0) || pasoY != -160) {
                if (cY > 2){
                    pasoY += 32
                    if (pasoY == 0) {
                        cY--
                        pasoY = -160
                    }
                    crearFondo(cX-3, cY-3, pasoX, pasoY, miLaberinto)
                }

            }
            Log.d("MyApp", "Pasox: " + pasoX + " PasoY: " + pasoY + "  cX: " + cX + "  cY:" + cY)
        }

        botonAbajo.setOnClickListener {
            if ((miLaberinto.map[cX][cY+1] == 0  && pasoX == 0) || pasoY != -160) {
                if (cY < 35) {
                    pasoY -= 32
                    if (pasoY == -320) {
                        cY++
                        pasoY = -160
                    }
                    crearFondo(cX-3, cY-3, pasoX, pasoY, miLaberinto)
                }

            }
            Log.d("MyApp", "Pasox: " + pasoX + " PasoY: " + pasoY + "  cX: " + cX + "  cY:" + cY)
        }



    }


    fun crearFondo(posX: Int, posY: Int, offsetInicialX: Int, offsetInicialY : Int, miLaberinto: Laberinto) {

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
        rectDestino.set(0,0,128,160)

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


        rectDestino.offsetTo(256, 240)
        lienzo.drawBitmap(fred_quieto, null, rectDestino, null)

        fondo.setImageBitmap(bitmapFondo)
    }

    fun dibujarLaberintoTexto (miLaberinto: Laberinto) {
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
}