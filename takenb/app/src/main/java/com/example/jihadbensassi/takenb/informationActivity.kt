package com.example.jihadbensassi.takenb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_compte.*
import kotlinx.android.synthetic.main.activity_information.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.toast

/**
 * Created by jihadbensassi on 26/06/2018.
 */


class informationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        Edittext_info_nom.visibility = View.INVISIBLE
        Edittext_info_prenom.visibility = View.INVISIBLE
        Edittext_info_mail.visibility = View.INVISIBLE
        Edittext_info_adresse.visibility = View.INVISIBLE
        Edittext_info_ville.visibility = View.INVISIBLE
        Edittext_info_numero.visibility = View.INVISIBLE
        Edittext_info_postale.visibility = View.INVISIBLE
        button_modif_confirm.visibility = View.INVISIBLE
        button_modif.setOnClickListener {
            textView_info_nom.visibility = View.INVISIBLE
            textView_info_prenom.visibility = View.INVISIBLE
            textView_info_mail.visibility = View.INVISIBLE
            textView_info_adresse.visibility = View.INVISIBLE
            textView_info_ville.visibility = View.INVISIBLE
            textView_info_numero.visibility = View.INVISIBLE
            textView_info_postale.visibility = View.INVISIBLE
            button_modif.visibility = View.INVISIBLE
            Edittext_info_nom.visibility = View.VISIBLE
            Edittext_info_prenom.visibility = View.VISIBLE
            Edittext_info_mail.visibility = View.VISIBLE
            Edittext_info_adresse.visibility = View.VISIBLE
            Edittext_info_ville.visibility = View.VISIBLE
            Edittext_info_numero.visibility = View.VISIBLE
            Edittext_info_postale.visibility = View.VISIBLE
            button_modif_confirm.visibility = View.VISIBLE
            Edittext_info_nom.setText( textView_info_nom.text.toString())
            Edittext_info_prenom.setText ( textView_info_prenom.text.toString())
            Edittext_info_mail.setText ( textView_info_mail.text.toString())
            Edittext_info_adresse.setText  (textView_info_adresse.text.toString())
            Edittext_info_ville.setText  (textView_info_ville.text.toString())
            Edittext_info_numero.setText  (textView_info_numero.text.toString())
            Edittext_info_postale.setText ( textView_info_postale.text.toString())
        }
        button_modif_confirm.setOnClickListener {
            user = Edittext_info_prenom.text.toString()
            myDataBase.use {
                update(MyDataBase.TABLE_INFO_COMPTE,
                        MyDataBase.COLUMN_INFO_NOM to Edittext_info_nom.text.toString(),
                        MyDataBase.COLUMN_INFO_PRENOM to Edittext_info_prenom.text.toString(),
                        MyDataBase.COLUMN_INFO_MAIL to Edittext_info_mail.text.toString(),
                        MyDataBase.COLUMN_INFO_ADRESSE to Edittext_info_adresse.text.toString(),
                        MyDataBase.COLUMN_INFO_VILLE to Edittext_info_ville.text.toString(),
                        MyDataBase.COLUMN_INFO_NUMERO to Edittext_info_numero.text.toString(),
                        MyDataBase.COLUMN_INFO_POSTAL to Edittext_info_postale.text.toString())
                        .whereArgs("${MyDataBase.COLUMN_INFO_MAIL} = {mail}", "mail" to mail).exec()
            }
                toast("Modifer")
                mail = Edittext_info_mail.text.toString()
                finish()
        }
        myDataBase.use {
            select(MyDataBase.TABLE_INFO_COMPTE,
                    MyDataBase.COLUMN_INFO_NOM,
                    MyDataBase.COLUMN_INFO_PRENOM,
                    MyDataBase.COLUMN_INFO_MAIL,
                    MyDataBase.COLUMN_INFO_ADRESSE,
                    MyDataBase.COLUMN_INFO_VILLE,
                    MyDataBase.COLUMN_INFO_NUMERO,
                    MyDataBase.COLUMN_INFO_POSTAL).whereArgs("(${MyDataBase.COLUMN_INFO_MAIL} = {mail})",
                            "mail" to mail)
                            .exec {
                                for (row in asMapSequence()) {
                                  textView_info_nom.text   = (row[MyDataBase.COLUMN_INFO_NOM] as String)
                                    textView_info_prenom.text   = (row[MyDataBase.COLUMN_INFO_PRENOM] as String)
                                    textView_info_mail.text   = (row[MyDataBase.COLUMN_INFO_MAIL] as String)
                                    textView_info_adresse.text   = (row[MyDataBase.COLUMN_INFO_ADRESSE] as String)
                                    textView_info_ville.text   = (row[MyDataBase.COLUMN_INFO_VILLE] as String)
                                    textView_info_numero.text   = (row[MyDataBase.COLUMN_INFO_NUMERO] as String)
                                    textView_info_postale.text   = (row[MyDataBase.COLUMN_INFO_POSTAL] as String)

                                }
                            }


        }
    }
}
