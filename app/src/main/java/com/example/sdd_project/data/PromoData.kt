package com.example.sdd_project.data

data class PromoData(
    val data: List<PromoItem>,
    val meta: Meta
)

data class PromoItem(
    val id: Int,
    val attributes: PromoAttributes
)

data class PromoAttributes(
    val title: String?,
    val count: Int,
    val alt: Int,
    val desc: String,
    val desc_promo: String?,
    val latitude: String,
    val longitude: String,
    val lokasi: String,
    val nama: String,
    val name_promo: String?,
    val createdAt: String,
    val updatedAt: String
)

data class Meta(
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val pageSize: Int,
    val pageCount: Int,
    val total: Int
)