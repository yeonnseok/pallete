package se.ohou.commerce.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import se.ohou.commerce.document.Content.Slot.Banner
import se.ohou.commerce.document.Content.Slot.Category
import se.ohou.commerce.document.Content.Slot.Keyword
import se.ohou.commerce.document.Content.Slot.Menu
import se.ohou.commerce.document.Content.Slot.StoreItem
import se.ohou.commerce.document.Content.Slot.Tab
import java.time.ZonedDateTime

@Document(collection = "Module")
data class ModuleDocument<T>(
    @Id
    val id: ObjectId? = null,

    val componentType: ComponentType,

    val component: T,

    val createdAt: ZonedDateTime?,

    val updatedAt: ZonedDateTime?,
) {
    var additionalInfo: AdditionalInfo? = null

    // Module 단건 조회시에는 addtionalInfo가 없기 때문에 true를 반환합니다.
    fun isActive(baseTime: ZonedDateTime): Boolean =
        additionalInfo?.let {
            (it.startAt == null || baseTime >= it.startAt) && (it.endAt == null || baseTime <= it.endAt)
        } ?: true

    enum class ComponentType {
        BANNER_GROUP,
        MENU_GRID,
        CATEGORY_CAROUSEL,
        ITEM_CAROUSEL,
        TAB_ITEM_CAROUSEL,
        BANNER_CAROUSEL,
        BANNER_ITEM_CAROUSEL,
        ITEM_CAROUSEL_ITEM_GRID,
        KEYWORD_GRID,
        ITEM_LIST
        ;
    }

    data class AdditionalInfo(
        val lazyLoading: Boolean,
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val divider: Divider?,
    ) {
        data class Divider(
            val top: Boolean,
            val bottom: Boolean,
            val color: String,
            val height: Float,
            val inset: Float,
        )
    }
}

data class BannerGroup(
    val banner: Content<Banner>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val mobileBannerRatio: Float?,
        val pcBannerRatio: Float?,
    )
}

data class MenuGrid(
    val menu: Content<Menu>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
    )
}

data class CategoryCarousel(
    val category: Content<Category>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
    )
}

data class ItemCarousel(
    val item: Content<StoreItem>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
        val moreInfo: MoreInfo?,
        val advertiseModule: Boolean,
    )
}

data class TabItemCarousel(
    val tab: Content<Tab>,
    val item: Content<StoreItem>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
        val advertiseModule: Boolean,
        val showReview: Boolean,
        val showBadge: Boolean,
    )
}

data class BannerCarousel(
    val banner: Content<Banner>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
        val mobileBannerRatio: Float?,
        val pcBannerRatio: Float?,
    )
}

data class BannerItemCarousel(
    val banner: Content<Banner>,
    val item: Content<StoreItem>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
        val mobileBannerRatio: Float?,
        val pcBannerRatio: Float?,
        val showPrice: Boolean,
    )
}

data class ItemCarouselItemGrid(
    val item: Content<StoreItem>,
    val item2: Content<StoreItem>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
    )
}

data class KeywordGrid(
    val keyword: Content<Keyword>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
    )
}

data class ItemList(
    val item: Content<StoreItem>,
    val detail: Detail,
) {
    data class Detail(
        val startAt: ZonedDateTime?,
        val endAt: ZonedDateTime?,
        val title: String?,
    )
}

data class MoreInfo(
    val landingUrl: String?,
    val text: String?,
    val commonLanding: Boolean,
)
