package com.flipkart.playground;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flipkart.models.Book;
import com.flipkart.persistence.BookContentProvider;
import com.flipkart.persistence.BookContentProviderHelperMethods;
import com.flipkart.persistence.DatabaseHelper;
import com.flipkart.toolbox.AppController;
import com.flipkart.toolbox.CustomPriorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Book> bookListFromDatabase = BookContentProviderHelperMethods.getBookListFromDatabase(this);

        if (bookListFromDatabase.isEmpty()) {
            Log.e("Book from CP", "No Books from CONTENT PROVIDER");
        } else {
            for (Book book : bookListFromDatabase) {
                Log.e("Book from CP", "Title - " + book.getTitle()
                        + ", Publish Date - " + book.getPublishDate());
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBook("https://www.googleapis.com/books/v1/volumes?q=isbn:0735619670");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getBook(String url) {
        CustomPriorityRequest getDataRequest = new CustomPriorityRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(MainActivity.this, "Success! Check your log for data :)", Toast.LENGTH_SHORT).show();

                    JSONArray mItemArray = response.getJSONArray("items");
                    JSONObject mBookObject = mItemArray.getJSONObject(0);
                    JSONObject mVolumeInfo = mBookObject.getJSONObject("volumeInfo");

                    Log.e("DATA", "Title - " + mVolumeInfo.getString("title")
                            + " Published Date - " + mVolumeInfo.getString("publishedDate"));

                    Book book = new Book(mVolumeInfo.getString("title"), mVolumeInfo.getString("publishedDate"));

                    boolean isBookInDB = BookContentProviderHelperMethods
                            .isBookInDatabase(MainActivity.this, book.getId());
                    if (isBookInDB) {
                        Uri contentUri = BookContentProvider.CONTENT_URI;
                        getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(book.getId())});
                        Log.e("CONTENT PROVIDER", "Book removed form DB");

                    } else {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.KEY_ID, book.getId());
                        values.put(DatabaseHelper.KEY_TITLE, book.getTitle());
                        values.put(DatabaseHelper.KEY_PUBLISH_DATE, book.getPublishDate());

                        getContentResolver().insert(BookContentProvider.CONTENT_URI, values);

                        Log.e("CONTENT PROVIDER", "Book added to DB");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        getDataRequest.setPriority(Request.Priority.LOW);
        // Add getDataRequest to our global instance of Volley RequestQueue
        AppController.getInstance().addToRequestQueue(getDataRequest);
    }
}
