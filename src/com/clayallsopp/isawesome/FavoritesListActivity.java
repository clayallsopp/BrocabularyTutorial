package com.clayallsopp.isawesome;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FavoritesListActivity  extends ListActivity implements OnClickListener {
    /** Called when the activity is first created. */
	ArrayList<Brocab> brocabList = new ArrayList<Brocab>();
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadBrocabulary();
    }
	
	public void loadBrocabulary() { 
        try {
            FileInputStream fis = openFileInput("favorites.ser");
            if (fis != null) {
                ObjectInputStream in = new ObjectInputStream(fis);
                brocabList = (ArrayList<Brocab>)in.readObject();
                in.close();
            }
            else {
                // File didn't exist, nothing to do right now
            }
        } catch (Throwable t) {
            handleError(t);
        }
        setListAdapter(new BrocabAdapter(this,android.R.layout.simple_list_item_1,brocabList));
    }
	
	@Override
    public void onClick(View v) {
		Brocab brocab = brocabList.get((Integer)v.getTag());
	    Intent i = new Intent(this, BrocabDetailActivity.class);
	    i.putExtra("brocab",brocab);
	    startActivity(i);
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
}