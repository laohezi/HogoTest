import com.example.app1.detail.optStringList
import org.json.JSONObject

object SeriesPcuModel {
    fun jsonToCategory(catJson: JSONObject): SeriesPcuCategoryItem {
        val category = SeriesPcuCategoryItem()
        category.apply {
            json = catJson
            val list = catJson.optJSONArray("list")
            if (list != null && list.length() > 0) {
                for (i in 0 until list.length()) {
                    childs.add(
                        jsonToCategory(list.getJSONObject(i)).apply {
                            parent = category
                        }
                    )
                }
            }
            id = catJson.optString("id")
            title = catJson.optString("title")
            price = catJson.optString("price")
            sort = catJson.optInt("sort")
            childType = catJson.optString("child_type")
            childTypeName = catJson.optString("child_type_name")
            productInfo = catJson.optString("product_info")
            val jsonArray = catJson.optJSONArray("thumbnails")
            jsonArray?.let {
                for (i in 0 until jsonArray.length()) {
                    jsonArray.optJSONObject(i)?.optString("url")?.let { url ->
                        thumbnails.add(url)
                    }
                }
            }
            /*if (thumbnails.isNullOrEmpty()) {
                thumbnails = catJson.optStringList("thumbnail") as MutableList<String>
            }*/
            skuCode = catJson.optString("size")
            typeIcon = catJson.optString("type_icon")
            typeName = catJson.optString("type_name")
            groupId = catJson.optString("group_id")
            isSaleInt = catJson.optInt("is_sale")
        }
        return category

    }
}


open class SeriesPcuCategoryItem {
    lateinit var json: JSONObject
    var thumbnails: MutableList<String> = ArrayList()
    var id: String? = null
    var title: String? = null
    var sort: Int = -1
    var price: String? = null
    var selected: Boolean = true
    var selectable: Boolean = false
    var childType: String = ""
    var childTypeName: String = ""
    var productInfo: String? = null
    var childs: MutableList<SeriesPcuCategoryItem> = ArrayList()
    var parent: SeriesPcuCategoryItem? = null
    var skuCode: String? = null
    var typeIcon: String? = null
    var typeName: String? = null
    var groupId: String? = null
    var isSaleInt: Int = -1

    fun clearStateRecursive() {
        childs.forEach {
            it.selected = false
            it.clearStateRecursive()
        }
    }

    fun isSale(): Boolean {
        return isSaleInt == 1
    }

}