package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Laberinto
import com.fred.R

class GotaAcido() : Enemigo () {
    lateinit var gota1 : Bitmap
    lateinit var gota2 : Bitmap
    lateinit var gota3 : Bitmap
    lateinit var gota4 : Bitmap
    lateinit var gota5 : Bitmap
    lateinit var gota6 : Bitmap
    lateinit var gota7 : Bitmap
    lateinit var gota8 : Bitmap
    lateinit var gota9 : Bitmap
    lateinit var gota10 : Bitmap
    lateinit var gota11 : Bitmap

    fun newGotaAcido (context : Context , miLaberinto : Laberinto) {
        // Las gotas pueden existir en las filas pares, de la 4 a la 32
        // siempre que haya bloques de piedra encima y debajo en su columna y en las dos de alrededor
        // y no exista ya una gota en esa posición
        gota1 = BitmapFactory.decodeResource(context.resources, R.drawable.gota1)
        gota2 = BitmapFactory.decodeResource(context.resources, R.drawable.gota2)
        gota3 = BitmapFactory.decodeResource(context.resources, R.drawable.gota3)
        gota4 = BitmapFactory.decodeResource(context.resources, R.drawable.gota4)
        gota5 = BitmapFactory.decodeResource(context.resources, R.drawable.gota5)
        gota6 = BitmapFactory.decodeResource(context.resources, R.drawable.gota6)
        gota7 = BitmapFactory.decodeResource(context.resources, R.drawable.gota7)
        gota8 = BitmapFactory.decodeResource(context.resources, R.drawable.gota8)
        gota9 = BitmapFactory.decodeResource(context.resources, R.drawable.gota9)
        gota10 = BitmapFactory.decodeResource(context.resources, R.drawable.gota10)
        gota11 = BitmapFactory.decodeResource(context.resources, R.drawable.gota11)

        do {
            val xAleat = (2..34).shuffled().last()          // La gota puede estar en cualquier posición horizontal siempre que se cumpla la condición mas abajo
            val yAleat = ((2..15).shuffled().last()) * 2    // En vertical puede estar de la fila 4 a la 30 (pares)

            if (miLaberinto.map[xAleat][yAleat] == 0 &&
                miLaberinto.map[xAleat][yAleat - 1] == 2 &&
                miLaberinto.map[xAleat][yAleat+1] == 2  &&
                miLaberinto.map[xAleat-1][yAleat - 1] == 2 &&
                miLaberinto.map[xAleat-1][yAleat+1] == 2 &&
                miLaberinto.map[xAleat+1][yAleat - 1] == 2 &&
                miLaberinto.map[xAleat+1][yAleat+1] == 2) {
                super.pX = xAleat
                super.pY = yAleat
            }

        } while (super.pX == 0 && super.pY == 0)

        animacionTick = (0..10).shuffled().last()
        super.offsetX = 384
        super.offsetY = 400

        Log.d("Miapp" , "GotaX: " + super.pX + " GotaY: " + super.pY)

    }

    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        animacionTick++
        if (animacionTick == 11) animacionTick = 0
    }

    override fun devolverBitmap(): Bitmap {
        when (animacionTick) {
            0-> return gota1
            1-> return gota2
            2-> return gota3
            3-> return gota4
            4-> return gota5
            5-> return gota6
            6-> return gota7
            7-> return gota8
            8-> return gota9
            9-> return gota10
            10-> return gota11
        }
        return gota11
    }

    override fun detectarColision(fX : Int , fY : Int, pasoX : Int, pasoY : Int): Boolean {
        if (animacionTick > 5) {
            if ((fX == pX && fY == pY && pasoX < 64 && pasoX > -64  ) ||
                (fX + 1 == pX  && fY == pY && pasoX < -64) ||
                (fX - 1 == pX  && fY == pY && pasoX > 64))  {
                Log.d("Miapp" , "Colisión!!!")
                return true
            }
        }

        return false
    }
}