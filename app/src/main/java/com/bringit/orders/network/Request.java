package com.bringit.orders.network;

import android.content.Context;
import android.util.Log;

import com.bringit.orders.models.OrderDetailsModel;
import com.bringit.orders.models.RegistrationModel;
import com.bringit.orders.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bringit.orders.utils.Utils.initPref;
import static com.bringit.orders.utils.Utils.openAlertDialog;

public class Request {

    private static Request sRequest;

    public static Request getInstance() {
        if (sRequest == null) {
            sRequest = new Request();
        }
        return sRequest;
    }

    public void logIn(final Context context, String password, String phone, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("password", password);

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("response: ", json.toString());
                try {
                    initPref(json.getJSONObject("user"));
                    Constants.saveLoggedIn(json.getString("utoken"), (json.getJSONObject("user")).getString("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (json.has("errorCode") && json.getInt("errorCode") == 1) {
                        listener.onDataDone(false);
                        openAlertMsg(context, json);
                    } else {
                        listener.onDataDone(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
//                openAlertMsg(context, json);
                listener.onDataDone(false);

            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.LOG_IN);
    }

    public void signUp(final Context context, RegistrationModel registrationModel, final RequestCallBackSuccess listener) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(registrationModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("SIGN_UP", json.toString());
                try {
                    if (json.getBoolean("status")) listener.onDataDone(true);
                    else openAlertMsg(context, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
                listener.onDataDone(false);

            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.SIGN_UP);
    }

    public void confirmUser(final Context context, String phone, String code, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("confirmPhone", json.toString());
                try {
                    Constants.saveLoggedIn(json.getString("utoken"), (json.getJSONObject("user")).getString("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (json.has("errorCode") && json.getInt("errorCode") == 1) {
                        listener.onDataDone(false);
                        openAlertMsg(context, json);
                    } else {
                        listener.onDataDone(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
//                openAlertMsg(context, json);
                listener.onDataDone(false);

            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.CONFIRM_USER);
    }

    public void addOrder(final Context context, String code, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("ADD_ORDER", json.toString());
                try {
                    if (json.getBoolean("status")) listener.onDataDone(true);
                    else openAlertMsg(context, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
//                openAlertMsg(context, json);
                listener.onDataDone(false);

            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.ADD_ORDER);
    }


    public void startOrEndWorkerClockByID(Context context, String workerId, boolean isStart, RequestCallBackSuccess listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("startEndWorkerClockByID", json.toString());
                listener.onDataDone(true);
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("startEndClock error", json.toString());
                listener.onDataDone(false);

            }
        });
//        network.sendRequest(context, isStart ? Network.RequestName.START_WORKER_CLOCK : Network.RequestName.END_WORKER_CLOCK, workerId);
    }

    public void confirmOrderDelivery(final Context context, String orderId, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", orderId);

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                listener.onDataDone(true);

                Log.d("confirmOrderDelivery", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("confirmOrderDel error", json.toString());
//                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.CONFIRM_ORDER_DELIVERING);
    }

    public void getDeliveryOrders(final Context context, String pageType, final RequestJsonCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getDeliveryOrders", json.toString());
                listener.onDataDone(json);
            }

            @Override
            public void onDataError(JSONObject json) {
                listener.onDataDone(json);
            }
        });
        network.sendRequest(context, Network.RequestName.GET_DELIVERS_ORDERS, pageType, true);
    }

    public void getOrderDetailsByID(Context context, String orderId, RequestOrderDetailsCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                try {
                    Log.d("getOrderDetailsByID", json.toString());
                    Gson gson = new Gson();
                    OrderDetailsModel orderDetailsModel = gson.fromJson(json.getString("order"), OrderDetailsModel.class);

                    listener.onDataDone(orderDetailsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("getOrderByID error", json.toString());

            }
        });
        network.sendRequest(context, Network.RequestName.GET_ORDER_DETAILS_BY_ID, orderId, true);
    }


    public void checkToken(Context context, final RequestCallBackSuccess listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("checkToken", json.toString());
                try {
                    JSONObject jsonBusinessModel = json.getJSONObject("user");
//                    BusinessModel.getInstance().initData(jsonBusinessModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listener.onDataDone(true);
                Log.d("checkToken", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                listener.onDataDone(false);
            }
        });
//        network.sendRequest(context, Network.RequestName.GET_LOGGED_MANAGER, "");
    }

    private void openAlertMsg(Context context, JSONObject json) {
        try {
            openAlertDialog(context, json.getString("message"), "");
            Log.d("response failed: ", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface RequestCallBack {
        void onDataDone();
    }

    public interface RequestCallBackSuccess {
        void onDataDone(boolean isDataSuccess);
    }

    public interface RequestOrderDetailsCallBack {
        void onDataDone(OrderDetailsModel response);
    }

    public interface RequestJsonCallBack {
        void onDataDone(JSONObject jsonObject);
    }

}
