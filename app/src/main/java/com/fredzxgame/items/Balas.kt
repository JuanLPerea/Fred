package com.fredzxgame.items

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import com.fredzxgame.R

class Balas() : Objeto() {

    lateinit var balas : Bitmap

    constructor(parcel: Parcel) : this() {
    //    balas = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    }

    override fun devolverBitmap(): Bitmap {
       return balas
    }

    override fun nuevoObjeto(context: Context, cX: Int, cY: Int) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        balas = BitmapFactory.decodeResource(context.resources, R.drawable.balas, opciones)
        oX = cX
        oY = cY
    }

    override fun detectarColision(cX: Int, cY: Int): Boolean {
        if (cX == oX && cY == oY) return true
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    //    parcel.writeParcelable(balas, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Balas> {
        override fun createFromParcel(parcel: Parcel): Balas {
            return Balas(parcel)
        }

        override fun newArray(size: Int): Array<Balas?> {
            return arrayOfNulls(size)
        }
    }


}