package se.ohou.commerce.database.common.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import se.ohou.commerce.database.common.document.CounterDocument.Companion.COLLECTION_NAME

@Document(collection = COLLECTION_NAME)
data class CounterDocument(
    @Id val id: String,
    val seq: Long,
) {
    companion object {
        const val COLLECTION_NAME = "Counter"
    }
}
