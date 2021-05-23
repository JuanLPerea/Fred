package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.fred.*

class Vampiro() : Enemigo() {

    lateinit var vampiro1d : Bitmap
    lateinit var vampiro2d : Bitmap
    lateinit var vampiro1i : Bitmap
    lateinit var vampiro2i : Bitmap

    var mirandoA = Direcciones.DERECHA
    var movimiento = Direcciones.PARADO
    var velocidad = 32

    constructor(parcel: Parcel) : this() {
        vampiro1d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        vampiro2d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        vampiro1i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        vampiro2i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        velocidad = parcel.readInt()
    }

    fun newVampiro (context: Context, coordenada: Coordenada) {
        vampiro1d = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro1d)
        vampiro2d = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro2d)
        vampiro1i = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro1i)
        vampiro2i = BitmapFactory.decodeResource(context.resources , R.drawable.vampiro2i)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400

     //   Log.d("Miapp", "Vampiro at: ${pX} - ${pY} ")
    }

    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // Pueden moverse en cualquier dirección siempre que la fila o la columna sea par
        // Respetan los muros
        // Se mueven en la misma dirección hasta que encuentran una bifurcacion en el laberinto
        // entonces deciden una nueva dirección

        // También se paran de vez en cuando y cambian de dirección aleatoriamente
        if ((0..5).shuffled().last() == 0 && offsetX== 384 && offsetY == 400) movimiento = Direcciones.PARADO

        // También pueden volar muy rápido aleatoriamente
        if ((0..50).shuffled().last() == 0) velocidad = 48

    //    Log.d("Miapp", "Vampiro at: ${pX} - ${pY} offsetX: ${offsetX} offsetY: ${offsetY} velocidad: ${velocidad} movimiento: ${movimiento}")

        when (movimiento) {
            Direcciones.PARADO -> {
                velocidad = 32
                var azar = mutableListOf<Direcciones>()
                // Mirar que direcciones podemos ir y coger una al azar
                if (pY % 2 == 0 && pX < 34 && miLaberinto.map[pX+1][pY] == 0 && offsetY == 400) azar.add(Direcciones.DERECHA)
                if (pY % 2 == 0 && pX > 4 && miLaberinto.map[pX-1][pY] == 0 && offsetY == 400) azar.add(Direcciones.IZQUIERDA)
                if (pX % 2 == 0 && pY < 34 && miLaberinto.map[pX][pY+1] == 0 && offsetX == 384) azar.add(Direcciones.ABAJO)
                if (pX % 2 == 0 && pY > 4 && miLaberinto.map[pX][pY-1] == 0 && offsetX == 384) azar.add(Direcciones.ARRIBA)
                azar.shuffle()
                movimiento = azar.last()
            }

            Direcciones.DERECHA -> {
                mirandoA = Direcciones.DERECHA
                offsetX += velocidad
                if (offsetX >= 512) {
                 //   Log.d("Miapp", "Salto Derecha ${offsetX}")
                    offsetX = 384
                    pX++
                    if (pX == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX+1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.IZQUIERDA -> {
                mirandoA = Direcciones.IZQUIERDA
                offsetX -= velocidad
                if (offsetX <= 256) {
                 //   Log.d("Miapp", "Salto Izquierda ${offsetX}")
                    offsetX = 384
                    pX--
                    if (pX == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX-1][pY] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ARRIBA -> {
                offsetY -= velocidad
                if (offsetY <= 240) {
                 //   Log.d("Miapp", "Salto Arriba: ${offsetY}")
                    offsetY = 400
                    pY--
                    if (pY == 4) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY-1] == 2) movimiento = Direcciones.PARADO
                }
            }

            Direcciones.ABAJO -> {
                offsetY += velocidad
                if (offsetY >= 560) {
                 //   Log.d("Miapp", "Salto Arriba: ${offsetY}")
                    offsetY = 400
                    pY++
                    if (pY == 34) movimiento = Direcciones.PARADO
                    if (miLaberinto.map[pX][pY+1] == 2) movimiento = Direcciones.PARADO
                }
            }
        }


    }

    override fun devolverBitmap(): Bitmap {
        if (mirandoA == Direcciones.IZQUIERDA) {
            if (animacionTick == 0) return vampiro1d else return vampiro2d
        } else {
            if (animacionTick == 0) return vampiro1i else return vampiro2i
        }

        return vampiro1d
    }


    override fun detectarColision(cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
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
        var desplazamientoX = 40
        var desplazamientoY = 10
        val anchura = 80
        val altura = 60

        if (mirandoA == Direcciones.DERECHA) {
            desplazamientoX = 10
        } else {
            desplazamientoX = 40
        }


        val diferenciaX = pX - cX
        val diferenciaY = pY - cY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + pasoX + offsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + pasoY + offsetY + desplazamientoY
        val x2 = x1 + anchura
        val y2 = y1 + altura

        // ------------------------------------------------------------------------------------------------------------------------------------------
        val coordenadasCaja = CajaDeColision(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat())
        return coordenadasCaja
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(vampiro1d, flags)
        parcel.writeParcelable(vampiro2d, flags)
        parcel.writeParcelable(vampiro1i, flags)
        parcel.writeParcelable(vampiro2i, flags)
        parcel.writeInt(velocidad)
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
        val CREATOR = object : Parcelable.Creator<Vampiro> {
            override fun createFromParcel(source: Parcel): Vampiro {
                return Vampiro(source)
            }
            override fun newArray(size: Int): Array<Vampiro?> {
                return arrayOfNulls(size)
            }
        }
    }
}