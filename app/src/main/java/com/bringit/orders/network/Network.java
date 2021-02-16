package com.bringit.orders.network;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bringit.orders.utils.Constants;
import com.bringit.orders.utils.SharedPrefs;
import com.bringit.orders.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Network {

    private final String SET_COOKIE_KEY = "Set-Cookie";
    private final String COOKIE_KEY = "Cookie";
    private final String SESSION_COOKIE = "Apikey";

    private NetworkCallBack listener;
    public static String BASE_URL = "https://api.bringit.org.il/?apiCtrl=deliver&do="; //dev
    private final String BASE_URL_2 = "https://api2.bringit.org.il/"; //dev

    public enum RequestName {
        LOG_IN, SIGN_UP, CONFIRM_USER,
        RESET_PASSWORD, CONFIRM_RESET_PASSWORD,
        ADD_ORDER, GET_DELIVERS_ORDERS,
        GET_ORDER_DETAILS_BY_ID, CONFIRM_ORDER_DELIVERING
    }

    Network(NetworkCallBack listener) {
        this.listener = listener;
    }

    public void sendRequest(final Context context, final RequestName requestName, String param1) {
        sendRequest(context, requestName, param1, false);
    }

    public void sendRequest(final Context context, final RequestName requestName, String param1, boolean isApi2) {
        String url = isApi2 ? BASE_URL_2 : BASE_URL;

        switch (requestName) {
//            case SIGN_UP:
//                url += "searchCities&q=" + param1;
//                break;
            case GET_DELIVERS_ORDERS: //api 2
                url += "delivering/orders/" + param1;
//                url += "getDeliversOrders&type=" + param1;
                break;
            case GET_ORDER_DETAILS_BY_ID: //api 2
                url += "delivering/order/" + param1;
                break;
        }
        sendRequestObject(requestName, url, context, listener);
    }

    private void sendRequestObject(final RequestName requestName, final String url, final Context context, final NetworkCallBack listener) {
        JsonObjectRequest jsonArrayRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            Log.d("Request url  11  ", url);
                            Log.d(TAG, "onResponse  :   " + response.toString());
                            listener.onDataDone(response);
                        },
                        error -> {
                            manageErrors(error, context);
                            try {
                                if (error.networkResponse != null)
                                    listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "Connection Error 22" + error.toString());
                        }) {

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        checkSessionCookie(response.headers);
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        if (!SharedPrefs.getData(Constants.TOKEN_PREF).equals("")) {
                            params.put(SESSION_COOKIE, SharedPrefs.getData(Constants.TOKEN_PREF));
                            Log.d(TAG, "token is: " + SharedPrefs.getData(Constants.TOKEN_PREF));

                            addSessionCookie(params);
                        }
                        return params;
                    }
                };
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
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
            case RESET_PASSWORD:
                url += "resetPassword";
                break;
            case CONFIRM_RESET_PASSWORD:
                url += "confirmResetPassword";
                break;
            case CONFIRM_ORDER_DELIVERING:
                url += "confirmOrderDelivering";
                break;

        }
        Log.d("POST url  ", url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        listener.onDataDone(response);
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR POST REQUEST", e.toString());
                    }
                }, error -> {
            VolleyLog.e("Error  11: ", error.getMessage());
            manageErrors(error, context);
            //                try {
            //
            //                   listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
            //                } catch (JSONException e) {
            //                    e.printStackTrace();
            //                }

        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                if (!SharedPrefs.getData(Constants.TOKEN_PREF).equals("")) {
                    params.put(SESSION_COOKIE, SharedPrefs.getData(Constants.TOKEN_PREF));
                    addSessionCookie(params);
                }
                return params;
            }
        };
        RequestQueueSingleton.getInstance(context).addToRequestQueue(req);
    }

    private void manageErrors(VolleyError error, Context context) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Utils.openAlertDialog(context, "בדוק חיבור לאינטרנט", "");

        } else if (error instanceof ParseError) {
            Log.e("parse error", error.toString());
            Toast.makeText(context, ("ParseError"), Toast.LENGTH_SHORT).show();
        } else {
            manageMsg(error, context);
        }
    }

    private void manageMsg(VolleyError error, Context context) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            try {
                JSONObject jsonError = new JSONObject(new String(networkResponse.data));
                if (networkResponse.statusCode == 403) {
                    // HTTP Status Code: 403 Unauthorized
                    listener.onDataError(jsonError);
                    Log.e("network error!!!", jsonError.toString());

//                    go to login
//                    if (jsonError.toString().contains("לא נמצאו נתוני משתמש, נא להתחבר למערכת")) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        context.startActivity(intent);
//                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPrefs.saveData(SESSION_COOKIE, cookie);
            }
        }
    }

    private void addSessionCookie(Map<String, String> headers) {
        String sessionId = SharedPrefs.getData(Constants.TOKEN_PREF);
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }

    public interface NetworkCallBack {
        void onDataDone(JSONObject json);

        void onDataError(JSONObject json);
    }
}
