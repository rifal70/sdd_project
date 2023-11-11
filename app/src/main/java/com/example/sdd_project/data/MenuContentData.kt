package com.example.sdd_project.data

import androidx.annotation.DrawableRes

data class BottomMenuContent(
    val title: String,
    @DrawableRes val iconId: Int
)
data class RecyclerMenuContent(
    val title: String,
    @DrawableRes val iconId: Int
)