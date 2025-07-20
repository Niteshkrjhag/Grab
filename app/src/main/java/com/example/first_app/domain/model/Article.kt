package com.example.first_app.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity    // this will create a table in room database and then we need to create a interface
            // called data access object (dao)
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
   @PrimaryKey val url: String,
    val urlToImage: String
):Parcelable

// In databases we can only save primitive datatypes like strings, integers,etc but we cannot
// save an object like Source so we need to convert it into a primitive datatype.
// for this we need to create a type converter
