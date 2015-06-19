/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass3.s_12110243.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mitch
 */
public class TestExceptionManager {
    private static URIBuilder uriBuilder;
    private CloseableHttpClient client;
    public TestExceptionManager() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        uriBuilder = new URIBuilder().setScheme("http")
                                     .setHost("localhost")
                                     .setPort(8084)
                                     .setPath("/assignment3_server_12110243/xlog");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        client = HttpClientBuilder.create().build();
    }
    
    @After
    public void tearDown() throws URISyntaxException, IOException {
        client.close();
        uriBuilder.setPath("/assignment3_server_12110243/xlog");
        URI uri = uriBuilder.build();
        HttpDelete request = new HttpDelete(uri);
        client = HttpClientBuilder.create().build();
        client.execute(request);
        client.close();
    }

    /**
     * Test of register method, of class ExceptionManager.
     * @throws java.lang.Exception
     */
    
    @Test
    public void testRegisterAll() throws Exception  {
        try {
            System.out.println("TestRegisterAll");
            Integer.parseInt("hello");
        }
        catch (Exception e) {
            ExceptionManager.register(e, 0);
            URI uri = uriBuilder.setPath("/assignment3_server_12110243/xlog/all").build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String temp = "", buffer = "";
            while ((temp = br.readLine())!= null) {
                buffer += temp;
            }
            JSONArray array = new JSONArray(buffer);
            assertTrue(array.length() == 1);
        }
    }
    
    @Test
    public void testRegisterID() throws Exception {
        try {
            System.out.println("TestRegisterID");
            Integer.parseInt("hello");
        }
        catch (Exception e) {
            ExceptionManager.register(e, 42);
            URI uri = uriBuilder.setPath("/assignment3_server_12110243/xlog/42").build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String temp = "", buffer = "";
            while ((temp = br.readLine())!= null) {
                buffer += temp;
            }
            JSONObject obj = new JSONObject(buffer);
            assertTrue((int)obj.get("id") == 42);
        }
    }
    
    @Test
    public void testRegisterWrongID() throws Exception {
        try {
            System.out.println("TestRegisterWrongID");
            Integer.parseInt("hello");
        }
        catch (Exception e) {
            ExceptionManager.register(e, 42);
            URI uri = uriBuilder.setPath("/assignment3_server_12110243/xlog/43").build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            assertTrue(response.getStatusLine().getStatusCode() == 404);
        }
    }
    
    @Test
    public void testRegisterDelete() throws Exception {
        try {
            System.out.println("TestRegisterDelete");
            Integer.parseInt("hello");
        }
        catch (Exception e) {
            ExceptionManager.register(e, 0);
        }
        try {
            ArrayList<String> list = new ArrayList();
            list.get(3);
        }
        catch (Exception e){
            ExceptionManager.register(e, 0);
            URI uri = uriBuilder.build();
            HttpDelete request = new HttpDelete(uri);
            client.execute(request);
            client.close();
        }
        client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uriBuilder.setPath("/assignment3_server_12110243/xlog/all")
                                                .build());
        HttpResponse response = client.execute(request);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String temp = "", buffer = "";
        while ((temp = br.readLine())!= null) {
            buffer += temp;
        }
        
        JSONArray array = new JSONArray(buffer);
        assertTrue(array.length() == 0);
    }
}
