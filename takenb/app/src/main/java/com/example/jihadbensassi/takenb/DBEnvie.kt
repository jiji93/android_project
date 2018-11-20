package com.example.jihadbensassi.takenb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by jihadbensassi on 25/05/2018.
 */
val Context.myDataBase_envie: MyDataBase_envie
    get() = MyDataBase_envie.getInstance(applicationContext)

class MyDataBase_envie(ctx: Context) : ManagedSQLiteOpenHelper(ctx,
        DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "MyDataBase_envie24.db" // fichier réel
        const val TABLE_INFO_ENVIE = "Compte_utilisateur"
        const val COLUMN_INFO_ID = "id"
        const val DATABASE_VERSION = 2
        const val COLUMN_INFO_IMAGE = "image"
        const val COLUMN_INFO_DATE = "date"
        const val COLUMN_INFO_PRODUIT = "produit"



        private var instance: MyDataBase_envie? = null
        @Synchronized
        fun getInstance(ctx: Context): MyDataBase_envie{
            if (instance == null) {
                instance = MyDataBase_envie(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_INFO_ENVIE, true,
                COLUMN_INFO_ID to INTEGER + PRIMARY_KEY,
                COLUMN_INFO_IMAGE to TEXT,
                COLUMN_INFO_PRODUIT to TEXT,
                COLUMN_INFO_DATE to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("TABLE_INFO_COMPTE", true)
        onCreate(db)
    }
}
