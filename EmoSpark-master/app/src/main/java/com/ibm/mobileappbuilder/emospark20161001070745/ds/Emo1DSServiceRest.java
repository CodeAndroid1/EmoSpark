
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

public interface Emo1DSServiceRest{

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo1DS")
	void queryEmo1DSItem(
		@Query("skip") String skip,
		@Query("limit") String limit,
		@Query("conditions") String conditions,
		@Query("sort") String sort,
		@Query("select") String select,
		@Query("populate") String populate,
		Callback<List<Emo1DSItem>> cb);

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo1DS/{id}")
	void getEmo1DSItemById(@Path("id") String id, Callback<Emo1DSItem> cb);

	@DELETE("/app/57ef62149d17e00300d4d9e4/r/emo1DS/{id}")
  void deleteEmo1DSItemById(@Path("id") String id, Callback<Emo1DSItem> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo1DS/deleteByIds")
  void deleteByIds(@Body List<String> ids, Callback<List<Emo1DSItem>> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo1DS")
  void createEmo1DSItem(@Body Emo1DSItem item, Callback<Emo1DSItem> cb);

  @PUT("/app/57ef62149d17e00300d4d9e4/r/emo1DS/{id}")
  void updateEmo1DSItem(@Path("id") String id, @Body Emo1DSItem item, Callback<Emo1DSItem> cb);

  @GET("/app/57ef62149d17e00300d4d9e4/r/emo1DS")
  void distinct(
        @Query("distinct") String colName,
        @Query("conditions") String conditions,
        Callback<List<String>> cb);
    
    @Multipart
    @POST("/app/57ef62149d17e00300d4d9e4/r/emo1DS")
    void createEmo1DSItem(
        @Part("data") Emo1DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo1DSItem> cb);
    
    @Multipart
    @PUT("/app/57ef62149d17e00300d4d9e4/r/emo1DS/{id}")
    void updateEmo1DSItem(
        @Path("id") String id,
        @Part("data") Emo1DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo1DSItem> cb);
}

