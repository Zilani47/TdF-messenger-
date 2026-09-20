package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.MediaType
import com.example.data.model.PlatformType

class Converters {
    @TypeConverter
    fun fromPlatformType(platform: PlatformType?): String? = platform?.name

    @TypeConverter
    fun toPlatformType(name: String?): PlatformType? =
        name?.let { runCatching { PlatformType.valueOf(it) }.getOrDefault(PlatformType.MESSENGER) }

    @TypeConverter
    fun fromMediaType(mediaType: MediaType?): String? = mediaType?.name

    @TypeConverter
    fun toMediaType(name: String?): MediaType? =
        name?.let { runCatching { MediaType.valueOf(it) }.getOrDefault(MediaType.NONE) }
}
