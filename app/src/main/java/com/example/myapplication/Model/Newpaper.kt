package com.example.myapplication.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result( val results:ArrayList<Newpaper>) : Parcelable

@Parcelize
data class Newpaper( val title:String,
                  val poster_path:String,
                  val backdrop_path:String,
                  val overview:String) : Parcelable