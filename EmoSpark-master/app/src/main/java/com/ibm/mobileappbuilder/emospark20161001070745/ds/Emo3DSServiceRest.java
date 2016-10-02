
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

public interface Emo3DSServiceRest{

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo3DS")
	void queryEmo3DSItem(
		@Query("skip") String skip,
		@Query("limit") String limit,
		@Query("conditions") String conditions,
		@Query("sort") String sort,
		@Query("select") String select,
		@Query("populate") String populate,
		Callback<List<Emo3DSItem>> cb);

	@GET("/app/57ef62149d17e00300d4d9e4/r/emo3DS/{id}")
	void getEmo3DSItemById(@Path("id") String id, Callback<Emo3DSItem> cb);

	@DELETE("/app/57ef62149d17e00300d4d9e4/r/emo3DS/{id}")
  void deleteEmo3DSItemById(@Path("id") String id, Callback<Emo3DSItem> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo3DS/deleteByIds")
  void deleteByIds(@Body List<String> ids, Callback<List<Emo3DSItem>> cb);

  @POST("/app/57ef62149d17e00300d4d9e4/r/emo3DS")
  void createEmo3DSItem(@Body Emo3DSItem item, Callback<Emo3DSItem> cb);

  @PUT("/app/57ef62149d17e00300d4d9e4/r/emo3DS/{id}")
  void updateEmo3DSItem(@Path("id") String id, @Body Emo3DSItem item, Callback<Emo3DSItem> cb);

  @GET("/app/57ef62149d17e00300d4d9e4/r/emo3DS")
  void distinct(
        @Query("distinct") String colName,
        @Query("conditions") String conditions,
        Callback<List<String>> cb);
    
    @Multipart
    @POST("/app/57ef62149d17e00300d4d9e4/r/emo3DS")
    void createEmo3DSItem(
        @Part("data") Emo3DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo3DSItem> cb);
    
    @Multipart
    @PUT("/app/57ef62149d17e00300d4d9e4/r/emo3DS/{id}")
    void updateEmo3DSItem(
        @Path("id") String id,
        @Part("data") Emo3DSItem item,
        @Part("picture") TypedByteArray picture,
        Callback<Emo3DSItem> cb);
}

