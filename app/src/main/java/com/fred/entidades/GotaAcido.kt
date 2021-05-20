package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.fred.*

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

    constructor(parcel: Parcel) : this() {
        gota1 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota2 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota3 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota4 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota5 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota6 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota7 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota8 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota9 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota10 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        gota11 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    }

    fun newGotaAcido (context : Context , coordenada: Coordenada) {
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

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..10).shuffled().last()
        offsetX = 384
        offsetY = 400

     //   Log.d("Miapp" , "GotaX: " + pX + " GotaY: " + pY)

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

    override fun detectarColision(cX : Int , cY : Int, pasoX : Int, pasoY : Int, fred: Fred): Boolean {
        val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)
        // finalmente comprobamos que las coordenadas no se solapen
        var colision = true
        val cajaColisionFred = fred.cajaColisionFred()
        if (coordenadasCaja.x1 > cajaColisionFred.x2) colision = false
        if (coordenadasCaja.x2 < cajaColisionFred.x1) colision = false
        if (coordenadasCaja.y1 > cajaColisionFred.y2) colision = false
        if (coordenadasCaja.y2 < cajaColisionFred.y1) colision = false

        //    Log.d ("Miapp" , "Espinete: ${coordenadasCaja.x1},${coordenadasCaja.y1}   ${coordenadasCaja.x2},${coordenadasCaja.y2}    Fred: ${coordenadasCaja.fredx1},${coordenadasCaja.fredy1}    ${coordenadasCaja.fredx2},${coordenadasCaja.fredy2}     ${colision}")
        return colision
    }

    fun dibujarCajasColision (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {
        val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)
        return coordenadasCaja
    }

    fun calcularCoordenadas (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {
        // Calcular el punto de arriba de la izquierda del sprite
        var desplazamientoX = 45
        var desplazamientoY = 0

        when (animacionTick) {
            6 -> desplazamientoY = 32
            7 -> desplazamientoY = 65
            8 -> desplazamientoY = 97
            9 -> desplazamientoY = 125
           10 -> desplazamientoY = 150
        }

        val anchuraGota = 40
        val alturaGota = 20
        val diferenciaX = pX - cX
        val diferenciaY = pY - cY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + pasoX + offsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + pasoY + offsetY + desplazamientoY
        val x2 = x1 + anchuraGota
        val y2 = y1 + alturaGota

        // ------------------------------------------------------------------------------------------------------------------------------------------
        val coordenadasCaja = CajaDeColision(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat())
        return coordenadasCaja
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(gota1, flags)
        parcel.writeParcelable(gota2, flags)
        parcel.writeParcelable(gota3, flags)
        parcel.writeParcelable(gota4, flags)
        parcel.writeParcelable(gota5, flags)
        parcel.writeParcelable(gota6, flags)
        parcel.writeParcelable(gota7, flags)
        parcel.writeParcelable(gota8, flags)
        parcel.writeParcelable(gota9, flags)
        parcel.writeParcelable(gota10, flags)
        parcel.writeParcelable(gota11, flags)
        parcel.writeInt( pX )
        parcel.writeInt( pY )
        parcel.writeInt( animacionTick )
        parcel.writeInt( offsetX )
        parcel.writeInt( offsetY )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<GotaAcido> {
            override fun createFromParcel(source: Parcel): GotaAcido {
                return GotaAcido(source)
            }
            override fun newArray(size: Int): Array<GotaAcido?> {
                return arrayOfNulls(size)
            }
        }
    }

}