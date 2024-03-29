package com.encouragee.camel.clientSearch.model;

import com.encouragee.model.solr.ConversationDocument;
import lombok.Data;

import java.util.List;

@Data
public class ConversationDocumentList {
	private final List<ConversationDocument> documents;

	public ConversationDocumentList(List<ConversationDocument> documents) {
		this.documents = documents;
	}
}
