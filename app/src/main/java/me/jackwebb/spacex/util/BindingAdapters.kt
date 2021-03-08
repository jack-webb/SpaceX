package me.jackwebb.spacex.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("loadUrl")
fun ImageView.loadFromUrl(url: String?) {
    this.load(url)
}