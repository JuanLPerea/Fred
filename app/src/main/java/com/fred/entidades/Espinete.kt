package com.fred.entidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.fred.*

class Espinete : Enemigo () {

    lateinit var espinete1D : Bitmap
    lateinit var espinete2D : Bitmap
    lateinit var espinete1I : Bitmap
    lateinit var espinete2I : Bitmap
    var direccion = 0

    fun newEspinete (context: Context, coordenada: Coordenada) {
        // los espinetes existen en los pasillos que tienen 3 o mas tiles de ancho
        // siempre tiene que ser en una fila par
        espinete1D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1d)
        espinete2D = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2d)
        espinete1I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete1i)
        espinete2I = BitmapFactory.decodeResource(context.resources , R.drawable.espinete2i)

        pX = coordenada.coordenadaX
        pY = coordenada.coordenadaY
        Log.d("Miapp" , "Espinete en: " + pX + " - " + pY)

        animacionTick = (0..1).shuffled().last()
        offsetX = 384
        offsetY = 400
    }


    override fun actualizarEntidad(miLaberinto: Laberinto, cX: Int, cY: Int) {
        if (animacionTick == 0) animacionTick = 1 else animacionTick = 0
        // avanzan 32 pixels cada tick osea 4 pasos por tile
        // en las esquinas dan la vuelta y solo hacen 3 pasos
      //  Log.d("Miapp" , "OffsetX: " + offsetX )

        if (direccion == 0) {
            offsetX += 32
            // Si el espinete está en un extremo de su recorrido...
            if (offsetX == 480 && (miLaberinto.map[pX+1][pY+1] == 0 || miLaberinto.map[pX+1][pY] == 2)) {
                // ponemos a 0 el offset y avanzamos un tile y cambiamos de dirección
                offsetX = 384
                direccion = 1
            }
            // si el espinete puede seguir en esa dirección...
            if (offsetX == 512) {
                // avanzamos al siguiente tile y reseteamos el offset
                pX++
                offsetX = 384
            }
        } else {
            offsetX -= 32
            // Si el espinete está en un extremo de su recorrido...
            if (offsetX == 288 && (miLaberinto.map[pX-1][pY+1] == 0 || miLaberinto.map[pX-1][pY] == 2)) {
                // ponemos a 0 el offset y avanzamos un tile y cambiamos de dirección
                offsetX = 384
                direccion = 0
            }
            // si el espinete puede seguir en esa dirección...
            if (offsetX == 256) {
                // avanzamos al siguiente tile y reseteamos el offset
                pX--
                offsetX = 384
            }
        }
    }

    override fun devolverBitmap(): Bitmap {
        if (direccion == 0) {
            if (animacionTick == 0) return espinete1D else return espinete2D
        } else {
            if (animacionTick == 1) return espinete1I else return espinete2I
        }
    }

    override fun detectarColision(cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred): Boolean {
        // Los espinetes hacen daño cuando Fred está en el suelo y no está saltando
     //   if (((fred.estadoFred == EstadosFred.QUIETO || fred.estadoFred == EstadosFred.CAMINANDO) && !fred.cuerda)) {

           // Log.d("Miapp", "fX: ${fX} pasoX: ${pasoX} pX: ${pX} offsetX: ${offsetX}")

            // Calcular el punto de arriba de la izquierda del sprite
            //  rectDestino.offsetTo((diferenciaX * 128) + pasoX + enemigo.offsetX, (diferenciaY * 160) + pasoY + enemigo.offsetY)
            val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)


            // finalmente comprobamos que las coordenadas no se solapen (Uffffff)
            var colision = true
            if (coordenadasCaja.x1 > coordenadasCaja.fredx2) colision = false
            if (coordenadasCaja.x2 < coordenadasCaja.fredx1) colision = false
            if (coordenadasCaja.y1 > coordenadasCaja.fredy2) colision = false
            if (coordenadasCaja.y2 < coordenadasCaja.fredy1) colision = false

            Log.d ("Miapp" , "Espinete: ${coordenadasCaja.x1},${coordenadasCaja.y1}   ${coordenadasCaja.x2},${coordenadasCaja.y2}    Fred: ${coordenadasCaja.fredx1},${coordenadasCaja.fredy1}    ${coordenadasCaja.fredx2},${coordenadasCaja.fredy2}     ${colision}")
            return colision

    }

    fun dibujarCajasColision (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {

            val coordenadasCaja = calcularCoordenadas(cX, cY,pasoX,pasoY,fred)

        return coordenadasCaja
    }

    fun calcularCoordenadas (cX: Int, cY: Int, pasoX: Int, pasoY: Int, fred: Fred) : CajaDeColision {
        // Calcular el punto de arriba de la izquierda del sprite
        //  rectDestino.offsetTo((diferenciaX * 128) + pasoX + enemigo.offsetX, (diferenciaY * 160) + pasoY + enemigo.offsetY)
        var desplazamientoX = 17
        var desplazamientoY = 135
        if (direccion == 1) desplazamientoX = 95

        val anchuraEspinete = 15
        val alturaEspinete = 20
        val diferenciaX = pX - cX
        val diferenciaY = pY - cY

        // punto de la esquina de arriba de la izquierda del sprite
        val x1 = (diferenciaX * 128) + pasoX + offsetX + desplazamientoX
        val y1 = (diferenciaY * 160) + pasoY + offsetY + desplazamientoY
        val x2 = x1 + anchuraEspinete
        val y2 = y1 + alturaEspinete

        // ------------------------------------------------------------------------------------------------------------------------------------------

        // Sprite Fred completo
        var fredx1 = 384
        var fredy1 = 240
        var fredx2 = 512
        var fredy2 = 400
        // Coordenadas de Fred, dependen de como se esté moviendo
        when {
            !fred.cuerda && fred.lado == Lado.DERECHA && (fred.estadoFred == EstadosFred.QUIETO || (fred.estadoFred == EstadosFred.CAMINANDO && fred.scrollTick != 2 ))-> {
                // Fred quieto derecha o caminando en la animacion 0 ò 1
                fredx1 = 400
                fredy1 = 264
                fredx2 = 496
                fredy2 = 400
            }
            !fred.cuerda && fred.lado == Lado.IZQUIERDA && (fred.estadoFred == EstadosFred.QUIETO || (fred.estadoFred == EstadosFred.CAMINANDO  && fred.scrollTick != 2))-> {
                // fred quieto izquierda o caminando en la animacion 0 ó 1
                fredx1 = 400
                fredy1 = 264
                fredx2 = 490
                fredy2 = 400
            }
            fred.estadoFred == EstadosFred.CAMINANDO && fred.scrollTick == 2 -> {
                // fred caminando derecha o izquierda
                fredx1 = 384
                fredy1 = 264
                fredx2 = 512
                fredy2 = 400
            }
            fred.estadoFred == EstadosFred.SALTANDO || fred.estadoFred == EstadosFred.SALTANDOCUERDA -> {
                // fred saltando
                fredx1 = 384
                fredy1 = 246
                fredx2 = 512
                fredy2 = 372
            }
            fred.lado == Lado.IZQUIERDA && fred.cuerda -> {
                // fred cuerda derecha
                fredx1 = 432
                fredy1 = 241
                fredx2 = 512
                fredy2 = 340
            }
            fred.lado == Lado.DERECHA && fred.cuerda -> {
                // fred cuerda izquierda
                fredx1 = 384
                fredy1 = 241
                fredx2 = 464
                fredy2 = 340
            }

        }

        val coordenadasCaja = CajaDeColision(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat() , fredx1.toFloat(),fredy1.toFloat(),fredx2.toFloat(),fredy2.toFloat())

        return coordenadasCaja
    }



}