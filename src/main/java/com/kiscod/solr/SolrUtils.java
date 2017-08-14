package com.kiscod.solr;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrUtils
{

	public static HttpSolrClient buildBaseHttpSolrClient(String baseUrl)
	{
		HttpSolrClient solrClient = new HttpSolrClient.Builder(baseUrl).build();
		solrClient.setSoTimeout(5000);
		solrClient.setConnectionTimeout(5000);
		return solrClient;
	}
	
	public static void closeSolrClient(SolrClient client)
	{
		if(null != client)
		{
			try
			{
				client.commit();
				client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
