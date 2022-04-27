package dev.begli.exchangerate.utils

import android.annotation.SuppressLint
import android.app.Activity
import dev.begli.exchangerate.R
import org.aviran.cookiebar2.CookieBar


class CookieBarNotify (private val activity: Activity){
    @SuppressLint("ResourceType")
    fun success(message: String){
        CookieBar.build(activity)
            .setTitle("Success")
            .setTitleColor(R.color.white)
            .setBackgroundColor(R.color.apple_green)
            .setIcon(R.drawable.ic_baseline_check_circle_24_white)
            .setMessage(message)
            .setDuration(2000) // 5 seconds
            .show()
    }

    fun error(message: String){
        CookieBar.build(activity)
            .setTitle("Error")
            .setTitleColor(R.color.white)
            .setBackgroundColor(R.color.apple_red)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage(message)
            .setDuration(2000) // 5 seconds
            .show()
    }

    fun unexpectedError(){
        CookieBar.build(activity)
            .setTitle("Error")
            .setTitleColor(R.color.white)
            .setBackgroundColor(R.color.apple_red)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage("Unexpected error")
            .setDuration(2000) // 5 seconds
            .show()
    }

    fun warning(title: String, message: String){
        CookieBar.build(activity)
            .setTitle(title)
            .setTitleColor(R.color.white)
            .setBackgroundColor(R.color.apple_orange)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage(message)
            .setDuration(2000) // 5 seconds
            .show()
    }

    fun warning(message: String){
        CookieBar.build(activity)
            .setTitle("Warning")
            .setTitleColor(R.color.white)
            .setBackgroundColor(R.color.apple_orange)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage(message)
            .setDuration(2000) // 5 seconds
            .show()
    }



}