package com.bringit.orders.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class Network {
    ProgressDialog pDialog;

    private RequestQueue queue;
    private NetworkCallBack listener;
    public static String BASE_URL = "https://api.bringit.co.il/?apiCtrl=deliver&do=";

    public enum RequestName {
        SIGN_UP, CONFIRM_USER, ADD_ORDER, LOG_IN, GET_DELIVERS_ORDERS,
        GET_ORDER_DETAILS_BY_ID, CONFIRM_ORDER_DELIVERING
    }

    public Network(NetworkCallBack listener, Context context) {
        this.listener = listener;
        queue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
    }


    public void sendRequest(final Context context, final RequestName requestName, String param1, String param2) {
        String url = BASE_URL;
        switch (requestName) {
            case SIGN_UP:
                url += "searchCities&q=" + param1;
                break;
            case GET_DELIVERS_ORDERS:
                url += "getDeliversOrders&type=" + param1;
                break;
            case GET_ORDER_DETAILS_BY_ID:
                url += "getOrderDetailsById&order_id=" + param1;
                break;
        }
        sendRequestObject(requestName, url, context, listener);
    }

    public void sendRequest(final Context context, final RequestName requestName) {
        String url = BASE_URL;
        switch (requestName) {
            case SIGN_UP:
                url += "signup";
                break;
        }
        Log.d("Request url  ", url);
        sendRequestObject(requestName, url, context, listener);
    }

    private void sendRequestObject(final RequestName requestName, final String url, final Context context, final NetworkCallBack listener) {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "url: " + url + "onResponse  :   " + response.toString());
                listener.onDataDone(requestName, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse 22" + error.toString() + " url " + url);
                manageError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                MyApp.get().checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("APIKey", SharePref.getInstance(context).getData(Constants.TOKEN_PREF));
                Log.d(TAG, "token is: " + SharePref.getInstance(context).getData(Constants.TOKEN_PREF));
                MyApp.get().addSessionCookie(params);
                return params;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void sendPostRequest(final Context context, final JSONObject params, final RequestName requestName) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        String url = BASE_URL;
        switch (requestName) {
            case SIGN_UP:
                url += "signup";
                break;
            case CONFIRM_USER:
                url += "confirmUser&q=" + params;
                break;
            case ADD_ORDER:
                url += "addOrder";
                break;
            case LOG_IN:
                url += "login";
                break;
            case CONFIRM_ORDER_DELIVERING:
                url += "confirmOrderDelivering";
                break;

        }
        Log.d("POST url  ", url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            listener.onDataDone(requestName, response);

                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR ROST REQUEST", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                manageError(error);
            }

        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                MyApp.get().checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("APIKey", SharePref.getInstance(context).getData(Constants.TOKEN_PREF));
                Log.d(TAG, "token is: " + SharePref.getInstance(context).getData(Constants.TOKEN_PREF));
                MyApp.get().addSessionCookie(params);
                return params;
            }
        };
        queue.add(req);

    }

    private void manageError(VolleyError error) {


        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            JSONObject err = null;
            try {
                err = new JSONObject(new String(error.networkResponse.data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleyLog.e("VolleyError  11: ", error.getMessage() + " networkResponse " + err + " networkResponse.statusCode " + networkResponse.statusCode);
            if (networkResponse.statusCode == 403) {
                listener.onDataDone(null, err);
            }
        }
    }

    public interface NetworkCallBack {
        void onDataDone(RequestName requestName, JSONObject json);
    }
}
