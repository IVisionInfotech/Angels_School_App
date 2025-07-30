package com.ivision.utils;

public interface ClickListener {
    default void onItemSelected(int position) {

    }

    default void onItemSelected(String str, String str2, String str3) {

    }
}