package com.example.kbasa.teaching.students;
import org.apache.http.*;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class RequestServerNotification extends AsyncTask<Void, Void, Void> {

    String tokenId="";

    RequestServerNotification(String tokenId){
        this.tokenId =tokenId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpPost httpost = new HttpPost("https://fcm.googleapis.com/fcm/send");
        httpost.addHeader("Content-Type", "application/json");
        httpost.addHeader("Authorization", "key=AAAACb5Mbek:APA91bGAinMM_9jxiBBo38GAJ4gDpJyGql2V-cSG3dRuJRyUF3nc_lZu0LG7FzepDWm42YZWhsLKrEMkwj9kFjFPAVA6Moe6PVZM-6prUlfwruimIiaLRa8ikxYSaAjgqeAfFdPwzeD3");
        JSONObject object = new JSONObject();

     //   tokenId = "ewTvZQEcACA:APA91bFiCgbQ9ogc2SS7GXjJn-zKt3bPqc7SdFzAVQvzbj2NejU9R2h70y0EI0b1pi6WJ9GBOSW60UxvFomEWJQTAnybClatTwt7oc_EP3nVWdeLld-VrmLHo5o0oqwjHISClYajdWVF";
        Log.i("tokenId",tokenId);
        try {
            StringEntity paramsEntity = new StringEntity("{\"to\":\""+ tokenId+"\",\"notification\":{\"body\":\"Some one enrolled in your class\",\"title\":\"Hurrah\"}}");
            httpost.setEntity(paramsEntity);
        }catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        }
        try {
            int resp = makeServerCall(httpost);
            //Toast.makeText(getApplicationContext(), "Status code is " + resp, Toast.LENGTH_LONG).show();

        } catch (Exception exception) {
            exception.printStackTrace();
            Log.i("Vamshik", " Some exceotuon");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i("RequestServerNoti", "onPostExecute RequestServerNotification");
    }

    public int makeServerCall(HttpPost httpPost) {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse resp = client.execute(httpPost);

            return resp.getStatusLine().getStatusCode();
        } catch (IOException exception) {
            exception.printStackTrace();
            return -1;
        }
    }
}
