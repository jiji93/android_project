package com.example.jihadbensassi.takenb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_compte.*
import kotlinx.android.synthetic.main.activity_connexion.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.toast

/**
 * Created by jihadbensassi on 22/04/2018.
 */
class CompteActivity : AppCompatActivity() {
    companion object {
        const val AJOUTER_NOM = "nom"
        const val AJOUTER_PRENOM = "prenom"
        const val AJOUTER_MAIL = "mail"
        const val AJOUTER_MDP = "mdp"
        const val AJOUTER_ADRESSE = "adr"
        const val AJOUTER_VILLE = "ville"
        const val AJOUTER_NUMERO = "numm"
        const val AJOUTER_CODEPOSTAL = "postal"
    }

    var nom =""
    var prenom = ""
    var mail_compte = ""
    var mdp = ""
    var conf_mdp = ""
    var adresse =""
    var ville = ""
    var numero = ""
    var postale = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compte)
        textView9.visibility = View.INVISIBLE
        textView_adr.visibility = View.INVISIBLE
        textView_ville.visibility = View.INVISIBLE
        textView_numero.visibility = View.INVISIBLE
        textView_postale.visibility = View.INVISIBLE
        editText_adr.visibility = View.INVISIBLE
        editText_ville.visibility = View.INVISIBLE
        editText_numero.visibility = View.INVISIBLE
        editText_postale.visibility = View.INVISIBLE
        btn_confirmer2.visibility = View.INVISIBLE
        btn_confirmer.setOnClickListener {
             nom = editText_nom.text.toString()
             prenom = editText_prenom.text.toString()
             mail_compte = editText_mail.text.toString()
             mdp = editText_mdp.text.toString()
             conf_mdp = editText_conf_mdp.text.toString()
            if (editText_prenom.text.toString() == "") {
                toast("Veuillez entrer votre prenom !")
            } else if (editText_nom.text.toString() == "") {
                toast("Veuillez entrer vote nom !")
            } else if (editText_mail.text.toString() == "") {
                toast("Veuillez entrer une adresse mail")
            } else if (editText_mdp.text.toString() == "") {
                toast("Veuillez entrer un mot de passe !")
            } else if (editText_conf_mdp.text.toString() != editText_mdp.text.toString()) {
                toast("Les mot de passe ne son pas identiques")
            } else {
                textView_creecompte.visibility = View.INVISIBLE
                btn_confirmer.visibility = View.INVISIBLE
                textView_nom.visibility = View.INVISIBLE
                textView_prenom.visibility = View.INVISIBLE
                textView_mail.visibility = View.INVISIBLE
                textView_mdp.visibility = View.INVISIBLE
                textView_conf_mdp.visibility = View.INVISIBLE
                editText_nom.visibility = View.INVISIBLE
                editText_prenom.visibility = View.INVISIBLE
                editText_mail.visibility = View.INVISIBLE
                editText_mdp.visibility = View.INVISIBLE
                editText_conf_mdp.visibility = View.INVISIBLE
                textView9.visibility = View.VISIBLE
                textView_adr.visibility = View.VISIBLE
                textView_ville.visibility = View.VISIBLE
                textView_numero.visibility = View.VISIBLE
                textView_postale.visibility = View.VISIBLE
                editText_adr.visibility = View.VISIBLE
                editText_ville.visibility = View.VISIBLE
                editText_numero.visibility = View.VISIBLE
                editText_postale.visibility = View.VISIBLE
                btn_confirmer2.visibility = View.VISIBLE
            }
        }
                btn_confirmer2.setOnClickListener {
                    adresse = editText_adr.text.toString()
                    ville = editText_ville.text.toString()
                    numero = editText_numero.text.toString()
                    postale = editText_postale.text.toString()

                    if (editText_adr.text.toString() == "") {
                        toast("Veuillez entrer une adresse !")
                    } else if (editText_ville.text.toString() == "") {
                        toast("Veuillez entrer une ville !")
                    } else if (editText_numero.text.toString() =="") {
                        toast("Veuillez entrer un numéro")
                    } else if (editText_postale.text.toString() == "") {
                        toast("Veuillez entrer votre code postale !")
                    }
                    else
                    {
                        val intent = Intent()
                        intent.putExtra(AJOUTER_NOM,nom)
                        intent.putExtra(AJOUTER_PRENOM,prenom)
                        intent.putExtra(AJOUTER_MAIL,mail_compte)
                        intent.putExtra(AJOUTER_MDP,mdp)
                        intent.putExtra(AJOUTER_ADRESSE,adresse)
                        intent.putExtra(AJOUTER_VILLE,ville)
                        intent.putExtra(AJOUTER_NUMERO,numero)
                        intent.putExtra(AJOUTER_CODEPOSTAL,postale)
                        setResult(RESULT_OK, intent)
                        toast("Votre compte à bien été crée")
                        finish()


                    }

                }

            }

        }


