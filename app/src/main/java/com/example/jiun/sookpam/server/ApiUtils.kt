package com.example.jiun.sookpam.server

class ApiUtils {
    companion object {
        const val BASE_URL = "http://ec2-52-79-187-67.ap-northeast-2.compute.amazonaws.com/"

        fun getRecordService():RecordService {
            return RetrofitClient.getClient(BASE_URL).create(RecordService::class.java)
        }
    }
}
