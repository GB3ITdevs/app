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

	public static void postData(String uri, String jsonString) {
		URL url;
		//BufferedWriter reader = null;
		//DataOutputStream output;
		try 
		{
			url = new URL(uri);
			//HttpURLConnection con = (HttpURLConnection) url.openConnection();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setEntity(new StringEntity(jsonString));
			
			//httpPost.setHeader("Content-Type", "application/json");
			//httpPost.setHeader("Accept-Encoding", "application/json");
			//httpPost.setHeader("Accept-Language", "en-US");
			
			httpClient.execute(httpPost);
			
			//reader = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			//output = new DataOutputStream(con.getOutputStream());
			//output.writeBytes(jsonString);
			//reader.write(jsonString);
			//reader.close();
			//output.close();
			
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