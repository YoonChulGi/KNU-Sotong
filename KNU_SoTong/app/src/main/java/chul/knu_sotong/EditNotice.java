package chul.knu_sotong;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 동휘 on 2017-01-23.
 */

public class EditNotice extends AppCompatActivity {
    EditText noticename,contents,editpassword;
    Button noticesave,noticecancel;
    private static final int DIALOG_YES_NO_MESSAGE = 1;
    String property;
    String str;
    String noticePw;

    String url;
    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_YES_NO_MESSAGE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("게시하겠습니까?")
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
                                str = property +"\n"
                                        +noticename.getText().toString() + "\n"
                                        + contents.getText().toString() + "\n"
                                        + Notice.getInstance().getUser() + "\n"
                                        + noticePw;


                                BtnAsyncTask aaa = new BtnAsyncTask();

                                switch(property) {
                                    case "인문대문헌정보학과":
                                        break;
                                    case "인문대국문학과":
                                        break;
                                    case "인문대중어중문학과":// 구현
                                        url = "http://192.168.43.241:8080/hong/hong/noticeUpdate2.jsp";
                                        aaa.execute(str);

                                        break;
                                    case "공과대컴미공": // 구현
                                        url = "http://192.168.43.241:8080/hong/hong/noticeUpdate.jsp";
                                        aaa.execute(str);

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

                                Toast.makeText(EditNotice.this, "공지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.editnotice);

        noticename = (EditText)findViewById(R.id.noticename);
        contents = (EditText)findViewById(R.id.contents);
        editpassword = (EditText) findViewById(R.id.editpassword);
        noticesave = (Button)findViewById(R.id.noticesave);
        noticecancel = (Button)findViewById(R.id.noticecancel);

        Intent i = getIntent();

        property = i.getStringExtra(("info"));

        Intent fix = getIntent();
        noticename.setText(fix.getStringExtra("t"));
        contents.setText(fix.getStringExtra("c"));

        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.noticesave:
                        if(noticename.getText().toString().equals("")) {
                            Toast.makeText(EditNotice.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(contents.getText().toString().equals("")) {
                            Toast.makeText(EditNotice.this, "본문을 입력해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if(editpassword.getText().toString().equals("")) {
                            Toast.makeText(EditNotice.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else{
                            noticePw = editpassword.getText().toString();
                        }

                        showDialog(DIALOG_YES_NO_MESSAGE);
                        break;
                    case R.id.noticecancel:
                        finish();
                        break;
                }
            }
        };
        noticesave.setOnClickListener(a);
        noticecancel.setOnClickListener(a);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(); //WRITE 버튼으로 EditNotice 액티비티로 왔을때
        intent.putExtra("title", "");
        intent.putExtra("contents", "");
        intent.putExtra("author", "");
        setResult(33, intent);
        finish();
    }

    //안드->서버
    class BtnAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";
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

}
