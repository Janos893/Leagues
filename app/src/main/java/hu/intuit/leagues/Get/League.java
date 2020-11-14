package hu.intuit.leagues.Get;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

public class League {

    public String result() throws UnirestException {
        HttpResponse<String> request;
        String requestString="";
        request = Unirest.get("https://raw.githubusercontent.com/openfootball/football.json/master/2020-21/en.1.json").asString();
       // JSONObject myObj = request.getBody().getObject();
        return requestString;
    }

}
