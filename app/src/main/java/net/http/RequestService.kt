package net.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RequestService {

    @GET("streetview/feed/gallery/data.json")
    fun getData(): Call<ResponseBody>

    @GET("streetview/feed/gallery/collection/{key}.json")
    fun getSmallData(@Path("key") key: String): Call<ResponseBody>
}