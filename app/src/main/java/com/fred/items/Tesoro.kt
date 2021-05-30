package com.fred.items

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import com.fred.R

class Tesoro() : Objeto() {

    lateinit var nefertiti : Bitmap
    lateinit var figurilla : Bitmap
    lateinit var ankh : Bitmap
    lateinit var rosetta : Bitmap
    lateinit var esfinge : Bitmap
    var puntos = 0
    var tipo = 0

    constructor(parcel: Parcel) : this() {
//        nefertiti = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        figurilla = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        ankh = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        rosetta = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        esfinge = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        puntos = parcel.readInt()
        tipo = parcel.readInt()
    }


    override fun devolverBitmap(): Bitmap {
        when (tipo) {
            0 -> return figurilla
            1 -> return nefertiti
            2 -> return ankh
            3 -> return rosetta
            4 -> return esfinge
        }
        return figurilla
    }

    override fun nuevoObjeto(context: Context, cX: Int, cY: Int) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        figurilla = BitmapFactory.decodeResource(context.resources, R.drawable.figurilla, opciones )
        nefertiti = BitmapFactory.decodeResource(context.resources, R.drawable.nefertiti, opciones)
        ankh = BitmapFactory.decodeResource(context.resources, R.drawable.ankh, opciones)
        rosetta = BitmapFactory.decodeResource(context.resources, R.drawable.rosetta, opciones)
        esfinge = BitmapFactory.decodeResource(context.resources, R.drawable.esfinge, opciones)
        oX = cX
        oY = cY

        val azar = (0..4).shuffled().last()
        when (azar) {
            0 -> {
                tipo = 0
                puntos = 500
            }
            1 -> {
                tipo = 1
                puntos = 750
            }
            2 -> {
                tipo = 2
                puntos = 1000
            }
            3 -> {
                tipo = 3
                puntos = 1500
            }
            4 -> {
                tipo = 4
                puntos = 2500
            }
        }
    }

    override fun detectarColision(cX: Int, cY: Int): Boolean {
        if (cX == oX && cY == oY) return true
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeParcelable(nefertiti, flags)
//        parcel.writeParcelable(figurilla, flags)
//        parcel.writeParcelable(ankh, flags)
//        parcel.writeParcelable(rosetta, flags)
//        parcel.writeParcelable(esfinge, flags)
        parcel.writeInt(puntos)
        parcel.writeInt(tipo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tesoro> {
        override fun createFromParcel(parcel: Parcel): Tesoro {
            return Tesoro(parcel)
        }

        override fun newArray(size: Int): Array<Tesoro?> {
            return arrayOfNulls(size)
        }
    }


}