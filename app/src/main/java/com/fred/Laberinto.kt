package com.fred

import android.util.Log
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random as Random

class Laberinto {

    // Array (36x37)
    var map = arrayOf(
        //          V  V  V  M  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  P  M  V  V  V
        //          0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),   // fila 0 cielo
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),   // fila 1 cielo
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),   // fila 2 cielo
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),   // fila 3 roca
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),   // fila 4 pasillo
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
    )

    // 1 -> Zona exterior del laberinto
    // 2 -> Muro
    // 0 -> Camino
    // 3 -> Trampilla Salida


    fun generarLaberinto() {
        // obtenemos la posición inicial aleatoriamente entre la columna 2 y la 32
        var posicionX = valorRandom(3..18) * 2
        var posicionY = 34
        var bifurcaciones = Array(600) { IntArray(2) }
        var indice_bifurcaciones = 0
        map[posicionX][posicionY] = 0
        var maxBuffer = 0

        // Dibujamos el laberinto en la memoria
        // Columnas pares puede haber camino arriba - abajo
        // Filas pares puede haber camino derecha - izquierda

        // Mover posición
        // Ver posibles movimientos
        // 0 -> Izquierda
        // 1 -> Abajo
        // 2 -> Derecha
        // 3 -> Arriba

        // Si estamos en una columna par podemos movernos a izquierda y derecha

        do {
            val salidasActuales = comprobarSalidas(posicionX, posicionY)

            if (salidasActuales.length > 0) {
                // si tenemos mas de un posible movimiento, guardamos la posición para volver si encontramos un punto sin salida
                bifurcaciones[indice_bifurcaciones][0] = posicionX
                bifurcaciones[indice_bifurcaciones][1] = posicionY
                indice_bifurcaciones++
                if (indice_bifurcaciones > maxBuffer) maxBuffer = indice_bifurcaciones

                val movpos = salidasActuales[(0..(salidasActuales.length - 1)).shuffled().last()]
                // hacer el movimiento
                when (movpos) {
                    '0' -> posicionX--
                    '1' -> posicionY++
                    '2' -> posicionX++
                    '3' -> posicionY--
                }
                map[posicionX][posicionY] = 0

            } else {
                // Si no hay salida, volver a una posición guardada que tuviera mas de una salida
                if (indice_bifurcaciones > 0) {
                    indice_bifurcaciones--
                    posicionX = bifurcaciones[indice_bifurcaciones][0]
                    posicionY = bifurcaciones[indice_bifurcaciones][1]
                }
            }
      //      Log.d("MyApp", "Bifurcaciones " + indice_bifurcaciones)
        } while (indice_bifurcaciones != 0)

     //   Log.d("MyApp", "Maximo del buffer: " + maxBuffer)

        // Situar salida
        do {
            var salida = 0

            val aleat = (4..34).shuffled().last()
            if (map[aleat][5] == 2 && map[aleat][4] == 0) {
                salida = aleat
                map[aleat][3] = 3
            }

        } while (salida == 0)


  //      Log.d("MyApp", "Fin blucle")

    }

    fun valorRandom(valores: IntRange): Int {
        var valorRandom = Random.nextInt(valores.last - valores.first) + valores.first
        return valorRandom
    }

    fun comprobarSalidas(posicionX: Int, posicionY: Int): String {
        var posiblesSalidas = ""

        if (posicionY % 2 == 0) {
            // movimiento horizontal
            if (map[posicionX - 1][posicionY] == 2 && map[posicionX - 2][posicionY] == 2 && posicionX > 4) {
                // Podemos movernos a la izquierda
                posiblesSalidas += "0"
            }
            if (map[posicionX + 1][posicionY] == 2 && map[posicionX + 2][posicionY] == 2 && posicionX < 34) {
                posiblesSalidas += "2"
            }
        }

        // Movimiento en vertical
        if (posicionX % 2 == 0) {
            if (map[posicionX][posicionY + 1] == 2 && map[posicionX][posicionY + 2] == 2 && posicionY < 34) {
                posiblesSalidas += "1"
            }
            if (map[posicionX][posicionY - 1] == 2 && map[posicionX][posicionY - 2] == 2 && posicionY > 4) {
                posiblesSalidas += "3"
            }
        }

        return posiblesSalidas
    }

    fun posiblesUbicacionesEspinete (miLaberinto : Laberinto)  : MutableList<Coordenada> {
        var listaCoordenadas : MutableList<Coordenada> = mutableListOf()

        for (y in 2..32 step 2) {
            for (x in 2..34) {
                // tiene que haber 3 bloques solidos debajo como mínimo
                if (miLaberinto.map[x][y] == 0 &&
                    miLaberinto.map[x - 1][y] == 0 &&
                    miLaberinto.map[x + 1][y] == 0 &&
                    miLaberinto.map[x][y+1] == 2  &&
                    miLaberinto.map[x-1][y+1] == 2 &&
                    miLaberinto.map[x+1][y+1] == 2) {
                    listaCoordenadas.add(Coordenada(x,y))
                }
            }
        }
        listaCoordenadas.shuffle()
        return listaCoordenadas
    }
    
    fun posiblesUbicacionesGota (miLaberinto: Laberinto) : MutableList<Coordenada> {
        var listaCoordenadas : MutableList<Coordenada> = mutableListOf()

        for (y in 2..32 step 2) {
            for (x in 2..34) {
                if (miLaberinto.map[x][y] == 0 &&
                    miLaberinto.map[x][y - 1] == 2 &&
                    miLaberinto.map[x][y+1] == 2  &&
                    miLaberinto.map[x-1][y - 1] == 2 &&
                    miLaberinto.map[x-1][y+1] == 2 &&
                    miLaberinto.map[x+1][y - 1] == 2 &&
                    miLaberinto.map[x+1][y+1] == 2) {
                    listaCoordenadas.add(Coordenada(x,y))
                }
            }
        }
        listaCoordenadas.shuffle()
        return listaCoordenadas
    }


}
