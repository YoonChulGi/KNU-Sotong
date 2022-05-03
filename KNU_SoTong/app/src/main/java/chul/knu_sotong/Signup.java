package chul.knu_sotong;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 동휘 on 2017-02-01.
 */

public class Signup extends AppCompatActivity {
    EditText join_id,join_pw1,join_pw2,name;
    Button idcheck,ok,cancel;
    String Id,Pw,Name,res;
    Boolean flag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        join_id = (EditText)findViewById(R.id.join_id);
        join_pw1 = (EditText)findViewById(R.id.join_pw1);
        join_pw2 = (EditText)findViewById(R.id.join_pw2);
        idcheck = (Button)findViewById(R.id.idcheck);
        name = (EditText)findViewById(R.id.name);
        ok = (Button)findViewById(R.id.OK);
        cancel = (Button)findViewById(R.id.cancel);
        Id=Pw=Name="";

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!join_pw1.getText().toString().equals(join_pw2.getText().toString())) {
                    Toast.makeText(Signup.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(join_id.getText().toString().equals("")) {
                    Toast.makeText(Signup.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(join_pw1.getText().toString().equals("") ||join_pw1.getText().toString().equals("")) {
                    Toast.makeText(Signup.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.getText().toString().equals("")) {
                    Toast.makeText(Signup.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Id = join_id.getText().toString() + "\n";
                Pw = join_pw1.getText().toString() + "\n";
                Name = name.getText().toString() + "\n"; // DB로 보낼 String 에 값을 넣어준다.

                if(flag) {
                    BtnAsyncTask aaa = new BtnAsyncTask();
                    res = Id+Pw+Name;
                    aaa.execute(res);
                }
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonLoadingTask aaa = new JsonLoadingTask();
                aaa.execute();
            }
        });
    }
    //안드->서버
    class BtnAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";

        String url = "http://192.168.43.241:8080/hong/hong/userUpdate.jsp";

        // http post 요청하는 동안 Loading... 노출
        @Override
        protected void onPreExecute(){
        }

        protected void onPostExecute(Void i){


        }


        @Override
        protected String doInBackground(String... args) {
            String info = args[0];
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("info", info));
            //result = "";
            try {
                result = goHttpPost(url, nameValuePairs);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public String goHttpPost(String host, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException{
            String msg = null;

            //http 연결 인증
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(host);

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            msg = client.execute(httppost, responseHandler);

            return msg;
        }
    }
    //서버 -> 안드


    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText();
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {
            String aaa[] = result.split("\n");
            int id_size = join_id.getText().toString().length();
            if(id_size == 0) {
                flag = false;
            }
            else if(!(id_size == 9 || id_size == 6)) {
                flag = false;
            }

            else {
                for (int i = 0; i < aaa.length; i++) {
                    if (join_id.getText().toString().equals(aaa[i])) {
                        flag = false;
                        break;
                    } else {
                        flag = true;

                    }
                }
            }


            if(!flag) {
                if(id_size == 0)
                    Toast.makeText(Signup.this, "ID를 입력해주세요!", Toast.LENGTH_SHORT).show();
                else if(!(id_size == 9 || id_size == 6))
                    Toast.makeText(Signup.this, "잘못된 학번입니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Signup.this, "ID가 중복되었습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Signup.this, "사용할 수 있는 아이디입니다", Toast.LENGTH_SHORT).show();
            }
        }
        // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask



    /**
     * 원격으로부터 JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();
        try {

            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl("http://192.168.43.241:8080/hong/hong/userConfirm.jsp");

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);

            //ksk_list의 값은 배열로 구성 되어있으므로 JSON 배열생성
            JSONArray jArr = json.getJSONArray("List");

            //배열의 크기만큼 반복하면서, ksNo과 korName의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //값을 추출함
                String id_confirm = json.getString("id");

                sb.append(id_confirm + "\n");

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.d("sb:",sb.toString());
        return sb.toString();
    }//getJsonText()-----------
    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl(String pUrl){

        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;

        StringBuffer page=new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성

        try {
            URL url= new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //자원해제
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.d("page:",page.toString());
        return page.toString();
    }// getStringFromUrl()-------------------------

}
