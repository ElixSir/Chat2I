package com.example.chat_2022_eleves;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

        @GET("conversations")
        Call<ListConversations> doGetListConversation(@Header("hash") String hash);

        @GET("conversations/{id}/messages")
        Call<ListMessages> doGetListMessage(@Header("hash") String hash, @Path("id") int convId);


        @FormUrlEncoded
        @POST("conversations/{id}/messages")
        Call<ResponseBody> doSendMessage(@Header("hash") String hash, @Path("id") int convId, @Field("contenu") String message);

        @FormUrlEncoded
        @POST("conversations")
        Call<ResponseBody> doCreateConversation(@Header("hash") String hash, @Field("theme") String message);

        @DELETE("conversations/{id}")
        Call<ResponseBody> doSupprimerConversation(@Header("hash") String hash, @Path("id") int convId);


        @PUT("conversations/{id}?")
        Call<ResponseBody> doActiveDesactiveConversation(@Header("hash") String hash, @Path("id") int convId , @Query("mode") String mode);


}
