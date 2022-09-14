package com.encouragee.controller.solr;

//import com.encouragee.model.solr.ClientConversationSearch;
import com.encouragee.model.solr.Product;
import com.encouragee.repository.solr.ProductRepository;
import com.google.common.base.Splitter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
//import org.springframework.util.MultiValueMapAdapter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.encouragee.model.solr.ConversationDocument.FIELD_CONVERSATION_ID;
import static com.encouragee.model.solr.ConversationDocument.FIELD_SCORE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.solr.core.query.AnyCriteria.any;
import static org.springframework.data.solr.core.query.Criteria.where;

//@RestController("/products")
public class SolrRestController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final Sort DEFAULT_SORT = unsorted();
    private final Splitter sortParamSplitter = Splitter.on(",").limit(2).trimResults();

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

        PageRequest pageRequest = getPageRequest(new MultivaluedHashMap<>());

        Criteria fulltextCriteria = any();

//        q=*:*&start=0&rows=20&fq=(id:dimaId+OR+name:333588577333)&fq=(name:3335577333+OR+name:3335577333)
//        SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(fulltextCriteria, pageRequest);
//
//        ClientConversationSearch clientConversationSearch = new ClientConversationSearch();
//        clientConversationSearch.setId("dimaId");
//        clientConversationSearch.setName("3335577333");
//
//        Criteria idCriteria = Optional.ofNullable(clientConversationSearch.getId())
//                    .filter(StringUtils::hasText)
//                    .map(id -> where("id").is(id)
//                            .or(where("name").is("333588577333")))
//                    .map(Criteria::connect).get();
//        Criteria nameCriteria = Optional.ofNullable(clientConversationSearch.getName())
//                    .filter(StringUtils::hasText)
//                    .map(name -> where("name").is(name)
//                            .or(where("name").is(name)))
//                    .map(Criteria::connect).get();
//
//        Criteria nameCriteriaOr = Optional.ofNullable(clientConversationSearch.getName())
//                .filter(StringUtils::hasText)
//                .map(name -> where("name").is("Desk")
//                        .or(where("name").is("Desk")))
//                .map(Criteria::connect).get();
//
//        List<Criteria> filterCriterias = Arrays.asList(idCriteria, nameCriteria);
//        filterCriterias.stream()
//                .map(SimpleFilterQuery::new)
//                .forEach(solrQuery::addFilterQuery);
//
//        SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery();
//        simpleFilterQuery.addCriteria(nameCriteriaOr);
////        solrQuery.setDefaultOperator(Query.Operator.OR);
////        solrQuery.addFilterQuery(simpleFilterQuery);
////        http://localhost:8983/solr/products/select?q=*:*&start=0&rows=20
////        &fq=(id:dimaId+OR+id:dimaId)
////        &fq=(name:dimavalue+OR+name:dimavalue)
////        &fq=(name:Desk+OR+name:Desk)&q.op=OR
//        Page<Product> results = solrTemplate.queryForPage("products", solrQuery, Product.class);

        SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(fulltextCriteria, pageRequest);

//        ClientConversationSearch clientConversationSearch = new ClientConversationSearch();
//        clientConversationSearch.setId("dimaId");
//        clientConversationSearch.setName("dimavalue");

//        Criteria idCriteria = Optional.ofNullable(clientConversationSearch.getId())
//                    .filter(StringUtils::hasText)
//                    .map(id -> where("id").is("dimaId")
//                            .and(where("name").is("dimavalue")).connect()
//                            .or(where("name").is("Desk")))
////                    .map(Criteria::connect)
//                .get();
//        Criteria nameCriteria = Optional.ofNullable(clientConversationSearch.getName())
//                    .filter(StringUtils::hasText)
//                    .map(name -> where("name").is(name)
//                            .or(where("name").is(name)))
//                    .map(Criteria::connect).get();
//
//        Criteria nameCriteriaOr = Optional.ofNullable(clientConversationSearch.getName())
//                .filter(StringUtils::hasText)
//                .map(name -> where("name").is("Desk")
//                        .or(where("name").is("Desk")))
//                .map(Criteria::connect).get();
//
//        List<Criteria> filterCriterias = Arrays.asList(idCriteria);
//        filterCriterias.stream()
//                .map(SimpleFilterQuery::new)
//                .forEach(solrQuery::addFilterQuery);

        SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery();
//        simpleFilterQuery.addCriteria(nameCriteriaOr);
        solrQuery.setDefaultOperator(Query.Operator.OR);
//        solrQuery.addFilterQuery(simpleFilterQuery);
//        http://localhost:8983/solr/products/select?q=*:*&start=0&rows=20
//        &fq=(id:dimaId+OR+id:dimaId)
//        &fq=(name:dimavalue+OR+name:dimavalue)
//        &fq=(name:Desk+OR+name:Desk)&q.op=OR
        Page<Product> results = solrTemplate.queryForPage("products", solrQuery, Product.class);
        return results.getContent();
    }

    private PageRequest getPageRequest(MultivaluedMap<String, String> queryParameters) {
        return PageRequest.of(
                Optional.ofNullable(queryParameters.getFirst("page"))
                        .map(Integer::valueOf)
                        .orElse(DEFAULT_PAGE),
                Optional.ofNullable(queryParameters.getFirst("size"))
                        .map(Integer::valueOf)
                        .orElse(DEFAULT_SIZE),
                Optional.ofNullable(queryParameters.get("sort"))
                        .map(sort -> Sort.by(sort.stream()
                                .map(Object::toString)
                                .map(param -> UriUtils.decode(param, UTF_8))
                                .map(sortParamSplitter::splitToList)
                                .filter(parts -> !parts.isEmpty()) // ignore empty sort param
                                .map(parts -> new Sort.Order(
                                        parts.size() > 1 ? Sort.Direction.fromString(parts.get(1)) : Sort.DEFAULT_DIRECTION,
                                        parts.get(0)))
                                .collect(toList())))
                        .orElse(DEFAULT_SORT));
    }

}
