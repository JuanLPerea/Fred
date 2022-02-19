package com.fredzxgame.items

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable

abstract class Objeto : Parcelable {
    var oX = 0
    var oY = 0

    // Tipos de objeto:
    //  Mapa
    //  Pocion
    //  Balas
    //  Tesoro

    abstract fun devolverBitmap() : Bitmap
    abstract fun nuevoObjeto(context: Context, cX : Int, cY : Int)
    abstract fun detectarColision(cX: Int, cY: Int) : Boolean
}