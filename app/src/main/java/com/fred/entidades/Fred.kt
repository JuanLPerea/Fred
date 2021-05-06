package com.fred.entidades

import android.util.Log

class Fred () {

    var vida = 15
    var balas = 6
    var puntos = 0
    var animacion = 0
    var animacionCuerda = 0
    var animacionSalto = 0
    var cuerda = false
    var saltando = false
    var lado = 0
    var cX = 0
    var cY = 0
    var pasoX = 0
    var pasoY = 0


    fun animar() {
        animacion++
        if (animacion==4) animacion=0
    }

    fun cambiarLado() {
        if (lado == 0) lado = 1 else lado = 0
    }

    fun saltar() {
        animacionSalto++
        if (animacionSalto == 4) {
            saltando = false
        }
    }

    fun animarCuerda() {
        if (animacionCuerda == 0) animacionCuerda = 1 else animacionCuerda = 0
    }


}