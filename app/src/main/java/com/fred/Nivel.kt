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
                 textoNivel = "Nivel 1 - Fred comienza su aventura, cuidado con los fantasmas, espinetes y gotas de ácido"
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
                textoNivel = "Nivel 2 - Fred comienza su aventura, aparecen los camaleones y momias"
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
                textoNivel = "Nivel 3 - Cuidado Fred, los Vampiros van a por tí!!!"
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
                textoNivel = "Nivel 4 - Esqueletos!! están locos por tus huesos!!"
            }


        }

    }
}