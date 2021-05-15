package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Coordenada
import com.fred.Direcciones
import com.fred.Laberinto
import com.fred.R

class Momia : Enemigo() {

    lateinit var momia1d: Bitmap
    lateinit var momia2d: Bitmap
    lateinit var momia1i: Bitmap
    lateinit var momia2i: Bitmap
    lateinit var momiacuerdad: Bitmap
    lateinit var momiacuerdai: Bitmap
    lateinit var momiapopd: Bitmap
    lateinit var momiapopi: Bitmap

    lateinit var direccion: Direcciones
    lateinit var direccionBak: Direcciones
    var choque = false
    var posicionXinicial = 0
    var posicionYinicial = 0

    fun newMomia(context: Context, miLaberinto: Laberinto, coordenada: Coordenada) {

        momia1d = BitmapFactory.decodeResource(context.resources, R.drawable.momia1d)
        momia2d = BitmapFactory.decodeResource(context.resources, R.drawable.momia2d)
        momia1i = BitmapFactory.decodeResource(context.resources, R.drawable.momia1i)
        momia2i = BitmapFactory.decodeResource(context.resources, R.drawable.momia2i)
        momiacuerdad = BitmapFactory.decodeResource(context.resources, R.drawable.momia3d)
        momiacuerdai = BitmapFactory.decodeResource(context.resources, R.drawable.momia3i)
        momiapopd = BitmapFactory.decodeResource(context.resources, R.drawable.momia4d)
        momiapopi = BitmapFactory.decodeResource(context.resources, R.drawable.momia4i)

        var azar = (0..1).shuffled().last()
        if (azar == 0) direccion = Direcciones.IZQUIERDA else direccion = Direcciones.DERECHA
        direccionBak = direccion

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY

        posicionXinicial = coordenada.coordenadaX
        posicionYinicial = coordenada.coordenadaY

        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        // actualizar la animación
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0

        // Log.d("Miapp", "offsetX ${offsetX} offsetY ${offsetY}")

        when (direccion) {
            Direcciones.DERECHA -> {
                if (animacionTick == 1) {
                    offsetX += 32
                    if (offsetX == 512) {
                        offsetX = 384
                        pX++
                    }

                    // Comprobar si podemos seguir caminando, hay un muro y se da la vuelta,
                    // si ya ha chocado contra un muro se teletransporta y si hay una cuerda cae por ella
                    when {
                        // Si hay camino, sigue caminando
                        // Si hay un muro delante, se da la vuelta y si ya ha chocado anteriormente, se teletransporta
                        miLaberinto.map[pX + 1][pY] == 2 && miLaberinto.map[pX + 1][pY + 1] == 2 && offsetX == 416 -> {
                            offsetX = 384
                            offsetY = 400
                            if (choque) {
                                // se teletransporta
                                direccion = Direcciones.PARADO
                                animacionTick = 0
                                choque = false
                            } else {
                                // se da la vuelta
                                direccion = Direcciones.IZQUIERDA
                                choque = true
                            }
                        }
                        // Si hay una cuerda, se tira por ella pa'bajo
                        miLaberinto.map[pX + 1][pY] == 0 && miLaberinto.map[pX + 1][pY + 1] == 0 && offsetX == 480 -> {
                            direccionBak = direccion
                            direccion = Direcciones.ABAJO
                            pX++
                            offsetX = 384
                            offsetY = 432       // La momia empieza cayendo un poco mas abajo
                        }
                    }


                }


            }

            Direcciones.IZQUIERDA -> {
                if (animacionTick == 1) {
                    offsetX -= 32
                    if (offsetX == 256) {
                        pX--
                        offsetX = 384
                    }

                    // Comprobar si podemos seguir caminando, hay un muro y se da la vuelta,
                    // si ya ha chocado contra un muro se teletransporta y si hay una cuerda cae por ella
                    when {
                        // Si hay camino, sigue caminando
                        // Si hay un muro delante, se da la vuelta y si ya ha chocado anteriormente, se teletransporta
                        miLaberinto.map[pX - 1][pY] == 2 && miLaberinto.map[pX - 1][pY + 1] == 2 && offsetX == 352 -> {
                            offsetX = 384
                            offsetY = 400
                            if (choque) {
                                // se teletransporta
                                direccion = Direcciones.PARADO
                                animacionTick = 0
                                choque = false
                            } else {
                                // se da la vuelta
                                direccion = Direcciones.DERECHA
                                choque = true
                            }
                        }
                        // Si hay una cuerda, se tira por ella pa'bajo
                        miLaberinto.map[pX - 1][pY] == 0 && miLaberinto.map[pX - 1][pY + 1] == 0 && offsetX == 288 -> {
                            direccionBak = direccion
                            direccion = Direcciones.ABAJO
                            pX--
                            offsetX = 384
                            offsetY = 432               // Punto en el que empieza a caer la momia
                        }
                    }

                }

            }

            Direcciones.ABAJO -> {
              //  Log.d("Miapp" , "offsetY: ${offsetY}")
                offsetY += 64
                // comprobar si puede seguir cayendo o ha llegado hasta un pasillo

                    if (miLaberinto.map[pX][pY + 1] == 2) {
                        // Ha llegado a un pasillo, hacemos que de un pequeño rebote
                        offsetY = 400
                        direccion = direccionBak
                        choque = false
                    } else {
                        // sigue cayendo
                        if (offsetY == 560) {
                            offsetY = 432
                            pY++
                        }
                    }


            }

        }


    }

    override fun devolverBitmap(): Bitmap {
        when (direccion) {
            Direcciones.DERECHA -> {
                if (animacionTick == 0) return momia1d else return momia2d
            }

            Direcciones.IZQUIERDA -> {
                if (animacionTick == 0) return momia1i else return momia2i
            }

            Direcciones.ABAJO -> {
                if (direccionBak == Direcciones.DERECHA) return momiacuerdad else return momiacuerdai
            }

            Direcciones.PARADO -> {
                // Pop
                // Teletransporte
                if (direccionBak == Direcciones.DERECHA) direccion =
                    Direcciones.IZQUIERDA else direccion = Direcciones.DERECHA
                choque = false
                pX = posicionXinicial
                pY = posicionYinicial
                offsetX = 384
                offsetY = 400
                if (direccion == Direcciones.DERECHA) return momiapopi else return momiapopd
            }
        }

        return momia1d
    }

    override fun detectarColision(fX: Int, fY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        return false
    }
}