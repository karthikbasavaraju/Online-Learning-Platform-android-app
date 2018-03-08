package com.example.kbasa.teaching;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import java.util.LinkedList;

public class StudentSearchActivity extends AppCompatActivity {

    LinkedList ll = new LinkedList();
    public StudentSearchActivity(){
        ll.add("Yosemite National Park");
        ll.add("Sequoia National Park");
        ll.add("Lassen Volcanic National Park");
        ll.add("Death Valley National Park");
        ll.add("Joshua Tree National Park");
        ll.add("Kings Canyon National Park");
        ll.add("Pinnacles National Park");
        ll.add("Channel Islands National Park");
        ll.add("Border Field State Park");
        ll.add("Point Reyes National Park");
        ll.add("Del Norte Coast Redwoods State Park");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_student_search);

        final ListView wikiLinks = findViewById(R.id.wikiLinks);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_textview, R.id.button, ll);

        SearchView sv = findViewById(R.id.searchButton);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(StudentSearchActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


        /*wikiLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StudentSearchActivity.this, Activity2.class);

                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    intent.putExtra("web_page",String.valueOf(wikiLinks.getItemIdAtPosition(i)));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });*/
        wikiLinks.setAdapter(adapter);
    }
}
