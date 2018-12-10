package com.example.jihadbensassi.takenb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

var mail = "" // le mail de l'utilisateur connecter qu'on va recup
var user = ""
class MainActivity : AppCompatActivity() {

    var bar_code_id_txt: TextView?=null
    var barcode:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bar_code_id_txt = findViewById(R.id.bar_code_id_txt)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        user = sharedPref.getString("nom","")
        mail = sharedPref.getString("mail","")
        utilisateur.text = user

        val intent = Intent(this, ScannerViewActivity::class.java)
        button_start_shopping.setOnClickListener {
            if(mail!="") {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 2)
                startActivityForResult(intent, 2)
            }
            else
                toast("Vous devez vous connecter pour avoir accès au shopping")
        }

            Suivi.setOnClickListener {
            startActivity<suiviActivity>()

        }
        Avis.setOnClickListener {
            toast("En cours de développement")

        }
        Amis_en_ligne.setOnClickListener {
            toast("En cours de développement")

        }
        Revenus.setOnClickListener {
            toast("En cours de développement")

        }
        Mes_envie.setOnClickListener {
            startActivity<MesenvieActivity>()
        }
    }

     override fun onStart() {
        super.onStart()
         utilisateur.text = user
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean { menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.déconnexion -> {
                user = ""
                mail = ""
                utilisateur.text = user
                toast("deconnexion")
                return true
            }
            R.id.button_connexion -> {
                startActivityForResult<Connexion>(12)

                return true
            }
            R.id.button_compte -> {
                startActivityForResult<CompteActivity>(11)
                return true
            }
            R.id.information -> {
                if(mail!="")
                startActivity<informationActivity>()
                else
                    toast("Aucun utilisateur connecté")
                return true
            }
            R.id.historique -> {
               toast("En cours de développement")
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            when (requestCode) {
                11 -> {
                    if (resultCode == RESULT_OK) {
                        val val_nom = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_NOM)
                                ?: false

                        val val_prenom = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_PRENOM)
                                ?: false

                        val val_mail = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_MAIL)
                                ?: false
                        val val_mdp = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_MDP)
                                ?: false

                        val val_adresse = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_ADRESSE)
                                ?: false

                        val val_ville = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_VILLE)
                                ?: false

                        val val_numero = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_NUMERO)
                                ?: false

                        val val_postal = data?.getStringExtra(
                                CompteActivity.
                                        AJOUTER_CODEPOSTAL)
                                ?: false

                        myDataBase.use {
                            insert(MyDataBase.TABLE_INFO_COMPTE,
                                    MyDataBase.COLUMN_INFO_NOM to val_nom,
                                    MyDataBase.COLUMN_INFO_PRENOM to val_prenom,
                                    MyDataBase.COLUMN_INFO_MAIL to val_mail,
                                    MyDataBase.COLUMN_INFO_MDP to val_mdp,
                                    MyDataBase.COLUMN_INFO_ADRESSE to val_adresse,
                                    MyDataBase.COLUMN_INFO_VILLE to val_ville,
                                    MyDataBase.COLUMN_INFO_NUMERO to val_numero,
                                    MyDataBase.COLUMN_INFO_POSTAL to val_postal
                                    )
                        }
                    }
                }
                12 -> {
                    if(data==null)
                    {
                        toast("Retour")
                    }
                    if (resultCode == RESULT_OK) {
                        val val_user = data?.getStringExtra(
                                Connexion.
                                        VERIF_PRENOM)
                                ?: true
                        user = val_user.toString()

                        val val_mail = data?.getStringExtra(
                                Connexion.
                                        VERIF_MAIL)
                                ?: true
                        mail = val_mail.toString()
                    }
                }


                2 -> {
                    if(data==null)
                    {
                        toast("Retour")
                    }
                    barcode = data?.extras?.getString("BarCode")

                    if (barcode.equals("")) {
                        Toast.makeText(this, "QrCode non trouver", Toast.LENGTH_LONG).show()
                    } else {
                        bar_code_id_txt?.text = barcode
                        toast("$barcode")
                    }
                    //val intent = Intent(this, PanierActivity::class.java)
                    val intent = Intent(this, Panier::class.java)
                    intent.putExtra("barcode", barcode)
                    startActivity(intent)
                }
            }
        }catch (e:NullPointerException){
            e.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        //déclaration de la variable
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        // enrigstrement du tableau message
        with(sharedPref.edit())
        {
                putString("nom", user) // systeme clefs, valeur
                putString("mail", mail) // systeme clefs, valeur
            apply()

        }


    }




}