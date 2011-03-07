package com.clayallsopp.isawesome;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BrocabDetailActivity extends Activity implements OnClickListener {
    Button back;
    Button favorite;

    Brocab brocab;
    boolean favorited;
    boolean fromFavorites;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        
        brocab = (Brocab)this.getIntent().getSerializableExtra("brocab");
        
        back=(Button)findViewById(R.id.detail_back);
        back.setOnClickListener(this);
        
        favorite=(Button)findViewById(R.id.detail_add);
        favorite.setOnClickListener(this);

        TextView term = (TextView)findViewById(R.id.detail_term);
        term.setText(brocab.getTerm());
        
        TextView description = (TextView)findViewById(R.id.detail_description);
        description.setText(brocab.getDescription());
        
        TextView author = (TextView)findViewById(R.id.detail_author);
        if(brocab.getAuthor() != null && brocab.getAuthor().length() > 0 && !brocab.getAuthor().equals("null")) {
            author.setText("Submitted by " + brocab.getAuthor());
        }
    }
    
    public void addToFavorites() {
        ArrayList<Brocab> brocabs = null;
        try {
            FileInputStream fis = openFileInput("favorites.ser");
            if (fis != null) {
                ObjectInputStream in = new ObjectInputStream(fis);
                brocabs = (ArrayList<Brocab>)in.readObject();
                in.close();
            }
            else {
                // File didn't exist
            }
        } catch (Throwable t) {
            handleError(t);
        }
        
        if (brocabs != null) {
            brocabs.add(brocab);
        }
        else {
            brocabs = new ArrayList<Brocab>();
            brocabs.add(brocab);
        }
        
        try {
            FileOutputStream fos = openFileOutput("favorites.ser",0);
            if (fos != null) {
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(brocabs);
                out.close();
                Toast.makeText(this, "Added to Favorites!",2000).show();
            }
            else {
                // :(
            }
        } catch (Throwable t) {
            handleError(t);
        }
    }
    
    public void handleError(Throwable e) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Uh, error bro");
        alertDialog.setMessage("There was a problem: " + e.toString());
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                      // here you can add functions
                   }
        });
        alertDialog.show();
    }
    
    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        }
        else if (v == favorite) {
        	addToFavorites();
        }
    }
}
