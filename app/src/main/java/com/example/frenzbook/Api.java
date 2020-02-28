package com.example.frenzbook;

import android.content.IntentSender;

import com.example.frenzbook.DTO.BaseResponse;
import com.example.frenzbook.DTO.FriendsDTO;
import com.example.frenzbook.DTO.GetUserDetailsDTO;
import com.example.frenzbook.DTO.LoginRequestDTO;
import com.example.frenzbook.DTO.LoginResponseDTO;
import com.example.frenzbook.DTO.PostDTO;
import com.example.frenzbook.DTO.ReactionDTO;
import com.example.frenzbook.DTO.ReactionShowResponse;
import com.example.frenzbook.DTO.SecondLoginRequestDTO;
import com.example.frenzbook.DTO.SecondSignUpDTO;
import com.example.frenzbook.DTO.SignUpRequestDTO;
import com.example.frenzbook.DTO.SignUpResponseDTO;
import com.example.frenzbook.DTO.TimelineDTO;
import com.example.frenzbook.DTO.UpdateRoleDTO;
import com.example.frenzbook.DTO.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL_PROXY = "http://172.16.20.172:8080";
    String BASE_URL_LOGIN = "http://172.16.20.32:8080";

    @GET("/user/getUserDetails/{id}")
    Call<BaseResponse<UserData>> getUserInfo(@Path("id") String id);

    @GET("/user/getFriends/{userId}")
    Call<BaseResponse<List<FriendsDTO>>> getFriends(@Path("userId") String id);

    @GET("/post/user/timeline/{userId}")
    Call<BaseResponse<List<TimelineDTO>>> getUsersAllPosts(@Path("userId") String id);

    @POST("/reaction/addActivity")
    Call<BaseResponse<String>> addReaction(@Body ReactionDTO reactionDTO);

    @POST("/reaction/showReactionsByUserId")
    Call<BaseResponse<ReactionShowResponse>> showReactionsByUserId(@Path("postId") String postId);


    @POST("/authentication/auth/signin")
    Call<LoginResponseDTO> sendLoginCredentials(@Body LoginRequestDTO loginRequestDTO);


    @POST("/authentication/auth/signup")
    Call<SignUpResponseDTO> sendSignUpCredentials(@Body SignUpRequestDTO signUpRequestDTO);

    @POST("/authentication/jwt/getUserDetails")
    Call<GetUserDetailsDTO>getUserDetails(@Body SecondLoginRequestDTO secondLoginRequestDTO, @Header("authorization") String authToken);

    @POST("/user/editDetails")
    Call<BaseResponse<SecondSignUpDTO>>editUserDetails(@Body SecondSignUpDTO secondSignUpDTO,@Header("Auth") String authToken);

    @POST("post/addPost")
    Call<BaseResponse<String>>addPost(@Body PostDTO postDTO);

    @POST("authentication/role/updateRole")
    Call<String>updateRole(@Body UpdateRoleDTO updateRoleDTO, @Header("authorization") String authToken);


}
