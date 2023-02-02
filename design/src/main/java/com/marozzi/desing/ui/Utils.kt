package com.marozzi.desing.ui

import android.content.ContentResolver
import android.content.Context
import android.net.Uri

fun resourceUri(context: Context, resourceId: Int): Uri {
    return with(context.resources) {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getResourcePackageName(resourceId))
            .appendPath(getResourceTypeName(resourceId))
            .appendPath(getResourceEntryName(resourceId))
            .build()
    }
}