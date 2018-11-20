package com.example.jihadbensassi.takenb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by jihadbensassi on 07/06/2018.
 */

val Context.myDataBase_historique: MyDataBase_historique
    get() = MyDataBase_historique.getInstance(applicationContext)

class MyDataBase_historique(ctx: Context) : ManagedSQLiteOpenHelper(ctx,
        DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "MyDataBase11.db" // fichier réel
        const val DATABASE_VERSION = 2
        const val TABLE_INFO_HISTORIQUE = "Historique"
        const val COLUMN_INFO_ID = "id"
        const val COLUMN_INFO_PRODUIT = "produit"
        const val COLUMN_INFO_PRIX = "prix"
        const val COLUMN_INFO_DATE = "date"



        private var instance: MyDataBase_historique? = null
        @Synchronized
        fun getInstance(ctx: Context): MyDataBase_historique {
            if (instance == null) {
                instance = MyDataBase_historique(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_INFO_HISTORIQUE, true,
                COLUMN_INFO_ID to INTEGER + PRIMARY_KEY,
                COLUMN_INFO_PRODUIT to TEXT,
                COLUMN_INFO_PRIX to TEXT,
                COLUMN_INFO_DATE to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("TABLE_INFO_HISTORIQUE", true)
        onCreate(db)
    }
}
