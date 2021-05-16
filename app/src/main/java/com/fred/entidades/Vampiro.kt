package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Coordenada
import com.fred.Direcciones
import com.fred.Laberinto
import com.fred.R

class Vampiro : Enemigo() {

    lateinit var vampiro1d : Bitmap
    lateinit var vampiro2d : Bitmap
    lateinit var vampiro1i : Bitmap
    lateinit var vampiro2i : Bitmap

    var mirandoA = Direcciones.DERECHA
    var movimiento = Direcciones.PARADO
    var velocidad = 32

    fun newVampiro (context: Context, coordenada: Coordenada) {
        vampiro1d = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro1d)
        vampiro2d = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro2d)
        vampiro1i = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro1i)
        vampiro2i = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro2i)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400

        Log.d("Miapp", "Vampiro at: ${pX} - ${pY} ")
    }

    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // Pueden moverse en cualquier dirección siempre que la fila o la columna sea par
        // Respetan los muros
        // Se mueven en la misma dirección hasta que encuentran una bifurcacion en el laberinto
        // entonces deciden una nueva dirección

        // También se paran de vez en cuando y cambian de dirección aleatoriamente
        if ((0..5).shuffled().last() == 0 && offsetX== 384 && offsetY == 400) movimiento = Direcciones.PARADO

        // También pueden volar muy rápido aleatoriamente
        if ((0..50).shuffled().last() == 0) velocidad = 48

        Log.d("Miapp", "Vampiro at: ${pX} - ${pY} offsetX: ${offsetX} offsetY: ${offsetY} velocidad: ${velocidad} movimiento: ${movimiento}")

        when (movimiento) {
            Direcciones.PARADO -> {
                velocidad = 32
                var azar = mutableListOf<Direcciones>()
                // Mirar que direcciones podemos ir y coger una al azar
                if (pY % 2 == 0 && pX < 34 && miLaberinto.map[pX+1][pY] == 0 && offsetY == 400) azar.add(Direcciones.DERECHA)
                if (pY % 2 == 0 && pX > 4 && miLaberinto.map[pX-1][pY] == 0 && offsetY == 400) azar.add(Direcciones.IZQUIERDA)
                if (pX % 2 == 0 && pY < 34 && miLaberinto.map[pX][pY+1] == 0 && offsetX == 384) azar.add(Direcciones.ABAJO)
                if (pX % 2 == 0 && pY > 4 && miLaberinto.map[pX][pY-1] == 0 && offsetX == 384) azar.add(Direcciones.ARRIBA)
                azar.shuffle()
                movimiento = azar.last()
            }

            Direcciones.DERECHA -> {
                mirandoA = Direcciones.DERECHA
                offsetX += velocidad
                if (offsetX >= 512) {
                    Log.d("Miapp", "Salto Derecha ${offsetX}")
                    offsetX = 384
                    pX++
                    if (pX == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX+1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.IZQUIERDA -> {
                mirandoA = Direcciones.IZQUIERDA
                offsetX -= velocidad
                if (offsetX <= 256) {
                    Log.d("Miapp", "Salto Izquierda ${offsetX}")
                    offsetX = 384
                    pX--
                    if (pX == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX-1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ARRIBA -> {
                offsetY -= velocidad
                if (offsetY <= 240) {
                    Log.d("Miapp", "Salto Arriba: ${offsetY}")
                    offsetY = 400
                    pY--
                    if (pY == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY-1] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ABAJO -> {
                offsetY += velocidad
                if (offsetY >= 560) {
                    Log.d("Miapp", "Salto Arriba: ${offsetY}")
                    offsetY = 400
                    pY++
                    if (pY == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY+1] == 2) movimiento = Direcciones.PARADO
                }
            }
        }


    }

    override fun devolverBitmap(): Bitmap {
        if (mirandoA == Direcciones.IZQUIERDA) {
            if (animacionTick == 0) return vampiro1d else return vampiro2d
        } else {
            if (animacionTick == 0) return vampiro1i else return vampiro2i
        }

        return vampiro1d
    }

    override fun detectarColision(fX: Int, fY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        return false
    }
}