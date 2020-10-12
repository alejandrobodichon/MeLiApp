package com.dev.meliapp.model

import android.os.Parcel
import android.os.Parcelable
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
                        val attributes: List<Attribute>? = ArrayList()
): Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Attribute)
    )

    fun getFormattedPrice(): String {
        return price.toString().stringToPrice(currencyId) ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeValue(price)
        parcel.writeString(availableQuantity)
        parcel.writeString(soldQuantity)
        parcel.writeString(thumbnail)
        parcel.writeString(currencyId)
        parcel.writeTypedList(attributes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }
}


data class Attribute(val name: String? ="",
                     val value_name: String? =""): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attribute> {
        override fun createFromParcel(parcel: Parcel): Attribute {
            return Attribute(parcel)
        }

        override fun newArray(size: Int): Array<Attribute?> {
            return arrayOfNulls(size)
        }
    }
}

data class ApiResult(val results: List<ProductModel>,
                     val query: String)