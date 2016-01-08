package br.com.luizgadao.materialdesign;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.com.luizgadao.materialdesign.model.Disk;

/**
 * Created by Luiz on 05/01/16.
 */
public class LoadDisk {

    public static final String URL = "https://raw.githubusercontent.com/nglauber/dominando_android2/master/enghaw/";

    public static Disk[] loadDisk(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(URL + "enghaw.json")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String json = response.body().string();
            return new Gson().fromJson(json, Disk[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
