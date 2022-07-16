package com.encouragee.controller.solr;

import com.encouragee.model.solr.Product;
import com.encouragee.repository.solr.ProductRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@RestController("/products")
public class SolrRestController {

    private final ProductRepository productRepository;
    private final SolrClient solrClient;
    private final SolrTemplate solrTemplate;

    public SolrRestController(ProductRepository productRepository, SolrClient solrClient, SolrTemplate solrTemplate) {
        this.productRepository = productRepository;
        this.solrClient = solrClient;
        this.solrTemplate = solrTemplate;
    }

    @GetMapping("/save/{id}/{name}")
    public String saveProduct(@PathVariable String id, @PathVariable String name) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        productRepository.save(product);
//        final Product retrievedProduct = productRepository.findById(product.getId()).get();
        return "success";
    }

    @GetMapping("/get")
    public Iterable<Product> getProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/find")
    public Iterable<Product> findProduct() throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("name:*es*");
        query.setStart(0);
        query.setRows(10);
        QueryResponse response = solrClient.query(query);
        List<Product> products = response.getBeans(Product.class);
        return products;
    }

    @GetMapping("/findByCriteria")
    public Iterable<Product> findWithCriteria() {

        Criteria conditions = createSearchConditions();
        SimpleQuery search = new SimpleQuery(conditions);

        Page<Product> results = solrTemplate.queryForPage("products", search, Product.class);
        return results.getContent();
    }

    private Criteria createSearchConditions() {
        Criteria conditions = new Criteria("name").contains("77");
        return conditions;
    }

}
