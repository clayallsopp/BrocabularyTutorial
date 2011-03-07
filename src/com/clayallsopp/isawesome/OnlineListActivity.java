package com.clayallsopp.isawesome;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.widget.Button;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class OnlineListActivity extends ListActivity implements OnClickListener {
    /** Called when the activity is first created. */
	Button refresh;
	Button create;
	ProgressDialog progress;
	ArrayList<Brocab> brocabList = new ArrayList<Brocab>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.online);
	    refresh=(Button)findViewById(R.id.online_refresh);
	    refresh.setOnClickListener(this);
	    
	    create=(Button)findViewById(R.id.online_create);
	    create.setOnClickListener(this);        
	}
	
	public void downloadBrocabulary() {
	    Handler handler = new Handler () {
	        public void handleMessage(Message message) {
	            switch (message.what) {
	                case HttpConnection.DID_START:
	                    refresh.setText("Refreshing...");
	                    break;
	                case HttpConnection.DID_SUCCEED:
	                    refresh.setText("Refresh");
	                    String response = (String)message.obj;
	                    loadBrocabulary(response);
	                    progress.dismiss();
	                    break;
	                case HttpConnection.DID_ERROR:
	                    refresh.setText("Refresh");
	                    Exception e = (Exception) message.obj;
	                    e.printStackTrace();
	                    progress.dismiss();
	                    handleError(e);
	                    break;
	            }
	        }
	    };

	    new HttpConnection(handler).get("http://clayallsopp.com/brocabs.json");
	}
	
	public void loadBrocabulary(String jsonRep) { 
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
	    if (v == refresh) {
	        progress = ProgressDialog.show(this, "Refreshing...","Just chill bro.",true,false);
	        downloadBrocabulary();
	    }
	    else if (v == create) {
            startActivity(new Intent(this, CreateBrocabActivity.class));
	    }
	    else {
	    	Brocab brocab = brocabList.get((Integer)v.getTag());
	        Intent i = new Intent(this, BrocabDetailActivity.class);
	        i.putExtra("brocab",brocab);
	        startActivity(i);
	    }
	}
	
	public void handleError(Exception e) {
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