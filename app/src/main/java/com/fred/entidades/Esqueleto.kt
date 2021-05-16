package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.Coordenada
import com.fred.Direcciones
import com.fred.Laberinto
import com.fred.R

class Esqueleto : Enemigo() {

    lateinit var esqueleto1d : Bitmap
    lateinit var esqueleto2d : Bitmap
    lateinit var esqueleto3d : Bitmap
    lateinit var esqueleto1i : Bitmap
    lateinit var esqueleto2i : Bitmap
    lateinit var esqueleto3i : Bitmap
    lateinit var esqueletocuerda1 : Bitmap
    lateinit var esqueletocuerda2 : Bitmap

    var direccion = Direcciones.DERECHA
    var movimiento = Direcciones.PARADO
    lateinit var listaCoordenadas : MutableList<Coordenada>

    fun newEsqueleto(context: Context, coordenada: Coordenada, miLaberinto: Laberinto) {
         var esqueleto1d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1d)
         var esqueleto2d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2d)
         var esqueleto3d = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3d)
         var esqueleto1i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto1i)
         var esqueleto2i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto2i)
         var esqueleto3i = BitmapFactory.decodeResource(context.resources, R.drawable.esqueleto3i)
         var esqueletocuerda1 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda1)
         var esqueletocuerda2 = BitmapFactory.decodeResource(context.resources, R.drawable.esqueletocuerda2)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        animacionTick = (0..2).shuffled().last()
        offsetX = 384
        offsetY = 400

        // añadimos esta coordenada a la lista como ya recorrida
        listaCoordenadas = mutableListOf()
        listaCoordenadas.add(Coordenada(pX, pY))

        Log.d("Miapp", "Esqueleto en: ${pX} - ${pY} ")

    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        // los esqueletos van siguiendo su camino, pueden subir y bajar cuerdas
        // si se encuentran sin salida, dan la vuelta
        // guardar el camino recorrido y seguir prioritariamente hacia la zona no recorrido del laberinto
        // en el laberinto siempre hay 511 tiles de pasillo
        // si ven a Fred, van a por el (esto no estoy seguro pero sería muy guay hacerlo)


        // seguir el camino en la dirección actual hasta que nos encontremos con un cambio de dirección o sin salida
        when (direccion) {
            Direcciones.DERECHA -> {
                offsetX += 32
                if (offsetX == 512) {
                    offsetX = 384
                    if (esCambioDireccion(miLaberinto)) {
                        elegirDireccion(miLaberinto)
                    }  else {
                        pX++
                        listaCoordenadas.add(Coordenada(pX,pY))
                    }
                }
            }

            Direcciones.IZQUIERDA -> {

            }

            Direcciones.ARRIBA -> {

            }

            Direcciones.ABAJO -> {

            }
        }



    }

    private fun elegirDireccion (miLaberinto: Laberinto) : Direcciones {

        var direccionElegida = Direcciones.DERECHA
        // comprobar las salidas posibles y elegir según los criterios
        var salidas = mutableListOf<Coordenada>()
        // Mirar que direcciones podemos ir
        if (pY % 2 == 0 && pX < 34 && miLaberinto.map[pX + 1][pY] == 0) salidas.add(Coordenada(pX + 1, pY))
        if (pY % 2 == 0 && pX > 4 && miLaberinto.map[pX - 1][pY] == 0) salidas.add(Coordenada(pX - 1, pY))
        if (pX % 2 == 0 && pY < 34 && miLaberinto.map[pX][pY + 1] == 0) salidas.add(Coordenada(pX , pY + 1))
        if (pX % 2 == 0 && pY > 4 && miLaberinto.map[pX][pY - 1] == 0) salidas.add(Coordenada(pX , pY + 1))

        // Mirar cuantas salidas tenemos
        when (salidas.size) {
            0 -> {
                // Sin salida imposible porque si hemos venido por algún sitio siempre podemos volvernos en dirección contraria
                Log.d("Miapp" , "No deberías ver esto, algo ha fallado")
            }
            1 -> {
                // Solo hay una dirección posible, elegirla
                direccion = traducirDireccion(salidas.first())
            }
            2 -> {
                // Hay dos direcciones posibles
                // Si no hemos recorrido ninguna de las 2, elegir una al azar
                // Si ya hemos recorrido alguna de las 2, elegir la que no hemos recorrido
                // si ya hemos recorrido las 2 esque estamos retrocediendo, dar la vuelta
                // TODO revisar la lógica del esqueleto, algo falla


            }
            3 -> {
                // Hay tres direcciones posibles
            }

        }



        return direccionElegida
    }

    private fun esCambioDireccion (miLaberinto: Laberinto) : Boolean {
        when (direccion) {
            Direcciones.DERECHA -> {
                if (miLaberinto.map[pX + 1][pY] == 0) return false
            }
            Direcciones.IZQUIERDA -> {
                if (miLaberinto.map[pX - 1][pY] == 0) return false
            }
            Direcciones.ABAJO -> {
                if (miLaberinto.map[pX][pY + 1] == 0) return false
            }
            Direcciones.ARRIBA -> {
                if (miLaberinto.map[pX][pY + 1] == 0) return false
            }
        }
        return true
    }

    private fun traducirDireccion (coordenada: Coordenada) : Direcciones {
        if (coordenada.coordenadaX == pX + 1) return Direcciones.DERECHA
        if (coordenada.coordenadaX == pX - 1) return Direcciones.IZQUIERDA
        if (coordenada.coordenadaY == pY + 1) return Direcciones.ABAJO
        if (coordenada.coordenadaY == pY - 1) return Direcciones.ARRIBA
        return Direcciones.PARADO
    }

    override fun devolverBitmap(): Bitmap {

        return esqueleto1d
    }

    override fun detectarColision(fX: Int, fY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        return false
    }
}