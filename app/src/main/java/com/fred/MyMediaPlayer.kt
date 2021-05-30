package com.fred

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class MyMediaPlayer (context: Context) {

    val mediaPlayer = MediaPlayer.create(context, R.raw.beep)
    var context = context

    fun playMedia (sonido : Int) {
        val assetMediaDescriptor = context.resources.openRawResourceFd(sonido)

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(assetMediaDescriptor.fileDescriptor, assetMediaDescriptor.startOffset, assetMediaDescriptor.declaredLength)
            mediaPlayer.prepare()
            mediaPlayer.start()
            assetMediaDescriptor.close()
        }
        catch (exception : IllegalArgumentException) {
            Log.d("Miapp" , "Error al reproducir sonido ${exception}")
        }
        catch (exception : IllegalStateException) {
            Log.d("Miapp" , "Error al reproducir sonido ${exception}")
        }
        catch (exception : IOException) {
            Log.d("Miapp" , "Error al reproducir sonido ${exception}")
        }
    }

    fun stopPlayer () {
        mediaPlayer.reset()
        mediaPlayer.stop()
    }


}