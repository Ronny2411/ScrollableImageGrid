package com.ronny.scrollableimagegrid.domain.model

data class Thumbnail(
    val aspectRatio: Int = 0,
    val basePath: String = "",
    val domain: String = "",
    val id: String = "",
    val key: String = "",
    val qualities: List<Int> = listOf(),
    val version: Int = 0
)