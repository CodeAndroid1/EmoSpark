
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

public interface Emo2DSServiceRest{

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo2DS")
	void queryEmo2DSItem(
		@Query("skip") String skip,
		@Query("limit") String limit,
		@Query("conditions") String conditions,
		@Query("sort") String sort,
		@Query("select") String select,
		@Query("populate") String populate,
		Callback<List<Emo2DSItem>> cb);

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo2DS/{id}")
	void getEmo2DSItemById(@Path("id") String id, Callback<Emo2DSItem> cb);

	@DELETE("/app/57ef62149d17e00300d4d9e4/r/emo2DS/{id}")
  void deleteEmo2DSItemById(@Path("id") String id, Callback<Emo2DSItem> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo2DS/deleteByIds")
  void deleteByIds(@Body List<String> ids, Callback<List<Emo2DSItem>> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo2DS")
  void createEmo2DSItem(@Body Emo2DSItem item, Callback<Emo2DSItem> cb);

  @PUT("/app/57ef62149d17e00300d4d9e4/r/emo2DS/{id}")
  void updateEmo2DSItem(@Path("id") String id, @Body Emo2DSItem item, Callback<Emo2DSItem> cb);

  @GET("/app/57ef62149d17e00300d4d9e4/r/emo2DS")
  void distinct(
        @Query("distinct") String colName,
        @Query("conditions") String conditions,
        Callback<List<String>> cb);
    
    @Multipart
    @POST("/app/57ef62149d17e00300d4d9e4/r/emo2DS")
    void createEmo2DSItem(
        @Part("data") Emo2DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo2DSItem> cb);
    
    @Multipart
    @PUT("/app/57ef62149d17e00300d4d9e4/r/emo2DS/{id}")
    void updateEmo2DSItem(
        @Path("id") String id,
        @Part("data") Emo2DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo2DSItem> cb);
}

