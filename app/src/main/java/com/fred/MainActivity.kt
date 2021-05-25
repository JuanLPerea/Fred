package com.fred

import android.app.Dialog
import android.graphics.*
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.fred.entidades.*
import com.fred.items.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    lateinit var botonArriba: FloatingActionButton
    lateinit var botonIzquierda: FloatingActionButton
    lateinit var botonAbajo: FloatingActionButton
    lateinit var botonDerecha: FloatingActionButton
    lateinit var botonDisparo : ImageButton
    lateinit var miLaberinto: Laberinto
    lateinit var alturaTV: TextView
    lateinit var balasTV : TextView
    lateinit var vidaTV : TextView
    lateinit var nivelTV : TextView
    lateinit var puntosTV : TextView
    lateinit var puntosMax : TextView
    lateinit var fred: Fred
    lateinit var bala: Bala
    lateinit var barraVida : ImageView
    lateinit var imageViewMapa : ImageView
    lateinit var mapa : Bitmap

    // Sonidos
    lateinit var mediaPlayer : MediaPlayer

    // Variables
    var cX = 0
    var cY = 0
    var pasoX = 0
    var pasoY = 0
    var eliminarEnemigo = -1
    var eliminarObjeto = -1
    var pulsadoAbajo = false
    var pulsadoArriba = false
    var pulsadoIzquierda = false
    var pulsadoDerecha = false
    var pulsadoDisparo = false
    var velocidadJuego = 150L
    var mapaEncontrado = false
    var fin = false
    var nivel = 1
    var recordPuntos = 0
    var tick = 0
    var sonidoPow = false


    // Establecemos el número de enemigos de cada tipo
    var numeroGotasAcidoEnLaberinto = 0
    var numeroEspinetesEnLaberinto = 0
    var numeroFantasmasEnLaberinto = 0
    var numeroLagartijasEnLaberinto = 0
    var numeroMomiasEnLaberinto = 0
    var numeroVampirosEnLaberinto = 50
    var numeroEsqueletosEnLaberinto = 0

    // Numero de objetos en el laberinto
    var totalObjetos = 10

    val timer = Timer()

    lateinit var fredd : Bitmap                                     // 0
    lateinit var  fredd1 : Bitmap                                   // 1
    lateinit var  fredd2  : Bitmap                                  // 2
    lateinit var  fredi  : Bitmap                                   // 3
    lateinit var  fredi1 : Bitmap                                   // 4
    lateinit var  fredi2  : Bitmap                                  // 5
    lateinit var  fredSaltandoI : Bitmap                            // 6
    lateinit var  fredSaltandoD  : Bitmap                           // 7
    lateinit var  fredCuerda1I  : Bitmap                            // 8
    lateinit var  fredCuerda2I  : Bitmap                            // 9
    lateinit var  fredCuerda1D  : Bitmap                            // 10
    lateinit var  fredCuerda2D  : Bitmap                            // 11
    lateinit var  fredDisparoPieI  : Bitmap                         // 12
    lateinit var  fredDisparoPieD  : Bitmap                         // 13
    lateinit var  fredDisparoSaltoI  : Bitmap                       // 14
    lateinit var  fredDisparoSaltoD  : Bitmap                       // 15

    lateinit var freddrojo : Bitmap                                     // 0
    lateinit var  fredd1rojo : Bitmap                                   // 1
    lateinit var  fredd2rojo  : Bitmap                                  // 2
    lateinit var  fredirojo  : Bitmap                                   // 3
    lateinit var  fredi1rojo : Bitmap                                   // 4
    lateinit var  fredi2rojo  : Bitmap                                  // 5
    lateinit var  fredSaltandoIrojo : Bitmap                            // 6
    lateinit var  fredSaltandoDrojo  : Bitmap                           // 7
    lateinit var  fredCuerda1Irojo  : Bitmap                            // 8
    lateinit var  fredCuerda2Irojo  : Bitmap                            // 9
    lateinit var  fredCuerda1Drojo  : Bitmap                            // 10
    lateinit var  fredCuerda2Drojo  : Bitmap                            // 11
    lateinit var  fredDisparoPieIrojo  : Bitmap                         // 12
    lateinit var  fredDisparoPieDrojo  : Bitmap                         // 13
    lateinit var  fredDisparoSaltoIrojo  : Bitmap                       // 14
    lateinit var  fredDisparoSaltoDrojo  : Bitmap                       // 15

    lateinit var fondo : ImageView
    lateinit var roca1 : Bitmap
    lateinit var roca2 : Bitmap
    lateinit var roca3 : Bitmap
    lateinit var fin_cuerda : Bitmap
    lateinit var cuerda : Bitmap
    lateinit var inicio_cuerda : Bitmap
    lateinit var tierra : Bitmap
    lateinit var trampilla : Bitmap
    lateinit var cielo : Bitmap
    lateinit var pasillo : Bitmap

    var listaEnemigos : ArrayList<Enemigo> = arrayListOf()
    var listaObjetos : ArrayList<Objeto> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        // Sonidos
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer.start()



        // Cargar Sprites
        cargarSprites()

        botonArriba = findViewById(R.id.flecha_arriba)
        botonIzquierda = findViewById(R.id.flecha_izquierda)
        botonAbajo = findViewById(R.id.flecha_abajo)
        botonDerecha = findViewById(R.id.flecha_derecha)
        botonDisparo = findViewById(R.id.boton_disparo)
        alturaTV = findViewById(R.id.alturaTV)
        balasTV = findViewById(R.id.balasTV)
        vidaTV = findViewById(R.id.vidaTV)
        barraVida = findViewById(R.id.barraVida)
        imageViewMapa = findViewById(R.id.imageViewMapa)
        nivelTV = findViewById(R.id.nivelTV)
        puntosTV = findViewById(R.id.puntosTV)
        puntosMax = findViewById(R.id.maxTV)

        // Creamos a nuestro protagonista
        fred = Fred()
        // Hay un objeto 'Bala' para gestionar los disparos
        bala = Bala()
        bala.newBala(this)

        // Necesitamos un laberinto para jugar
        miLaberinto = Laberinto()

     //   dibujarLaberintoTexto(miLaberinto)

        // Variables para la posición -------------------------------------------------------------------------------------------------------
        if (savedInstanceState == null) {
            miLaberinto.generarLaberinto()
            cX = 0
            cY = 0
            pasoX = 0
            pasoY = -160

            // Situar al personaje en un punto aleatorio en la fila de mas abajo del laberinto
            cY = 34
            do {
                cX = (3..29).shuffled().last()
            } while (miLaberinto.map[cX][cY] != 0)
            crearFondo(cX - 4, cY - 3, pasoX, pasoY)
            actualizarBarraVida()
            crearEnemigos()
            crearObjetos()
        }
        // ---------------------------------------------------------------------------------------------------------------------------------------

        establecerListeners()

        // Con este Timer se actualiza la pantalla cada 200ms osea 5 fps!!
        // lo que le da ese toque tan rítmico que enganchaba en los 80's tic-tac-tic-tac-tic-tac
        // ¿fundiré un procesador del siglo 21??? sometiéndole a este terrible trabajo :D ???


        val ticks = object : TimerTask() {
            override fun run() {

         //       Log.d("Miapp" , "cX: $cX cY: $cY pasoX: $pasoX pasoY: $pasoY")
                // Cada tick se comprueban los botones y se muestra la pantalla actualizada
                // aquí gestionamos el estado de Fred para mostrar la animación que corresponda
                if (fred.tocado > 0) {
                    fred.tocado++
                    if (fred.tocado == 8) fred.tocado = 0
                }

                if (pulsadoDisparo ) {
                    if ((fred.cuerda && fred.estadoFred== EstadosFred.SALTANDOCUERDA) || (!fred.cuerda && (fred.estadoFred == EstadosFred.CAMINANDO || fred.estadoFred == EstadosFred.SALTANDO || fred.estadoFred == EstadosFred.SALTANDOCUERDA || fred.estadoFred == EstadosFred.QUIETO))) {
                        if (fred.balas > 0 && !bala.disparo) {
                            fred.disparando = true
                            fred.balas--
                            bala.disparar(cX, cY , pasoX , fred.lado, fred.estadoFred)
                        }
                    }
                    pulsadoDisparo = false
                }

                when {
                    // si Fred está saltando de una cuerda no hacer caso de los botones que pulsa el usuario
                    fred.estadoFred == EstadosFred.SALTANDOCUERDA -> {
                        if (fred.lado == Lado.DERECHA) {
                            moverDerecha()
                        } else {
                            moverIzquierda()
                        }
                    }

                    // si ha acabado de saltar dedicamos un tick a mirar si nos agarramos a una cuerda o seguimos de pie
                    fred.estadoFred == EstadosFred.DECISIONSALTO -> {
                        if (miLaberinto.map[cX][cY-1] == 0 || miLaberinto.map[cX][cY+1] == 0) {
                            if (pasoX == 0) fred.cuerda = true
                         //   if (fred.lado == Lado.DERECHA) fred.lado = Lado.IZQUIERDA else fred.lado = Lado.DERECHA
                        }
                        fred.estadoFred = EstadosFred.QUIETO
                    }

                    // Si no hay ningun botón pulsado, y Fred está caminando o moviéndose por una cuerda, hacemos que se quede quieto
                    !pulsadoDerecha && !pulsadoIzquierda && !pulsadoAbajo && !pulsadoArriba && (fred.estadoFred == EstadosFred.CAMINANDO || fred.estadoFred == EstadosFred.MOVIENDOCUERDA) -> {
                        fred.estadoFred = EstadosFred.QUIETO
                        fred.scrollTick = 0
                    }

                    // Si pulsamos el botón derecho ...
                    pulsadoDerecha -> {
                        // Lo primero mirar que no estemos saltando y si estamos saltando, no hacemos nada, dejamos que la vida fluya
                        if (fred.estadoFred == EstadosFred.CAMINANDO || fred.estadoFred == EstadosFred.QUIETO) {
                            // Miramos si está en una cuerda
                            if (fred.cuerda) {
                                // Si Fred está en una cuerda y mira a la izquierda y puede saltar a la derecha, salta
                                if (fred.lado == Lado.IZQUIERDA) {
                                    if (esPosibleMoverDerecha()) {
                                        moverDerecha()
                                        fred.lado = Lado.DERECHA
                                        fred.estadoFred = EstadosFred.SALTANDOCUERDA
                                    }
                                } else {
                                    // Si está mirando a la derecha, le damos la vuelta
                                    fred.lado = Lado.IZQUIERDA
                                }
                            } else {
                                // Si estamos en un pasillo y puede moverse a la derecha, y mira a la derecha, lo movemos
                                if (fred.lado == Lado.DERECHA) {
                                    if (esPosibleMoverDerecha()) {
                                        // Detectar si hay una cuerda y saltar si la hay
                                        moverDerecha()
                                        if (miLaberinto.map[cX + 1][cY + 1] == 0 && pasoX == -32) {
                                            fred.estadoFred = EstadosFred.SALTANDOCUERDA
                                        } else {
                                        // Si no hay cuerda, seguimos caminando
                                            fred.estadoFred = EstadosFred.CAMINANDO
                                        }

                                    } else {
                                        fred.estadoFred = EstadosFred.QUIETO
                                    }
                                } else {
                                    // si estamos en un pasillo y mira para el otro lado, le damos la vuelta
                                    fred.lado = Lado.DERECHA
                                }
                            }
                        }
                    }

                    pulsadoIzquierda -> {
                        // Lo primero mirar que no estemos saltando y si estamos saltando, no hacemos nada, dejamos que la vida fluya
                        if (fred.estadoFred == EstadosFred.CAMINANDO || fred.estadoFred == EstadosFred.QUIETO) {
                            // Miramos si está en una cuerda
                            if (fred.cuerda) {
                                // Si Fred está en una cuerda y mira a la izquierda y puede saltar a la derecha, salta
                                if (fred.lado == Lado.DERECHA) {
                                    if (esPosibleMoverIzquierda()) {
                                        moverIzquierda()
                                        fred.lado = Lado.IZQUIERDA
                                        fred.estadoFred = EstadosFred.SALTANDOCUERDA
                                    }
                                } else {
                                    // Si está mirando a la derecha, le damos la vuelta
                                    fred.lado = Lado.DERECHA
                                }
                            } else {
                                // Si estamos en un pasillo y puede moverse a la derecha, y mira a la derecha, lo movemos
                                if (fred.lado == Lado.IZQUIERDA) {
                                    if (esPosibleMoverIzquierda()) {
                                        moverIzquierda()
                                        if (miLaberinto.map[cX - 1][cY + 1] == 0 && pasoX == 32) {
                                            fred.estadoFred = EstadosFred.SALTANDOCUERDA
                                        } else {
                                            fred.estadoFred = EstadosFred.CAMINANDO
                                        }
                                    } else {
                                        fred.estadoFred = EstadosFred.QUIETO
                                    }
                                } else {
                                    // si estamos en un pasillo y mira para el otro lado, le damos la vuelta
                                    fred.lado = Lado.IZQUIERDA
                                }
                            }
                        }

                    }

                    pulsadoArriba -> {
                        if (fred.cuerda) {
                            // si está en una cuerda ...
                            // si se puede subir ... si no, quieto
                            if (esPosibleMoverArriba() && pasoX == 0) {
                                fred.estadoFred = EstadosFred.MOVIENDOCUERDA
                                moverArriba()
                            } else fred.estadoFred = EstadosFred.QUIETO
                        } else {
                            // si está en un pasillo ... saltar
                            fred.estadoFred = EstadosFred.SALTANDO
                            if (miLaberinto.map[cX][cY-1] == 3 && pasoX == 0) {
                               fin = true
                            }
                        }
                    }

                    pulsadoAbajo -> {
                        if (fred.cuerda) {
                            if (esPosibleMoverAbajo()) {
                                fred.estadoFred = EstadosFred.MOVIENDOCUERDA
                                moverAbajo()
                            } else fred.estadoFred = EstadosFred.QUIETO
                        }
                    }
                }


                listaEnemigos.forEach { enemigo ->
                    // actualizar enemigos
                    enemigo.actualizarEntidad(miLaberinto, cX, cY)
                }

                if (bala.disparo) {
                    bala.actualizarBala(miLaberinto)
                    if (bala.bX - cX > 4 || bala.bX - cX < -4) bala.eliminarBala()
                }


                runOnUiThread {
                    if (fin) {
                        fin = false
                        dialogoFin("Enhorabuena has salido!!")
                    } else {

                        // Dibujar el fondo
                        crearFondo(cX - 4, cY - 3, pasoX, pasoY)
                        if (eliminarEnemigo != -1) {
                            listaEnemigos.remove(listaEnemigos.get(eliminarEnemigo))
                            eliminarEnemigo = -1
                        }
                        if (eliminarObjeto != -1) {
                            sonidoPow = true
                            listaObjetos.remove(listaObjetos.get(eliminarObjeto))
                            eliminarObjeto = -1
                        }
                        alturaTV.text = "Altura: ${cY - 3}"
                        balasTV.text = "Balas ${fred.balas}"
                        nivelTV.text = "Nivel: ${nivel}"
                        vidaTV.text = "Vida: ${fred.vida}"
                        puntosTV.text = "Puntos: ${fred.puntos}"
                        puntosMax.text = "Max: ${recordPuntos}"

                        if (mapaEncontrado) {
                            imageViewMapa.setImageBitmap(mapa)
                            mapaEncontrado = false
                        }

                    }



                }
                //    Log.d("Miapp" , "pasoX: " + pasoX + " PasoY: " + pasoY)
                // Sonidos, lanzamos una corrutina para evitar bloquear el hilo principal
                CoroutineScope(Default).launch {
                    sonido()
                }
            }
        }

        timer.schedule(ticks, velocidadJuego, velocidadJuego)
    }


    suspend fun sonido() {
      //  mediaPlayer.reset()

       tick++
       if (tick == 4) tick = 0

        if (mediaPlayer.isPlaying) mediaPlayer.stop()

        when {
            fred.tocado == 2 -> {
                Log.d("Sonidos" , "Estado Fred ${fred.estadoFred}  TOCADO ${fred.tocado}")
                mediaPlayer = MediaPlayer.create(this, R.raw.hurt)
                mediaPlayer.start()
            }

            (fred.scrollTick == 2 && fred.estadoFred == EstadosFred.CAMINANDO) || (fred.cuerda && fred.scrollTickSaltoCuerda == 3) -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.tac)
                mediaPlayer.start()
            }

            fred.scrollTickSalto == 1 || fred.scrollTickSaltoCuerda == 1 ->{
                mediaPlayer = MediaPlayer.create(this, R.raw.salto)
                mediaPlayer.start()
            }

            tick == 2 && fred.cuerda  && fred.estadoFred == EstadosFred.MOVIENDOCUERDA -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.toc)
                mediaPlayer.start()
            }

            tick == 0 && fred.cuerda && fred.estadoFred == EstadosFred.MOVIENDOCUERDA -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.tic)
                mediaPlayer.start()
            }



            sonidoPow -> {
                sonidoPow = false
                mediaPlayer = MediaPlayer.create(this, R.raw.pow)
                mediaPlayer.start()
            }

        }

    }




    private fun crearObjetos() {

        // Ponemos un mapa en un sitio al azar
        var listaUbicacionesPasilloHorizontal = miLaberinto.posiblesUbicacionesGota(miLaberinto)
        listaUbicacionesPasilloHorizontal.shuffle()
        val mapa = Mapa()
         mapa.nuevoObjeto(this, listaUbicacionesPasilloHorizontal.first().coordenadaX, listaUbicacionesPasilloHorizontal.first().coordenadaY)
        listaObjetos.add(mapa)

        if (totalObjetos > listaUbicacionesPasilloHorizontal.size) totalObjetos = listaUbicacionesPasilloHorizontal.size-1

        // Creamos objetos al azar
        for (n in 1..totalObjetos) {

            val azar = (0..20).shuffled().last()
            when (azar) {
                in 1..5 -> {
                    val objeto = Pocima()
                    objeto.nuevoObjeto(this, listaUbicacionesPasilloHorizontal.get(n).coordenadaX, listaUbicacionesPasilloHorizontal.get(n).coordenadaY)
                    listaObjetos.add(objeto)
                }
                in 6..15 -> {
                    val objeto = Tesoro()
                    objeto.nuevoObjeto(this, listaUbicacionesPasilloHorizontal.get(n).coordenadaX, listaUbicacionesPasilloHorizontal.get(n).coordenadaY)
                    listaObjetos.add(objeto)
                }
                0 -> {
                    val objeto = Mapa()
                    objeto.nuevoObjeto(this, listaUbicacionesPasilloHorizontal.get(n).coordenadaX, listaUbicacionesPasilloHorizontal.get(n).coordenadaY)
                    listaObjetos.add(objeto)
                }
                in 15..20 -> {
                    val objeto = Balas()
                    objeto.nuevoObjeto(this, listaUbicacionesPasilloHorizontal.get(n).coordenadaX, listaUbicacionesPasilloHorizontal.get(n).coordenadaY)
                    listaObjetos.add(objeto)
                }
            }

        }


    }

    private fun crearEnemigos() {
        // crear enemigos optimizar para que no caiga en bucle infinito
        var totalEnemigos = 0
        // Crear Gotas de ácido
        var listaUbicacionesPasilloHorizontal = miLaberinto.posiblesUbicacionesGota(miLaberinto)
        if (numeroGotasAcidoEnLaberinto > listaUbicacionesPasilloHorizontal.size) numeroGotasAcidoEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in 1..numeroGotasAcidoEnLaberinto) {
            val gotaAcidoTMP = GotaAcido()
            gotaAcidoTMP.newGotaAcido(this, listaUbicacionesPasilloHorizontal.get(n))
            gotaAcidoTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(gotaAcidoTMP)
        }
        listaUbicacionesPasilloHorizontal.shuffle()
        // Crear espinetes
        if (numeroEspinetesEnLaberinto > listaUbicacionesPasilloHorizontal.size) numeroEspinetesEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in (1..numeroEspinetesEnLaberinto)) {
            val espineteTMP = Espinete()
            espineteTMP.newEspinete(this , listaUbicacionesPasilloHorizontal.get(n))
            espineteTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(espineteTMP)
        }
        listaUbicacionesPasilloHorizontal.shuffle()
        // Crear fantasmas
        if (numeroFantasmasEnLaberinto > listaUbicacionesPasilloHorizontal.size) numeroFantasmasEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in 1..numeroFantasmasEnLaberinto) {
            val fantasmaTMP = Fantasma()
            fantasmaTMP.newFantasma(this, listaUbicacionesPasilloHorizontal.get(n))
            fantasmaTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(fantasmaTMP)
        }

        // Crear lagartijas
        var listaUbicacionesLagartija = miLaberinto.posiblesUbicacionesLagartija(miLaberinto)
        if (numeroLagartijasEnLaberinto >= listaUbicacionesLagartija.size) numeroLagartijasEnLaberinto = listaUbicacionesLagartija.size - 1
        for (n in 1..numeroLagartijasEnLaberinto) {
            val lagartijaTMP = Lagartija()
            lagartijaTMP.newLagartija(this, listaUbicacionesLagartija.get(n))
            lagartijaTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(lagartijaTMP)
        }

        // Crear momias
        if (numeroMomiasEnLaberinto >= listaUbicacionesPasilloHorizontal.size) numeroMomiasEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in 1..numeroMomiasEnLaberinto) {
            val momiaTMP = Momia()
            momiaTMP.newMomia(this, listaUbicacionesPasilloHorizontal, n)
            momiaTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(momiaTMP)
        }

        listaUbicacionesPasilloHorizontal.shuffle()
        // Crear Vampiros
        if (numeroVampirosEnLaberinto >= listaUbicacionesPasilloHorizontal.size) numeroVampirosEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in 1..numeroVampirosEnLaberinto) {
            val vampiroTMP = Vampiro()
            val coordenada = listaUbicacionesPasilloHorizontal.get(n)
            vampiroTMP.newVampiro(this, coordenada)
            vampiroTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(vampiroTMP)
        }

        listaUbicacionesPasilloHorizontal.shuffle()
        // Crear Esqueletos
        if (numeroEsqueletosEnLaberinto >= listaUbicacionesPasilloHorizontal.size) numeroEsqueletosEnLaberinto = listaUbicacionesPasilloHorizontal.size - 1
        for (n in 1..numeroEsqueletosEnLaberinto) {
            val esqueletoTMP = Esqueleto()
            val coordenada = listaUbicacionesPasilloHorizontal.get(n)
            esqueletoTMP.newEsqueleto(this, coordenada, miLaberinto)
            esqueletoTMP.id = totalEnemigos
            totalEnemigos++
            listaEnemigos.add(esqueletoTMP)
        }

    }

    private fun establecerListeners() {

        botonDisparo.setOnTouchListener { v , event ->
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        pulsadoDisparo = true
                    }
                    MotionEvent.ACTION_UP -> {
                       // pulsadoDisparo = false
                    }
                }
            true
            }



        botonArriba.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsadoArriba = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsadoArriba = false
                }

            }
            true
        }


        botonAbajo.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsadoAbajo = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsadoAbajo = false
                }
            }

            true
        }


        botonDerecha.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsadoDerecha = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsadoDerecha = false
                }
            }
            true
        }

        botonIzquierda.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pulsadoIzquierda = true
                }
                MotionEvent.ACTION_UP -> {
                    pulsadoIzquierda = false
                }
            }



            true
        }
    }


    fun crearFondo(
        posX: Int,
        posY: Int,
        offsetInicialX: Int,
        offsetInicialY: Int,
    ) {

        var offsetx = offsetInicialX - 128
        var offsety = offsetInicialY - 80

        var bitmapFondo = Bitmap.createBitmap(896, 800, Bitmap.Config.ARGB_8888)
        var lienzo = Canvas(bitmapFondo)
        val rectDestino = Rect()
        rectDestino.set(0, 0, 128, 160)

        // La pantalla en horizontal está dividida en 6 espacios completos y está centrado de forma que se ven 1/2 por cada lado. En horizontal hace scroll en 4 pasos cada uno
        // en vertical son 4 completos y se ve 2 pasos del siguiente. Hace scroll con 5 pasos en vertical
        for (y in 0..7) {
            for (x in 0..8) {
                rectDestino.offsetTo(offsetx, offsety)
                // en los bordes dibujar la tierra o el cielo
                if (posY == 2 && y < 1) {
                    lienzo.drawBitmap(cielo, null, rectDestino, null)
                } else {

                    if (posY == 1 && y < 2) {
                        lienzo.drawBitmap(cielo, null, rectDestino, null)
                    } else {

                        if (posX + x <= 2 || posX + x > 35 || posY + y > 35) {
                            lienzo.drawBitmap(tierra, null, rectDestino, null)
                        } else {
                            if (miLaberinto.map[posX + x][posY + y] == 2) {
                                var muro_azar = 0
                                if ((posX + x) % 2 == 0) muro_azar = 1
                                if ((posX + x) % 3 == 0) muro_azar = 2
                                when (muro_azar) {
                                    0 -> lienzo.drawBitmap(roca1, null, rectDestino, null)
                                    1 -> lienzo.drawBitmap(roca2, null, rectDestino, null)
                                    2 -> lienzo.drawBitmap(roca3, null, rectDestino, null)
                                }
                            } else {
                                if (miLaberinto.map[posX + x][posY + y] == 3) {
                                    lienzo.drawBitmap(trampilla, null, rectDestino, null)
                                } else {
                                    // si estamos en un pasillo dibujar las cuerdas o fondo negro
                                    if (miLaberinto.map[posX + x][posY + y - 1] == 2 && miLaberinto.map[posX + x][posY + y + 1] != 2) {
                                        lienzo.drawBitmap(inicio_cuerda, null, rectDestino, null)
                                    } else {
                                        if (miLaberinto.map[posX + x][posY + y + 1] == 2 && miLaberinto.map[posX + x][posY + y - 1] != 2) {
                                            lienzo.drawBitmap(fin_cuerda, null, rectDestino, null)
                                        } else {
                                            if (miLaberinto.map[posX + x][posY + y + 1] != 2 && miLaberinto.map[posX + x][posY + y - 1] != 2) {
                                                lienzo.drawBitmap(cuerda, null, rectDestino, null)
                                            } else {
                                                lienzo.drawBitmap(pasillo, null, rectDestino, null)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                offsetx += 128
            }
            offsetx = offsetInicialX - 128
            offsety += 160
        }

        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Pintar los enemigos
        listaEnemigos.forEach { enemigo ->
            // si las coordenadas de la entidad enemiga están dentro de la zona visible....
            if (enemigo.pX > (cX-5) && enemigo.pX < (cX + 5) && enemigo.pY > (cY-4) && enemigo.pY < (cY + 4)) {
/*
                val cajaBala = bala.coordenadasCajaBala(cX,cY,pasoX,pasoY)
                var pintura = Paint()
                pintura.style = Paint.Style.STROKE
                pintura.strokeWidth = 4F
                pintura.setARGB(255,0,255,255)
                lienzo.drawCircle(cajaBala.coordenadaX.toFloat() , cajaBala.coordenadaY.toFloat() , 20F , pintura)
*/
                // Detectar que cada enemigo no le ha alcanzado una bala
                    if (!bala.impacto && bala.disparo && bala.detectarColision(cX, cY, pasoX, pasoY, enemigo)) {
                        when {
                            enemigo is Momia || enemigo is Esqueleto -> {
                                // Se destruye el enemigo y se muestra la animación de la nube
                                eliminarEnemigo = listaEnemigos.indexOf(enemigo)
                                bala.impacto = true
                            }
                            enemigo is Vampiro -> {
                                eliminarEnemigo = listaEnemigos.indexOf(enemigo)
                                bala.impacto = true
                            }
                            enemigo is Fantasma -> {
                                bala.impacto = false
                                bala.eliminarBala()
                                bala.animacionNube = 1
                                when (enemigo.movimiento) {
                                    Direcciones.DERECHA -> enemigo.movimiento = Direcciones.IZQUIERDA
                                    Direcciones.IZQUIERDA -> enemigo.movimiento = Direcciones.DERECHA
                                    Direcciones.ARRIBA -> enemigo.movimiento = Direcciones.ABAJO
                                    Direcciones.ABAJO -> enemigo.movimiento = Direcciones.ARRIBA
                                }

                            }
                        }
                    }




                // Detectar colisiones ...
/*
                // Descomentar este bloque de código para ver las cajas de colisión
              if (enemigo is Vampiro) {
                  val coordenadas = enemigo.dibujarCajasColision(cX,cY,pasoX,pasoY)
                  var pintura = Paint()
                  pintura.style = Paint.Style.STROKE
                  pintura.strokeWidth = 4F
                  pintura.setARGB(255,0,255,255)
                  lienzo.drawRect(coordenadas.x1 , coordenadas.y1 , coordenadas.x2  , coordenadas.y2, pintura)
              }
*/
                   if (enemigo.detectarColision(cX, cY, pasoX, pasoY, fred)) {
                       if (fred.tocado == 0) {
                           fred.vida--
                           fred.tocado = 1
                           actualizarBarraVida()
                       }

                       if (fred.vida == 0) {
                           dialogoFin("D.E.P. Fred")
                       }
                   }

                // Pintar enemigo ...
                val diferenciaX = enemigo.pX - cX
                val diferenciaY = enemigo.pY - cY
                val enemigoBitmap = enemigo.devolverBitmap()
                rectDestino.offsetTo((diferenciaX * 128) + pasoX + enemigo.offsetX, (diferenciaY * 160) + pasoY + enemigo.offsetY)
                lienzo.drawBitmap(enemigoBitmap, null, rectDestino, null)



            }
        }

        //Dibujar Objetos ---------------------------------------------------------------------------------------------------------------------------------------
        listaObjetos.forEach { objeto ->
            if (objeto.oX > (cX-5) && objeto.oX < (cX + 5) && objeto.oY > (cY-4) && objeto.oY < (cY + 4)) {

                if (objeto.detectarColision(cX,cY)) {
                    // Fred ha cogido el objeto
                    eliminarObjeto = listaObjetos.indexOf(objeto)

                    if (objeto is Mapa) {
                        mapaEncontrado = true
                        mapa = objeto.dibujarMapa(cX,cY,miLaberinto)
                    }

                    if (objeto is Pocima) {
                        fred.vida += 2
                        if (fred.vida > 15) fred.vida = 15
                    }

                    if (objeto is Tesoro) {
                        fred.puntos += objeto.puntos
                    }

                    if (objeto is Balas) {
                        fred.balas = 6
                    }

                }

                // Pintar objeto ...
                val diferenciaX = objeto.oX - cX
                val diferenciaY = objeto.oY - cY
                val objetoBitmap = objeto.devolverBitmap()
                rectDestino.offsetTo((diferenciaX * 128) + pasoX + 384, (diferenciaY * 160) + pasoY + 400)
                lienzo.drawBitmap(objetoBitmap, null, rectDestino, null)

            }

        }

        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Pintar a Fred
        rectDestino.offsetTo(384, 240)
        if (fred.tocado == 0) {
            when (fred.animacionFred()) {
                0 -> lienzo.drawBitmap(fredd, null, rectDestino, null)              // Quieto a la derecha
                1 -> lienzo.drawBitmap(fredd1, null, rectDestino, null)             // Caminando 1 derecha
                2 -> lienzo.drawBitmap(fredd2, null, rectDestino, null)             // Caminando 2 derecha
                3 -> lienzo.drawBitmap(fredi, null, rectDestino, null)              // Quieto izquierda
                4 -> lienzo.drawBitmap(fredi1, null, rectDestino, null)             // Caminando 1 izquierda
                5 -> lienzo.drawBitmap(fredi2, null, rectDestino, null)             // Caminando 2 izquierda
                6 -> lienzo.drawBitmap(fredSaltandoI, null, rectDestino, null)      // Saltando derecha
                7 -> lienzo.drawBitmap(fredSaltandoD, null, rectDestino, null)      // Saltando izquierda
                8 -> lienzo.drawBitmap(fredCuerda1D, null, rectDestino, null)       // En cuerda 1 izquierda
                9 -> lienzo.drawBitmap(fredCuerda2D, null, rectDestino, null)       // En cuerda 2 izquierda
                10 -> lienzo.drawBitmap(fredCuerda1I, null, rectDestino, null)      // En cuerda 1 derecha
                11 -> lienzo.drawBitmap(fredCuerda2I, null, rectDestino, null)      // En cuerda 2 derecha
                12 -> lienzo.drawBitmap(fredDisparoPieI, null, rectDestino, null)      // Disparo pie izquierda
                13 -> lienzo.drawBitmap(fredDisparoPieD, null, rectDestino, null)      // Disparo pie derecha
                14 -> lienzo.drawBitmap(fredDisparoSaltoI, null, rectDestino, null)      // Disparo salto izquierda
                15 -> lienzo.drawBitmap(fredDisparoSaltoD, null, rectDestino, null)      // Disparo salto derecha
            }
        } else {
            when (fred.animacionFred()) {
                0 -> lienzo.drawBitmap(freddrojo, null, rectDestino, null)              // Quieto a la derecha
                1 -> lienzo.drawBitmap(fredd1rojo, null, rectDestino, null)             // Caminando 1 derecha
                2 -> lienzo.drawBitmap(fredd2rojo, null, rectDestino, null)             // Caminando 2 derecha
                3 -> lienzo.drawBitmap(fredirojo, null, rectDestino, null)              // Quieto izquierda
                4 -> lienzo.drawBitmap(fredi1rojo, null, rectDestino, null)             // Caminando 1 izquierda
                5 -> lienzo.drawBitmap(fredi2rojo, null, rectDestino, null)             // Caminando 2 izquierda
                6 -> lienzo.drawBitmap(fredSaltandoIrojo, null, rectDestino, null)      // Saltando derecha
                7 -> lienzo.drawBitmap(fredSaltandoDrojo, null, rectDestino, null)      // Saltando izquierda
                8 -> lienzo.drawBitmap(fredCuerda1Drojo, null, rectDestino, null)       // En cuerda 1 izquierda
                9 -> lienzo.drawBitmap(fredCuerda2Drojo, null, rectDestino, null)       // En cuerda 2 izquierda
                10 -> lienzo.drawBitmap(fredCuerda1Irojo, null, rectDestino, null)      // En cuerda 1 derecha
                11 -> lienzo.drawBitmap(fredCuerda2Irojo, null, rectDestino, null)      // En cuerda 2 derecha
                12 -> lienzo.drawBitmap(fredDisparoPieIrojo, null, rectDestino, null)      // Disparo pie izquierda
                13 -> lienzo.drawBitmap(fredDisparoPieDrojo, null, rectDestino, null)      // Disparo pie derecha
                14 -> lienzo.drawBitmap(fredDisparoSaltoIrojo, null, rectDestino, null)      // Disparo salto izquierda
                15 -> lienzo.drawBitmap(fredDisparoSaltoDrojo, null, rectDestino, null)      // Disparo salto derecha
            }
        }



/*
        // pintar la caja de colision
        var pintura2 = Paint()
        pintura2.style = Paint.Style.STROKE
        pintura2.strokeWidth = 4F
        pintura2.setARGB(128,255,255,255)
        val coordenadas = fred.cajaColisionFred()
        lienzo.drawRect(coordenadas.x1 , coordenadas.y1 , coordenadas.x2  , coordenadas.y2, pintura2)
*/
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Pintar la bala si existe
        if (bala.disparo) {
            val diferenciaXbala = bala.bX - cX
            val diferenciaYbala = bala.bY - cY
            rectDestino.offsetTo((diferenciaXbala * 128) + pasoX + bala.balaOffsetX, (diferenciaYbala * 160) + pasoY + bala.balaOffsetY)
            lienzo.drawBitmap(bala.devolverBitmap(),null, rectDestino,null)
        }


        //pintamos finalmente el fondo compuesto con todos los bitmaps
        fondo.setImageBitmap(bitmapFondo)
    }

    private fun actualizarBarraVida() {
        var bitmapVida = Bitmap.createBitmap(150, 20, Bitmap.Config.ARGB_8888)
        var lienzovida = Canvas(bitmapVida)
        var pinturavida = Paint()
        pinturavida.style = Paint.Style.FILL
        pinturavida.strokeWidth = 4F
        pinturavida.setARGB(255,50 + (fred.vida * 10),150 - (fred.vida * -10),25 )
        lienzovida.drawRect(0F,0F, (fred.vida  * 10).toFloat(),20F, pinturavida)
        barraVida.setImageBitmap(bitmapVida)
    }

// **********************************************************************************************************************

    private fun dibujarLaberintoTexto(miLaberinto: Laberinto) {
        var fila = ""
        for (y in 0..35) {
            for (x in 0..36) {
                when (miLaberinto.map[x][y]) {
                    0 -> {
                        fila += " "
                    }
                    2 -> fila += "X"
                }
            }
            fila += "\n"
            Log.d("Miapp", fila)
        }
    }


    private fun esPosibleMoverArriba(): Boolean {
        if (((miLaberinto.map[cX][cY - 1] == 0 || miLaberinto.map[cX][cY - 1] == 3) && pasoX == 0) || pasoY != -160) {
            if (cY > 2) {
                return true
            }
        }
        return false
    }

    private fun moverArriba () {
        pasoY += 32
        if (pasoY == 0) {
            cY--
            pasoY = -160
        }
    }

    private fun esPosibleMoverAbajo(): Boolean {
        if ((miLaberinto.map[cX][cY + 1] == 0 && pasoX == 0) || pasoY != -160) {
            if (cY < 35) {
                return true
            }
        }
        return false
    }

    private fun moverAbajo () {
        pasoY -= 32
        if (pasoY == -320) {
            cY++
            pasoY = -160
        }
    }

    private fun esPosibleMoverIzquierda(): Boolean {
        // Izquierda
        if ((miLaberinto.map[cX - 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
            if (cX > 2) {
                return true
            }
        }
        return false
    }

    private fun moverIzquierda () {
        pasoX += 32
        if (pasoX == 128) {
            cX--
            pasoX = 0
        }
    }

    private fun esPosibleMoverDerecha(): Boolean {
        if ((miLaberinto.map[cX + 1][cY] == 0 && pasoY == -160) || pasoX != 0) {
            if (cX < 35) {
                return true
            }
        }
        return false
    }

    private fun moverDerecha() {
        pasoX -= 32
        if (pasoX == -128) {
            cX++
            pasoX = 0
        }
    }


    private fun cargarSprites() {
        fredd = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar)                // 0
        fredd1 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar2)              // 1
        fredd2 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar3)              // 2
        fredi = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar6)               // 3
        fredi1 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar5)              // 4
        fredi2 = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar4)              // 5
        fredSaltandoI = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_1)       // 6
        fredSaltandoD = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_2)       // 7
        fredCuerda1I = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda1)         // 8
        fredCuerda2I = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda2)         // 9
        fredCuerda1D = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda3)         // 10
        fredCuerda2D = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda4)         // 11
        fredDisparoPieI = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_pie_izquierda)                 // 12
        fredDisparoPieD = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_pie_derecha)                  // 13
        fredDisparoSaltoI = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_salto_izquierda)            // 14
        fredDisparoSaltoD = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_salto_derecha)              // 15

        freddrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar_rojo)                // 0
        fredd1rojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar2_rojo)              // 1
        fredd2rojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar3_rojo)              // 2
        fredirojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar6_rojo)               // 3
        fredi1rojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar5_rojo)              // 4
        fredi2rojo = BitmapFactory.decodeResource(resources, R.drawable.fred_caminar4_rojo)              // 5
        fredSaltandoIrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_1_rojo)       // 6
        fredSaltandoDrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_saltar_2_rojo)       // 7
        fredCuerda1Irojo = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda1_rojo)         // 8
        fredCuerda2Irojo = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda2_rojo)         // 9
        fredCuerda1Drojo = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda3_rojo)         // 10
        fredCuerda2Drojo = BitmapFactory.decodeResource(resources, R.drawable.fred_cuerda4_rojo)         // 11
        fredDisparoPieIrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_pie_izquierda_rojo)                 // 12
        fredDisparoPieDrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_pie_derecha_rojo)                  // 13
        fredDisparoSaltoIrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_salto_izquierda_rojo)            // 14
        fredDisparoSaltoDrojo = BitmapFactory.decodeResource(resources, R.drawable.fred_disparo_salto_derecha_rojo)              // 15

        fondo = findViewById(R.id.image_view_juego)
        roca1 = BitmapFactory.decodeResource(resources, R.drawable.roca1)
        roca2 = BitmapFactory.decodeResource(resources, R.drawable.roca2)
        roca3 = BitmapFactory.decodeResource(resources, R.drawable.roca3)
        fin_cuerda = BitmapFactory.decodeResource(resources, R.drawable.finalcuerda)
        cuerda = BitmapFactory.decodeResource(resources, R.drawable.mediocuerda)
        inicio_cuerda = BitmapFactory.decodeResource(resources, R.drawable.arribacuerda)
        tierra = BitmapFactory.decodeResource(resources, R.drawable.tierra)
        trampilla = BitmapFactory.decodeResource(resources, R.drawable.trampilla)
        cielo = BitmapFactory.decodeResource(resources, R.drawable.cielo)
        pasillo = BitmapFactory.decodeResource(resources, R.drawable.pasillo_negro)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        timer.cancel()
        outState.putInt("POSICIONX", cX)
        outState.putInt("POSICIONY", cY)
        outState.putInt("OFFSETX", pasoX)
        outState.putInt("OFFSETY", pasoY)
        outState.putParcelableArrayList("LISTAENEMIGOS", listaEnemigos)
        outState.putParcelableArrayList("LISTAOBJETOS", listaObjetos)
        outState.putParcelable("LABERINTO", miLaberinto)
        outState.putParcelable("FRED", fred)

        Log.d("Miapp" , "Salvar estado")

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("Miapp" , "Recuperar estado")
        cX = savedInstanceState.getInt("POSICIONX")
        cY = savedInstanceState.getInt("POSICIONY")
        pasoX = savedInstanceState.getInt("OFFSETX")
        pasoY = savedInstanceState.getInt("OFFSETY")
        listaEnemigos.addAll(savedInstanceState.getParcelableArrayList("LISTAENEMIGOS")!!)
        listaObjetos.addAll(savedInstanceState.getParcelableArrayList("LISTAOBJETOS")!!)
        miLaberinto = savedInstanceState.getParcelable("LABERINTO")!!
        fred = savedInstanceState.getParcelable("FRED")!!
    }

    private fun dialogoFin(texto : String) {
        timer.cancel()

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogofin)
        val textofin = dialog.findViewById(R.id.textofinTV) as TextView
        textofin.text = texto
        val yesBtn = dialog.findViewById(R.id.botonfinjugar) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()

    }

}

enum class Direcciones  {
    ARRIBA, DERECHA, IZQUIERDA, ABAJO, PARADO
}

enum class Lado  {
    DERECHA, IZQUIERDA
}

enum class EstadosFred  {
    QUIETO, CAMINANDO, MOVIENDOCUERDA, SALTANDO, SALTANDOCUERDA, DECISIONSALTO
}
