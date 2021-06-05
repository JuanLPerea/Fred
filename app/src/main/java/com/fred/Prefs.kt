package com.fred

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {


        val PREFS_NAME = "fred.sharedpreferences"
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

        var velocidadJuego : Long
        get() = prefs.getLong("VELOCIDAD" , 200L)
        set(value) = prefs.edit().putLong("VELOCIDAD", value).apply()

        var tipoFred : Boolean
        get() = prefs.getBoolean("TIPOFRED", false)
        set(value) = prefs.edit().putBoolean("TIPOFRED", value).apply()

        var nivelInicio : Int
        get() = prefs.getInt("NIVELINICIO", 1)
        set(value) = prefs.edit().putInt("NIVELINICIO", value).apply()

        var record1 : String
        get () = prefs.getString("RECORD1", "")!!
        set (value) = prefs.edit().putString("RECORD1", value).apply()

        var record1Name : String
                get () = prefs.getString("RECORD1NAME", "")!!
                set (value) = prefs.edit().putString("RECORD1NAME", value.toUpperCase()).apply()

        var record2 : String
                get () = prefs.getString("RECORD2", "")!!
                set (value) = prefs.edit().putString("RECORD2", value).apply()

        var record2Name : String
                get () = prefs.getString("RECORD2NAME", "")!!
                set (value) = prefs.edit().putString("RECORD2NAME", value.toUpperCase()).apply()

        var record3 : String
                get () = prefs.getString("RECORD3", "")!!
                set (value) = prefs.edit().putString("RECORD3", value).apply()

        var record3Name : String
                get () = prefs.getString("RECORD3NAME", "")!!
                set (value) = prefs.edit().putString("RECORD3NAME", value.toUpperCase()).apply()

        var record4 : String
                get () = prefs.getString("RECORD4", "")!!
                set (value) = prefs.edit().putString("RECORD4", value).apply()

        var record4Name : String
                get () = prefs.getString("RECORD4NAME", "")!!
                set (value) = prefs.edit().putString("RECORD4NAME", value.toUpperCase()).apply()

        fun firstrecords() {
                SharedApp.prefs.record1 = "000000"
                SharedApp.prefs.record1Name = "FRED"

                SharedApp.prefs.record2 = "000000"
                SharedApp.prefs.record2Name = "FRED"

                SharedApp.prefs.record3 = "000000"
                SharedApp.prefs.record3Name = "FRED"

                SharedApp.prefs.record4 = "000000"
                SharedApp.prefs.record4Name = "FRED"
        }

        var secreto : Boolean
                get () = prefs.getBoolean("SECRETO", false)
                set (value) = prefs.edit().putBoolean("SECRETO", value).apply()

        var reto1 : Boolean
                get () = prefs.getBoolean("RETO1", false)
                set (value) = prefs.edit().putBoolean("RETO1", value).apply()

        var reto2 : Boolean
                get () = prefs.getBoolean("RETO2", false)
                set (value) = prefs.edit().putBoolean("RETO2", value).apply()

        var reto3 : Boolean
                get () = prefs.getBoolean("RETO3", false)
                set (value) = prefs.edit().putBoolean("RETO3", value).apply()

        var reto4 : Boolean
                get () = prefs.getBoolean("RETO4", false)
                set (value) = prefs.edit().putBoolean("RETO4", value).apply()

        var reto5 : Boolean
                get () = prefs.getBoolean("RETO5", false)
                set (value) = prefs.edit().putBoolean("RETO5", value).apply()

        var reto6 : Boolean
                get () = prefs.getBoolean("RETO6", false)
                set (value) = prefs.edit().putBoolean("RETO6", value).apply()


        var numerodegotas : Int
                get () = prefs.getInt("NUMERODEGOTAS", 0)
                set (value) = prefs.edit().putInt("NUMERODEGOTAS", value).apply()

        var numerodeespinetes : Int
                get () = prefs.getInt("NUMERODEESPINETES", 0)
                set (value) = prefs.edit().putInt("NUMERODEESPINETES", value).apply()

        var numerodefantasmas : Int
                get () = prefs.getInt("NUMERODEFANTASMAS", 0)
                set (value) = prefs.edit().putInt("NUMERODEFANTASMAS", value).apply()

        var numerodelagartijas : Int
                get () = prefs.getInt("NUMERODELAGARTIJAS", 0)
                set (value) = prefs.edit().putInt("NUMERODELAGARTIJAS", value).apply()

        var numerodemomias : Int
                get () = prefs.getInt("NUMERODEMOMIAS", 0)
                set (value) = prefs.edit().putInt("NUMERODEMOMIAS", value).apply()

        var numerodevampiros : Int
                get () = prefs.getInt("NUMERODEVAMPIROS", 0)
                set (value) = prefs.edit().putInt("NUMERODEVAMPIROS", value).apply()

        var numerodeesqueletos : Int
                get () = prefs.getInt("NUMERODEESQUELETOS", 0)
                set (value) = prefs.edit().putInt("NUMERODEESQUELETOS", value).apply()

        var numerodebalas : Int
                get () = prefs.getInt("NUMERODEBALAS", 0)
                set (value) = prefs.edit().putInt("NUMERODEBALAS", value).apply()


}