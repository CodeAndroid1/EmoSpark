
package com.ibm.mobileappbuilder.emospark20161001070745.ds;
import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.POST;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Path;
import retrofit.http.PUT;
import retrofit.mime.TypedByteArray;
import retrofit.http.Part;
import retrofit.http.Multipart;

public interface Emo4DSServiceRest{

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo4DS")
	void queryEmo4DSItem(
		@Query("skip") String skip,
		@Query("limit") String limit,
		@Query("conditions") String conditions,
		@Query("sort") String sort,
		@Query("select") String select,
		@Query("populate") String populate,
		Callback<List<Emo4DSItem>> cb);

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo4DS/{id}")
	void getEmo4DSItemById(@Path("id") String id, Callback<Emo4DSItem> cb);

	@DELETE("/app/57ef62149d17e00300d4d9e4/r/emo4DS/{id}")
  void deleteEmo4DSItemById(@Path("id") String id, Callback<Emo4DSItem> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo4DS/deleteByIds")
  void deleteByIds(@Body List<String> ids, Callback<List<Emo4DSItem>> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo4DS")
  void createEmo4DSItem(@Body Emo4DSItem item, Callback<Emo4DSItem> cb);

  @PUT("/app/57ef62149d17e00300d4d9e4/r/emo4DS/{id}")
  void updateEmo4DSItem(@Path("id") String id, @Body Emo4DSItem item, Callback<Emo4DSItem> cb);

  @GET("/app/57ef62149d17e00300d4d9e4/r/emo4DS")
  void distinct(
        @Query("distinct") String colName,
        @Query("conditions") String conditions,
        Callback<List<String>> cb);
    
    @Multipart
    @POST("/app/57ef62149d17e00300d4d9e4/r/emo4DS")
    void createEmo4DSItem(
        @Part("data") Emo4DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo4DSItem> cb);
    
    @Multipart
    @PUT("/app/57ef62149d17e00300d4d9e4/r/emo4DS/{id}")
    void updateEmo4DSItem(
        @Path("id") String id,
        @Part("data") Emo4DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo4DSItem> cb);
}

