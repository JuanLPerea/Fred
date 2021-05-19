package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.fred.*

class Fantasma : Enemigo() {

    lateinit var fantasma1d : Bitmap
    lateinit var fantasma1i : Bitmap
    lateinit var fantasma2d : Bitmap
    lateinit var fantasma2i : Bitmap

    var mirandoA = Direcciones.DERECHA
    var movimiento = Direcciones.PARADO


    fun newFantasma (context: Context, coordenada: Coordenada) {
        fantasma1d = BitmapFactory.decodeResource(context.resources , R.drawable.fantasmad1)
        fantasma2d = BitmapFactory.decodeResource(context.resources , R.drawable.fantasmad2)
        fantasma1i = BitmapFactory.decodeResource(context.resources , R.drawable.fantasmai1)
        fantasma2i = BitmapFactory.decodeResource(context.resources , R.drawable.fantasmai2)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400

    }

    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // Pueden moverse en cualquier dirección siempre que la fila o la columna sea par
        // Pueden atravesar muros
        // Se mueven en la misma dirección hasta que encuentran una bifurcacion en el laberinto
        // entonces deciden una nueva dirección sin tener en cuenta los muros

        // También se paran de vez en cuando y cambian de dirección aleatoriamente
        if ((0..5).shuffled().last() == 0 && offsetX== 384 && offsetY == 400) movimiento = Direcciones.PARADO

        when (movimiento) {
            Direcciones.PARADO -> {
                var azar = mutableListOf<Direcciones>()
                    // Mirar que direcciones podemos ir y coger una al azar
                    if (pY % 2 == 0 && pX < 34) azar.add(Direcciones.DERECHA)
                    if (pY % 2 == 0 && pX > 4) azar.add(Direcciones.IZQUIERDA)
                    if (pX % 2 == 0 && pY < 34) azar.add(Direcciones.ABAJO)
                    if (pX % 2 == 0 && pY > 4) azar.add(Direcciones.ARRIBA)
                    azar.shuffle()
                    movimiento = azar.last()

            }

            Direcciones.DERECHA -> {
                mirandoA = Direcciones.DERECHA
                offsetX += 32
                if (offsetX == 512) {
                    offsetX = 384
                    pX++
                    if (pX == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX+1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.IZQUIERDA -> {
                mirandoA = Direcciones.IZQUIERDA
                offsetX -= 32
                if (offsetX == 256) {
                    offsetX = 384
                    pX--
                    if (pX == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX-1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ARRIBA -> {
                offsetY -= 32
                if (offsetY == 240) {
                    offsetY = 400
                    pY--
                    if (pY == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY-1] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ABAJO -> {
                offsetY += 32
                if (offsetY == 560) {
                    offsetY = 400
                    pY++
                    if (pY == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY+1] == 2) movimiento = Direcciones.PARADO
                }
            }
        }


    }


    override fun devolverBitmap(): Bitmap {
        if (mirandoA == Direcciones.DERECHA) {
            if (animacionTick == 0) return fantasma1d else return fantasma2d
        } else {
            if (animacionTick == 0) return fantasma1i else return fantasma2i
        }

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
        var desplazamientoY = 52

        val anchura = 90
        val altura = 108
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