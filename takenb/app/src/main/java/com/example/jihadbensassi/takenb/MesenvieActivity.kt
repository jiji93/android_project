package com.example.jihadbensassi.takenb

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

/**
 * Created by jihadbensassi on 22/05/2018.
 */
class MesenvieActivity : AppCompatActivity() {

    val maListe = ArrayList<Produit>()
    var photoPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envie)
        maj()
        rv_liste.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        rv_liste.adapter = ProduitRecyclerAdapter(maListe) {
            // Code qui s’exécute quand on touche un élément // it = le Message de la ligne touchée
            toast( "${it.id}" )
            //envoi de l'id à detailActivity
        }
        btn_photo.setOnClickListener {
            // On utilise une fonction pour pouvoir relancer la prise de photo après la permition granted si necessaire
            takePicture()
        }



    }
    data class Produit(val id: Long, /*val produit: String*/val date : Date, val image : String?)
    class ProduitRecyclerAdapter(val list: List<Produit>, val listener: (Produit) -> Unit) : RecyclerView.Adapter<ProduitRecyclerAdapter.ProduitViewHolder>() {

        inner class ProduitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var produit: TextView = view.findViewById(R.id.tv_produit)
            var date: TextView = view.findViewById(R.id.tv_date)
            var image: ImageView = view.findViewById(R.id.tv_image)
        }


        override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
            //holder.produit.text = list[position].produit
            holder.date.text = list[position].date.toLocaleString()
            if(list[position].image != null) {
                holder.image.imageBitmap = BitmapFactory.decodeFile(list[position].image)
            } else {
                holder.image.imageBitmap = BitmapFactory.decodeResource(holder.image.context.resources, R.drawable.cancel)
            }




            holder.itemView.setOnClickListener { listener(list[position]) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProduitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_envie, parent, false))
        override fun getItemCount() = list.size
    }

    override fun onStart() {
        super.onStart()
        if(photoPath!="") {
            myDataBase_envie.use {
                insert(MyDataBase_envie.TABLE_INFO_ENVIE,
                        //MyDataBase.COLUMN_INFO_PRODUIT to val_produit,
                        MyDataBase_envie.COLUMN_INFO_IMAGE to photoPath,
                        MyDataBase_envie.COLUMN_INFO_DATE to System.currentTimeMillis())
            }
            maListe.clear()
            maj()
            photoPath = ""
        }

    }
    override fun onPause() {
        super.onPause()
        myDataBase_envie.use {
            delete(MyDataBase_envie.TABLE_INFO_ENVIE)
            for (i in maListe.indices)
                insert(MyDataBase_envie.TABLE_INFO_ENVIE,

                        //MyDataBase_envie.COLUMN_INFO_PRODUIT to maListe[i].produit)
                        MyDataBase_envie.COLUMN_INFO_DATE to Date(maListe[i].date.toString()).time,
                        MyDataBase_envie.COLUMN_INFO_IMAGE to maListe[i].image)


        }
    }




    ///////////////////////////////////////FONCTION PRENDRE PHOTO /////////////////////////////////////////////////////////////


    fun takePicture() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fichier.jpeg")
            //val uri = FileProvider.getUriForFile(this,"com.example.android.fileprovider", file)
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            with(contentResolver.query(uri, arrayOf(android.provider.MediaStore.Images.ImageColumns.DATA), null, null, null)) {
                moveToFirst()
                photoPath = getString(0)
                close()
            }
            if (intentPhoto.resolveActivity(packageManager) != null) {
                startActivityForResult(intentPhoto, 2)
            }
        } else {
            // On demande la permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            2 -> { // WRITE_EXTERNAL_STORAGE
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission accordée, on relance la prise de photo
                    takePicture()
                } else {
                    toast("Vous n'avez pas accepter la permission pour prendre une photo ") // TODO
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2 -> { // ACTION_IMAGE_CAPTURE
                val photoFile = File(photoPath)
                // On garde le même nom de fichier (il a l'air d'être unique) mais on copie dans notre stockage interne (filesDir)
                val photoDest = File(this.filesDir, photoFile.name)
                photoFile.copyTo(photoDest)
                // Après la copie, on supprime le fichier d'origine (sur la carte SD)
                photoFile.delete()

                // On stocke le nouveau nom du fichier dans photoPath (pour pouvoir l'ajouter à la base de données)
                photoPath = photoDest.path

            }
        }
    }
    ////////////////:MAJ////////////////////////////////////////////////////////////////////////
    private fun maj() {
        myDataBase_envie.use {
            select(MyDataBase_envie.TABLE_INFO_ENVIE,
                    MyDataBase_envie.COLUMN_INFO_ID,
                    //MyDataBase.COLUMN_INFO_PRODUIT,
                    MyDataBase_envie.COLUMN_INFO_IMAGE,
                    MyDataBase_envie.COLUMN_INFO_DATE)
                    .exec {
                        for (row in asMapSequence()) {
                            val date = Date()
                            date.time = row[MyDataBase_envie.COLUMN_INFO_DATE] as Long
                            val test = Produit(
                                    row[MyDataBase_envie.COLUMN_INFO_ID] as Long,
                                    //row[MyDataBase_envie.COLUMN_INFO_PRODUIT] as String
                                    date,
                                    row[MyDataBase_envie.COLUMN_INFO_IMAGE] as String?


                            )
                            maListe.add(test)

                        }
                    }
        }
        rv_liste.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
    }
}

