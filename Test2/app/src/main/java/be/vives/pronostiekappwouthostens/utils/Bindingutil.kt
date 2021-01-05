package be.vives.pronostiekappwouthostens.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import be.vives.pronostiekappwouthostens.R
import be.vives.pronostiekappwouthostens.classes.Matches
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(this)
    }
}

@BindingAdapter("resultPronostiek")
fun TextView.setStatus(item: Matches) {
    var result = "Pronostiek niet ingevuld"
    if (item.pronostiekUser != null) {
        if (item.pronostiekUser == item.pronostiekresultaat) {
            result = "Correct!"
        } else {
            result = "Niet correct!"
        }
    }
    text = result
}