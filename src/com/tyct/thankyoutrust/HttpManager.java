package com.tyct.thankyoutrust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpManager {

	public static String getData(String uri) {

		BufferedReader reader = null;

		try {
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

	}

	public static void postData(String uri, String jsonString) 
	{
		URL url;

		try 
		{
			url = new URL(uri);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setEntity(new StringEntity(jsonString));
			
			httpPost.setHeader("Content-Type", "application/json");

			
			httpClient.execute(httpPost);
			
		} 
		catch (IOException e) 
		{

			e.printStackTrace();
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}
	}
	
	//Method to delete an object from the database,
	//the uri passed in must end with the resource id of the object
	public static void deleteData(String uri)
	{
		URL url;
		
		try 
		{
			url = new URL(uri);

			HttpClient httpClient = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(url.toURI());
			
			httpDelete.setHeader("Content-Type", "application/json");
			
			httpClient.execute(httpDelete);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Method to update an object from the database,
	//the uri passed in must end with the resource id of the object needing to be updated
	public static void updateData(String uri, String jsonString)
	{
		URL url;
		try 
		{
			url = new URL(uri);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPut httpPut = new HttpPut(url.toURI());
			
			httpPut.setEntity(new StringEntity(jsonString));
			
			httpPut.setHeader("Content-Type", "application/json");
			
			httpClient.execute(httpPut);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
	}
}