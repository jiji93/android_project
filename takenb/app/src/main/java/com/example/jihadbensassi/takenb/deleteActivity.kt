package com.example.jihadbensassi.takenb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_delete.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by jihadbensassi on 19/06/2018.
 */
class deleteActivity : AppCompatActivity() {
    companion object {
        const val VAL_REMOVE = 0
        var ID = "deleteActivity.ID"

    }
    var produit = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)
        val id = intent.getLongExtra(ID, 0)
        myDataBase_historique.use {
            select(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_PRODUIT,
                    MyDataBase_historique.COLUMN_INFO_PRIX).whereArgs("(${MyDataBase.COLUMN_INFO_ID} = {id})",
                    "id" to id)
                    .exec {
                        for (row in asMapSequence()) {
                            produit = (row[MyDataBase_historique.COLUMN_INFO_PRODUIT] as String)
                        }
                    }
        }

        ID = id.toString()
        textView_data.text = produit
        button_oui.setOnClickListener {
            myDataBase_historique.use {
                delete(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                        "(${MyDataBase_historique.COLUMN_INFO_ID} = {id})",
                        "id" to id)
            }
            val intent = Intent()

            setResult(RESULT_OK,intent)
            toast("Supprimé")
            finish()
        }
        button_non.setOnClickListener {
            val intent = Intent()

            setResult(RESULT_CANCELED,intent)
            finish()
        }
    }
}