package com.lifu.aadtop20.service;


import com.lifu.aadtop20.Submission;
import com.lifu.aadtop20.dto.HoursDTO;
import com.lifu.aadtop20.dto.SkillIqDTO;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ScoreService {
    @GET("scores/iq")
    Call<List<SkillIqDTO>> getScoresIQ();

    @GET("scores/hours")
    Call<List<HoursDTO>> getScoresHours();

    /*@POST("ideas")
    Call<SkillIqDTO> createIdea(@Body SkillIqDTO newIdea);*/

    @FormUrlEncoded
    @PUT("1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse")
    Call<Submission> projectSubmission(
            @Field("entry.1877115667") String firstName,
            @Field("entry.2006916086") String lastName,
            @Field("entry.1824927963") String email,
            @Field("entry.284483984") String githubLink
    );

    /*@DELETE("ideas/{id}")
    Call<Void> deleteIdea(@Path("id") int id);*/
}
