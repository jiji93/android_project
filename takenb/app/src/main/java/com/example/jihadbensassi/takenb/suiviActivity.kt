package com.example.jihadbensassi.takenb

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_suivi.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by jihadbensassi on 04/07/2018.
 */
class suiviActivity : AppCompatActivity() {
    val maListe_suivi = ArrayList<Suivi>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suivi)
        maj_suivi()
        rv_suivi.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        rv_suivi.adapter = SuiviRecyclerAdapter(maListe_suivi) {
            // Code qui s’exécute quand on touche un élément // it = le Message de la ligne touchée
            toast("${it.id}")
            //envoi de l'id à detailActivity
        }
    }










    data class Suivi(val id: Long, val nom: String, val prix : String, val etat : String)
    class SuiviRecyclerAdapter(val list: List<Suivi>, val listener: (Suivi) -> Unit) : RecyclerView.Adapter<SuiviRecyclerAdapter.SuiviViewHolder>() {

        inner class SuiviViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var id: TextView = view.findViewById(R.id.textView_db_id)
            var nom: TextView = view.findViewById(R.id.textView_db_nom)
            var prix: TextView = view.findViewById(R.id.textView_db_prix)
                    var etat: TextView = view.findViewById(R.id.textView_db_etat)
        }


        override fun onBindViewHolder(holder: SuiviViewHolder, position: Int) {
            holder.id.text = list[position].id.toString()
            holder.nom.text = list[position].nom
            holder.prix.text = list[position].prix + "€"
            holder.etat.text = list[position].etat





            holder.itemView.setOnClickListener { listener(list[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SuiviViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_suivi, parent, false))
        override fun getItemCount() = list.size
    }




    override fun onPause() {
        super.onPause()
        myDataBase_suivi.use {
            delete(MyDataBase_suivi.TABLE_INFO_SUIVI)
            for (i in maListe_suivi.indices)
                insert(MyDataBase_suivi.TABLE_INFO_SUIVI,

                        MyDataBase_suivi.COLUMN_INFO_NOM to maListe_suivi[i].nom,
                        MyDataBase_suivi.COLUMN_INFO_PRIX to maListe_suivi[i].prix,
                        MyDataBase_suivi.COLUMN_INFO_ETAT to maListe_suivi[i].etat)


        }
    }




















    private fun maj_suivi() {
        myDataBase_suivi.use {
            select(MyDataBase_suivi.TABLE_INFO_SUIVI,
                    MyDataBase_suivi.COLUMN_INFO_ID,
                    MyDataBase_suivi.COLUMN_INFO_NOM,
                    MyDataBase_suivi.COLUMN_INFO_PRIX,
                    MyDataBase_suivi.COLUMN_INFO_ETAT)
                    .exec {
                        for (row in asMapSequence()) {
                            val test = Suivi(
                                    row[MyDataBase_suivi.COLUMN_INFO_ID] as Long,
                                    row[MyDataBase_suivi.COLUMN_INFO_NOM] as String ,
                                            row[MyDataBase_suivi.COLUMN_INFO_PRIX] as String,
                                    row[MyDataBase_suivi.COLUMN_INFO_ETAT] as String


                            )
                            maListe_suivi.add(test)

                        }
                    }
        }
        rv_suivi.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
    }
}