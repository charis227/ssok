package com.example.jiun.sookpam.server

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecordService {
    @GET("{category}/{division}")
    fun getRecords(
            @Path("category") category: String,
            @Path("division") division: String
    ): Call<List<RecordResponse>>

    @GET("recommend/student_grade={student_grade}&student_year={student_year}&major1={major1}&major2={major2}&school_scholar={school_scholar}&government_scholar={government_scholar}&external_scholar={external_scholar}&student_status={student_status}&interest_scholarship={interest_scholarship}&interest_academic={interest_academic}&interest_entrance={interest_entrance}&interest_recruit={interest_recruit}&interest_system={interest_system}&interest_global={interest_global}&interest_career={interest_career}&interest_student={interest_student}")
    fun getRecommendRecords(
            @Path("student_grade") studentGrade: Int,
            @Path("student_year") studentYear: Int,
            @Path("major1") major1: String,
            @Path("major2") major2: String,
            @Path("school_scholar") schoolScholar: Boolean,
            @Path("government_scholar") governmentScholar: Boolean,
            @Path("external_scholar") externalScholarship: Boolean,
            @Path("student_status") studentStatus: Boolean,
            @Path("interest_scholarship") interestScholarship: Int,
            @Path("interest_academic") interestAcademic: Int,
            @Path("interest_entrance") interestEntrance: Int,
            @Path("interest_recruit") interestRecruit: Int,
            @Path("interest_system") interestSystem: Int,
            @Path("interest_global") interestGlobal: Int,
            @Path("interest_career") interestCareer: Int,
            @Path("interest_student") interestStudent: Int
    ): Call<List<RecordResponse>>
}
