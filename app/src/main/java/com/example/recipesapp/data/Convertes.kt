package com.example.recipesapp.data

import androidx.room.TypeConverter
import com.example.recipesapp.model.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromIngredientList(list: List<Ingredient>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return Gson().fromJson(value, type)
    }
}