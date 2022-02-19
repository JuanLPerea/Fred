package com.fredzxgame.items

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import com.fredzxgame.Laberinto
import com.fredzxgame.R

class Mapa() : Objeto() {

    lateinit var mapa : Bitmap

    constructor(parcel: Parcel) : this() {
     //   mapa = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    }

    override fun devolverBitmap () : Bitmap {
        return mapa
    }

    override fun nuevoObjeto(context: Context, cX : Int, cY : Int) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        mapa = BitmapFactory.decodeResource(context.resources , R.drawable.mapa, opciones)
        oX = cX
        oY = cY
    }

    override fun detectarColision(cX : Int, cY : Int): Boolean {
        if (cX == oX && cY == oY) return true
        return false
    }

    fun dibujarMapa (fX : Int, fY : Int, miLaberinto: Laberinto) : Bitmap {
        var mapaBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888)
        var lienzo = Canvas(mapaBitmap)

        var pintura = Paint()
        pintura.style = Paint.Style.FILL
        pintura.strokeWidth = 1F
        pintura.setARGB(255,0,255,255)

        val inicioX = fX - 10
        val inicioY = fY - 10


        for (y in 0..20) {
            for (x in 0..20) {
                // Fondo negro
                pintura.setARGB(255,0,0,75)
                lienzo.drawRect((x*4).toFloat() , (y*4).toFloat() , ((x*4)+4).toFloat() , ((y*4)+4).toFloat(), pintura)

                if (x+inicioX > 2 && x+inicioX < 36 && y+inicioY>2 && y+inicioY<36) {
                    if (miLaberinto.map[x + inicioX][y + inicioY] == 2) {
                        // Pintamos los muros
                        pintura.setARGB(255,100,255,255)
                        lienzo.drawRect((x*4).toFloat() , (y*4).toFloat() , ((x*4)+4).toFloat() , ((y*4)+4).toFloat(), pintura)
                    }
                }

            }
        }

        return mapaBitmap
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
     //   parcel.writeParcelable(mapa, flags)
        parcel.writeInt( oX )
        parcel.writeInt( oY )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mapa> {
        override fun createFromParcel(parcel: Parcel): Mapa {
            return Mapa(parcel)
        }

        override fun newArray(size: Int): Array<Mapa?> {
            return arrayOfNulls(size)
        }
    }


}