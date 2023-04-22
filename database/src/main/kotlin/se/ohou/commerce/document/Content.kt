package se.ohou.commerce.document

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import se.ohou.commerce.database.common.Utils.toZonedDateTime
import java.time.ZonedDateTime

data class Content<T>(
    val layout: LayoutInfo,
    val source: Source,
    val slots: List<Slot<T>>,
) {
    data class LayoutInfo(
        val mobile: Layout?,
        val tablet: Layout?,
        val pc: Layout?,
    ) {
        data class Layout(
            val row: Float?,
            val col: Float?,
        )
    }

    enum class Source {
        ADMIN,
        TODAY_DEAL,
        ADVERTISE,
        RECENTLY_VIEWED,
        RECENTLY_VIEWED_RELATED,
        CF_RECOMMEND,
        P2V_RECOMMEND,
        POPULAR_ITEMS,
        POPULAR_KEYWORDS
        ;
    }

    class Slot<T>(
        val type: SlotType,
        val slot: T
    ) {
        enum class SlotType {
            BANNER, MENU, CATEGORY, GOODS, DEAL, TAB, KEYWORD;
        }

        data class Banner(
            val pcImageUrl: String,
            val mobileImageUrl: String,
            val landingUrl: String?,
            val title: String?,
            val subtitle: String?,
            val badgeText: String?,
            val startAt: String?,
            val endAt: String?,
        ) {
            fun isActive(baseTime: ZonedDateTime): Boolean {
                val startTime = startAt?.toZonedDateTime()
                val endTime = endAt?.toZonedDateTime()
                return (startTime == null || startTime < baseTime) && (endTime == null || endTime >= baseTime)
            }
        }

        data class Menu(
            val imageUrl: String?,
            val landingUrl: String,
            val name: String,
            val badgeText: String?,
        )

        data class Category(
            val imageUrl: String?,
            val landingUrl: String,
            val name: String,
            val categoryCode: String?,
            val textColor: String,
            val bgColor: String,
        )

        data class Goods(
            val type: SlotType = SlotType.GOODS,
            override val id: String?,
        ) : StoreItem

        data class Deal(
            val type: SlotType = SlotType.DEAL,
            override val id: String?,
        ) : StoreItem

        data class Tab(
            val name: String,
            val landingUrl: String?,
        )

        data class Keyword(
            val name: String,
            val landingUrl: String?,
            val rank: Int,
            val diffRank: Int,
            val new: Boolean,
        )

        @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
        )
        @JsonSubTypes(
            JsonSubTypes.Type(value = Goods::class, name = "GOODS"),
            JsonSubTypes.Type(value = Deal::class, name = "DEAL"),
        )
        interface StoreItem {
            val id: String?
        }
    }
}
