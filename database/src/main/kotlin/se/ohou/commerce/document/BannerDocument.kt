package se.ohou.commerce.document

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@QueryEntity
@Document("Banner")
data class BannerDocument(
    @Id
    val id: ObjectId? = null,
    val type: BannerType,
    val active: Boolean,
    val position: Int,
    val bannerUrl: String = "",

    val startAt: ZonedDateTime,
    val endAt: ZonedDateTime,

    val createdAt: ZonedDateTime?,
    val updatedAt: ZonedDateTime?,
) {
    enum class BannerType {
        TODAY_DEAL,
    }
}
