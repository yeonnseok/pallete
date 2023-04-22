package se.ohou.commerce.document

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import se.ohou.commerce.database.common.ItemType
import java.time.ZonedDateTime

/**
 * itemActive: product.isActive
 * active: set by batch.update-today-deal-product
 */
@QueryEntity
@Document("TodayDealV2")
data class TodayDealDocument(
    @Id
    val id: ObjectId?,
    val version: Long,
    val title: String,

    val itemType: ItemType,
    val itemId: String,
    val startAt: ZonedDateTime,
    val endAt: ZonedDateTime,
    val position: Int? = null,

    val popularity: Int? = null,
    val sellingCost: Int? = null,
    val discountRate: Double? = null,
    val itemActive: Boolean? = null,
    val displayCategoryIds: MutableList<Long> = mutableListOf(),

    val active: Boolean = false,

    val createdAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime? = null,
) {
    companion object {
        const val documentName = "TodayDealV2"
    }
}
