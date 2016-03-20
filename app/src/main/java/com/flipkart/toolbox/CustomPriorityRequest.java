package com.flipkart.toolbox;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by kushal.sharma on 12/03/16.
 */
public class CustomPriorityRequest extends JsonObjectRequest {

    Priority priority = Priority.NORMAL;

    public CustomPriorityRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority p) {
        priority = p;
    }
}
