package com.clayallsopp.isawesome;

import java.net.URLEncoder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBrocabActivity extends Activity implements OnClickListener {
    Button upload;
    Button back;
    
    boolean uploaded;

    EditText term;
    EditText description;
    EditText author;

    ProgressDialog progress;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        uploaded = false;

        upload=(Button)findViewById(R.id.create_upload);
        upload.setOnClickListener(this);

        back=(Button)findViewById(R.id.create_back);
        back.setOnClickListener(this);

        term=(EditText)findViewById(R.id.create_term);
        description=(EditText)findViewById(R.id.create_description);
        author=(EditText)findViewById(R.id.create_author);
    }
    
    @Override
    public void onClick(View v) {
        if(v == back) {
            // Get rid of the Activity
        	finish();
        }
        else if (v == upload && !uploaded) {
            progress = ProgressDialog.show(this, "Uploading...","Just chill bro, it's going to space.",true,false);
            upload();
        }
    }
    
    public void upload() {
        String u_term = term.getText().toString();
        String u_description = description.getText().toString();
        String u_author = author.getText().toString();
        
        boolean term_is_zero = (u_term.length() == 0);
        boolean description_is_zero = (u_description.length() == 0);

        if (term_is_zero || description_is_zero) {
            if(term_is_zero && description_is_zero) 
                showErrorWithString("Bro, you need a brocab term and definition!");
            else if (term_is_zero)
                showErrorWithString("Bro, you need a brocab term!");
            else if (description_is_zero)
                showErrorWithString("Bro, you need a definition!");
        }
        
        else {
            Handler handler = new Handler () {
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case HttpConnection.DID_START:
                            upload.setText("Uploading...");
                            break;
                        case HttpConnection.DID_SUCCEED:
                            upload.setText("Uploaded!");
                            uploadToast();
                            uploaded = true;
                            progress.dismiss();
                            break;
                        case HttpConnection.DID_ERROR:
                            upload.setText("Upload");
                            Exception e = (Exception) message.obj;
                            progress.dismiss();
                            handleError(e);
                            break;
                    }
                }
            };
            
            try {
                String c_term = URLEncoder.encode(u_term,"utf-8");
                String c_description = URLEncoder.encode(u_description,"utf-8");
                String c_author = null;
                if (u_author.length() > 0)
                    c_author = URLEncoder.encode(u_author,"utf-8");
                String query;
                if (c_author != null)
                    query = "term="+c_term+"&description="+c_description+"&author="+c_author;
                else
                    query = "term="+c_term+"&description="+c_description;
                new HttpConnection(handler).post("http://brocabserver.heroku.com/brocabs/new",query);
            }
            catch (Exception T) {
                handleError(T);
            }
        }
    }
    
    public void handleError(Exception e) {
        showErrorWithString("There was a problem: " + e.toString());
    }

    public void showErrorWithString(String e) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Uh, error bro");
        alertDialog.setMessage(e);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                      // here you can add functions
                   }
        });
        alertDialog.show();
    }

    public void uploadToast() {
        Toast.makeText(this,"Uploaded successful",2000).show();
    }
}