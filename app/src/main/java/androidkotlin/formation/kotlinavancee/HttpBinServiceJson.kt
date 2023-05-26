package androidkotlin.formation.kotlinavancee

import retrofit2.Call
import retrofit2.http.GET

interface HttpBinServiceJson {

    @GET("get")
    fun getInfoUser(): Call<GetData>
}