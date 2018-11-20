package com.example.jihadbensassi.takenb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by jihadbensassi on 04/07/2018.
 */
val Context.myDataBase_livraison: MyDataBase_livraison
    get() = MyDataBase_livraison.getInstance(applicationContext)

class MyDataBase_livraison(ctx: Context) : ManagedSQLiteOpenHelper(ctx,
        DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "MyDataBase300.db" // fichier réel
        const val DATABASE_VERSION = 2
        const val TABLE_INFO_LIVRAISON = "Livraison"
        const val COLUMN_INFO_ID = "id"
        const val COLUMN_INFO_NOM = "nom"
        const val COLUMN_INFO_PRENOM = "prenom"
        const val COLUMN_INFO_MDP = "mot_de_passe"
        const val COLUMN_INFO_ADRESSE = "adresse"
        const val COLUMN_INFO_VILLE = "ville"
        const val COLUMN_INFO_NUMERO = "numero"
        const val COLUMN_INFO_POSTAL = "postal"



        private var instance: MyDataBase_livraison? = null
        @Synchronized
        fun getInstance(ctx: Context): MyDataBase_livraison {
            if (instance == null) {
                instance = MyDataBase_livraison(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_INFO_LIVRAISON, true,
                COLUMN_INFO_ID to INTEGER + PRIMARY_KEY,
              COLUMN_INFO_NOM to TEXT,
              COLUMN_INFO_PRENOM to TEXT,
              COLUMN_INFO_MDP to TEXT,
              COLUMN_INFO_ADRESSE to TEXT,
              COLUMN_INFO_VILLE to TEXT,
              COLUMN_INFO_NUMERO to TEXT,
              COLUMN_INFO_POSTAL to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("TABLE_INFO_LIVRAISON", true)
        onCreate(db)
    }
}
