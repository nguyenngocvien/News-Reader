package com.example.myapplication.Activity

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Model.Newpaper
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_detail_newspaper.*
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DetailNewspaperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_newspaper)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "Tin Chi tiết"
        getDataNewspaper()
    }

    private fun getDataNewspaper() {
        val data= intent.extras
        if (data != null) {
            val newspaper = data.getParcelable(NEWSPAPER_KEY) as Newpaper
            val linkNews = newspaper.backdrop_path
            GetData().execute("https://tuoitre.vn$linkNews")
        }
    }

    inner class GetData : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            var content: StringBuilder = StringBuilder()
            val url: URL = URL(params[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

            var line: String? = ""
            try {
                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        content.append(line)
                    }
                } while (line != null)
                bufferedReader.close()
            } catch (e: Exception) {
                Log.d("AAA", e.toString())
            }
            return content.toString() // tra ve noi dung web
        }
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val doc = Jsoup.parse(result)
            val element = doc.select("div[class=content-detail column-2]")
            val title = element.select("h1[class=article-title]").text()
            val time = element.select("div[class=date-time]").text()
            val pic = element.select("img").attr("src")
            val overview = element.select("h2[class=sapo]").text()
            val content: StringBuilder = StringBuilder()
            element.select("p").forEach { item ->
                val line = item.select("p").text()
                content.append("$line\n\n")
            }

            //setup view
            tvTitle.text = title //load title
            tvTime.text = time //load time
            tvOverview.text = overview //load overview
            //load image
            Glide.with(this@DetailNewspaperActivity)
                .load(pic)
                .placeholder(R.drawable.ic_launcher_background)
                .apply(RequestOptions.fitCenterTransform())
                .into(imageNews)
            tvContent.text = content //get content
        }
    }
}
