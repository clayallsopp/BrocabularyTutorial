package com.clayallsopp.isawesome;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.widget.Toast;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class OfflineListActivity extends ListActivity implements OnClickListener {
    /** Called when the activity is first created. */
	ArrayList<Brocab> brocabList = new ArrayList<Brocab>();
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadBrocabulary();
    }
	
	public void loadBrocabulary() { 
	    String jsonRep = null;
	    try {
	        InputStream in = getResources().openRawResource(R.raw.iphone);
	        if (in != null) {
	            Writer writer = new StringWriter();
	            char[] buffer = new char[1024];
	            try {
	                Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	                int n;
	                while((n = reader.read(buffer)) != -1) {
	                    writer.write(buffer, 0, n);
	                }
	            } finally {
	                in.close();
	            }
	            jsonRep = writer.toString();
	        }
	        else {
	            Toast.makeText(this, "Problem opening the file", 2000).show();
	        }
	    } catch (Throwable t) {
	        Toast.makeText(this, "Exception: "+t.toString(), 2000).show();
	    }

	    try {
	        JSONArray jsonArray = new JSONArray(jsonRep);
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject brocabContainerDict = jsonArray.getJSONObject(i);
	            JSONObject brocabDict = brocabContainerDict.getJSONObject("brocab");
	    
	            String term = brocabDict.getString("term");
	            String author = null;
	            if(brocabDict.has("author")) {
	                author = brocabDict.getString("author");
	            }               
	            String description = brocabDict.getString("description");
	    
	            Brocab brocab = new Brocab();
	            brocab.setTerm(term);
	            brocab.setAuthor(author);
	            brocab.setDescription(description);
	    
	            brocabList.add(brocab);
	        }
	    } catch (JSONException e) {
	        Toast.makeText(this, "JSONException: "+e.toString(), 2000).show();
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
}