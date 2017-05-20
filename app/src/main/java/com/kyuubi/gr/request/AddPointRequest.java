package com.kyuubi.gr.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 16/05/2017.
 */

public class AddPointRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://nqminh278.000webhostapp.com/gr/CreatePoint.php";
    private Map<String , String> params;

    public AddPointRequest(String classid, String learner, double point, Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("classid",classid);
        params.put("learner",learner);
        params.put("point",point+"");
    }

    public Map<String, String> getParams(){
        return params;
    }
}
