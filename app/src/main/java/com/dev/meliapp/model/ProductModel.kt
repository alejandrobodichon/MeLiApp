package com.dev.meliapp.model

import com.dev.meliapp.util.stringToPrice
import com.google.gson.annotations.SerializedName

data class ProductModel(val id: String? = "",
                        val title: String? ="",
                        val price: Float? =0f,
                        @SerializedName("available_quantity")
                        val availableQuantity: String? ="",
                        @SerializedName("sold_quantity")
                        val soldQuantity: String? ="",
                        val thumbnail: String? ="",
                        @SerializedName("currency_id")
                        val currencyId: String? ="",
                        val condition: String? = "",
                        val pictures: List<PicturerModel>? = ArrayList(),
                        val attributes: List<Attribute>? = ArrayList()
) {
    fun getFormattedPrice(): String {
        return price.toString().stringToPrice(currencyId) ?: ""
    }
}


data class Attribute(val name: String? ="",
                     @SerializedName("value_name")
                     val valueName: String? ="")


data class PicturerModel(val id: String? = "",
                         @SerializedName("secure_url")
                         val url: String? = ""
)


data class ApiResult(val results: List<ProductModel>,
                     val query: String)