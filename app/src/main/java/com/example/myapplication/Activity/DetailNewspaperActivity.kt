package com.example.myapplication.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.Model.Newpaper
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_detail_newspaper.*

class DetailNewspaperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_newspaper)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "Tin Chi tiết"
        getDataNewspaper()
    }

    private fun getDataNewspaper() {
        val data = intent.extras
        if (data != null) {
            val newspaper = data.getParcelable(NEWSPAPER_KEY) as Newpaper
            setUpView(newspaper)
        }
    }

    private fun setUpView(newspaper: Newpaper){
        tvTitle.text = newspaper.title
        tvContent.text = newspaper.overview
    }
}
