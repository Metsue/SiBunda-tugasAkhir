package com.example.sibunda.core.utils

object Constants {
    const val DATABASE_NAME = "sibunda_db"
    const val PREFS_NAME = "sibunda_prefs"

    const val KEY_NAMA = "KEY_NAMA"
    const val KEY_NIK = "KEY_NIK"
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_PASSWORD = "KEY_PASSWORD"
    const val KEY_TELEPON = "KEY_TELEPON"
    const val KEY_ALAMAT = "KEY_ALAMAT"
    const val KEY_FOTO_PROFILE = "KEY_FOTO_PROFILE"
    const val KEY_IS_LOGIN = "KEY_IS_LOGIN"

    const val KEY_APP_THEME = "KEY_APP_THEME"

    const val KEY_NOTIF_ENABLED = "KEY_NOTIF_ENABLED"
    const val KEY_NOTIF_HOUR = "KEY_NOTIF_HOUR"
    const val KEY_NOTIF_MINUTE = "KEY_NOTIF_MINUTE"
    const val KEY_NOTIF_DAY_BEFORE = "KEY_NOTIF_DAY_BEFORE"

    fun accountKey(email: String, field: String): String {
        return "ACCOUNT_${email.trim().lowercase()}_$field"
    }
}