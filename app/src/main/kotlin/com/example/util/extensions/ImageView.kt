package com.example.util.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import org.koin.core.KoinComponent
import org.koin.core.inject

object GlideInstance : KoinComponent {
    val glide: RequestManager by inject()
}

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imageUrl: String?) {
    imageUrl?.let { url ->
        GlideInstance.glide
            .load(url)
            .into(this)
    }
}
