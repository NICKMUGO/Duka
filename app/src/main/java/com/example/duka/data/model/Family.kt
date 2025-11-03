package com.example.duka.data.model

//holds data regarding the famil. basically a blueprint for a family object
data class Family(
    val id: String,
    val name: String,
    val memberCount: Int = 1,
    val isOwner: Boolean = false
)