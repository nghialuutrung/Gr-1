package com.kyuubi.gr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kyuubi.gr.request.AddPostRequest;
import com.kyuubi.gr.request.GetClassTeacherRequest;
import com.kyuubi.gr.request.GetPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    ArrayList<Post> postList = new ArrayList<Post>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        final String classid = intent.getStringExtra("classid");

        final EditText etPost = (EditText) findViewById(R.id.etPost);
        final ListView lvPost = (ListView) findViewById(R.id.lvPost);
        final Button bPost = (Button) findViewById(R.id.bPost);
        final PostAdapter postAdapter = new PostAdapter(GroupActivity.this,R.layout.post_item,postList);
        lvPost.setAdapter(postAdapter);
        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupActivity.this,CommentActivity.class);
                Post post = postList.get(position);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("classid",post.getClassid());
                intent.putExtra("name",post.getUsername());
                intent.putExtra("content",post.getContent());
                intent.putExtra("timestamp",post.getTimestamp());
                GroupActivity.this.startActivity(intent);
            }
        });

        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = etPost.getText().toString();
                SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                String username = share.getString("username", "");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            etPost.setText("");
                            postList.clear();
                            if(postList.size()==0) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            JSONArray jsonArray = jsonResponse.getJSONArray("post");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonclass = jsonArray.getJSONObject(i);

                                                Post post = new Post(jsonclass.getString("postid"),jsonclass.getString("classid"),jsonclass.getString("name"),jsonclass.getString("content"),jsonclass.getString("timestamp"));
                                                postList.add(post);
                                            }
                                            postAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                GetPostRequest getPostRequest = new GetPostRequest(classid, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(GroupActivity.this);
                                queue.add(getPostRequest);
                            }else{
                                postAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddPostRequest addPostRequest = new AddPostRequest(classid,username,content, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GroupActivity.this);
                queue.add(addPostRequest);
            }
        });


        if(postList.size()==0) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("post");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonclass = jsonArray.getJSONObject(i);
                            Post post = new Post(jsonclass.getString("postid"),jsonclass.getString("classid"),jsonclass.getString("name"),jsonclass.getString("content"),jsonclass.getString("timestamp"));
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            GetPostRequest getPostRequest = new GetPostRequest(classid, responseListener);
            RequestQueue queue = Volley.newRequestQueue(GroupActivity.this);
            queue.add(getPostRequest);
        }else{
            postAdapter.notifyDataSetChanged();
        }
    }
}
