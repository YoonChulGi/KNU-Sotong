package chul.knu_sotong;

/**
 * Created by hong on 2017-03-06.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class NoticePage extends AppCompatActivity {
    TextView n_title,contents,n_author;
    Button n_delete,n_fix,complete;
    EditText pw;
    private static final int DIALOG_YES_NO_MESSAGE = 1;
    boolean able;

    String url;
    String property;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_YES_NO_MESSAGE:
                new JsonLoadingTask().execute();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() { // No 를 눌렀을때
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() { // Yes 를 눌렀을때
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new BtnAsyncTask().execute(Integer.toString(Notice.getInstance().getIndex2()));

                                JsonLoadingTask2 json = new JsonLoadingTask2();

                                switch(property) {
                                    case "인문대문헌정보학과":
                                        break;
                                    case "인문대국문학과":
                                        break;
                                    case "인문대중어중문학과":// 구현하기
                                        url = "http://192.168.43.241:8080/hong/hong/noticedelete2.jsp";
                                        json.execute();
                                        break;
                                    case "공과대컴미공": // 구현하기
                                        url = "http://192.168.43.241:8080/hong/hong/noticedelete.jsp";
                                        json.execute();
                                        break;
                                    case "공과대도시공":
                                        break;
                                    case "공과대산시공":
                                        break;
                                    case "예체대음악학과":
                                        break;
                                    case "사체과":
                                        break;
                                }

                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticepage);
        able = Notice.getInstance().getAuthority();
        n_title = (TextView)findViewById(R.id.n_title);
        contents = (TextView)findViewById(R.id.n_contents);
        n_author = (TextView)findViewById(R.id.n_author);
        pw = (EditText) findViewById(R.id.pw);

        n_delete = (Button)findViewById(R.id.n_delete);
        n_fix = (Button)findViewById(R.id.n_fix);
        complete = (Button) findViewById(R.id.complete);

        if(able) {
            n_delete.setVisibility(View.VISIBLE);
            n_fix.setVisibility(View.VISIBLE);
        }
        else {
            n_delete.setVisibility(View.GONE);
            n_fix.setVisibility(View.GONE);
        }


        new BtnAsyncTask().execute(Integer.toString(Notice.getInstance().getIndex2()));
        Intent intent = getIntent();
        property = intent.getStringExtra("info");
        switch(property) {
            case "인문대문헌정보학과":
                break;
            case "인문대국문학과":
                break;
            case "인문대중어중문학과":// 구현하기
                url = "http://192.168.43.241:8080/hong/hong/noticepage2.jsp";
                new JsonLoadingTask().execute();

                break;
            case "공과대컴미공": // 구현하기
                url = "http://192.168.43.241:8080/hong/hong/noticepage.jsp";
                new JsonLoadingTask().execute();

                break;
            case "공과대도시공":
                break;
            case "공과대산시공":
                break;
            case "예체대음악학과":
                break;
            case "사체과":
                break;

        }


        n_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.setVisibility(View.VISIBLE);
                complete.setVisibility(View.VISIBLE);

                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pw.getText().toString().equals(Notice.getInstance().getNoticePw())){
                            Intent intent = new Intent(NoticePage.this, NoticeModify.class);
                            intent.putExtra("info",property);
                            intent.putExtra("title", n_title.getText().toString());
                            intent.putExtra("contents", contents.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        });

        n_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.setVisibility(View.VISIBLE);
                complete.setVisibility(View.VISIBLE);

                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pw.getText().toString().equals(Notice.getInstance().getNoticePw())){
                            Toast.makeText(NoticePage.this, "일치", Toast.LENGTH_SHORT).show();
                            showDialog(DIALOG_YES_NO_MESSAGE);

                        }
                        else
                            Toast.makeText(NoticePage.this, "불일치", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    //안드->서버
    class BtnAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";

        String url = "http://192.168.43.241:8080/hong/hong/noticeIndex.jsp";

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
            String[] strarr = result.split("\n");

            String title="";
            String content="";
            String writer="";
            String pw="";

            if(strarr.length > 2)
            {
                for (int i = 0; i < strarr.length; i++) {
                    title = strarr[i];
                    i++;
                    content = strarr[i];
                    i++;
                    writer = strarr[i];
                    i++;
                    pw = strarr[i];
                }

                n_title.setText(title);
                contents.setText(content);
                n_author.setText("글쓴이: "+writer);
                Notice.getInstance().setNoticePw(pw);
            }


        }// onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask

    /**
     * 원격으로부터 JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();

        try {
            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl(url);

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);

            //ksk_list의 값은 배열로 구성 되어있으므로 JSON 배열생성
            JSONArray jArr = json.getJSONArray("List");

            //배열의 크기만큼 반복하면서, ksNo과 korName의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //값을 추출함
                String title = json.getString("title");
                String content = json.getString("content");
                String writer = json.getString("writer");
                String pw = json.getString("pw");
                //StringBuffer 출력할 값을 저장
                sb.append(title + "\n");
                sb.append(content + "\n");
                sb.append(writer + "\n");
                sb.append(pw + "\n");
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

    private class JsonLoadingTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText2();
        } // doInBackground : 백그라운드 작업을 진행한다.

        @Override
        protected void onPostExecute(String result) {

            finish();


        }// onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask

    /**
     * 원격으로부터 JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText2() {

        StringBuffer sb = new StringBuffer();

        try {
            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl(url);

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);

            //ksk_list의 값은 배열로 구성 되어있으므로 JSON 배열생성
            JSONArray jArr = json.getJSONArray("List");

            //배열의 크기만큼 반복하면서, ksNo과 korName의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //ksNo,korName의 값을 추출함
                String title = json.getString("title");
                String content = json.getString("content");

                sb.append(title + "\n");
                sb.append(content + "\n");
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.d("sb:",sb.toString());
        return sb.toString();
    }//getJsonText()-----------
    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl2(String pUrl){

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