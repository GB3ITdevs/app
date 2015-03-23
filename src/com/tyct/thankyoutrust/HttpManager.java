package com.tyct.thankyoutrust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

//Class to manage the HTTP Connection to the database
public class HttpManager 
{

	//Method to retrieve JSON data in string format from a parsed in URL
public static String getData(String uri) 
{
		//Declare a Buffered Reader and set default to null
		BufferedReader reader = null;
		
		try 
		{
			//Create a URL from the parsed in URL string
			URL url = new URL(uri);
			
			//Create a new HTTP URL Connection from the URL
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//Create a new String Builder
			StringBuilder sb = new StringBuilder();
			
			//Initialize the Buffered Reader to the input stream from the HTTP URL Connection
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			//Declare a new string
			String line;
			
			//While the read line method of the buffered reader is not null add the input line to the string builder
			while ((line = reader.readLine()) != null) 
			{
				sb.append(line + "\n");
			}
			
			//Return the string builder in string format
			return sb.toString();
			
		} 
		
		//If the is an exception, print the stack trace and return null
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		} 
		
		//Then close the Buffered Reader
		finally 
		{
			if (reader != null) 
			{
				try 
				{
					reader.close();
				} 
				//If the is an exception, print the stack trace and return null
				catch (IOException e) 
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		
	}

	//Method to post a parsed in JSON data in string format to a parsed in URL
	public static void postData(String uri, String jsonString)
	{
		//Create a new URL
		URL url;
		
		try 
		{
			//Set the URL to the parsed in URL string
			url = new URL(uri);
			
			//Create a new default HTTP Client
			HttpClient httpClient = new DefaultHttpClient();
			
			//Create a new HTTP post from the URL
			HttpPost httpPost = new HttpPost(url.toURI());
			
			//
			httpPost.setEntity(new StringEntity(jsonString));
			
			httpPost.setHeader("Content-Type", "application/json");
			
			httpClient.execute(httpPost);
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}