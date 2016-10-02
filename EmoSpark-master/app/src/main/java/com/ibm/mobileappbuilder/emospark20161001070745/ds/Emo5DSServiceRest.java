
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

public interface Emo5DSServiceRest{

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo5DS")
	void queryEmo5DSItem(
		@Query("skip") String skip,
		@Query("limit") String limit,
		@Query("conditions") String conditions,
		@Query("sort") String sort,
		@Query("select") String select,
		@Query("populate") String populate,
		Callback<List<Emo5DSItem>> cb);

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo5DS/{id}")
	void getEmo5DSItemById(@Path("id") String id, Callback<Emo5DSItem> cb);

	@DELETE("/app/57ef62149d17e00300d4d9e4/r/emo5DS/{id}")
  void deleteEmo5DSItemById(@Path("id") String id, Callback<Emo5DSItem> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo5DS/deleteByIds")
  void deleteByIds(@Body List<String> ids, Callback<List<Emo5DSItem>> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo5DS")
  void createEmo5DSItem(@Body Emo5DSItem item, Callback<Emo5DSItem> cb);

  @PUT("/app/57ef62149d17e00300d4d9e4/r/emo5DS/{id}")
  void updateEmo5DSItem(@Path("id") String id, @Body Emo5DSItem item, Callback<Emo5DSItem> cb);

  @GET("/app/57ef62149d17e00300d4d9e4/r/emo5DS")
  void distinct(
        @Query("distinct") String colName,
        @Query("conditions") String conditions,
        Callback<List<String>> cb);
    
    @Multipart
    @POST("/app/57ef62149d17e00300d4d9e4/r/emo5DS")
    void createEmo5DSItem(
        @Part("data") Emo5DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo5DSItem> cb);
    
    @Multipart
    @PUT("/app/57ef62149d17e00300d4d9e4/r/emo5DS/{id}")
    void updateEmo5DSItem(
        @Path("id") String id,
        @Part("data") Emo5DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo5DSItem> cb);
}

