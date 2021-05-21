package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.*

class Bala {

    lateinit var baladerecha : Bitmap
    lateinit var balaizquierda : Bitmap
    lateinit var nube1d : Bitmap
    lateinit var nube2d : Bitmap
    lateinit var nube3d : Bitmap
    lateinit var nube1i : Bitmap
    lateinit var nube2i : Bitmap
    lateinit var nube3i : Bitmap

    var direccion = Direcciones.DERECHA
    var bX = 0
    var bY = 0
    var balaOffsetX = 0
    var balaOffsetY = 0
    var impacto = false
    var disparo = false
    var animacionNube = 1

    fun newBala (context: Context) {
        baladerecha = BitmapFactory.decodeResource(context.resources , R.drawable.baladerecha)
        balaizquierda = BitmapFactory.decodeResource(context.resources , R.drawable.balaizquierda)
        nube1d = BitmapFactory.decodeResource(context.resources , R.drawable.nube1d)
        nube2d = BitmapFactory.decodeResource(context.resources , R.drawable.nube2d)
        nube3d = BitmapFactory.decodeResource(context.resources , R.drawable.nube3d)
        nube1i = BitmapFactory.decodeResource(context.resources , R.drawable.nube1i)
        nube2i = BitmapFactory.decodeResource(context.resources , R.drawable.nube2i)
        nube3i = BitmapFactory.decodeResource(context.resources , R.drawable.nube3i)
    }

    fun actualizarBala (miLaberinto: Laberinto) {

    //    Log.d("Miapp" , "Bala en: ${bX},${bY} disparo: ${disparo} , impacto: ${impacto}     offsetX ${balaOffsetX}")

        if (!impacto) {
            if (direccion == Direcciones.DERECHA) {
                // Movemos la bala
                    if (miLaberinto.map[bX+1][bY] == 0 ) {
                        bX++
                        balaOffsetX = 384
                    } else {
                        // La bala ha chocado con un muro
                        eliminarBala()
                    }


            } else {
                // bala que va a la izquierda
                // Movemos la bala
                    if (miLaberinto.map[bX-1][bY] == 0 ) {
                        bX--
                        balaOffsetX = 384
                    } else {
                        // La bala ha chocado con un muro
                       eliminarBala()
                    }

            }
        }

    }

    fun devolverBitmap () : Bitmap {
        if (!impacto) {
                if (direccion == Direcciones.IZQUIERDA) return baladerecha else return balaizquierda
        } else {
            animacionNube++
            if (direccion == Direcciones.IZQUIERDA) {
                when (animacionNube) {
                    1 -> return nube1d
                    2 -> return nube2d
                    3 -> {
                        impacto = false
                        animacionNube = 1
                        eliminarBala()
                        return nube3d
                    }

                }
            } else {
                when (animacionNube) {
                    1 -> return nube1i
                    2 -> return nube2i
                    3 -> {
                        impacto = false
                        animacionNube = 1
                        eliminarBala()
                        return nube3i
                    }

                }
            }

        }

        animacionNube = 1
        return balaizquierda

    }

    fun detectarColision( enemigo: Enemigo ): Boolean {
        // Detectar si la bala alcanza a algún enemigo
        var impacto = false
        if (bX == enemigo.pX && bY == enemigo.pY) {
            impacto = true
            balaOffsetX = enemigo.offsetX
        }

        if (impacto) Log.d("Miapp" , "Bala en: ${bX},${bY} disparo: ${disparo} , impacto: ${impacto}     offsetX ${balaOffsetX}")
        return impacto
    }


    fun eliminarBala () {
        disparo = false
    }

    fun disparar(fX : Int, fY : Int , lado: Lado, estadoFred: EstadosFred) {
        bX = fX
        bY = fY
        balaOffsetX = 384
        if (lado == Lado.DERECHA) {
            direccion = Direcciones.DERECHA
        } else {
            direccion = Direcciones.IZQUIERDA
        }
        disparo = true
        impacto = false
        if (estadoFred == EstadosFred.SALTANDO || estadoFred == EstadosFred.SALTANDOCUERDA) balaOffsetY = 368 else balaOffsetY = 400


    }

}