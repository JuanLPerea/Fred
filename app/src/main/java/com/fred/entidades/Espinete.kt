package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Laberinto
import com.fred.R

class Espinete : Enemigo () {

    lateinit var espinete1D : Bitmap
    lateinit var espinete2D : Bitmap
    lateinit var espinete1I : Bitmap
    lateinit var espinete2I : Bitmap
    var direccion = 0

    fun newEspinete (context: Context , miLaberinto: Laberinto) {
        // los espinetes existen en los pasillos que tienen 3 o mas tiles de ancho
        // siempre tiene que ser en una fila par
        espinete1D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1d)
        espinete2D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2d)
        espinete1I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1i)
        espinete2I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2i)

        do {
            val xAleat = (2..34).shuffled().last()          // La gota puede estar en cualquier posición horizontal siempre que se cumpla la condición mas abajo
            val yAleat = ((2..15).shuffled().last()) * 2    // En vertical puede estar de la fila 4 a la 30 (pares)
            // tiene que haber 3 bloques solidos debajo como mínimo
            if (miLaberinto.map[xAleat][yAleat] == 0 &&
                miLaberinto.map[xAleat - 1][yAleat] == 0 &&
                miLaberinto.map[xAleat + 1][yAleat] == 0 &&
                miLaberinto.map[xAleat][yAleat+1] == 2  &&
                miLaberinto.map[xAleat-1][yAleat+1] == 2 &&
                miLaberinto.map[xAleat+1][yAleat+1] == 2) {
                pX = xAleat
                pY = yAleat
            }

        } while (pX == 0 && pY == 0)
        Log.d("Miapp" , "Espinete en: " + pX + " - " + pY)

        animacionTick = (0..10).shuffled().last()
        offsetX = 384
        offsetY = 400

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // avanzan 32 pixels cada tick osea 4 pasos por tile
        // en las esquinas dan la vuelta y solo hacen 3 pasos
        Log.d("Miapp" , "OffsetX: " + offsetX )

        if (direccion == 0) {
            offsetX += 32
            // Si el espinete está en un extremo de su recorrido...
            if (offsetX == 480 && (miLaberinto.map[pX+1][pY+1] == 0 || miLaberinto.map[pX+1][pY] == 2)) {
                // ponemos a 0 el offset y avanzamos un tile y cambiamos de dirección
                offsetX = 384
                direccion = 1
            }
            // si el espinete puede seguir en esa dirección...
            if (offsetX == 512) {
                // avanzamos al siguiente tile y reseteamos el offset
                pX++
                offsetX = 384
            }
        } else {
            offsetX -= 32
            // Si el espinete está en un extremo de su recorrido...
            if (offsetX == 288 && (miLaberinto.map[pX-1][pY+1] == 0 || miLaberinto.map[pX-1][pY] == 2)) {
                // ponemos a 0 el offset y avanzamos un tile y cambiamos de dirección
                offsetX = 384
                direccion = 0
            }
            // si el espinete puede seguir en esa dirección...
            if (offsetX == 256) {
                // avanzamos al siguiente tile y reseteamos el offset
                pX--
                offsetX = 384
            }
        }



    }

    override fun devolverBitmap(): Bitmap {
        if (direccion == 0) {
            if (animacionTick == 0) return espinete1D else return espinete2D
        } else {
            if (animacionTick == 1) return espinete1I else return espinete2I
        }
    }

    override fun detectarColision(fX: Int, fY: Int, pasoX: Int, pasoY: Int): Boolean {
        // TODO calcular cuando hay contacto basándose en la posición X
        return false
    }




}