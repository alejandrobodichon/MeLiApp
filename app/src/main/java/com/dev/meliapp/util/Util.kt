package com.dev.meliapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.meliapp.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

const val ARS = "ARS"
const val USD = "USD"

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?){
    view.loadImage(url, getProgressDrawable(view.context))
}

fun String.stringToPrice(currency: String?): String? {
    val syms = DecimalFormatSymbols(Locale.GERMANY)
    syms.decimalSeparator = ','
    syms.groupingSeparator = '.'
    var currencyFormat = ""
    currency?.let {
        currencyFormat =
            when (currency) {
                ARS -> "$"
                USD -> "U\$S"
                else -> currency
            }
    }
    val myFormatter = DecimalFormat("$currencyFormat ###,###.00", syms)
    return convertNumberToPrice(this, myFormatter)
}

private fun convertNumberToPrice(_sValue: String, myFormatter: DecimalFormat): String? {
    var _sValue: String? = _sValue
    if (_sValue == null || !isDouble(_sValue)) {
        return "0,00"
    }
    var sZero = ""
    if (_sValue.toDouble() < 1 && _sValue.toDouble() >= 0) {
        sZero = "0"
    } else if (_sValue.toDouble() < 0 && _sValue.toDouble() >= -1 && _sValue.toDouble() < 0) {
        sZero = "- 0"
        _sValue = _sValue.substring(1)
    }
    return sZero + myFormatter.format(_sValue.toDouble())
}

private fun isDouble(_sValue: String): Boolean {
    return try {
        _sValue.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

