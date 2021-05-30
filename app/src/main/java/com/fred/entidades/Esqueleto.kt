package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.fred.*

class Esqueleto() : Enemigo() {

    lateinit var esqueleto1d : Bitmap
    lateinit var esqueleto2d : Bitmap
    lateinit var esqueleto3d : Bitmap
    lateinit var esqueleto1i : Bitmap
    lateinit var esqueleto2i : Bitmap
    lateinit var esqueleto3i : Bitmap
    lateinit var esqueletocuerda1 : Bitmap
    lateinit var esqueletocuerda2 : Bitmap

    var direccion = Direcciones.PARADO
    lateinit var laberintoEsqueleto : Laberinto

    constructor(parcel: Parcel) : this() {
     //   esqueleto1d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
      //  esqueleto2d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
     //   esqueleto3d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    //    esqueleto1i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    //    esqueleto2i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    //    esqueleto3i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        esqueletocuerda1 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        esqueletocuerda2 = parcel.readParcelable(Bitmap::class.java.classLoader)!!
    }

    fun newEsqueleto(context: Context, coordenada: Coordenada, miLaberinto: Laberinto) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

         esqueleto1d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1d, opciones)
         esqueleto2d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2d, opciones)
         esqueleto3d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3d, opciones)
         esqueleto1i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1i, opciones)
         esqueleto2i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2i, opciones)
         esqueleto3i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3i, opciones)
         esqueletocuerda1 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda1, opciones)
         esqueletocuerda2 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda2, opciones)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..2).shuffled().last()
        offsetX = 384
        offsetY = 400

        // Cada esqueleto se acuerda de su propio laberinto, así que lo copiamos para cada esqueleto
        // (No creo que en el Spectrum lo hicieran así, espero que no pete la memoria!!!!)
        laberintoEsqueleto = Laberinto()
        //laberintoEsqueleto = miLaberinto
        // decrementamos el valor de la posición del esqueleto ...
        // ... a menor valor, mayor número de veces que el esqueleto ha pasado por allí
        // siempre elegirá el valor mas alto para decidir la dirección a la que va
        laberintoEsqueleto.map[pX][pY]--
        direccion = elegirDireccion(miLaberinto, 0, 0)

      //  Log.d("Miapp", "Esqueleto en: ${pX} - ${pY} ")

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        // los esqueletos van siguiendo su camino, pueden subir y bajar cuerdas
        // si se encuentran sin salida, dan la vuelta
        // guardar el camino recorrido y seguir prioritariamente hacia la zona no recorrida del laberinto
        // en el laberinto siempre hay 512 tiles de pasillo
        // si ven a Fred, van a por el!!

            // Mover el esqueleto
            if (offsetX == 512) {
                offsetX = 384
                pX++
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto, cX, cY)
            }

            if (offsetX == 256) {
                offsetX = 384
                pX--
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto, cX, cY)
            }

            if (offsetY == 560) {
                offsetY = 400
                pY++
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto, cX, cY)
            }

            if (offsetY == 240) {
                offsetY = 400
                pY--
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto, cX, cY)
            }



        // actualizamos la animación
        animacionTick++
        if (animacionTick == 4) animacionTick = 0

        // actualizamos la posición
        when (direccion) {
            Direcciones.DERECHA -> {
                offsetX += 32
            }
            Direcciones.IZQUIERDA -> {
                offsetX -= 32
            }
            Direcciones.ABAJO -> {
                offsetY += 32
            }
            Direcciones.ARRIBA -> {
                offsetY -= 32
            }
        }

    }

    private fun elegirDireccion (miLaberinto: Laberinto, cX: Int, cY: Int) : Direcciones {
        var direccionElegida = Direcciones.DERECHA
        // comprobar las salidas posibles y elegir según los criterios
        var salidas = mutableListOf<DecisionEsqueleto>()
        var valorsalida = 0


        // Mirar que direcciones podemos ir, si el valor es menor de '2' (Muro) esque podemos movernos...
        if (miLaberinto.map[pX + 1][pY] < 2) {
            valorsalida = laberintoEsqueleto.map[pX + 1][pY]
            salidas.add(DecisionEsqueleto(Direcciones.DERECHA, valorsalida))
        }
        if (miLaberinto.map[pX - 1][pY] < 2) {
            valorsalida = laberintoEsqueleto.map[pX - 1][pY]
            salidas.add(DecisionEsqueleto(Direcciones.IZQUIERDA, valorsalida))
        }
        if (miLaberinto.map[pX][pY + 1] < 2) {
            valorsalida = laberintoEsqueleto.map[pX][pY + 1]
            salidas.add(DecisionEsqueleto(Direcciones.ABAJO, valorsalida))
        }
        if (miLaberinto.map[pX][pY - 1] < 2) {
            valorsalida = laberintoEsqueleto.map[pX][pY - 1]
            salidas.add(DecisionEsqueleto(Direcciones.ARRIBA, valorsalida))
        }


     //   Log.d("Miapp" , "El esqueleto tiene ${salidas.size} salidas")

        when (salidas.size) {
            0 -> {
                // En teoría siempre tiene que haber una salida, pongo esto por curiosidad
                Log.d("Miapp" , "Esto es imposible, no puedes verlo")
            }
            1 -> {
                // Callejón sin salida
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto porque es un callejón sin salida
                return salidas.first().direccion
            }
            2 -> {
                salidas.sortBy { it.valor }
                return salidas.last().direccion
            }
            else -> {
                // hay mas de 2 salidas, comprobar que no ve a fred
                val direccionFred = fredALaVista(cX, cY , miLaberinto)
                if (direccionFred != Direcciones.PARADO) {
                    // Fred está cerca, el esqueleto puede verlo....
                    return direccionFred
                } else {
                    // No ve a Fred, elegir la dirección según los valores establecidos en el laberinto del esqueleto
                    // ordenamos las salidas por su valor y elegimos la mas alta, que será la que no hemos recorrido o hemos pasado menos veces
                    salidas.sortBy { it.valor }
                    return salidas.last().direccion
                }
            }
        }
        return Direcciones.PARADO
    }

    private fun fredALaVista (cX: Int , cY: Int , miLaberinto: Laberinto) : Direcciones {

        // Niños no hardcodeéis esto en casa
        when {
            // Mirar a la izquierda a ver si ve a Fred
            (direccion != Direcciones.DERECHA && pX > 4 && miLaberinto.map[pX-1][pY] == 0 && cX  == pX - 1 && cY == pY) -> return Direcciones.IZQUIERDA
            (direccion != Direcciones.DERECHA && pX > 5 && miLaberinto.map[pX-1][pY] == 0 && miLaberinto.map[pX-2][pY] == 0 && cX == pX - 2 && cY == pY) -> return Direcciones.IZQUIERDA
            (direccion != Direcciones.DERECHA && pX > 6 && miLaberinto.map[pX-1][pY] == 0 && miLaberinto.map[pX-2][pY] == 0 && miLaberinto.map[pX-3][pY] == 0 && cX == pX - 3 && cY == pY) -> return Direcciones.IZQUIERDA
            (direccion != Direcciones.DERECHA && pX > 7 && miLaberinto.map[pX-1][pY] == 0 && miLaberinto.map[pX-2][pY] == 0 && miLaberinto.map[pX-3][pY] == 0 && miLaberinto.map[pX-4][pY] == 0 && cX == pX - 4 && cY == pY) -> return Direcciones.IZQUIERDA
            // Mirar a la derecha a ver si ve a Fred
            (direccion != Direcciones.IZQUIERDA && pX < 34 && miLaberinto.map[pX+1][pY] == 0 && cX == pX + 1 && cY == pY) -> return Direcciones.DERECHA
            (direccion != Direcciones.IZQUIERDA &&pX < 33 && miLaberinto.map[pX+1][pY] == 0 && miLaberinto.map[pX+2][pY] == 0 && cX == pX +2 && cY == pY) -> return Direcciones.DERECHA
            (direccion != Direcciones.IZQUIERDA &&pX < 32 && miLaberinto.map[pX+1][pY] == 0 && miLaberinto.map[pX+2][pY] == 0 && miLaberinto.map[pX+3][pY] == 0 && cX == pX +3 && cY == pY) -> return Direcciones.DERECHA
            (direccion != Direcciones.IZQUIERDA &&pX < 31 && miLaberinto.map[pX+1][pY] == 0 && miLaberinto.map[pX+2][pY] == 0 && miLaberinto.map[pX+3][pY] == 0 && miLaberinto.map[pX+4][pY] == 0 && cX == pX + 4 && cY == pY) -> return Direcciones.DERECHA
            // Mirar abajo a ver si ve a Fred
            (direccion != Direcciones.ARRIBA && pY < 34 && miLaberinto.map[pX][pY+1] == 0 && cX == pX && cY == pY + 1) -> return Direcciones.ABAJO
            (direccion != Direcciones.ARRIBA && pY < 33 && miLaberinto.map[pX][pY+1] == 0 && miLaberinto.map[pX][pY+2] == 0 && cX == pX && cY == pY + 2 ) -> return Direcciones.ABAJO
            (direccion != Direcciones.ARRIBA && pY < 32 && miLaberinto.map[pX][pY+1] == 0 && miLaberinto.map[pX][pY+2] == 0 && miLaberinto.map[pX][pY+3] == 0 && cX == pX && cY == pY + 3) -> return Direcciones.ABAJO
            (direccion != Direcciones.ARRIBA && pY < 31 && miLaberinto.map[pX][pY+1] == 0 && miLaberinto.map[pX][pY+2] == 0 && miLaberinto.map[pX][pY+3] == 0 && miLaberinto.map[pX][pY + 4] == 0 && cX == pX && cY == pY + 4) -> return Direcciones.ABAJO
            // Mirar arriba a ver si ve a Fred
            (direccion != Direcciones.ABAJO && pY > 4 && miLaberinto.map[pX][pY-1] == 0 && cX == pX && cY == pY - 1) -> return Direcciones.ARRIBA
            (direccion != Direcciones.ABAJO && pY > 5 && miLaberinto.map[pX][pY-1] == 0 && miLaberinto.map[pX][pY-2] == 0 && cX == pX && cY == pY - 2 ) -> return Direcciones.ARRIBA
            (direccion != Direcciones.ABAJO && pY > 6 && miLaberinto.map[pX][pY-1] == 0 && miLaberinto.map[pX][pY-2] == 0 && miLaberinto.map[pX][pY-3] == 0 && cX == pX && cY == pY - 3) -> return Direcciones.ARRIBA
            (direccion != Direcciones.ABAJO && pY > 7 && miLaberinto.map[pX][pY-1] == 0 && miLaberinto.map[pX][pY-2] == 0 && miLaberinto.map[pX][pY-3] == 0 && miLaberinto.map[pX][pY - 4] == 0 && cX == pX && cY == pY - 4) -> return Direcciones.ARRIBA
        }
        return Direcciones.PARADO
    }



    override fun devolverBitmap(): Bitmap {
        when (direccion) {
            Direcciones.DERECHA -> {
                when (animacionTick) {
                    0 -> return esqueleto1d
                    1 -> return esqueleto2d
                    2 -> return esqueleto3d
                    3 -> return esqueleto2d
                }
            }

            Direcciones.IZQUIERDA -> {
                when (animacionTick) {
                    0 -> return esqueleto1i
                    1 -> return esqueleto2i
                    2 -> return esqueleto3i
                    3 -> return esqueleto2i
                }
            }

            Direcciones.ABAJO , Direcciones.ARRIBA -> {
                if (animacionTick % 2 == 0) {
                    return esqueletocuerda1
                } else {
                    return esqueletocuerda2
                }
            }

        }

        return esqueleto1d
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
        var desplazamientoX = 20
        var desplazamientoY = 52

        val anchura = 90
        val altura = 108
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
     //   parcel.writeParcelable(esqueleto1d, flags)
    //    parcel.writeParcelable(esqueleto2d, flags)
    //    parcel.writeParcelable(esqueleto3d, flags)
    //    parcel.writeParcelable(esqueleto1i, flags)
     //   parcel.writeParcelable(esqueleto2i, flags)
     //   parcel.writeParcelable(esqueleto3i, flags)
     //   parcel.writeParcelable(esqueletocuerda1, flags)
     //   parcel.writeParcelable(esqueletocuerda2, flags)
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
        val CREATOR = object : Parcelable.Creator<Esqueleto> {
            override fun createFromParcel(source: Parcel): Esqueleto {
                return Esqueleto(source)
            }
            override fun newArray(size: Int): Array<Esqueleto?> {
                return arrayOfNulls(size)
            }
        }
    }
}