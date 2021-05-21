package com.fred.entidades

import android.graphics.Bitmap
import android.os.Parcelable
import com.fred.CajaDeColision
import com.fred.Laberinto

abstract class Enemigo : Parcelable {
    var pX = 0
    var pY = 0
    var animacionTick = 0
    var offsetX = 0
    var offsetY = 0
    var id = 0
    abstract fun actualizarEntidad (miLaberinto: Laberinto, cX : Int, cY : Int)
    abstract fun devolverBitmap () : Bitmap
    abstract fun detectarColision (fX : Int, fY : Int, pasoX : Int, pasoY : Int, fred : Fred) : Boolean
    abstract fun calcularCoordenadas (cX: Int, cY: Int, pasoX: Int, pasoY: Int) : CajaDeColision

}