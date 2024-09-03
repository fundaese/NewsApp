package com.funda.newsapp.common

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

//publish date
fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) {
        return "Bilinmiyor"
    }

    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date? = originalFormat.parse(dateString)

        val targetFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        date?.let { targetFormat.format(it) } ?: "Bilinmiyor"
    } catch (e: Exception) {
        "Bilinmiyor"
    }
}




