package com.example.mediaplayer

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.fragment.app.Fragment
import android.provider.MediaStore
import android.provider.OpenableColumns


open class PageFragment(public var file: String) : Fragment() {
    open lateinit var mode : String

    open fun getFileNameFromURI(contentUri: Uri): String? {
        var result: String? = null
        if (contentUri.scheme.equals("content")) {
            var cursor: Cursor? =
                context?.contentResolver?.query(contentUri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result =
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close();
            }
        }
        if (result == null) {
            result = contentUri.path
            var cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result
    }
}