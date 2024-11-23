package com.example.kursnewsapp.db

import androidx.room.TypeConverter
import com.example.kursnewsapp.models.Source


class Converters {

    @TypeConverter
     fun convertFromSource(source: Source):String{
         return source.name
     }

    @TypeConverter
    fun convertToSource(name:String):Source{
        return Source(name,name)
    }
}