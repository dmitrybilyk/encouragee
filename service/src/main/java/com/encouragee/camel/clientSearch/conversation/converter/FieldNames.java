package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationDocument;
import com.zoomint.encourage.model.conversation.ConversationParticipantType;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Radek Mensik, 10/31/2014
 */
public final class FieldNames {

	private FieldNames() {
	}

	/**
	 * Creates Solr field name for phrase+channel combination.
	 *
	 * @param phraseId        phraseId of the phrase in the database
	 * @param participantType type of the participant pronouncing the phrase
	 * @return name of the Solr field
	 */
	@NotNull
	public static String getPhraseFieldName(@NotNull Long phraseId, @NotNull ConversationParticipantType participantType) {
		return ConversationDocument.FIELD_SPEECH_PHRASE_DYNAMIC_PREFIX
				+ phraseId + "_" + participantType.getShortName();
	}

	@NotNull
	public static String getMetadataFieldName(@NotNull String key) {
		return ConversationDocument.FIELD_METADATA_DYNAMIC_PREFIX	+ requireNonNull(key, "Metadata key is required");
	}

	@NotNull
	public static String getNumberMetadataFieldName(@NotNull String key) {
		return ConversationDocument.FIELD_METADATA_NUMBER_DYNAMIC_PREFIX + requireNonNull(key, "Metadata key is required");
	}
}
