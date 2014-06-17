package pg.pagalguy.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jibi on 17/6/14.
 */
public class AsyncGet {
    Handler onComplete;

    public AsyncGet(String url, Handler handler){
        this.onComplete = handler;
        new HttpAsyncTask().execute(url);
    }
    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject json;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                json = null;
                e.printStackTrace();
            }
            Message message;
            if(onComplete != null && json != null){
                message = new Message();
                message.obj = json;
                onComplete.handleMessage(message);
            }
        }
    }

    private String GET(String url){
        InputStream inputStream = null;
        String result = "";
        HttpGet httpget;
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            httpget = new HttpGet(url);
            httpget.setHeader("Accept", "application/json");

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpget);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = null;
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}