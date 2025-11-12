package com.example.security

import java.security.MessageDigest

fun md5(data: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(data.toByteArray())
    return digest.toHexString()
}