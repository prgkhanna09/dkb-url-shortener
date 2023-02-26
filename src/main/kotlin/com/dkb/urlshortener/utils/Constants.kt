package com.dkb.urlshortener.utils

object Constants {
    // keeping this here, as in db we store the hash
    // we can also read this at service level based on profile from the application-{profile}.properties
    const val DOMAIN = "http://www.dkb.com/"
}
