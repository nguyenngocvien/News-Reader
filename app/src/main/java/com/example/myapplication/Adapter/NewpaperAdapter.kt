package com.example.myapplication.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.Interface.NewpaperItemClickListener
import com.example.myapplication.Model.Newpaper
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_newpaper.view.*

var tmpViewHolder: NewspaperViewHolder? = null

class NewpaperAdapter(var items: ArrayList<Newpaper>, val context: Context) : RecyclerView.Adapter<NewspaperViewHolder>() {

    lateinit var mListener: NewpaperItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NewspaperViewHolder {
        return NewspaperViewHolder(LayoutInflater.from(context).inflate(R.layout.item_newpaper, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(newspaperViewHolder: NewspaperViewHolder, position: Int) {
        Glide.with(context)
            .load(items[position].poster_path)
            .placeholder(R.drawable.ic_launcher_background)
            .into(newspaperViewHolder.imageMovie)

        newspaperViewHolder.tvTitle.text = items[position].title
        newspaperViewHolder.tvOverview.text = items[position].overview

        newspaperViewHolder.itemView.setOnClickListener {
            mListener.onItemCLicked(position)
        }

        newspaperViewHolder.btn_Play_Item.setOnClickListener {
            if (tmpViewHolder != null && tmpViewHolder != newspaperViewHolder) {
                tmpViewHolder!!.btn_Play_Item.setImageResource(android.R.drawable.ic_media_play)
                tmpViewHolder!!.statePlay = false
            }

            if (newspaperViewHolder.statePlay) {
                newspaperViewHolder.btn_Play_Item.setImageResource(android.R.drawable.ic_media_play)
                newspaperViewHolder.statePlay = false
                mListener.onItemPauseMedia(position)
            } else {
                newspaperViewHolder.btn_Play_Item.setImageResource(android.R.drawable.ic_media_pause)
                newspaperViewHolder.statePlay = true
                tmpViewHolder = newspaperViewHolder
                mListener.onItemPlayMedia(position)
            }
        }
    }

    fun setListener(listener: NewpaperItemClickListener) {
        this.mListener = listener
    }

    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }

    fun addAll(movies: ArrayList<Newpaper>){
        items.addAll(movies)
        notifyDataSetChanged()
    }
}

class NewspaperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageMovie = view.imageNewspaper
    var tvTitle = view.tvTitle
    var tvOverview = view.tvOverview
    var btn_Play_Item = view.btn_Play_Item
    var statePlay = false
}