package com.dev.meliapp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun hideKeyboard(activity: Activity) {
    val view: View = activity.findViewById(android.R.id.content)
    view?.let {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .centerCrop()
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

fun alertDialog(context: Context, text: String, onAcceptClick: DialogInterface.OnClickListener){
    val builder = AlertDialog.Builder(context)
    builder.setMessage(text)
    builder.setPositiveButton("Aceptar", onAcceptClick)
    builder.setCancelable(false)
    builder.show()
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

