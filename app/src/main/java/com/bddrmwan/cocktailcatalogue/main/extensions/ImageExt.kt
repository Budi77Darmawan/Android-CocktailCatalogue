package com.bddrmwan.cocktailcatalogue.main.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bddrmwan.cocktailcatalogue.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable? = null) {
    val option = RequestOptions()
        .placeholder(progressDrawable)
//        .error(R.drawable.)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(uri)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressDrawable?.stop()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressDrawable?.stop()
                return false
            }
        })
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

fun ImageView.loadImage(drawable: Int) {
    Glide.with(this)
        .load(drawable)
        .into(this)
}

fun getProgressDrawable(mContext: Context): CircularProgressDrawable {
    return CircularProgressDrawable(mContext).apply {
        strokeWidth = 10f
        centerRadius = 50f
        setColorSchemeColors(ContextCompat.getColor(mContext, R.color.purple_200))
        start()
    }
}