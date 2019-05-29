package com.example.myapplication.Data

import android.util.Log
import com.example.myapplication.Model.ResultFPT
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

const val URL = "http://api.openfpt.vn/text2speech/v4"
const val API_KEY = "50c2d99878874c42a30904b35c05b9ff"

class ApiService {

    companion object {

        val client = OkHttpClient()

        fun postNewspaper(content:String, completion: (String?) -> Unit) {

            val request = Request.Builder()
                .url(URL)
                .header("api_key",API_KEY)
                .addHeader("voice","male")
                .post(RequestBody.create(null,content))
                .build()

            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("Result",e.toString())
                        completion(null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = response.body()!!.string()
                        val result = Gson().fromJson(json, ResultFPT::class.java)
                        completion(result.async)
                    }
                })
        }

    }
}