package com.example.jihadbensassi.takenb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_compte.*
import kotlinx.android.synthetic.main.activity_connexion.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

/**
 * Created by jihadbensassi on 22/04/2018.
 */
class Connexion : AppCompatActivity() {
    companion object {
        const val VERIF_MAIL = "mail"
        const val VERIF_MDP = "mdp"
        const val VERIF_PRENOM = "prenom"
        const val ERR = "err"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        val intent = Intent()
        intent.putExtra(Connexion.VERIF_PRENOM,"")
        setResult(RESULT_CANCELED)
        button_co.setOnClickListener{
            var mdp = ""
            var prenom = ""
            myDataBase.use {
                select(MyDataBase.TABLE_INFO_COMPTE,
                        MyDataBase.COLUMN_INFO_MDP,
                                MyDataBase.COLUMN_INFO_PRENOM)
                        .whereArgs("(${MyDataBase.COLUMN_INFO_MAIL} = {mail})", "mail" to editText4.text.toString() )
                        .exec {
                            for (row in asMapSequence()) {
                                mdp = row[MyDataBase.COLUMN_INFO_MDP] as String
                                prenom = row[MyDataBase.COLUMN_INFO_PRENOM] as String
                            }
                        }
            }




            if(editText3.text.toString()=="" || editText4.text.toString()=="")
            {
                toast("veuillez entrer les info")
            }

                else if(mdp==editText3.text.toString()) {
                    toast("Vous êtes maintenant connecter" + " " + prenom)
                intent.putExtra(Connexion.VERIF_PRENOM,prenom)
                intent.putExtra(Connexion.VERIF_MAIL,editText4.text.toString())
                setResult(RESULT_OK, intent)
                    finish()
                }

            else if (mdp == "")
                {
                    toast("compte inexistant")
                    finish()
                }
            else
                {
                    toast("mauvais mot de passe ou mail")
                }

        }
    }
}
