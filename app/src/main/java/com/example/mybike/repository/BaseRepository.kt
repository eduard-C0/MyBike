package com.example.mybike.repository

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import com.example.mybike.localdatasource.sharedpreferences.MyBikeSharedPreferences
import javax.inject.Inject

class BaseRepository @Inject constructor(private val myBikeSharedPreferences: MyBikeSharedPreferences, private val myBikeDataBase: MyBikeDataBase) {

}