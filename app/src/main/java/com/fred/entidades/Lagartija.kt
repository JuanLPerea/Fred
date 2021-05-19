package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.*

class Lagartija : Enemigo() {

    lateinit var lagartija1DAbajo : Bitmap
    lateinit var lagartija2DAbajo : Bitmap
    lateinit var lagartija1IAbajo : Bitmap
    lateinit var lagartija2IAbajo : Bitmap
    lateinit var lagartija1DArriba : Bitmap
    lateinit var lagartija2DArriba : Bitmap
    lateinit var lagartija1IArriba : Bitmap
    lateinit var lagartija2IArriba : Bitmap

    var lado = Lado.DERECHA
    var direccion = Direcciones.ARRIBA
    var cambiando = false

    fun newLagartija (context: Context, coordenada: Coordenada) {
        // las lagartijas existen en los pasillos verticales, siempre que haya muros a los 2 lados
        // siempre tiene que ser en una columna par
        lagartija1DAbajo = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija1d)
        lagartija2DAbajo = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija2d)
        lagartija1IAbajo = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija1i)
        lagartija2IAbajo = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija2i)
        lagartija1DArriba = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija1darriba)
        lagartija2DArriba = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija2darriba)
        lagartija1IArriba = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija1iarriba)
        lagartija2IArriba = BitmapFactory.decodeResource(context.resources , R.drawable.lagartija2iarriba)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        Log.d("Miapp" , "Lagartija en: " + pX + " - " + pY)

        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400
    }

    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0

        if ((0..50).shuffled().last() == 0) cambiando = true

        if (!cambiando) {
           // Si está en un lado del pasillo, la lagartija sube o baja según la dirección actual
           // Tienen 5 pasos por tile y si están en un extremo solo 4

            if (direccion == Direcciones.ARRIBA) {
                // si está subiendo
                offsetY -= 32
                // Si es el final del recorrido...
                if (offsetY == 272 && (miLaberinto.map[pX+1][pY-1] == 0 || miLaberinto.map[pX-1][pY-1] == 0 || miLaberinto.map[pX][pY-1]==2)) {
                   offsetY = 400
                   direccion = Direcciones.ABAJO
                }
                // Si puede seguir subiendo...
                if (offsetY == 240) {
                    pY--
                    offsetY = 400
                }


            } else {
                // si está bajando
                // comprobamos si es un extremo del pasillo
                offsetY += 32
                // Si es el final del recorrido...
                if (offsetY == 528 && (miLaberinto.map[pX+1][pY+1] == 0 || miLaberinto.map[pX-1][pY+1] == 0 ||  miLaberinto.map[pX][pY+1]==2)) {
                    offsetY = 400
                    direccion = Direcciones.ARRIBA
                }
                // Si puede seguir subiendo...
                if (offsetY == 560) {
                    pY++
                    offsetY = 400
                }

            }


        } else {
            // cambiando de lado
            if (lado == Lado.DERECHA) {
                offsetX -= 32
                if (offsetX == 256) {
                    lado = Lado.IZQUIERDA
                    offsetX = 384
                    cambiando = false
                }
            } else {
                offsetX += 32
                if (offsetX == 512) {
                    lado = Lado.DERECHA
                    offsetX = 384
                    cambiando = false
                }
            }
        }



    }

    override fun devolverBitmap(): Bitmap {
        when {
            (direccion == Direcciones.ABAJO && lado == Lado.DERECHA) -> {
                if (animacionTick == 0) return lagartija1DArriba else return lagartija2DArriba
            }
            (direccion == Direcciones.ABAJO && lado == Lado.IZQUIERDA) -> {
                if (animacionTick == 0) return lagartija1IArriba else return lagartija2IArriba
            }
            (direccion == Direcciones.ARRIBA && lado == Lado.DERECHA) -> {
                if (animacionTick == 0) return lagartija1DAbajo else return lagartija2DAbajo
            }
            (direccion == Direcciones.ARRIBA && lado == Lado.IZQUIERDA) -> {
                if (animacionTick == 0) return lagartija1IAbajo else return lagartija2IAbajo
            }
        }

        return lagartija1DArriba
    }



    override fun detectarColision(cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)
        // finalmente comprobamos que las coordenadas no se solapen
        var colision = true
        val cajaColisionFred = fred.cajaColisionFred()
        if (coordenadasCaja.x1 > cajaColisionFred.x2) colision = false
        if (coordenadasCaja.x2 < cajaColisionFred.x1) colision = false
        if (coordenadasCaja.y1 > cajaColisionFred.y2) colision = false
        if (coordenadasCaja.y2 < cajaColisionFred.y1) colision = false

        //    Log.d ("Miapp" , "Espinete: ${coordenadasCaja.x1},${coordenadasCaja.y1}   ${coordenadasCaja.x2},${coordenadasCaja.y2}    Fred: ${coordenadasCaja.fredx1},${coordenadasCaja.fredy1}    ${coordenadasCaja.fredx2},${coordenadasCaja.fredy2}     ${colision}")
        return colision
    }


    fun dibujarCajasColision (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {
        val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)
        return coordenadasCaja
    }

    fun calcularCoordenadas (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {
        // Calcular el punto de arriba de la izquierda del sprite
        var desplazamientoX = 20
        var desplazamientoY = 10
        val anchura = 32
        val altura = 54

        if (lado == Lado.IZQUIERDA) desplazamientoX = 0 else desplazamientoX = 96
        if (direccion == Direcciones.ABAJO) desplazamientoY = 0 else desplazamientoY = 96


        val diferenciaX = pX - cX
        val diferenciaY = pY - cY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + pasoX + offsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + pasoY + offsetY + desplazamientoY
        val x2 = x1 + anchura
        val y2 = y1 + altura

        // ------------------------------------------------------------------------------------------------------------------------------------------
        val coordenadasCaja = CajaDeColision(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat())
        return coordenadasCaja
    }
}