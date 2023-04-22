package se.ohou.commerce.document

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@QueryEntity
@Document("FeedVersion")
data class FeedVersionDocument(
    @Id
    val id: ObjectId? = null,
    val feed: FeedType,
    val version: Long,
    val status: VersionStatus,
    val excelFileUrl: String,
    val resultFileUrl: String = "",
    val message: String = "",

    val createdAt: ZonedDateTime,
    var updatedAt: ZonedDateTime,
) {
    enum class FeedType {
        TODAY_DEAL,
    }

    enum class VersionStatus {
        REGISTERED,
        UPLOADING,
        ACTIVE,
        DEACTIVE,
        ERROR,
    }

    companion object {
        fun create(feed: FeedType, version: Long, excelFileUrl: String): FeedVersionDocument {
            return FeedVersionDocument(
                feed = feed,
                version = version,
                excelFileUrl = excelFileUrl,
                status = VersionStatus.REGISTERED,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        }
    }
}
