package com.ronny.scrollableimagegrid.domain.model

data class ImageDataItem(
    val backupDetails: BackupDetails? = null,
    val coverageURL: String = "",
    val id: String = "",
    val language: String = "",
    val mediaType: Int = 0,
    val publishedAt: String = "",
    val publishedBy: String = "",
    val thumbnail: Thumbnail = Thumbnail(),
    val title: String = ""
)