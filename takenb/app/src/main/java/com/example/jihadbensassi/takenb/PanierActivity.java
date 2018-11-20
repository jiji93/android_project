package com.example.jihadbensassi.takenb;

/**
 * Created by jihadbensassi on 29/05/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.SparseBooleanArray;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        //import com.google.zxing.integration.android.IntentIntegrator;
        //import com.google.zxing.integration.android.IntentResult;

        import java.util.ArrayList;

public class PanierActivity extends AppCompatActivity {


    Button scanner;
    Button ajouter;
    Button valider;
    TextView infoproduit;

    ArrayList<String> itemliste;
    ArrayAdapter<String> adapter;
    ListView lv;

    String value[];
    TextView totalsomme;

    double somme = 0;
    double valeurRecentAddition;
    double valeurRecentSoustraction;

    double somme_total = 0;
    /*constante pour le sta de intentpaiement */ private final static int cons = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);
        lv = (ListView) findViewById(R.id.listeView);

        scanner = (Button) findViewById(R.id.scanner);
        valider = (Button) findViewById(R.id.valider);
        infoproduit = (TextView) findViewById(R.id.info);

        itemliste = new ArrayList<>();
        //recupére val panier
        SharedPreferences mSharedPreference1 =   getSharedPreferences("panier",0);
        int size = mSharedPreference1.getInt("Status_size", 0);
        for(int i=0;i<size;i++)
        {
            itemliste.add(mSharedPreference1.getString("Status_" + i, null));
        }
        ajouter = (Button) findViewById(R.id.ajouter);
        adapter = new ArrayAdapter<String>(PanierActivity.this, android.R.layout.simple_list_item_multiple_choice, itemliste);

        totalsomme = (TextView) findViewById(R.id.textviewtotal);
        Intent intent = getIntent();
        String barcode = intent.getStringExtra("barcode");
        infoproduit.setText(barcode);
        SharedPreferences mSharedPreference2 =   getSharedPreferences("prix",0);
        somme_total = mSharedPreference2.getFloat("Somme",0);
        totalsomme.setText(Double.toString(somme_total)+" £");

        View.OnClickListener addlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemliste.add(infoproduit.getText().toString());

                String value[] = infoproduit.getText().toString().split("--");
                somme = somme_total;
                 totalsomme.setText(value[2]);

                //conversion String en Double
                valeurRecentAddition = Double.parseDouble(value[2]);
                somme_total=faireSomme(valeurRecentAddition);
                //conversion Double en String et mise sur le textView
                totalsomme.setText(Double.toString(somme_total)+" £");
                //infoproduit.setText("");
                infoproduit.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        };


        //Supression d'un élement du panier

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                SparseBooleanArray positionchercker = lv.getCheckedItemPositions();

                int count = lv.getCount();
                for (int item = count - 1; item >= 0; item--) {
                    if (positionchercker.get(item)) {
                        //recuperer l'element supprimé
                        String str = itemliste.get(item).toString();
                        //recuperer le 2eme valeur de cette élément
                        String value1[] = str.split("--");
                        //
                        valeurRecentSoustraction = Double.parseDouble(value1[2]);
                        somme_total = faireSoustraction(valeurRecentSoustraction);
                        if (somme_total >= 0.0)
                        {
                            totalsomme.setText(Double.toString(somme_total) + " £");
                        //enfin enlever l'element
                        adapter.remove(itemliste.get(item));
                        //message de notification
                        Toast.makeText(PanierActivity.this, "produit supprimé de panier", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            somme_total = 0;
                            totalsomme.setText(Double.toString(somme_total) + " £");
                            //enfin enlever l'element
                            adapter.remove(itemliste.get(item));
                            //message de notification
                            Toast.makeText(PanierActivity.this, "produit supprimé de panier", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                positionchercker.clear();
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        ajouter.setOnClickListener(addlistener);
        lv.setAdapter(adapter);
        /*scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(PanierActivity.this);
                scanIntegrator.initiateScan();
            }
        });*/

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (somme_total != 0.0) {
                    adapter.clear();
                    somme_total = 0;
                    //verificationConnexion();
                } else {
                    Toast.makeText(getBaseContext(), "Veuillez faire vos achats au préalable ", Toast.LENGTH_LONG).show();
                }
                /*Intent intentPaiement =  new Intent(MainActivity.this,Paiement.class);
                intentPaiement.putExtra("SommeApayer",totalsomme.getText().toString());
                startActivity(intentPaiement);*/
            }
        });

    }


    //@Override
    /*protected void onActivityResult(int requestCode, int resultCcode, Intent in){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode ,resultCcode,in);
        if (scanningResult != null){
            // String contenu = in.getStringExtra("");

            String scanContent = scanningResult.getContents();
            infoproduit.setText(scanContent);
            // Toast.makeText(MainActivity.this,scanContent,Toast.LENGTH_LONG).show();
        }
    }*/

    public double faireSomme(double val) {

        return somme = somme + val;
    }

    public double faireSoustraction(double val) {

        return somme = somme - val;
    }

    //verification conexion du bouton VALIDER

    /*public void verificationConnexion(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //Toast.makeText(getBaseContext(), ""/*+ networkInfo.getTypeName(), /*Toast.LENGTH_SHORT).show();
            Intent intentPaiement =  new Intent(PanierActivity.this,Paiement.class);
            intentPaiement.putExtra("SommeApayer",totalsomme.getText().toString());
            startActivity(intentPaiement);
        }
        else{
            Toast.makeText(getBaseContext(), "Veuillez vous connecté à l'internet ", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("panier",0);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* sKey is an array */
        mEdit1.putInt("Status_size", itemliste.size());

        for(int i=0;i<itemliste.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, itemliste.get(i));
        }

        mEdit1.commit();
        SharedPreferences sp2 = getSharedPreferences("prix",0);
        SharedPreferences.Editor mEdit2 = sp2.edit();
        mEdit2.putFloat("Somme",(float)somme_total);
        mEdit2.commit();
    }



}

