package com.example.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;




public class SharedPreferenceManager {
    private static SharedPreferences sSharedPreferences;

    private static final SharedPreferenceManager sharedPrefManagerInstance = new SharedPreferenceManager();

    public static SharedPreferenceManager getInstance(Context context) {
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefManagerInstance;
    }

    private SharedPreferenceManager() {
    }

    private void storeStringInSharedPreferences(String key, String content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key, content);
        editor.apply();
    }

    private void storeIntInSharedPreferences(String key, int content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(key, content);
        editor.apply();
    }

    private void storeFloatInSharedPreferences(String key, Float content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putFloat(key, content);
        editor.apply();
    }

    private void storeBooleanInSharedPreferences(String key, boolean status) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(key, status);
        editor.apply();
    }

    private long getLongFromSharedPreferences(String key) {
        return sSharedPreferences.getLong(key, 0L);
    }

    private int getIntFromSharedPreferences(String key) {
        return sSharedPreferences.getInt(key, 0);
    }

    private float getFloatFromSharedPreferences(String key) {
        return sSharedPreferences.getFloat(key, 0.0F);
    }

    private String getStringFromSharedPreferences(String key) {
        return sSharedPreferences.getString(key, "");
    }

    private boolean getBooleanFromSharedPreferences(String key) {
        return sSharedPreferences.getBoolean(key, false);
    }

    private static final String LATITUDE = "latitude";
    private static final String Radius = "radius";

    public void setLatitude(double latitude){
        storeFloatInSharedPreferences(LATITUDE, (float) latitude);
    }

    public void setRadius(int radius){
        storeIntInSharedPreferences(Radius, radius);
    }

    public  int getRadius() {
        return getIntFromSharedPreferences(Radius);
    }

    public double getLatitude(){
        return getFloatFromSharedPreferences(LATITUDE);
    }

    private static final String LONGITUDE = "longitude";

    public void setLongitude(double longitude){
        storeFloatInSharedPreferences(LONGITUDE, (float) longitude);
    }

    public Float getLongitude(){
        return getFloatFromSharedPreferences(LONGITUDE);
    }

}
