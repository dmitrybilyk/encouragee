package com.encouragee.model.solr;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Getter
@Setter
@SolrDocument(collection = "products")
public class Product {
   
    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "name", type = "string")
    private String name;

}