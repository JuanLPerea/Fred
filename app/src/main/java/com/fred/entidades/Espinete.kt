package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.TextView
import com.fred.*

class   Espinete() : Enemigo () {

    lateinit var espinete1D : Bitmap
    lateinit var espinete2D : Bitmap
    lateinit var espinete1I : Bitmap
    lateinit var espinete2I : Bitmap
    var direccion = 0

    constructor(parcel: Parcel) : this() {
        espinete1D = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        espinete2D = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        espinete1I = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        espinete2I = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        direccion = parcel.readInt()
    }

    fun newEspinete (context: Context, coordenada: Coordenada) {
        // los espinetes existen en los pasillos que tienen 3 o mas tiles de ancho
        // siempre tiene que ser en una fila par
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        espinete1D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1d, opciones)
        espinete2D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2d, opciones)
        espinete1I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1i, opciones)
        espinete2I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2i, opciones)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
    //    Log.d("Miapp" , "Espinete en: " + pX + " - " + pY)

        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400
    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // avanzan 32 pixels cada tick osea 4 pasos por tile
        // en las esquinas dan la vuelta y solo hacen 3 pasos
      //  Log.d("Miapp" , "OffsetX: " + offsetX )

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

    override fun detectarColision(cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        // Los espinetes hacen daño cuando Fred está en el suelo y no está saltando
            val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY)
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

    fun dibujarCajasColision (cX: Int, cY: Int, pasoX: Int, pasoY: Int) : CajaDeColision {
            val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY)
        return coordenadasCaja
    }

    override fun calcularCoordenadas (cX: Int, cY: Int, pasoX: Int, pasoY: Int) : CajaDeColision {
        // Calcular el punto de arriba de la izquierda del sprite

        var desplazamientoX = 17
        var desplazamientoY = 135
        if (direccion == 1) desplazamientoX = 95

        val anchuraEspinete = 15
        val alturaEspinete = 20
        val diferenciaX = pX - cX
        val diferenciaY = pY - cY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + pasoX + offsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + pasoY + offsetY + desplazamientoY
        val x2 = x1 + anchuraEspinete
        val y2 = y1 + alturaEspinete

        // ------------------------------------------------------------------------------------------------------------------------------------------

        val coordenadasCaja = CajaDeColision(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat())

        return coordenadasCaja
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(espinete1D, flags)
        parcel.writeParcelable(espinete2D, flags)
        parcel.writeParcelable(espinete1I, flags)
        parcel.writeParcelable(espinete2I, flags)
        parcel.writeInt(direccion)
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
    val CREATOR = object : Parcelable.Creator<Espinete> {
        override fun createFromParcel(source: Parcel): Espinete {
            return Espinete(source)
            }
        override fun newArray(size: Int): Array<Espinete?> {
            return arrayOfNulls(size)
            }
        }
     }



}