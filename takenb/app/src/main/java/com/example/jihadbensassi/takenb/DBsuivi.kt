package com.example.jihadbensassi.takenb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by jihadbensassi on 05/07/2018.
 */

val Context.myDataBase_suivi: MyDataBase_suivi
    get() = MyDataBase_suivi.getInstance(applicationContext)

class MyDataBase_suivi(ctx: Context) : ManagedSQLiteOpenHelper(ctx,
        DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "MyDataBase3.db" // fichier réel
        const val DATABASE_VERSION = 2
        const val TABLE_INFO_SUIVI = "Suivi"
        const val COLUMN_INFO_ID = "id"
        const val COLUMN_INFO_NOM = "nom"
        const val COLUMN_INFO_PRIX = "prix"
        const val COLUMN_INFO_ETAT = "etat"



        private var instance: MyDataBase_suivi? = null
        @Synchronized
        fun getInstance(ctx: Context): MyDataBase_suivi {
            if (instance == null) {
                instance = MyDataBase_suivi(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_INFO_SUIVI, true,
                COLUMN_INFO_ID to INTEGER + PRIMARY_KEY,
                COLUMN_INFO_NOM to TEXT,
                COLUMN_INFO_PRIX to TEXT,
                COLUMN_INFO_ETAT to TEXT

        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("TABLE_INFO_SUIVI", true)
        onCreate(db)
    }
}
