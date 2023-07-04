package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.LoginUtils;
import com.lexoff.ergowrapper.NotAuthorizedException;
import com.lexoff.ergowrapper.info.Info;

import java.io.IOException;

import okhttp3.Response;

public abstract class Extractor {
    protected String BASE_URL="http://192.168.1.1/";

    protected String url;
    protected Client client;
    protected boolean pageFetched = false;

    protected String dataToSend;

    protected String xmlStrResponse;

    protected Extractor(Client client, String url){
        this.client=client;
        this.url=url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public void setData(String data){
        this.dataToSend=data;
    }

    protected void fetchPage() throws IOException {
        if (pageFetched) return;
        Response response=client.post(url, dataToSend.getBytes("UTF-8"));
        if (response.code()!=200){
            throw new IOException("Response code is "+response.code());
        }

        onPageFetched(response);
        pageFetched = true;
    }

    protected void assertPageFetched() {
        if (!pageFetched) throw new IllegalStateException("Page is not fetched. Make sure you call fetchPage()");
    }

    protected boolean isPageFetched() {
        return pageFetched;
    }

    public void onPageFetched(Response response) throws IOException {
        String body=response.body().string();

        xmlStrResponse=body;
    }

    protected boolean isResponseValid(){
        return !(xmlStrResponse==null || xmlStrResponse.isEmpty() || xmlStrResponse.contains("<error_cause>5</error_cause>"));
    }

    public Info getInfo() throws IOException, NotAuthorizedException {
        fetchPage();

        if (!isResponseValid()){
            pageFetched=false;
            client.get(LoginUtils.LOGIN_URL);
            fetchPage();

            if (!isResponseValid()){
                throw new NotAuthorizedException("Your login credentials are not valid");
            }
        }

        return buildInfo();
    }

    protected abstract Info buildInfo();
}
