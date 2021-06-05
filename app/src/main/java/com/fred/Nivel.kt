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
                totalBalas = 4
                textoNivel = "Nivel 2 - Aparecen los camaleones y momias y siguen los Fantasmas ¿o son gorros de cocinero? :)"
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
                textoNivel = "Nivel 5 - Nivel bidón, recoge todos los tesoros que puedas!!\nPero sal antes de que se acabe el tiempo!!"
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
                textoNivel = "Nivel 6 - La venganza de Tut, 50 momias contra Fred ¿Quién ganará?"
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
                textoNivel = "Nivel 7 - Un montón de huesos, lucha contra 50 esqueletos!!!"
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
                textoNivel = "Nivel 8 - La pirámide de Drácula, 50 vampiros revolotean por sus pasillos"
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
                textoNivel = "Nivel 9 - Infierno Egipcio, 200 enemigos contra Fred ¿Sobrevivirás?"
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
                textoNivel = "Nivel 999 - Tu lo has elegido, así que no te quejes y disfruta!!!"
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
                textoNivel = "Nivel Extra - Eres un/a bestia parda!!\nGracias por jugar a Fred!!"
            }


        }

    }
}