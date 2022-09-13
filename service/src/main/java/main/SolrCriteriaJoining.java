package main;

//import com.encouragee.model.solr.ClientConversationSearch;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.encouragee.model.solr.ConversationDocument.FIELD_CONVERSATION_ID;
import static com.encouragee.model.solr.ConversationDocument.FIELD_PREVIOUS_IDS;
import static org.springframework.data.solr.core.query.Criteria.where;

public class SolrCriteriaJoining {
    public static void main(String[] args) {
        Stream<Criteria> criteria1 = Stream.of(createCriteria("name1"), createCriteria("name2"),
                createCriteria("name3"));

//        criteria1.collect(Collectors.joining());

    }

    private static Criteria createCriteria(String fieldName) {
        return Optional.ofNullable(fieldName)
                .filter(StringUtils::hasText)
                .map(conversationId -> where(FIELD_CONVERSATION_ID).is(conversationId)
                        .or(where(FIELD_PREVIOUS_IDS).is(conversationId)))
                .map(Criteria::connect).get();
    }
}
