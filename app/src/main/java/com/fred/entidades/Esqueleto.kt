package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.*

class Esqueleto : Enemigo() {

    lateinit var esqueleto1d : Bitmap
    lateinit var esqueleto2d : Bitmap
    lateinit var esqueleto3d : Bitmap
    lateinit var esqueleto1i : Bitmap
    lateinit var esqueleto2i : Bitmap
    lateinit var esqueleto3i : Bitmap
    lateinit var esqueletocuerda1 : Bitmap
    lateinit var esqueletocuerda2 : Bitmap

    var direccion = Direcciones.PARADO
    lateinit var laberintoEsqueleto : Laberinto
    var valorsalidaderecha = 0
    var valorsalidaizquierda = 0
    var valorsalidaarriba = 0
    var valorsalidaabajo = 0

    fun newEsqueleto(context: Context, coordenada: Coordenada, miLaberinto: Laberinto) {
         esqueleto1d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1d)
         esqueleto2d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2d)
         esqueleto3d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3d)
         esqueleto1i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1i)
         esqueleto2i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2i)
         esqueleto3i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3i)
         esqueletocuerda1 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda1)
         esqueletocuerda2 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda2)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..2).shuffled().last()
        offsetX = 384
        offsetY = 400

        // Cada esqueleto se acuerda de su propio laberinto, así que lo copiamos para cada esqueleto
        // (No creo que en el Spectrum lo hicieran así, espero que no pete la memoria!!!!)
        laberintoEsqueleto = miLaberinto
        // decrementamos el valor de la posición del esqueleto ...
        // ... a menor valor, mayor número de veces que el esqueleto ha pasado por allí
        // siempre elegirá el valor mas alto para decidir la dirección a la que va
        laberintoEsqueleto.map[pX][pY]--
        direccion = elegirDireccion(miLaberinto)

        Log.d("Miapp", "Esqueleto en: ${pX} - ${pY} ")

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        // los esqueletos van siguiendo su camino, pueden subir y bajar cuerdas
        // si se encuentran sin salida, dan la vuelta
        // guardar el camino recorrido y seguir prioritariamente hacia la zona no recorrida del laberinto
        // en el laberinto siempre hay 512 tiles de pasillo
        // si ven a Fred, van a por el!!

        val direccionFred = fredALaVista(cX , cY)

        if (direccionFred != Direcciones.PARADO) {
            // Si ve a Fred, elegir la dirección
            direccion = direccionFred
        } else {
            // si no ve a Fred, elige la dirección mas conveniente cuando estemos en el centro del tile
            if (offsetX == 512) {
                offsetX = 384
                pX++
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto)
            }

            if (offsetX == 256) {
                offsetX = 384
                pX--
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto)
            }

            if (offsetY == 560) {
                offsetY = 400
                pY++
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto)
            }

            if (offsetY == 240) {
                offsetY = 400
                pY--
                laberintoEsqueleto.map[pX][pY]--            // Decrementamos en 1 el valor de la posición del laberinto del esqueleto
                direccion = elegirDireccion(miLaberinto)
            }

        }

        // actualizamos la animación
        animacionTick++
        if (animacionTick == 4) animacionTick = 0

        // actualizamos la posición
        when (direccion) {
            Direcciones.DERECHA -> {
                offsetX += 32
            }
            Direcciones.IZQUIERDA -> {
                offsetX -= 32
            }
            Direcciones.ABAJO -> {
                offsetY += 32
            }
            Direcciones.ARRIBA -> {
                offsetY -= 32
            }
        }

    }

    private fun elegirDireccion (miLaberinto: Laberinto) : Direcciones {
        var direccionElegida = Direcciones.DERECHA
        // comprobar las salidas posibles y elegir según los criterios
        var salidas = mutableListOf<DecisionEsqueleto>()

        // Mirar que direcciones podemos ir, si el valor es menor de '2' (Muro) esque podemos movernos...
        valorsalidaderecha = miLaberinto.map[pX + 1][pY]
        if (valorsalidaderecha < 2) salidas.add(DecisionEsqueleto(Direcciones.DERECHA, valorsalidaderecha))
        valorsalidaizquierda = miLaberinto.map[pX - 1][pY]
        if (valorsalidaizquierda < 2) salidas.add(DecisionEsqueleto(Direcciones.IZQUIERDA, valorsalidaizquierda))
        valorsalidaabajo = miLaberinto.map[pX][pY + 1]
        if (valorsalidaabajo < 2) salidas.add(DecisionEsqueleto(Direcciones.ABAJO, valorsalidaabajo))
        valorsalidaarriba = miLaberinto.map[pX][pY - 1]
        if (valorsalidaarriba < 2) salidas.add(DecisionEsqueleto(Direcciones.ARRIBA, valorsalidaarriba))

        // ordenamos las salidas por su valor y elegimos la mas alta, que será la que no hemos recorrido o hemos pasado menos veces
        salidas.sortBy { it.valor }
        return salidas.first().direccion
    }

    private fun fredALaVista (cX: Int , cY: Int) : Direcciones {
        // Niños no hardcodeeis esto en casa
        when {
            // Mirar a la izquierda a ver si ve a Fred
            (pX > 4 && laberintoEsqueleto.map[pX-1][pY] != 2 && cX - 1 == pX && cY == pY) -> return Direcciones.IZQUIERDA
            (pX > 5 && laberintoEsqueleto.map[pX-1][pY] != 2 && laberintoEsqueleto.map[pX-2][pY] != 2 && cX - 2 == pX && cY == pY) -> return Direcciones.IZQUIERDA
            (pX > 6 && laberintoEsqueleto.map[pX-1][pY] != 2 && laberintoEsqueleto.map[pX-2][pY] != 2 && laberintoEsqueleto.map[pX-3][pY] != 2 && cX - 3 == pX && cY == pY) -> return Direcciones.IZQUIERDA
            (pX > 7 && laberintoEsqueleto.map[pX-1][pY] != 2 && laberintoEsqueleto.map[pX-2][pY] != 2 && laberintoEsqueleto.map[pX-3][pY] != 2 && laberintoEsqueleto.map[pX-4][pY] != 2 && cX - 4 == pX && cY == pY) -> return Direcciones.IZQUIERDA
            // Mirar a la derecha a ver si ve a Fred
            (pX < 34 && laberintoEsqueleto.map[pX+1][pY] != 2 && cX + 1 == pX && cY == pY) -> return Direcciones.DERECHA
            (pX < 33 && laberintoEsqueleto.map[pX+1][pY] != 2 && laberintoEsqueleto.map[pX+2][pY] != 2 && cX + 2 == pX && cY == pY) -> return Direcciones.DERECHA
            (pX < 32 && laberintoEsqueleto.map[pX+1][pY] != 2 && laberintoEsqueleto.map[pX+2][pY] != 2 && laberintoEsqueleto.map[pX+3][pY] != 2 && cX + 3 == pX && cY == pY) -> return Direcciones.DERECHA
            (pX < 31 && laberintoEsqueleto.map[pX+1][pY] != 2 && laberintoEsqueleto.map[pX+2][pY] != 2 && laberintoEsqueleto.map[pX+3][pY] != 2 && laberintoEsqueleto.map[pX+4][pY] != 2 && cX + 4 == pX && cY == pY) -> return Direcciones.DERECHA
            // Mirar abajo a ver si ve a Fred
            (pY < 34 && laberintoEsqueleto.map[pX][pY+1] != 2 && cX == pX && cY + 1 == pY) -> return Direcciones.ABAJO
            (pY < 33 && laberintoEsqueleto.map[pX][pY+1] != 2 && laberintoEsqueleto.map[pX][pY+2] != 2 && cX == pX && cY + 2 == pY ) -> return Direcciones.ABAJO
            (pY < 32 && laberintoEsqueleto.map[pX][pY+1] != 2 && laberintoEsqueleto.map[pX][pY+2] != 2 && laberintoEsqueleto.map[pX][pY+3] != 2 && cX == pX && cY + 3 == pY) -> return Direcciones.ABAJO
            (pY < 31 && laberintoEsqueleto.map[pX][pY+1] != 2 && laberintoEsqueleto.map[pX][pY+2] != 2 && laberintoEsqueleto.map[pX][pY+3] != 2 && laberintoEsqueleto.map[pX][pY + 4] != 2 && cX == pX && cY + 4 == pY) -> return Direcciones.ABAJO
            // Mirar arriba a ver si ve a Fred
            (pY > 4 && laberintoEsqueleto.map[pX][pY-1] != 2 && cX == pX && cY - 1 == pY) -> return Direcciones.ABAJO
            (pY > 5 && laberintoEsqueleto.map[pX][pY-1] != 2 && laberintoEsqueleto.map[pX][pY-2] != 2 && cX == pX && cY - 2 == pY ) -> return Direcciones.ABAJO
            (pY > 6 && laberintoEsqueleto.map[pX][pY-1] != 2 && laberintoEsqueleto.map[pX][pY-2] != 2 && laberintoEsqueleto.map[pX][pY-3] != 2 && cX == pX && cY - 3 == pY) -> return Direcciones.ABAJO
            (pY > 7 && laberintoEsqueleto.map[pX][pY-1] != 2 && laberintoEsqueleto.map[pX][pY-2] != 2 && laberintoEsqueleto.map[pX][pY-3] != 2 && laberintoEsqueleto.map[pX][pY - 4] != 2 && cX == pX && cY - 4 == pY) -> return Direcciones.ABAJO
        }
        return Direcciones.PARADO
    }



    override fun devolverBitmap(): Bitmap {
        when (direccion) {
            Direcciones.DERECHA -> {
                when (animacionTick) {
                    0 -> return esqueleto1d
                    1 -> return esqueleto2d
                    2 -> return esqueleto3d
                    3 -> return esqueleto2d
                }
            }

            Direcciones.IZQUIERDA -> {
                when (animacionTick) {
                    0 -> return esqueleto1i
                    1 -> return esqueleto2i
                    2 -> return esqueleto3i
                    3 -> return esqueleto2i
                }
            }

            Direcciones.ABAJO , Direcciones.ARRIBA -> {
                if (animacionTick % 2 == 0) {
                    return esqueletocuerda1
                } else {
                    return esqueletocuerda2
                }
            }

        }

        return esqueleto1d
    }

    override fun detectarColision(fX: Int, fY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        return false
    }
}