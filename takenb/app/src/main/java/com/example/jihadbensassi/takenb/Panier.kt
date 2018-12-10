package com.example.jihadbensassi.takenb

import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_panier.*
import org.jetbrains.anko.imageBitmap
import java.util.*
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_envie.*
import kotlinx.android.synthetic.main.activity_paniers.*
import kotlinx.android.synthetic.main.list_panier.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus


/**
 * Created by jihadbensassi on 13/06/2018.
 */
class Panier : AppCompatActivity() {
    val monPanier = ArrayList<Panier.Produit>()
    var itemliste = ArrayList<String>()
    var id : Long = 0
    var convCal = 0.0 // pour convertir string en Long
    var convStr : String = "" // variable qui contiendra un chiffre en string
    var total_somme = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paniers)
        val intent = intent
        val barcode = intent.getStringExtra("barcode")
        textView_barcode.text = barcode
        maj()
        //calcul_Cal()



       /* val mSharedPreference2 = getSharedPreferences("prix", 0)
        val total = mSharedPreference2.getFloat("Somme", 0f).toDouble()
        total_somme = total
        total_prix.text = total_somme.toString()*/
        //recupére val panier
        //val mSharedPreference1 = getSharedPreferences("panier", 0)
        //val size = mSharedPreference1.getInt("Status_size", 0)
        /*for (i in 0..size - 1) {
            itemliste.add(mSharedPreference1.getString("Status_" + i, null))
            val value = itemliste[i].toString().split("--".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val test = Panier.Produit(value[1], value[2] + value[3],i)
            monPanier.add(test)
            rv_panier.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false)
        }*/


        btn_ajouter.setOnClickListener {
            val value = textView_barcode.getText().toString().split("--".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if(value.lastIndex == 2) {
                itemliste.add(textView_barcode.text.toString())
                ajoute(value)
                total_somme = total_somme + value[2].toFloat()
                total_prix.text = total_somme.toString()
                rv_panier.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false)
            }
            else
                toast("Impossible de récupérer le produit : notation du QR non reconnue ")
        }
        btn_scan.setOnClickListener {
            val intent = Intent(this, ScannerViewActivity::class.java)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 2)
            startActivityForResult(intent, 2)
        }
        btn_valider.setOnClickListener{
            myDataBase_suivi.use {
                insert(MyDataBase_suivi.TABLE_INFO_SUIVI,
                        MyDataBase_suivi.COLUMN_INFO_NOM to tv_name_produit.text.toString(),
                        MyDataBase_suivi.COLUMN_INFO_PRIX to tv_name_prix.text.toString(),
                        MyDataBase_suivi.COLUMN_INFO_ETAT to "En cours"
                )
            }
            startActivityForResult<valider_infoActivity>(3)
            deleteDB()
            monPanier.clear()
        }
        rv_panier.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        rv_panier.adapter = Panier.ProduitRecyclerAdapter(monPanier) {
            // Code qui s’exécute quand on touche un élément // it = le Message de la ligne touchée
            startActivityForResult<deleteActivity>(1,deleteActivity.ID to it.id)


        }



    }


    data class Produit( val id : Long, val produit: String, val prix: String /*val date : Date*/)
    class ProduitRecyclerAdapter(val list: List<Produit>, val listener: (Produit) -> Unit) : RecyclerView.Adapter<ProduitRecyclerAdapter.ProduitViewHolder>() {

        inner class ProduitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var produit: TextView = view.findViewById(R.id.tv_name_produit)
            var prix: TextView = view.findViewById(R.id.tv_name_prix)
        }


        override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
            holder.produit.text = list[position].produit
            holder.prix.text = list[position].prix
            holder.itemView.setOnClickListener { listener(list[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProduitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_panier, parent, false))
        override fun getItemCount() = list.size
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2 -> {
                var code = ""
                code = data!!.extras.getString("BarCode")

                if (code.equals("")) {
                    Toast.makeText(this, "QrCode non trouver", Toast.LENGTH_LONG).show()
                } else {
                    textView_barcode?.text = code
                }

            }
            1 -> {
                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {

                    monPanier.clear()
                    maj()
                    calcul_Cal()


                }

            }
            3 -> {
               finish()

            }

        }
    }
   override fun onPause() {
        super.onPause()
     deleteDB()

       /*val sp2 = getSharedPreferences("prix", 0)
       val mEdit2 = sp2.edit()
       mEdit2.putFloat("Somme", total_somme.toFloat())
       mEdit2.commit()*/

    }
///////////////////:MAJ//////////////////////////////////////////

    private fun maj() {
        myDataBase_historique.use {
            select(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_ID,
                    MyDataBase_historique.COLUMN_INFO_PRODUIT,
                    MyDataBase_historique.COLUMN_INFO_PRIX)
                    .exec {
                        for (row in asMapSequence()) {
                            val test = Panier.Produit(row[MyDataBase_historique.COLUMN_INFO_ID] as Long,
                                    row[MyDataBase_historique.COLUMN_INFO_PRODUIT] as String,
                                    row[MyDataBase_historique.COLUMN_INFO_PRIX] as String
                            )
                            monPanier.add(test)

                        }
                    }
        }
        rv_panier.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
    }
//////////////////////////////////////////////////////////////////////////

    ////////////ajouter/////////////
    private fun ajoute(itemliste : Array<String>)
    {
        myDataBase_historique.use {
            insert(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_PRODUIT to itemliste[1] ,
                    MyDataBase_historique.COLUMN_INFO_PRIX to itemliste[2])
                    //MyDataBase_historique.COLUMN_INFO_DATE to System.currentTimeMillis())
        }
        monPanier.clear()
        maj()
        }
    /////////////////////////////////////////////////////////////////////////
    fun calcul_Cal() {
        total_somme = 0.0
        myDataBase_historique.use {
            select(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_PRIX)
                    .exec {
                        for (row in asMapSequence()) {
                            convStr = row[MyDataBase_historique.COLUMN_INFO_PRIX] as String // mettre le chiffre string dans la variable
                            convCal = convStr.toDouble() // le convertir en long
                            total_somme = total_somme +  convCal.toFloat() // l'additionner


                        }
                    }
        }
        total_prix.text = total_somme.toString()
    }
    fun deleteDB()
    {
        myDataBase_historique.use {
            delete(MyDataBase_historique.TABLE_INFO_HISTORIQUE)
            for (i in monPanier.indices)
                insert(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                        MyDataBase_historique.COLUMN_INFO_PRODUIT to monPanier[i].produit,
                        MyDataBase_historique.COLUMN_INFO_PRIX to monPanier[i].prix)

        }
    }


}


