package chul.knu_sotong;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Login extends AppCompatActivity {
    Button finish,login,signup;
    EditText login_id,login_pw;

    int cnt1, cnt2;
    boolean id_flag=false;
    boolean pw_flag=false;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        signup = (Button)findViewById(R.id.signup);
        finish = (Button)findViewById(R.id.finish);
        login = (Button)findViewById(R.id.login);
        login_id = (EditText)findViewById(R.id.login_id);
        login_pw = (EditText)findViewById(R.id.login_pw);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });
        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.finish:
                        finish();
                    case R.id.login:
                        if(login_id.getText().toString().equals("1")&&login_pw.getText().toString().equals("1")) {
                            login_pw.setText("");
                            Intent intent = new Intent(Login.this, NoticeMain.class);
                            Notice.getInstance().setAuthority(true);
                            startActivity(intent);
                        }
                        JsonLoadingTask aaa = new JsonLoadingTask();
                        aaa.execute();
                }
            }
        };
        finish.setOnClickListener(a);
        login.setOnClickListener(a);
    }
    //서버 -> 안드
    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText();
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {
            // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
            String aaa[] = result.split("\n");
            for (int i = 0; i < aaa.length; i++) {
                if (login_id.getText().toString().equals(aaa[i])&&login_pw.getText().toString().equals(aaa[i+1])) {
                    id_flag = true;
                    pw_flag = true;
                    cnt1 = i;
                    cnt2 = i + 1;
                    break;
                } else {
                    id_flag = false;
                    pw_flag = false;
                }
            }



            if (id_flag == true && pw_flag == true) {
                Notice.getInstance().setUser(aaa[cnt2+1]);
                userName = aaa[cnt1+2];

                if (login_id.getText().toString().length() == 6) {
                    login_pw.setText("");
                    Intent intent = new Intent(Login.this, NoticeMain.class);
                    Notice.getInstance().setAuthority(true);
                    startActivity(intent);
                } else if (login_id.getText().toString().length() == 9) {
                    login_pw.setText("");
                    Intent intent = new Intent(Login.this, NoticeMain.class);
                    Notice.getInstance().setAuthority(false);
                    startActivity(intent);
                }

                if (id_flag == false || pw_flag == false) {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    } // JsonLoadingTask



    /**
     * 원격으로부터 JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();
        try {

            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl("http://192.168.43.241:8080/hong/hong/login.jsp");

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);

            //ksk_list의 값은 배열로 구성 되어있으므로 JSON 배열생성
            JSONArray jArr = json.getJSONArray("List");

            //배열의 크기만큼 반복하면서, 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //값을 추출함
                String id_confirm = json.getString("id");
                String pw_confirm = json.getString("pw");
                String name_confirm = json.getString("name");
                //StringBuffer 출력할 값을 저장
                sb.append(id_confirm + "\n");
                sb.append(pw_confirm + "\n");
                sb.append(name_confirm + "\n");
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
