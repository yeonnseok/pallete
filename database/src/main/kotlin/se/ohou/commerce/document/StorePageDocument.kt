package se.ohou.commerce.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "StorePage")
data class StorePageDocument(
    @Id
    val id: ObjectId? = null,

    val pageType: PageType,

    val modules: ModuleGroup,

    val createdAt: ZonedDateTime?,

    val updatedAt: ZonedDateTime?,
) {

    enum class PageType {
        SHOPPING_HOME,
        ;
    }

    data class ModuleGroup(
        val aGroup: List<ModuleDocument<*>>,
        val bGroup: List<ModuleDocument<*>>,
    )

    fun getAbFilteredModuleList(abGroup: String): List<ModuleDocument<*>> {
        return if (abGroup == "B") {
            modules.bGroup
        } else {
            modules.aGroup
        }
    }
}
