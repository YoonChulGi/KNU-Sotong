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
    //?????? -> ??????
    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText();
        } // doInBackground : ??????????????? ????????? ????????????.
        @Override
        protected void onPostExecute(String result) {
            // onPostExecute : ??????????????? ????????? ?????? ??? UI ????????? ????????????.
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
     * ?????????????????? JSON????????? ????????? ?????????
     * JSON ????????? ????????? ????????? ???????????? ????????? ????????? ??????
     */
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();
        try {

            //????????? URL ????????? ????????? ???????????? ?????????.
            String jsonPage = getStringFromUrl("http://192.168.43.241:8080/hong/hong/login.jsp");

            //???????????? JSON????????? ???????????? JSON????????? ??????
            JSONObject json = new JSONObject(jsonPage);

            //ksk_list??? ?????? ????????? ?????? ?????????????????? JSON ????????????
            JSONArray jArr = json.getJSONArray("List");

            //????????? ???????????? ???????????????, ?????? ?????????
            for (int i=0; i<jArr.length(); i++){

                //i?????? ?????? ??????
                json = jArr.getJSONObject(i);

                //?????? ?????????
                String id_confirm = json.getString("id");
                String pw_confirm = json.getString("pw");
                String name_confirm = json.getString("name");
                //StringBuffer ????????? ?????? ??????
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
    // getStringFromUrl : ????????? URL??? ????????? ????????? ???????????? ??????
    public String getStringFromUrl(String pUrl){

        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;

        StringBuffer page=new StringBuffer(); //????????? ???????????? ????????? StringBuffer?????? ??????

        try {
            URL url= new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            //????????? ????????? ????????? ???????????? ??????(line), Page??? ?????????
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //????????????
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
