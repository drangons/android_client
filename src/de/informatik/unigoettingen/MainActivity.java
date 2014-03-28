package de.informatik.unigoettingen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	int mPosition;
	String mProgramURL;
	private ArrayList<String> mProgramList = new ArrayList<String>();
	final Handler mHandler = new Handler();
	private ArrayList<String> mResults = new ArrayList<String>();
	ArrayAdapter<String> madapter;
	
	// Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };
    
    protected void startLongRunningOperation(final String link) {

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                mResults = doSomethingExpensive(link);
                mHandler.post(mUpdateResults);
            }
        };
        t.start();
    }
    
    private void updateResultsInUi() {

        // Back in the UI thread -- update our UI elements based on the data in mResults
    	mProgramList=mResults;
    	madapter.notifyDataSetChanged();
    	
    }
	public ArrayList<String> doSomethingExpensive(String Url)
	{
 
		ArrayList<String> ar=new ArrayList<String>();
		DefaultHttpClient client = new DefaultHttpClient();
		  HttpGet request= new HttpGet(Url);  
		  try {
		      // Execute the method.
			  HttpResponse response = client.execute(request);
			  BufferedReader rd = new BufferedReader
					  (new InputStreamReader(response.getEntity().getContent()));
			  String line = "";
			 
			  while ((line = rd.readLine()) != null) {
				  ar.add(line);
				  
			  }


		  }catch (IOException e) {
		      e.printStackTrace();
		    }
		  
		return ar;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
		String Url="http://user.informatik.uni-goettingen.de/~d.siddapurahemashe/ex1.php";
		//new SimpleHttpClient().execute(URL);
		mProgramList=doSomethingExpensive(Url);
		 Url="http://user.informatik.uni-goettingen.de/~d.siddapurahemashe/ex1.php?programlist=true";
		//String[] LIST = new String[]();
		// new SimpleHttpClient().execute(URL);
		 mProgramList=doSomethingExpensive(Url);
		final ListView listview = (ListView) findViewById(R.id.listview);
		madapter=new ArrayAdapter<String>(this, R.layout.fruit_list,mProgramList);
		listview.setAdapter(madapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			    Toast.makeText(getApplicationContext(),
			      "Click ListItem Number " + position, Toast.LENGTH_LONG)
			      .show();
			    mProgramURL="http://user.informatik.uni-goettingen.de/~d.siddapurahemashe/ex1.php?program="+position;
			    //new SimpleHttpClient().execute(mProgramURL);
			    doSomethingExpensive(mProgramURL);
			    CreateNewActivity();
			  }
			}); 
	    
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void CreateNewActivity()
	{
	    Intent intent = new Intent(this, DisplayCompileActivity.class);	
		startActivity(intent);
	}
	/*private class SimpleHttpClient extends AsyncTask<String, Void, ArrayList<String>> {
			
			  protected ArrayList<String> doInBackground(String... links) {
				  ArrayList<String> ar=new ArrayList<String>();
				  DefaultHttpClient client = new DefaultHttpClient();
				  HttpGet request= new HttpGet(links[0]);  
				  try {
				      // Execute the method.
					  HttpResponse response = client.execute(request);
					  BufferedReader rd = new BufferedReader
							  (new InputStreamReader(response.getEntity().getContent()));
					  String line = "";
					 
					  while ((line = rd.readLine()) != null) {
						  ar.add(line);
						  
					  }


				  }catch (IOException e) {
				      e.printStackTrace();
				    }
				  
				return ar;
				  
			  }
			@Override
			  protected void onPostExecute(ArrayList<String> result) {
				mProgramList=result;
			}
			
			
		}*/


}

