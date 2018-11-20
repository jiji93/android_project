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
import kotlinx.android.synthetic.main.activity_envie.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by jihadbensassi on 05/06/2018.
 */
class HistoriqueActivity : AppCompatActivity() {

    val monHistorique = ArrayList<HistoriqueActivity.Produit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historique)
        // dans le panier
        /*myDataBase_historique.use {
            insert(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_PRODUIT to "hi",
                    MyDataBase_historique.COLUMN_INFO_DATE to System.currentTimeMillis(),
                     MyDataBase_historique.COLUMN_INFO_PRIX to "2 euro ")
        }*/
        maj()
        rv_liste.adapter = HistoriqueActivity.ProduitRecyclerAdapter(monHistorique) {
            // Code qui s’exécute quand on touche un élément // it = le Message de la ligne touchée
            toast("${it.id}")
            //envoi de l'id à detailActivity
        }
    }

    data class Produit(val id: Long, val produit: String, val date : Date, /*val image : String?*/ val prix : String)
    class ProduitRecyclerAdapter(val list: List<Produit>, val listener: (Produit) -> Unit) : RecyclerView.Adapter<ProduitRecyclerAdapter.ProduitViewHolder>() {

        inner class ProduitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var produit: TextView = view.findViewById(R.id.tv_Produit)
            var date: TextView = view.findViewById(R.id.tv_date)
            var image: ImageView = view.findViewById(R.id.tv_image)
            var prix : TextView = view.findViewById(R.id.tv_prix)
        }


        override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
            holder.produit.text = list[position].produit
            holder.date.text = list[position].date.toLocaleString()
            holder.prix.text = list[position].prix
            holder.image.imageBitmap = BitmapFactory.decodeResource(holder.image.context.resources, R.drawable.cancel)





            holder.itemView.setOnClickListener { listener(list[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProduitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_historique, parent, false))
        override fun getItemCount() = list.size
    }
    private fun maj() {
        myDataBase_historique.use {
            select(MyDataBase_historique.TABLE_INFO_HISTORIQUE,
                    MyDataBase_historique.COLUMN_INFO_ID,
                    MyDataBase_historique.COLUMN_INFO_PRODUIT,
                    MyDataBase_historique.COLUMN_INFO_DATE,
                    MyDataBase_historique.COLUMN_INFO_PRIX
            )
                    .exec {
                        for (row in asMapSequence()) {
                            val date = Date()
                            date.time = row[MyDataBase_historique.COLUMN_INFO_DATE] as Long
                            val test = HistoriqueActivity.Produit(row[MyDataBase_historique.COLUMN_INFO_ID] as Long,
                                    row[MyDataBase_historique.COLUMN_INFO_PRODUIT] as String,
                                    date,
                                    row[MyDataBase_historique.COLUMN_INFO_PRIX] as String
                                    )

                            monHistorique.add(test)

                        }
                    }
        }
        rv_liste.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
    }
}
