package com.fred.entidades

import android.util.Log
import com.fred.Laberinto

class Fred() {

    var vida = 15
    var balas = 6
    var puntos = 0
    var cuerda = false
    var saltando = false
    var saltandoDeCuerda = false
    var scroll = true
    var animacion = true
    var scrollTick = 0
    var scrollTickSalto = 0
    var scrollTickCuerda = 0
    var lado = 0

    fun animacionFred(
        pulsadoDerecha: Boolean,
        pulsadoIzquierda: Boolean,
        pulsadoArriba: Boolean,
        pulsadoAbajo: Boolean,
    ): Int {

        //  Log.d("Miapp" , "pasoX: " + pasoX + " PasoY: " + pasoY + " Lx-1: " + miLaberinto.map[cX+1][cY] + " Lx+1: " + miLaberinto.map[cX+1][cY])

        // Posible nuevo planteamiento: Estados de Fred
        // Parado
        // Caminando
        // Agarrado a cuerda
        // Saltando en pasillo
        // saltando de o desde cuerda
        // disparando de pie
        // disparando saltando


        if (animacion == true) {

            if (!saltando && !cuerda && !saltandoDeCuerda) {
                // quieto derecha
                if (lado == 0 && (!pulsadoAbajo && !pulsadoArriba && !pulsadoDerecha && !pulsadoIzquierda)) {
                    scroll = false
                    scrollTick = 0
                    return 0
                }

                // quieto izquierda
                if (lado == 1 && (!pulsadoAbajo && !pulsadoArriba && !pulsadoDerecha && !pulsadoIzquierda)) {
                    scroll = false
                    scrollTick = 0
                    return 3
                }


                // andando derecha 1
                if (pulsadoDerecha && scrollTick == 0) {
                    scroll = true
                    scrollTick = 1
                    return 0
                }

                // andando derecha 2
                if (pulsadoDerecha && scrollTick == 1) {
                    scroll = true
                    scrollTick = 2
                    return 1
                }

                // andando derecha 3
                if (pulsadoDerecha && scrollTick == 2) {
                    scroll = true
                    scrollTick = 3
                    return 2
                }

                // andando derecha 4
                if (pulsadoDerecha && scrollTick == 3) {
                    scroll = true
                    scrollTick = 0
                    return 1
                }


                // andando izquierda 1
                if (pulsadoIzquierda && scrollTick == 0) {
                    scroll = true
                    scrollTick = 1
                    return 4
                }
                // andando izquierda 2
                if (pulsadoIzquierda && scrollTick == 1) {
                    scroll = true
                    scrollTick = 2
                    return 5
                }
                // andando izquierda 3
                if (pulsadoIzquierda && scrollTick == 2) {
                    scroll = true
                    scrollTick = 3
                    return 4
                }
                // andando izquierda 4
                if (pulsadoIzquierda && scrollTick == 3) {
                    scroll = true
                    scrollTick = 0
                    return 3
                }

            }


            // Saltar cuando estás en un pasillo horizontal
            if (pulsadoArriba && !saltando && scrollTickSalto == 0 && !cuerda) {
                saltando = true
                scroll = false
                scrollTickSalto == 1
                if (lado == 0) return 6 else return 7
            }
            if (saltando) {
                scrollTickSalto++
                when (scrollTickSalto) {
                    in 1..4 -> {
                        if (lado == 0) return 6 else return 7
                    }
                    5 -> {
                        scrollTickSalto = 0
                        saltando = false
                        if (cuerda) {
                            if (lado == 0) return 8 else return 10
                        } else {
                            if (lado == 0) return 0 else return 3
                        }
                    }
                }
            }


            // Controlar la animación en la cuerda
            if (cuerda) {
                if ((pulsadoArriba || pulsadoAbajo)) {
                    if (scrollTickCuerda == 0) scrollTickCuerda = 1 else scrollTickCuerda = 0
                    scroll = true
                    if (lado == 0) {
                        if (scrollTickCuerda == 0) return 8 else return 9
                    } else {
                        if (scrollTickCuerda == 0) return 10 else return 11
                    }
                }
                if (pulsadoDerecha || pulsadoIzquierda && !saltandoDeCuerda) {
                    if (lado == 0) lado = 1 else lado = 0
                    scrollTickCuerda = 0
                }

            }


            if (saltandoDeCuerda) {
                // Animación del salto de cuerda
                scrollTickCuerda++
                when (scrollTickCuerda) {
                    in 1..4-> if (lado == 0)  return 6  else return 7
                    5 -> {
                        saltandoDeCuerda = false
                        scrollTickCuerda = 0
                        if (cuerda) {
                            cuerda = false
                        } else {
                            cuerda = true
                        }
                    }


                }

            } else {

            }



        } else {
            // cuando está al lado de un muro y no hay que animar al personaje
            if (lado == 0) {
                if (!cuerda) return 0 else return 8
            } else {
                if (!cuerda) return 3 else return 10
            }
        }


        // agarrado cuerda derecha 1
        // agarrado cuerda derecha 2
        // agarrado cuerda izquierda 1
        // agarrado cuerda izqueirda 2


        return 99


    }

}