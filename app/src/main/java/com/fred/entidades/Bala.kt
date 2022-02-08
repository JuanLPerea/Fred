package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.*

class Bala {

    lateinit var bala : Bitmap
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
        bala = BitmapFactory.decodeResource(context.resources , R.drawable.bala)
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
                    balaOffsetX+=96
                 if (balaOffsetX >= 512) {
                     if (miLaberinto.map[bX+1][bY] == 0 ) {
                         bX++
                         balaOffsetX = 384
                     } else {
                         // La bala ha chocado con un muro
                         eliminarBala()
                     }
                 }



            } else {
                // bala que va a la izquierda
                // Movemos la bala
                    balaOffsetX-=96
                if (balaOffsetX <= 256) {
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

    }

    fun devolverBitmap () : Bitmap {
        if (!impacto) {
                return bala
        } else {
            animacionNube++
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
        }

        animacionNube = 1
        return bala

    }

    fun detectarColision( cX : Int, cY : Int , pasoX : Int, pasoY : Int , enemigo: Enemigo ): Boolean {
        // Detectar si la bala alcanza a algún enemigo
        var impacto = true
        val coordenadasCaja = coordenadasCajaBala(cX,cY,pasoX,pasoY)

        val cajaColisionEnemigo = enemigo.calcularCoordenadas(cX,cY, pasoX, pasoY)
        if (coordenadasCaja.coordenadaX - 16 > cajaColisionEnemigo.x2) impacto = false
        if (coordenadasCaja.coordenadaX + 16 < cajaColisionEnemigo.x1) impacto = false
        if (coordenadasCaja.coordenadaY > cajaColisionEnemigo.y2) impacto = false
        if (coordenadasCaja.coordenadaY + 32 < cajaColisionEnemigo.y1) impacto = false

        if (impacto == true) {
            bX = enemigo.pX
            bY = enemigo.pY
            balaOffsetX = enemigo.offsetX
            balaOffsetY = enemigo.offsetY
            if (enemigo is Vampiro) {
                balaOffsetY -= 48
                balaOffsetX -= 16
            }
        }

        return impacto
    }

    fun coordenadasCajaBala ( cX : Int, cY : Int , pasoX : Int, pasoY : Int) : Coordenada {
        val desplazamientoX = 62
        val desplazamientoY = 76
        val diferenciaXbala = bX - cX
        val diferenciaYbala = bY - cY
        val coordenadaX = diferenciaXbala * 128 + pasoX + balaOffsetX + desplazamientoX
        val coordenadaY = diferenciaYbala * 160 + pasoY + balaOffsetY + desplazamientoY
        val coordenada = Coordenada(coordenadaX, coordenadaY)
        return coordenada
    }


    fun eliminarBala () {
        disparo = false
    }

    fun disparar(fX : Int, fY : Int, offsetX : Int , lado: Lado, estadoFred: EstadosFred) {
        bX = fX
        bY = fY

        if (lado == Lado.DERECHA) {
            direccion = Direcciones.DERECHA
            balaOffsetX = 384 + (offsetX * -1)
        } else {
            direccion = Direcciones.IZQUIERDA
            balaOffsetX = 384 + (offsetX * -1)
        }
        disparo = true
        impacto = false
        if (estadoFred == EstadosFred.SALTANDO || estadoFred == EstadosFred.SALTANDOCUERDA) balaOffsetY = 372 else balaOffsetY = 416

       // Log.d("Miapp" , "Bala en: ${bX},${bY} disparo: ${disparo} , impacto: ${impacto}     balaoffsetX ${balaOffsetX}   offsetX: ${offsetX}")
    }

}