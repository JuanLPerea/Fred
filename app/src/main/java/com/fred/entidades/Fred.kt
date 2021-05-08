package com.fred.entidades

import android.util.Log
import com.fred.Laberinto

class Fred() {

    var vida = 15
    var balas = 6
    var puntos = 0
    var cuerda = false
    var saltando = false
    var scroll = true
    var scrollTick = 0
    var lado = 0

    fun animacionFred(
        pulsadoDerecha: Boolean,
        pulsadoIzquierda: Boolean,
        pulsadoArriba: Boolean,
        pulsadoAbajo: Boolean,
    ): Int {

        //  Log.d("Miapp" , "pasoX: " + pasoX + " PasoY: " + pasoY + " Lx-1: " + miLaberinto.map[cX+1][cY] + " Lx+1: " + miLaberinto.map[cX+1][cY])

        if (!saltando && !cuerda) {
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



        // saltando derecha
        if (pulsadoArriba && !saltando && scrollTick == 0) {
            saltando = true
            scroll = false
            scrollTick == 1
            if (lado == 0) return 6 else return 7
        }
        if (saltando) {
            scrollTick++
            when (scrollTick) {
                in 1..4 -> {
                    if (lado == 0) return 6 else return 7
                }
                5 -> {
                    scrollTick = 0
                    saltando = false
                    if (cuerda) {
                        if (lado == 0) return 8 else return 10
                    } else {
                        if (lado == 0) return 0 else return 3
                    }

                }
            }
        }


    // saltando izquierda
    // agarrado cuerda derecha 1
    // agarrado cuerda derecha 2
    // agarrado cuerda izquierda 1
    // agarrado cuerda izqueirda 2


    return 99


}


/*

    // comprobamos si fred mira a la derecha o la izquierda
    if (fred.lado == 0) {
        // Fred mira a la derecha
        // comprobar si está saltando
        if (fred.saltando && fred.animacionSalto <= 4) {
            fred.saltar()
            lienzo.drawBitmap(fredSaltandoI, null, rectDestino, null)

            if (fred.animacionSalto == 4) {
                // Comprobar si hay cuerda y agarrarse si la hay
                if (miLaberinto.map[cX][cY-1] == 0 && pasoX == 0) {
                    fred.cuerda = true
                    fred.animacionSalto = 0
                    fred.saltando = false
                }
            }

        } else {

            // Esperar 2 ticks después de un salto hasta que pueda continuar
            if (fred.animacionSalto != 0) fred.animacionSalto++
            if (fred.animacionSalto == 6) {
                fred.animacionSalto = 0
                fred.saltando = false
            }


            // comprobamos si está en una cuerda
            if (fred.cuerda) {
                if (pulsado_arriba || pulsado_abajo) {
                    when (fred.animacionCuerda) {
                        0-> lienzo.drawBitmap(fredCuerda1D, null, rectDestino, null)
                        1-> lienzo.drawBitmap(fredCuerda2D, null, rectDestino, null)
                    }
                    fred.animarCuerda()
                } else {
                    lienzo.drawBitmap(fredCuerda1D, null, rectDestino, null)
                }

                if (pulsado_izquierda) {
                    if (miLaberinto.map[cX-1][cY] == 0 && pasoY == -160) {
                        fred.saltandoDeCuerda = true
                        fred.cuerda = false
                    }

                }



            } else {
                // fred por tanto está de pie
                when (fred.animacion) {
                    0 -> lienzo.drawBitmap(fredd, null, rectDestino, null)
                    1 -> lienzo.drawBitmap(fredd1, null, rectDestino, null)
                    2 -> lienzo.drawBitmap(fredd2, null, rectDestino, null)
                    3 -> lienzo.drawBitmap(fredd1, null, rectDestino, null)
                }
            }
        }

        // -----------------------------------------------------------------------------------------------------------------

    } else {
        // Fred mira a la izquierda
        if (fred.saltando && fred.animacionSalto <= 4) {
            fred.saltar()
            lienzo.drawBitmap(fredSaltandoD, null, rectDestino, null)

            if (fred.animacionSalto == 4) {
                // Comprobar si hay cuerda y agarrarse si la hay
                if (miLaberinto.map[cX][cY-1] == 0 && pasoX == 0) {
                    fred.cuerda = true
                    fred.animacionSalto = 0
                    fred.saltando = false
                }
            }

        }else {
            if (fred.animacionSalto != 0) fred.animacionSalto++
            if (fred.animacionSalto == 6) {
                fred.animacionSalto = 0
                fred.saltando = false
            }

            // comprobamos si está en una cuerda
            if (fred.cuerda) {
                if ((pulsado_arriba || pulsado_abajo) ) {
                    when (fred.animacionCuerda) {
                        0 -> lienzo.drawBitmap(fredCuerda1I, null, rectDestino, null)
                        1 -> lienzo.drawBitmap(fredCuerda2I, null, rectDestino, null)
                    }
                    fred.animarCuerda()
                }else {
                    lienzo.drawBitmap(fredCuerda1I, null, rectDestino, null)
                }


            } else {
                // fred por tanto está de pie
                when (fred.animacion) {
                    0 -> lienzo.drawBitmap(fredi, null, rectDestino, null)
                    1 -> lienzo.drawBitmap(fredi1, null, rectDestino, null)
                    2 -> lienzo.drawBitmap(fredi2, null, rectDestino, null)
                    3 -> lienzo.drawBitmap(fredi1, null, rectDestino, null)
                }

            }
        }
    }






 */


}