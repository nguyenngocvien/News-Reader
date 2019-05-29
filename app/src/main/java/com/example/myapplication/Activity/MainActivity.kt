package com.example.myapplication.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.myapplication.Adapter.PageFragmentAdapter
import com.example.myapplication.Data.ApiService
import com.example.myapplication.Fragment.PageFragment
import com.example.myapplication.Model.Newpaper
import com.example.myapplication.R
import com.example.myapplication.Utility.MediaPlay
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.play_newspaper_layout.*
import java.text.FieldPosition

const val NEWSPAPER_KEY = "NEWSPAPER_KEY"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PageFragment.Listener {

    private var positionTmp:Int? = null
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        viewpager.adapter =
            PageFragmentAdapter(supportFragmentManager, this@MainActivity)
        sliding_tabs.setupWithViewPager(viewpager)

        setUpViewControlPlayNewspaper()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is PageFragment) {
            fragment.setListener(this)
        }
    }

    override fun openDetailNewspaperScreen(newspaper: Newpaper) {
        Log.e("",newspaper.overview)
        val intent = Intent(this@MainActivity,DetailNewspaperActivity::class.java)
        intent.putExtra(NEWSPAPER_KEY, newspaper)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {

            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initSeekBar(){
        //SEEK BAR
        runnable = Runnable {
            tv_TimeEnd.text = "${MediaPlay.mediaPlayer.duration/1000}s"
            seek_bar.max = MediaPlay.mediaPlayer.duration
            tv_TimeBegin.text = "${MediaPlay.mediaPlayer.currentPosition/1000}s"
            var prog = MediaPlay.mediaPlayer.currentPosition
            seek_bar.progress = prog
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun setUpViewControlPlayNewspaper(){


        //BUTTON PLAY
        MediaPlay.mediaPlayer.setOnCompletionListener {
            btn_Play.setImageResource(android.R.drawable.ic_media_play)
        }

        btn_Play.setImageResource(android.R.drawable.ic_media_play)
        btn_Play.setOnClickListener {
            if (positionTmp != null) {
                if (!MediaPlay.mediaPlayer.isPlaying) {
                    btn_Play.setImageResource(android.R.drawable.ic_media_pause)
                    MediaPlay.instance.restartMedia()
                } else {
                    btn_Play.setImageResource(android.R.drawable.ic_media_play)
                    MediaPlay.instance.pauseMedia()
                }
            }
        }

        //BUTTON NEXT
    }

    override fun playMediaWith(newspaper: Newpaper,position: Int) {
        if (positionTmp == position) {
            btn_Play.setImageResource(android.R.drawable.ic_media_pause)
            MediaPlay.instance.restartMedia()
        } else {
            tv_Name.text = newspaper.title
            ApiService.postNewspaper(newspaper.title + "  " + newspaper.overview,completion = {
                if (it != null) {
                    positionTmp = position
                    MediaPlay.instance.playMedia(it)
                    initSeekBar()
                    btn_Play.setImageResource(android.R.drawable.ic_media_pause)
                } else {
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun pauseMedia(position: Int) {
        handler.removeCallbacks(runnable)
        MediaPlay.instance.pauseMedia()
        btn_Play.setImageResource(android.R.drawable.ic_media_play)
    }

}
