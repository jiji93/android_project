package com.example.jihadbensassi.takenb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_valider_info.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.startActivityForResult

/**
 * Created by jihadbensassi on 04/07/2018.
 */
class valider_infoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valider_info)
        myDataBase.use {
            select(MyDataBase.TABLE_INFO_COMPTE,
                    MyDataBase.COLUMN_INFO_NOM,
                    MyDataBase.COLUMN_INFO_PRENOM,
                    MyDataBase.COLUMN_INFO_ADRESSE,
                    MyDataBase.COLUMN_INFO_VILLE,
                    MyDataBase.COLUMN_INFO_NUMERO,
                    MyDataBase.COLUMN_INFO_POSTAL).whereArgs("(${MyDataBase.COLUMN_INFO_MAIL} = {mail})",
                    "mail" to mail)
                    .exec {
                        for (row in asMapSequence()) {
                            textView_info_nom.text = (row[MyDataBase.COLUMN_INFO_NOM] as String)
                            textView_info_prenom.text = (row[MyDataBase.COLUMN_INFO_PRENOM] as String)
                            textView_info_adresse.text = (row[MyDataBase.COLUMN_INFO_ADRESSE] as String)
                            textView_info_ville.text = (row[MyDataBase.COLUMN_INFO_VILLE] as String)
                            textView_info_numero.text = (row[MyDataBase.COLUMN_INFO_NUMERO] as String)
                            textView_info_postale.text = (row[MyDataBase.COLUMN_INFO_POSTAL] as String)

                        }
                    }
        }
        Edittext_info_nom.visibility = View.INVISIBLE
        Edittext_info_prenom.visibility = View.INVISIBLE
        Edittext_info_adresse.visibility = View.INVISIBLE
        Edittext_info_ville.visibility = View.INVISIBLE
        Edittext_info_numero.visibility = View.INVISIBLE
        Edittext_info_postale.visibility = View.INVISIBLE
        button_modif_confirm.visibility = View.INVISIBLE
        button_modif.setOnClickListener {
            textView_info_nom.visibility = View.INVISIBLE
            textView_info_prenom.visibility = View.INVISIBLE
            textView_info_adresse.visibility = View.INVISIBLE
            textView_info_ville.visibility = View.INVISIBLE
            textView_info_numero.visibility = View.INVISIBLE
            textView_info_postale.visibility = View.INVISIBLE
            button_modif.visibility = View.INVISIBLE
            Edittext_info_nom.visibility = View.VISIBLE
            Edittext_info_prenom.visibility = View.VISIBLE
            Edittext_info_adresse.visibility = View.VISIBLE
            Edittext_info_ville.visibility = View.VISIBLE
            Edittext_info_numero.visibility = View.VISIBLE
            Edittext_info_postale.visibility = View.VISIBLE
            button_modif_confirm.visibility = View.VISIBLE
            Edittext_info_nom.setText( textView_info_nom.text.toString())
            Edittext_info_prenom.setText ( textView_info_prenom.text.toString())
            Edittext_info_adresse.setText  (textView_info_adresse.text.toString())
            Edittext_info_ville.setText  (textView_info_ville.text.toString())
            Edittext_info_numero.setText  (textView_info_numero.text.toString())
            Edittext_info_postale.setText ( textView_info_postale.text.toString())
        }

    button_modif_confirm.setOnClickListener {

        myDataBase_livraison.use {
            insert(MyDataBase_livraison.TABLE_INFO_LIVRAISON,
                    MyDataBase_livraison.COLUMN_INFO_NOM to Edittext_info_nom.text.toString(),
                    MyDataBase_livraison.COLUMN_INFO_PRENOM to Edittext_info_prenom.text.toString(),
                    MyDataBase_livraison.COLUMN_INFO_ADRESSE to Edittext_info_adresse.text.toString(),
                    MyDataBase_livraison.COLUMN_INFO_VILLE to Edittext_info_ville.text.toString(),
                    MyDataBase_livraison.COLUMN_INFO_NUMERO to Edittext_info_numero.text.toString())
                    MyDataBase_livraison.COLUMN_INFO_POSTAL to Edittext_info_postale.text.toString()
        }
        startActivityForResult<paiementActivity>(4)

    }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            4 -> {
                finish()
            }

        }
    }
}