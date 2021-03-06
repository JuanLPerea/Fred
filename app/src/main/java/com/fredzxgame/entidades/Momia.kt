package com.fredzxgame.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.fredzxgame.*

class Momia() : Enemigo() {

    lateinit var momia1d: Bitmap
    lateinit var momia2d: Bitmap
    lateinit var momia1i: Bitmap
    lateinit var momia2i: Bitmap
    lateinit var momiacuerdad: Bitmap
    lateinit var momiacuerdai: Bitmap
    lateinit var momiapopd: Bitmap
    lateinit var momiapopi: Bitmap
    lateinit var posicionesSpawn : MutableList<Coordenada>

    lateinit var direccion: Direcciones
    lateinit var direccionBak: Direcciones
    var choque = false
    var indiceSpawn = 1
    var momiaID = 0

    constructor(parcel: Parcel) : this() {
//        momia1d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momia2d = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momia1i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momia2i = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momiacuerdad = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momiacuerdai = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momiapopd = parcel.readParcelable(Bitmap::class.java.classLoader)!!
//        momiapopi = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        choque = parcel.readByte() != 0.toByte()
        indiceSpawn = parcel.readInt()
        momiaID = parcel.readInt()
    }


    fun newMomia(context: Context, listaCoordenadas: MutableList<Coordenada>, id : Int) {
        val opciones = BitmapFactory.Options()
        opciones.inPreferredConfig = Bitmap.Config.RGB_565
        opciones.inSampleSize = 2

        momia1d = BitmapFactory.decodeResource(context.resources, R.drawable.momia1d, opciones)
        momia2d = BitmapFactory.decodeResource(context.resources, R.drawable.momia2d, opciones)
        momia1i = BitmapFactory.decodeResource(context.resources, R.drawable.momia1i, opciones)
        momia2i = BitmapFactory.decodeResource(context.resources, R.drawable.momia2i, opciones)
        momiacuerdad = BitmapFactory.decodeResource(context.resources, R.drawable.momia3d, opciones)
        momiacuerdai = BitmapFactory.decodeResource(context.resources, R.drawable.momia3i, opciones)
        momiapopd = BitmapFactory.decodeResource(context.resources, R.drawable.momia4d, opciones)
        momiapopi = BitmapFactory.decodeResource(context.resources, R.drawable.momia4i, opciones)
        posicionesSpawn = listaCoordenadas

        momiaID = id

        var azar = (0..1).shuffled().last()
        if (azar == 0) direccion = Direcciones.IZQUIERDA else direccion = Direcciones.DERECHA
        direccionBak = direccion

        indiceSpawn = (1..(posicionesSpawn.size - 1)).shuffled().last()
        pX = posicionesSpawn.get(indiceSpawn).coordenadaX
        pY = posicionesSpawn.get(indiceSpawn).coordenadaY

      //  Log.d("Miapp", "Momia ${momiaID} en ${pX} - ${pY}")

        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        // actualizar la animaci??n
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0

     //    Log.d("Miapp", "direccion: ${direccion} ID: ${momiaID}")

        when (direccion) {
            Direcciones.DERECHA -> {
                if (animacionTick == 1) {
                    offsetX += 32
                    if (offsetX == 512) {
                        offsetX = 384
                        pX++
                    }

                    // Comprobar si podemos seguir caminando, hay un muro y se da la vuelta,
                    // si ya ha chocado contra un muro se teletransporta y si hay una cuerda cae por ella
                    when {
                        // Si hay camino, sigue caminando
                        // Si hay un muro delante, se da la vuelta y si ya ha chocado anteriormente, se teletransporta
                        miLaberinto.map[pX + 1][pY] == 2 && miLaberinto.map[pX + 1][pY + 1] == 2 && offsetX == 416 -> {
                            offsetX = 384
                            offsetY = 400
                            if (choque) {
                                // se teletransporta
                                direccion = Direcciones.PARADO
                                animacionTick = 0
                                choque = false
                            } else {
                                // se da la vuelta
                                direccion = Direcciones.IZQUIERDA
                                choque = true
                            }
                        }
                        // Si hay una cuerda, se tira por ella pa'bajo
                        miLaberinto.map[pX + 1][pY] == 0 && miLaberinto.map[pX + 1][pY + 1] == 0 && offsetX == 480 -> {
                            direccionBak = direccion
                            direccion = Direcciones.ABAJO
                            pX++
                            offsetX = 384
                            offsetY = 432       // La momia empieza cayendo un poco mas abajo
                        }
                    }


                }


            }

            Direcciones.IZQUIERDA -> {
                if (animacionTick == 1) {
                    offsetX -= 32
                    if (offsetX == 256) {
                        pX--
                        offsetX = 384
                    }

                    // Comprobar si podemos seguir caminando, hay un muro y se da la vuelta,
                    // si ya ha chocado contra un muro se teletransporta y si hay una cuerda cae por ella
                    when {
                        // Si hay camino, sigue caminando
                        // Si hay un muro delante, se da la vuelta y si ya ha chocado anteriormente, se teletransporta
                        miLaberinto.map[pX - 1][pY] == 2 && miLaberinto.map[pX - 1][pY + 1] == 2 && offsetX == 352 -> {
                            offsetX = 384
                            offsetY = 400
                            if (choque) {
                                // se teletransporta
                                direccion = Direcciones.PARADO
                                animacionTick = 0
                                choque = false
                            } else {
                                // se da la vuelta
                                direccion = Direcciones.DERECHA
                                choque = true
                            }
                        }
                        // Si hay una cuerda, se tira por ella pa'bajo
                        miLaberinto.map[pX - 1][pY] == 0 && miLaberinto.map[pX - 1][pY + 1] == 0 && offsetX == 288 -> {
                            direccionBak = direccion
                            direccion = Direcciones.ABAJO
                            pX--
                            offsetX = 384
                            offsetY = 432               // Punto en el que empieza a caer la momia
                        }
                    }

                }

            }

            Direcciones.ABAJO -> {
                if (offsetY == 400) {
                    // esto es para que haga un peque??o rebote cuando cae y choca contra el suelo
                    direccion = direccionBak
                } else {
                    // comprobar si puede seguir cayendo o ha llegado hasta un pasillo
                    offsetY += 64
                    if (miLaberinto.map[pX][pY + 1] == 2) {
                        offsetY = 400
                        choque = false
                    } else {
                        // sigue cayendo
                        if (offsetY == 560) {
                            offsetY = 432
                            pY++
                        }
                    }
                }




            }

            Direcciones.PARADO -> {
                // Pop
                // Teletransporte
                if (direccionBak == Direcciones.DERECHA) direccion = Direcciones.IZQUIERDA else direccion = Direcciones.DERECHA
                choque = false
                indiceSpawn = (1..(posicionesSpawn.size - 1)).shuffled().last()
                pX = posicionesSpawn.get(indiceSpawn).coordenadaX
                pY = posicionesSpawn.get(indiceSpawn).coordenadaY
            //    Log.d("Miapp", "Momia ${momiaID} popped at: ${pX} - ${pY} indice: ${indiceSpawn} direcci??n: ${direccion}")
                offsetX = 384
                offsetY = 400
            }

        }


    }

    override fun devolverBitmap(): Bitmap {
        when (direccion) {
            Direcciones.DERECHA -> {
                if (animacionTick == 0) return momia1d else return momia2d
            }

            Direcciones.IZQUIERDA -> {
                if (animacionTick == 0) return momia1i else return momia2i
            }

            Direcciones.ABAJO -> {
                if (direccionBak == Direcciones.DERECHA) return momiacuerdad else return momiacuerdai
            }

            Direcciones.PARADO -> {
                if (direccion == Direcciones.DERECHA) return momiapopi else return momiapopd
            }
        }

        return momia1d
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeParcelable(momia1d, flags)
//        parcel.writeParcelable(momia2d, flags)
//        parcel.writeParcelable(momia1i, flags)
//        parcel.writeParcelable(momia2i, flags)
//        parcel.writeParcelable(momiacuerdad, flags)
//        parcel.writeParcelable(momiacuerdai, flags)
//        parcel.writeParcelable(momiapopd, flags)
//        parcel.writeParcelable(momiapopi, flags)
        parcel.writeBoolean(choque)
        parcel.writeInt(indiceSpawn)
        parcel.writeInt(momiaID)
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
        val CREATOR = object : Parcelable.Creator<Momia> {
            override fun createFromParcel(source: Parcel): Momia {
                return Momia(source)
            }
            override fun newArray(size: Int): Array<Momia?> {
                return arrayOfNulls(size)
            }
        }
    }

}