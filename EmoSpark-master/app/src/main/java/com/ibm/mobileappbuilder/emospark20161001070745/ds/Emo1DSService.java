
package com.ibm.mobileappbuilder.emospark20161001070745.ds;
import java.net.URL;
import com.ibm.mobileappbuilder.emospark20161001070745.R;
import ibmmobileappbuilder.ds.RestService;
import ibmmobileappbuilder.util.StringUtils;

/**
 * "Emo1DSService" REST Service implementation
 */
public class Emo1DSService extends RestService<Emo1DSServiceRest>{

    public static Emo1DSService getInstance(){
          return new Emo1DSService();
    }

    private Emo1DSService() {
        super(Emo1DSServiceRest.class);

    }

    @Override
    public String getServerUrl() {
        return "https://ibm-pods.buildup.io";
    }

    @Override
    protected String getApiKey() {
        return "3m0XEchU";
    }

    @Override
    public URL getImageUrl(String path){
        return StringUtils.parseUrl("https://ibm-pods.buildup.io/app/57ef62149d17e00300d4d9e4",
                path,
                "apikey=3m0XEchU");
    }

}

