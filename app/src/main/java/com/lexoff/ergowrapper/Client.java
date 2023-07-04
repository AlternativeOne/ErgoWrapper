package com.lexoff.ergowrapper;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0";
    public static final String authString="Digest username=\"\", realm=\"undefined\", nonce=\"undefined\", uri=\"/cgi/xml_action.cgi\", response=\"undefined\", qop=undefined, nc=undefined, cnonce=\"undefined\"";
    public static String cookieString="";

    private static Client instance;
    private OkHttpClient client;

    private Client(OkHttpClient.Builder builder) {
        OkHttpClient.Builder b = builder
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)

                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);

                        for (Cookie cookie : cookies){
                            if (cookie.name().contains("CGISID")) {
                                cookieString = String.format("CGISID=%s", cookie.value());

                                break;
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                });

        this.client=b.build();
    }

    public static Client init(@Nullable OkHttpClient.Builder builder) {
        return instance = new Client(builder != null ? builder : new OkHttpClient.Builder());
    }

    public static Client getInstance() {
        return instance;
    }

    public Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .method("GET", null)
                .build();

        Response response=client.newCall(request).execute();

        return response;
    }

    public Response post(String url, @Nullable byte[] dataToSend)
            throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(MediaType.get("application/x-www-form-urlencoded"), dataToSend))
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Authorization", authString)
                .addHeader("Cookie", cookieString)
                .build();

        Response response=client.newCall(request).execute();

        return response;
    }
}
