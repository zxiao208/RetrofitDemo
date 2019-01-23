package scut.carson_ho.retrofitdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Carson_Ho on 17/3/21.
 */
public class PostRequest extends AppCompatActivity {

    Button btnreq;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnreq = (Button) findViewById(R.id.btn_req);
        textView= (TextView) findViewById(R.id.rsp_content);
        btnreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okpost();
            }
        });

//        request();
    }

    public void okpost(){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        JsonObject jsonObject = new JsonObject();
//        Map<String,Object> map = new TreeMap<>();
//        map.put("name","张三");
        jsonObject.addProperty("name","张三");
        RequestBody requestBody = RequestBody.create(mediaType,jsonObject.toString());
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient =new OkHttpClient();
        //2.创建Request对象，设置一个url地址,设置请求方式。
        Request request = new Request.Builder().url("http://45.76.10.243/ssmapp/paper/phone/addPaper").method("POST",requestBody).build();
        okhttp3.Call call=okHttpClient.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.i("postFail",e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           textView.setText(response.toString());
                       }
                   });
                }
            });
    }

    private Request.Builder getBuilder(){
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0")
                .header("sid", "eyJhZGRDaGFubmVsIjoiYXBwIiwiYWRkUHJvZHVjd" +
                        "CI6InFia3BsdXMiLCJhZGRUaW1lIjoxNTAzOTk1NDQxOTEzLCJyb2xlIjoiUk9MRV9VU0VSIiwidXBkYXRlVGltZSI6MTUwMzk5NTQ0MTkxMywidXNlcklkIjoxNjQxMTQ3fQ==.b0e5fd6266ab475919ee810a82028c0ddce3f5a0e1faf5b5e423fb2aaf05ffbf");
            return builder;
    }

    public void request() {

        //步骤4:创建Retrofit对象
        Retrofit myretrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.7.91:8080/myssmapp/paper/phone/addPaper") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        PostRequest_Interface request = myretrofit.create(PostRequest_Interface.class);

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<String> call = request.getCall();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<String>() {

            //请求成功时回调
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // 请求处理,输出结果
                // 输出翻译的内容
                System.out.println("翻译是："+ response.body().toString());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                System.out.println("请求失败");
                System.out.println(throwable.getMessage());
            }
        });
    }


}