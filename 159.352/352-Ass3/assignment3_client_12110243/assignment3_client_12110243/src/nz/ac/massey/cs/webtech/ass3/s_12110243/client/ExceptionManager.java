/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass3.s_12110243.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.abs;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.server.UID;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author mitch
 */
public class ExceptionManager {
    
    public static void register(Throwable x, int id) throws URISyntaxException, UnsupportedEncodingException, IOException, JSONException {
        HttpClient client = new DefaultHttpClient();
        JSONObject obj = new JSONObject();
        if (id == 0) {
            obj.put("id", abs(new UID().hashCode()));
        }
        else {
            obj.put("id", id);
        }
        obj.put("class", x.getClass().toString());
        obj.put("timestamp", System.currentTimeMillis());
        obj.put("message", x.getMessage());
        obj.put("stacktrace", x.getStackTrace());
        URI uri = new URIBuilder().setScheme("http")
                                  .setHost("localhost")
                                  .setPort(8084)
                                  .setPath("/assignment3_server_12110243/xlog")
                                  .build();
        
        HttpPost request = new HttpPost(uri);
        ArrayList<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("exception", obj.toString()));
        request.setEntity(new UrlEncodedFormEntity(parameters));
        client.execute(request);
    }
}
