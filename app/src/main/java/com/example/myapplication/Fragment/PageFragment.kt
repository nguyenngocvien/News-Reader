package com.example.myapplication.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.myapplication.Adapter.NewpaperAdapter
import com.example.myapplication.Interface.NewpaperItemClickListener
import com.example.myapplication.Data.FakeService
import com.example.myapplication.Model.Newpaper
import com.example.myapplication.Model.Result
import com.example.myapplication.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_page.*
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class PageFragment : Fragment() {

    companion object {

        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    interface Listener {
        fun openDetailNewspaperScreen(newspaper: Newpaper)
        fun playMediaWith(newspaper: Newpaper, position:Int)
        fun pauseMedia(position:Int)
    }

    lateinit var mListener:PageFragment.Listener
    private var newpapers:ArrayList<Newpaper> = ArrayList()
    lateinit var newspaperAdapter: NewpaperAdapter
    private var mPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments!!.getInt(ARG_PAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData().execute("https://baomoi.com/")
    }

    inner class getData : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?) : String {
            var content: StringBuilder = StringBuilder()
            val url: URL = URL(params[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

            var line:String = ""
            try {
                do {
                    line = bufferedReader.readLine()
                    if(line != null){
                        content.append(line)
                    }
                }while (line != null)
                bufferedReader.close()
            } catch (e: Exception){
                Log.d("AAA", e.toString())
            }
            return content.toString() // tra ve noi dung web
        }

        // tien hanh boc tach noi dung web sau do dua vao recycleview
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val doc = Jsoup.parse(result)
            doc.select("div[class=story]")
                .forEach { element ->
                    val title = element.select("h4[class=story__heading]").select("a").text()
                    val linkImage = element.select("img").attr("src")
                    val linkNews = element.select("h4[class=story__heading]").attr("href")
                    val additionalInfo = element.select("a[class=source]").text()

                    newpapers.add(Newpaper(title, linkImage, linkNews, additionalInfo)) //add thong tin vao danh sach news
                }

            setUpRecycleView() // show news
        }
    }

    private fun setUpRecycleView(){
        /*newpapers = when (mPage) {
            0-> { val json = FakeService.getNewspaperSecondRaw()
                val result = Gson().fromJson(json, Result::class.java)
                result.results
            }
            1-> { val json = FakeService.getNewspaperFirstRaw()
                val result = Gson().fromJson(json, Result::class.java)
                result.results
            }
            else -> {
                val json = FakeService.getNewspaperSecondRaw()
                val result = Gson().fromJson(json, Result::class.java)
                result.results
            }
        }*/

        rvNewspaper.layoutManager = LinearLayoutManager(context as Context)
        newspaperAdapter = NewpaperAdapter(newpapers, context as Context)

        rvNewspaper.adapter = newspaperAdapter
        newspaperAdapter.setListener(newspaperItemClickListener)
    }

    private val newspaperItemClickListener = object : NewpaperItemClickListener {
        override fun onItemCLicked(position: Int) {
            mListener.openDetailNewspaperScreen(newpapers[position])
        }

        override fun onItemPlayMedia(position: Int) {
            mListener.playMediaWith(newpapers[position],position)
        }

        override fun onItemPauseMedia(position: Int) {
            mListener.pauseMedia(position)
        }
    }

    fun setListener(listener: PageFragment.Listener) {
        mListener = listener
    }
}