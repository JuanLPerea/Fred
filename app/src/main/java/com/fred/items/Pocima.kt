package com.fred.items

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import com.fred.R

class Pocima() : Objeto() {

    lateinit var pocimaBitmap: Bitmap

    constructor(parcel: Parcel) : this() {
        pocimaBitmap = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    }

    override fun devolverBitmap(): Bitmap {
     return pocimaBitmap
    }

    override fun nuevoObjeto(context: Context, cX: Int, cY: Int) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        pocimaBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pocion, opciones)
        oX = cX
        oY = cY
    }

    override fun detectarColision(cX: Int, cY: Int): Boolean {
        if (cX == oX && cY == oY) return true
        return false
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(pocimaBitmap, flags)
        parcel.writeInt( oX )
        parcel.writeInt( oY )
    }

    companion object CREATOR : Parcelable.Creator<Pocima> {
        override fun createFromParcel(parcel: Parcel): Pocima {
            return Pocima(parcel)
        }

        override fun newArray(size: Int): Array<Pocima?> {
            return arrayOfNulls(size)
        }
    }
}