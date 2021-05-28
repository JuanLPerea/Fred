package com.fred

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {

        val PREFS_NAME = "fred.sharedpreferences"
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

        var record1 : String
        get () = prefs.getString("RECORD1", "")!!
        set (value) = prefs.edit().putString("RECORD1", value).apply()

        var record1Name : String
                get () = prefs.getString("RECORD1NAME", "")!!
                set (value) = prefs.edit().putString("RECORD1NAME", value).apply()

        var record2 : String
                get () = prefs.getString("RECORD1", "")!!
                set (value) = prefs.edit().putString("RECORD1", value).apply()

        var record2Name : String
                get () = prefs.getString("RECORD1NAME", "")!!
                set (value) = prefs.edit().putString("RECORD1NAME", value).apply()

        var record3 : String
                get () = prefs.getString("RECORD1", "")!!
                set (value) = prefs.edit().putString("RECORD1", value).apply()

        var record3Name : String
                get () = prefs.getString("RECORD1NAME", "")!!
                set (value) = prefs.edit().putString("RECORD1NAME", value).apply()

        var record4 : String
                get () = prefs.getString("RECORD1", "")!!
                set (value) = prefs.edit().putString("RECORD1", value).apply()

        var record4Name : String
                get () = prefs.getString("RECORD1NAME", "")!!
                set (value) = prefs.edit().putString("RECORD1NAME", value).apply()

}