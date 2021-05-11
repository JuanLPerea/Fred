package com.fred.entidades

import android.graphics.Bitmap
import com.fred.Laberinto

abstract class Enemigo {
    var pX = 0
    var pY = 0
    var animacionTick = 0
    var offsetX = 0
    var offsetY = 0
    abstract fun actualizarEntidad (miLaberinto: Laberinto, cX : Int, cY : Int)
    abstract fun devolverBitmap () : Bitmap

}