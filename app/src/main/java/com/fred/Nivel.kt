package com.fred

class Nivel {

    var numeroGotasAcidoEnLaberinto = 0
    var numeroEspinetesEnLaberinto = 0
    var numeroFantasmasEnLaberinto = 0
    var numeroLagartijasEnLaberinto = 0
    var numeroMomiasEnLaberinto = 0
    var numeroVampirosEnLaberinto = 0
    var numeroEsqueletosEnLaberinto = 0
    // Numero de objetos en el laberinto
    var totalObjetos = 0
    var totalBalas = 0
    var textoNivel = ""


    fun cargarNivel(nivel: Int) {
        when (nivel) {
            1 -> {
                 numeroGotasAcidoEnLaberinto = 10
                 numeroEspinetesEnLaberinto = 10
                 numeroFantasmasEnLaberinto = 5
                 numeroLagartijasEnLaberinto = 0
                 numeroMomiasEnLaberinto = 0
                 numeroVampirosEnLaberinto = 0
                 numeroEsqueletosEnLaberinto = 0
                // Numero de objetos en el laberinto
                 totalObjetos = 15
                 totalBalas = 1
                 textoNivel = R.string.nivel1.toString()
            }

            2-> {
                numeroGotasAcidoEnLaberinto = 10
                numeroEspinetesEnLaberinto = 10
                numeroFantasmasEnLaberinto = 5
                numeroLagartijasEnLaberinto = 10
                numeroMomiasEnLaberinto = 10
                numeroVampirosEnLaberinto = 0
                numeroEsqueletosEnLaberinto = 0
                // Numero de objetos en el laberinto
                totalObjetos = 15
                totalBalas = 4
                textoNivel = R.string.nivel2.toString()
            }

            3-> {
                numeroGotasAcidoEnLaberinto = 10
                numeroEspinetesEnLaberinto = 10
                numeroFantasmasEnLaberinto = 5
                numeroLagartijasEnLaberinto = 10
                numeroMomiasEnLaberinto = 10
                numeroVampirosEnLaberinto = 5
                numeroEsqueletosEnLaberinto = 0
                // Numero de objetos en el laberinto
                totalObjetos = 10
                totalBalas = 5
                textoNivel = R.string.nivel3.toString()
            }

            4-> {
                numeroGotasAcidoEnLaberinto = 5
                numeroEspinetesEnLaberinto = 5
                numeroFantasmasEnLaberinto = 2
                numeroLagartijasEnLaberinto = 10
                numeroMomiasEnLaberinto = 2
                numeroVampirosEnLaberinto = 5
                numeroEsqueletosEnLaberinto = 5
                // Numero de objetos en el laberinto
                totalObjetos = 10
                textoNivel = R.string.nivel4.toString()
            }

            5-> {
                numeroGotasAcidoEnLaberinto = 0
                numeroEspinetesEnLaberinto = 0
                numeroFantasmasEnLaberinto = 0
                numeroLagartijasEnLaberinto = 0
                numeroMomiasEnLaberinto = 0
                numeroVampirosEnLaberinto = 0
                numeroEsqueletosEnLaberinto = 1
                // Numero de objetos en el laberinto
                totalObjetos = 100
                totalBalas = 0
                textoNivel = R.string.nivel5.toString()
            }

            6 -> {
                numeroGotasAcidoEnLaberinto = 0
                numeroEspinetesEnLaberinto = 0
                numeroFantasmasEnLaberinto = 0
                numeroLagartijasEnLaberinto = 0
                numeroMomiasEnLaberinto = 50
                numeroVampirosEnLaberinto = 0
                numeroEsqueletosEnLaberinto = 0
                // Numero de objetos en el laberinto
                totalObjetos = 5
                totalBalas = 20
                textoNivel = R.string.nivel6.toString()
            }

            7 -> {
                numeroGotasAcidoEnLaberinto = 0
                numeroEspinetesEnLaberinto = 0
                numeroFantasmasEnLaberinto = 0
                numeroLagartijasEnLaberinto = 0
                numeroMomiasEnLaberinto = 0
                numeroVampirosEnLaberinto = 0
                numeroEsqueletosEnLaberinto = 50
                // Numero de objetos en el laberinto
                totalObjetos = 10
                totalBalas = 10
                textoNivel = R.string.nivel7.toString()
            }

            8-> {
                numeroGotasAcidoEnLaberinto = 0
                numeroEspinetesEnLaberinto = 0
                numeroFantasmasEnLaberinto = 0
                numeroLagartijasEnLaberinto = 0
                numeroMomiasEnLaberinto = 0
                numeroVampirosEnLaberinto = 50
                numeroEsqueletosEnLaberinto = 0
                // Numero de objetos en el laberinto
                totalObjetos = 10
                totalBalas = 20
                textoNivel = R.string.nivel8.toString()
            }

            9-> {
                numeroGotasAcidoEnLaberinto = 20
                numeroEspinetesEnLaberinto = 20
                numeroFantasmasEnLaberinto = 20
                numeroLagartijasEnLaberinto = 20
                numeroMomiasEnLaberinto = 50
                numeroVampirosEnLaberinto = 30
                numeroEsqueletosEnLaberinto = 30
                // Numero de objetos en el laberinto
                totalObjetos = 10
                totalBalas = 20
                textoNivel = R.string.nivel9.toString()
            }

            999 -> {
                numeroGotasAcidoEnLaberinto = SharedApp.prefs.numerodegotas
                numeroEspinetesEnLaberinto = SharedApp.prefs.numerodeespinetes
                numeroFantasmasEnLaberinto = SharedApp.prefs.numerodefantasmas
                numeroLagartijasEnLaberinto = SharedApp.prefs.numerodelagartijas
                numeroMomiasEnLaberinto = SharedApp.prefs.numerodemomias
                numeroVampirosEnLaberinto = SharedApp.prefs.numerodevampiros
                numeroEsqueletosEnLaberinto = SharedApp.prefs.numerodeesqueletos
                // Numero de objetos en el laberinto
                totalObjetos = 10
                totalBalas = SharedApp.prefs.numerodebalas
                textoNivel = R.string.nivel999.toString()
            }

            else -> {
                numeroGotasAcidoEnLaberinto = (0..30).shuffled().last()
                numeroEspinetesEnLaberinto = (0..30).shuffled().last()
                numeroFantasmasEnLaberinto = (0..30).shuffled().last()
                numeroLagartijasEnLaberinto = (0..30).shuffled().last()
                numeroMomiasEnLaberinto = (0..30).shuffled().last()
                numeroVampirosEnLaberinto = (0..30).shuffled().last()
                numeroEsqueletosEnLaberinto = (0..30).shuffled().last()
                // Numero de objetos en el laberinto
                totalObjetos = (0..30).shuffled().last()
                totalBalas = (0..30).shuffled().last()
                textoNivel = R.string.nivelbestia.toString()
            }


        }

    }
}