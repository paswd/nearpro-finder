package ru.paswd.nearprofinder.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.paswd.nearprofinder.config.NPF;


public class ApiManager extends AsyncTask<String, String, String> {
    private Context context;
    private String apiHref;
    private JSONObject sendObject;
    //private JSONObject result;
    private OnTaskCompleted listener;

    boolean isError;

    public ApiManager(Context ctx, OnTaskCompleted _listener) {
        context = ctx;
        listener = _listener;
        isError = false;
        //apiHref = _apiHref;
        //jsonObject = request;
    }

    public void setMsgRegister(String login, String password, String email) {
        try {
            apiHref = NPF.Server.API.REGISTER;
            sendObject = new JSONObject();
            sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
            sendObject.put("login", login);
            sendObject.put("password", password);
            sendObject.put("email", email);
        } catch (JSONException ignored) {
            isError = true;
        }

    }

    public void setMsgAuth(String login, String password) {
        try {
            apiHref = NPF.Server.API.AUTH;
            sendObject = new JSONObject();
            sendObject.put("access_token", NPF.Server.ACCESS_TOKEN);
            sendObject.put("login", login);
            sendObject.put("password", password);
        } catch (JSONException ignored) {
            isError = true;
        }
    }

    /*public JSONObject getResult() {
        return result;
    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String res) {
        listener.onCompleted(res);
    }

    @Override
    protected String doInBackground(String... params) {
        if (!checkNetworkConnection()) {
            return "{\"status\":0,\"error_num\":-1,\"error_msg\":\"No network connection\",\"data\":\"\"}";
        }
        if (isError) {
            return "{\"status\":0,\"error_num\":-2,\"error_msg\":\"Unknown error\",\"data\":\"\"}";
        }

        return send();
    }

    private String send() {
        try {
            URL url = new URL(apiHref); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

//            JSONObject jsonParam = new JSONObject();
//            jsonParam.put("uname", params[0].getUser());
//            jsonParam.put("message", params[0].getMessage());
//            jsonParam.put("latitude", "0");
//            jsonParam.put("longitude", "0");
//            jsonParam.put("id", "1");

//            String message = URLEncoder.encode(jsonObject.toString(), "UTF-8");
            String message = sendObject.toString();
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(message);

            os.flush();
            os.close();

            int status = conn.getResponseCode();

            //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            //Log.i("MSG" , conn.getResponseMessage());
            //result = conn.getResponseMessage();


            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    //result = new JSONObject(sb.toString());
                    return sb.toString();
            }

            sendObject = null;
            conn.disconnect();
        } catch (Exception ignored) {

        }
        return null;
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }


}
