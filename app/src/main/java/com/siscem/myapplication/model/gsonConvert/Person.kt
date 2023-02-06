package com.siscem.myapplication.model.gsonConvert

import com.google.gson.annotations.SerializedName
import com.siscem.myapplication.model.People


data class Person(
    val persons: List<People>
)