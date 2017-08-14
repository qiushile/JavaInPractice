package com.kiscod.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolrConnect {

    public void Process() throws IOException, SolrServerException {
        HttpSolrClient client = SolrUtils.buildBaseHttpSolrClient("http://123.135.111.2:8032/solr/core4");
        List<SolrInputDocument> documentList = new ArrayList<SolrInputDocument>();


        String ids = "";


        String[] arr = ids.split("\\|");
        for(int i=0;i<arr.length;i++)
        {
            SolrInputDocument document = new SolrInputDocument();
            Map<String,String> map = new HashMap<String,String>();
//            map.put("set", value);
//            document.setField(fieldName, map);
            document.addField("id", arr[i]);
            document.addField("content", "ccccsweibo");
            document.addField("name", "mm");
            documentList.add(document);
        }
        UpdateResponse reponse = client.add(documentList,documentList.size());
        client.commit();
//        log.info(reponse.getResponse());

    }


}
