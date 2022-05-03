package chul.knu_sotong;

/**
 * Created by hong on 2017-03-06.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NoticeList extends AppCompatActivity {
    /*ArrayList<String> noticeTitles = new ArrayList<>();
    ArrayList<String> writer = new ArrayList<>();
    ArrayList<String> noticecontents = new ArrayList<>();
    ArrayList<String> editpasswords = new ArrayList<>();*/

    String property;
    String url;
    ListView list;

    TextView propertyTv;
    Button writebtn;

    Boolean able;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticelist);
        Intent intent = getIntent();
        propertyTv = (TextView)findViewById(R.id.propertyTv);
        property = intent.getStringExtra("info");

        able = Notice.getInstance().getAuthority();

        writebtn = (Button)findViewById(R.id.writebtn);
        if(able) writebtn.setVisibility(View.VISIBLE);
        else writebtn.setVisibility(View.GONE);


        url = "";
        new JsonLoadingTask().execute();
        Toast.makeText(this, "구독하실 공지사항을 선택해주세요 ♥", Toast.LENGTH_SHORT).show();
        switch(property) {
            case "인문대문헌정보학과":
                break;
            case "인문대국문학과":
                break;
            case "인문대중어중문학과":// 구현하기
                propertyTv.setText("♥인문대학 중어중문학과♥");
                url = "http://192.168.43.241:8080/hong/hong/noticelist2.jsp";
                new JsonLoadingTask().execute();
                break;
            case "공과대컴미공": // 구현하기
                propertyTv.setText("♥공과대학 컴퓨터미디어정보공학부♥");
                url = "http://192.168.43.241:8080/hong/hong/noticelist.jsp";
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


        list = (ListView)findViewById(R.id.noticelist);


        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(NoticeList.this,EditNotice.class);
                i1.putExtra("info",property);
                startActivity(i1);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notice.getInstance().setIndex2(Notice.getInstance().getIndex1() -i);
                Intent intent = new Intent(NoticeList.this,NoticePage.class);
                intent.putExtra("info",property);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.noticelist);
        Intent intent = getIntent();
        propertyTv = (TextView)findViewById(R.id.propertyTv);
        property = intent.getStringExtra("info");
        if(property.equals("공과대컴미공")) {
            propertyTv.setText("♥공과대학 컴퓨터미디어정보공학부♥");
        }
        else if(property.equals("인문대중어중문학과")) {
            propertyTv.setText("♥인문대학 중어중문학과♥");
        }
        else {
            propertyTv.setText("미구현");
        }

        able = Notice.getInstance().getAuthority();

        writebtn = (Button)findViewById(R.id.writebtn);
        if(able) writebtn.setVisibility(View.VISIBLE);
        else writebtn.setVisibility(View.GONE);

        url = "";
        new JsonLoadingTask().execute();
        switch(property) {
            case "인문대문헌정보학과":
                break;
            case "인문대국문학과":
                break;
            case "인문대중어중문학과":// 구현하기
                url = "http://192.168.43.241:8080/hong/hong/noticelist2.jsp";
                new JsonLoadingTask().execute();
                break;
            case "공과대컴미공": // 구현하기
                url = "http://192.168.43.241:8080/hong/hong/noticelist.jsp";
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

        list = (ListView)findViewById(R.id.noticelist);


        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(NoticeList.this,EditNotice.class);
                i1.putExtra("info",property);

                startActivity(i1);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notice.getInstance().setIndex2(Notice.getInstance().getIndex1() -i);
                Intent intent = new Intent(NoticeList.this,NoticePage.class);
                intent.putExtra("info",property);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    //서버 -> 안드


    private class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText();
        } // doInBackground : 백그라운드 작업을 진행한다.

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    String[] strarr = result.split("\n");

                    String index = "";
                    String title = "";
                    String content = "";
                    String[] idx = {""};

                    if (strarr.length > 0) {
                        for (int i = 0; i < strarr.length; i++) {
                            index += strarr[i] + "\n";
                            i++;
                            title += strarr[i] + "\n";
                            i++;
                            content += strarr[i] + "\n";
                        }
                        idx = index.split("\n");

                        Notice.getInstance().setIndex1(Integer.parseInt(idx[0]));
                    }

                    if (Integer.parseInt(idx[0]) == 0) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.listitem);
                        if (Notice.getInstance().getIndex1() != 0) {
                            String[] _title = title.split("\n");
                            String[] _content = content.split("\n");
                            for (int i = 0; i < Notice.getInstance().getIndex1(); i++) {
                                try {
                                    adapter.add(_title[i]);
                                    Notice.getInstance().setContent(_content[i]);
                                } catch (Exception e) {

                                }
                            }
                        }
                        list.setAdapter(adapter);
                    }
                }
            } catch (Exception e){

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
                String index = json.getString("index");
                String title = json.getString("title");
                String content = json.getString("content");
                //StringBuffer 출력할 값을 저장
                sb.append(index + "\n");
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