package com.fred.entidades

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.fred.CajaDeColision
import com.fred.EstadosFred
import com.fred.Laberinto
import com.fred.Lado

class Fred() : Parcelable {

    var vida = 15
    var balas = 6
    var puntos = 0
    var estadoFred = EstadosFred.QUIETO
    var cuerda = false
    var disparando = false
    var tocado = 0
    var scrollTick = 0
    var scrollTickSalto = 0
    var scrollTickCuerda = 0
    var scrollTickSaltoCuerda = 0
    var lado = Lado.DERECHA

    constructor(parcel: Parcel) : this() {
        vida = parcel.readInt()
        balas = parcel.readInt()
        puntos = parcel.readInt()
        cuerda = parcel.readByte() != 0.toByte()
        disparando = parcel.readByte() != 0.toByte()
        tocado = parcel.readInt()
        scrollTick = parcel.readInt()
        scrollTickSalto = parcel.readInt()
        scrollTickCuerda = parcel.readInt()
        scrollTickSaltoCuerda = parcel.readInt()
    }

    fun animacionFred(): Int {
        // Lógica Planteamiento: EstadosFred. de Fred
        // Parado
        // Caminando
        // Saltando
        // Subiendo o bajando cuerda
        // Saltando desde una cuerda o hacia ella

        when (estadoFred) {
            EstadosFred.QUIETO -> {
                if (lado == Lado.DERECHA) {
                    if (cuerda) return 10 else {
                        if (disparando) {
                            disparando = false
                            return 13
                        } else return 0
                    }
                } else {
                    if (cuerda) return 8 else {
                        if (disparando) {
                            disparando = false
                            return 12
                        } else return 3
                    }
                }
                scrollTick = 0
                scrollTickSalto = 0
            }
            EstadosFred.CAMINANDO -> {
                if (lado == Lado.DERECHA) {
                    // Fred camina a la derecha
                    scrollTick++
                    if (scrollTick == 4) scrollTick = 0
                    if (disparando) {
                        disparando = false
                        return 13
                    } else {
                        if (disparando) {
                            disparando = false
                            return 12
                        } else {
                            when (scrollTick) {
                                0 -> return 0
                                1 -> return 1
                                2 -> return 2
                                3 -> return 1
                            }
                        }
                    }
                } else {
                    // Fred camina a la izquierda
                    scrollTick++
                    if (scrollTick == 4) scrollTick = 0
                    when (scrollTick) {
                        0 -> return 3
                        1 -> return 4
                        2 -> return 5
                        3 -> return 4
                    }
                }
            }
            EstadosFred.SALTANDO -> {
                // Fred está saltando
                scrollTickSalto++
                if (scrollTickSalto == 3) {
                    scrollTickSalto = 0
                    estadoFred = EstadosFred.DECISIONSALTO
                }

                    if (disparando) {
                        if (lado == Lado.DERECHA) return  15 else  return 14
                    } else {
                        if (lado == Lado.DERECHA) return  6 else  return 7
                    }


            }
            EstadosFred.MOVIENDOCUERDA -> {
                if (scrollTickCuerda == 0) scrollTickCuerda = 1 else scrollTickCuerda = 0
                if (lado == Lado.DERECHA) {
                    when (scrollTickCuerda) {
                        0 -> return 10
                        1 -> return 11
                    }
                } else {
                    when (scrollTickCuerda) {
                        0 -> return 8
                        1 -> return 9
                    }
                }
            }
            EstadosFred.SALTANDOCUERDA -> {
                // Fred está saltando desde una cuerda hacia un pasillo
                scrollTickSaltoCuerda++
                if (scrollTickSaltoCuerda == 4) {
                    scrollTickSaltoCuerda = 0

                    if (cuerda) {
                        estadoFred = EstadosFred.CAMINANDO
                        cuerda = false
                    } else {
                        cuerda = true
                        estadoFred = EstadosFred.QUIETO
                    }
                }
                if (lado == Lado.DERECHA) {
                    if (scrollTickSaltoCuerda != 0 ) {
                        if (disparando) return 15 else return 6
                    } else {
                        if (cuerda) return 10 else return 0
                    }
                } else {
                    if (scrollTickSaltoCuerda != 0 ) {
                        if (disparando) return 14 else return 7
                    } else {
                        if (cuerda) return 8 else return 3
                    }
                }
            }
            else -> Log.d("Miapp" , "Fred sin estado!!!!")
        }
        return 99
    }

    fun cajaColisionFred () : CajaDeColision {
        // Sprite Fred completo
        var fredx1 = 384
        var fredy1 = 240
        var fredx2 = 512
        var fredy2 = 400
        // Coordenadas de Fred, dependen de como se esté moviendo
        when {
            !cuerda && lado == Lado.DERECHA && (estadoFred == EstadosFred.QUIETO || (estadoFred == EstadosFred.CAMINANDO && scrollTick != 2 ))-> {
                // Fred quieto derecha o caminando en la animacion 0 ò 1
                fredx1 = 400
                fredy1 = 264
                fredx2 = 496
                fredy2 = 400
            }
            !cuerda && lado == Lado.IZQUIERDA && (estadoFred == EstadosFred.QUIETO || (estadoFred == EstadosFred.CAMINANDO  && scrollTick != 2))-> {
                // fred quieto izquierda o caminando en la animacion 0 ó 1
                fredx1 = 400
                fredy1 = 264
                fredx2 = 490
                fredy2 = 400
            }
            estadoFred == EstadosFred.CAMINANDO && scrollTick == 2 -> {
                // fred caminando derecha o izquierda
                fredx1 = 384
                fredy1 = 264
                fredx2 = 512
                fredy2 = 400
            }
            estadoFred == EstadosFred.SALTANDO || estadoFred == EstadosFred.SALTANDOCUERDA -> {
                // fred saltando
                fredx1 = 384
                fredy1 = 246
                fredx2 = 512
                fredy2 = 372
            }
            lado == Lado.IZQUIERDA && cuerda -> {
                // fred cuerda derecha
                fredx1 = 432
                fredy1 = 241
                fredx2 = 512
                fredy2 = 340
            }
            lado == Lado.DERECHA && cuerda -> {
                // fred cuerda izquierda
                fredx1 = 384
                fredy1 = 241
                fredx2 = 464
                fredy2 = 340
            }

        }
        val coordenada = CajaDeColision(fredx1.toFloat(), fredy1.toFloat(), fredx2.toFloat(), fredy2.toFloat())
        return coordenada
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(vida)
        parcel.writeInt(balas)
        parcel.writeInt(puntos)
        parcel.writeBoolean(cuerda)
        parcel.writeBoolean(disparando)
        parcel.writeInt(tocado)
        parcel.writeInt(scrollTick)
        parcel.writeInt(scrollTickSalto)
        parcel.writeInt(scrollTickCuerda)
        parcel.writeInt(scrollTickSaltoCuerda)
        //parcel.writeValue(lado)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Fred> {
            override fun createFromParcel(source: Parcel): Fred {
                return Fred(source)
            }
            override fun newArray(size: Int): Array<Fred?> {
                return arrayOfNulls(size)
            }
        }
    }

}

