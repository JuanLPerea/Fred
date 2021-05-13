package com.fred.entidades

import android.util.Log
import com.fred.EstadosFred
import com.fred.Lado

class Fred() {

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

    fun animacionFred(): Int {
        // Lógica Planteamiento: Estados de Fred
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
                            balas--
                            disparando = false
                            return 13
                        } else return 0
                    }
                } else {
                    if (cuerda) return 8 else {
                        if (disparando) {
                            balas--
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
                        balas--
                        disparando = false
                        return 13
                    } else {
                        if (disparando) {
                            balas--
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
                    if (lado == Lado.DERECHA) return  6 else  return 7

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
                        return 6
                    } else {
                        if (cuerda) return 10 else return 0
                    }
                } else {
                    if (scrollTickSaltoCuerda != 0 ) {
                        return 7
                    } else {
                        if (cuerda) return 8 else return 3
                    }
                }
            }
            else -> Log.d("Miapp" , "Fred sin estado!!!!")
        }
        return 99
    }

}

