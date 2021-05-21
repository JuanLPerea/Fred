package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Coordenada
import com.fred.Direcciones
import com.fred.Laberinto
import com.fred.R

class Bala {

    lateinit var bala : Bitmap
    var direccion = Direcciones.DERECHA
    var bX = 0
    var bY = 0
    var balaOffsetX = 384
    var balaOffsetY = 400
    var impacto = false
    var disparo = false

    fun newBala (context: Context) {
        bala = BitmapFactory.decodeResource(context.resources , R.drawable.bala)
    }

    fun actualizarBala (miLaberinto: Laberinto) {

        Log.d("Miapp" , "Bala en: ${bX},${bY} disparo: ${disparo} , impacto: ${impacto}     offsetX ${balaOffsetX}")

        if (!impacto) {
            if (direccion == Direcciones.DERECHA) {
                // Movemos la bala
                balaOffsetX += 64
                if (balaOffsetX == 512) {
                    if (miLaberinto.map[bX+1][bY] == 0 ) {
                        bX++
                        balaOffsetX = 320
                    } else {
                        // La bala ha chocado con un muro
                        disparo = false
                        balaOffsetX = 320
                    }
                }

            } else {
                // bala que va a la izquierda
                // Movemos la bala
                balaOffsetX -= 64
                if (balaOffsetX == 256) {
                    if (miLaberinto.map[bX-1][bY] == 0 ) {
                        bX--
                        balaOffsetX = 448
                    } else {
                        // La bala ha chocado con un muro
                        disparo = false
                        balaOffsetX = 448
                    }
                }
            }
        }

    }

    fun devolverBitmap () : Bitmap {
        return bala
    }

    fun detectarColision( enemigo: Enemigo , fX : Int, fY: Int, fredOffsetX : Int , fredOffsetY : Int): Boolean {
        // finalmente comprobamos que las coordenadas no se solapen
        var impacto = true

        // Calcular el punto de arriba de la izquierda del sprite
        var desplazamientoX = 62
        var desplazamientoY = 76

        val diferenciaX = fX - bX
        val diferenciaY = fY - bY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + fredOffsetX + balaOffsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + fredOffsetY + balaOffsetY + desplazamientoY

        val cajaColisionEnemigo = enemigo.calcularCoordenadas(enemigo.pX , enemigo.pY , enemigo.offsetX , enemigo.offsetY)
        if (x1 > cajaColisionEnemigo.x2) impacto = false
        if (x1 < cajaColisionEnemigo.x1) impacto = false
        if (y1 > cajaColisionEnemigo.y2) impacto = false
        if (y1 < cajaColisionEnemigo.y1) impacto = false

        return impacto
    }

}