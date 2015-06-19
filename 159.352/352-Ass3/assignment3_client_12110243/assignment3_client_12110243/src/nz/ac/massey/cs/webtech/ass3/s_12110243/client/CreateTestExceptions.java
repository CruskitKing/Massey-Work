/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass3.s_12110243.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;

/**
 *
 * @author mitch
 */

// I created this class to add some exceptions to the ThrowbleRegistryServlet 
// to manually test my outputs

public class CreateTestExceptions {
    public static void main(String[] args) throws URISyntaxException, IOException, UnsupportedEncodingException, JSONException {
        try {
            Integer.parseInt("hello");
        }
        catch (Exception e) {
            ExceptionManager.register(e, 42);
        }
        try {
            ArrayList<String> list = new ArrayList();
            list.get(3);
        }
        catch (Exception e) {
            ExceptionManager.register(e, 0);
        }
    }
}
