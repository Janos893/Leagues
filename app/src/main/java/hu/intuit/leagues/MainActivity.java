package hu.intuit.leagues;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mashape.unirest.http.exceptions.UnirestException;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.intuit.leagues.Get.League;
import hu.intuit.leagues.ui.main.SectionsPagerAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView leagueView;
    TextView matchesView;
    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        TabLayout tabs = findViewById(R.id.tabs);

        leagueView = (TextView) findViewById(R.id.responseView);
        matchesView = (TextView) findViewById(R.id.matchesView);

        refresh = (Button) findViewById(R.id.button);
        refresh.setEnabled(false);
        getResponse();
        refresh.setEnabled(true);

        matchesView.setMovementMethod(new ScrollingMovementMethod());
        refresh.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                refresh.setEnabled(false);
                leagueView.setText("Loading, please wait...");
                matchesView.setText("");
                getResponse();
            }
        });
    }

    private String getObject(String response){
        String leagueName = "";
        try {
            JSONObject obj = new JSONObject(response);
            leagueName = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "League: " + leagueName;
    }

    private List<String> getRound(String response){
        String matches="";
        List<String> list= new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr=obj.getJSONArray("matches");
            for(int i=0; i<arr.length(); i++){
            list.add(arr.getJSONObject(i).getString("round"));
            }
            matches = obj.getString("matches");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getDate(String response){
        String matches="";
        List<String> list= new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr=obj.getJSONArray("matches");
            for(int i=0; i<arr.length(); i++){
                list.add(arr.getJSONObject(i).getString("date"));
            }
            matches = obj.getString("matches");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getTeam1(String response){
        String matches="";
        List<String> list= new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr=obj.getJSONArray("matches");
            for(int i=0; i<arr.length(); i++){
                list.add(arr.getJSONObject(i).getString("team1"));
            }
            matches = obj.getString("matches");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getTeam2(String response){
        String matches="";
        List<String> list= new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr=obj.getJSONArray("matches");
            for(int i=0; i<arr.length(); i++){
                list.add(arr.getJSONObject(i).getString("team2"));
            }
            matches = obj.getString("matches");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void  getResponse(){
        OkHttpClient client = new OkHttpClient();
        String url="https://raw.githubusercontent.com/openfootball/football.json/master/2020-21/en.1.json";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            leagueView.setText(getObject(myResponse));
                            for(int i=0; i<getRound(myResponse).size(); i++){
                                matchesView.append(getRound(myResponse).get(i) + "\n");
                                matchesView.append(getDate(myResponse).get(i) + ": ");
                                matchesView.append(getTeam1(myResponse).get(i) + " - ");
                                matchesView.append(getTeam2(myResponse).get(i) + "\n");
                                matchesView.append("\n");
                            }
                        }
                    });
                }

            }
        });
    }

}