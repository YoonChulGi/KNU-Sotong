package chul.knu_sotong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class NoticeMain extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<String> majorList = new ArrayList<>();
    Button addmynotice;
    ListView listview;
    String info="";
    String splitinfo[];
    CustomAdapter adapter;
    TextView msgTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticemain);
        msgTv = (TextView)findViewById(R.id.msgTv);
        Toast.makeText(this, Notice.getInstance().getUser()+"님 환영합니다♥", Toast.LENGTH_SHORT).show();
        adapter = new CustomAdapter(this);

        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);

        info = sharedPreferences.getString("info","");



        listview = (ListView) findViewById(R.id.listView);
        addmynotice = (Button) findViewById(R.id.addmynotice);
        splitinfo = new String[100];
        splitinfo = info.split("\n");

        majorList.clear();
        info = "";
        for (int i = 0; i < splitinfo.length; i++) {

            if(splitinfo[i].startsWith(" ")) continue;
            else {
                info += splitinfo[i] + "\n";
                majorList.add(splitinfo[i]);
            }
        }

        if(majorList.isEmpty())
            msgTv.setText("공지사항을 구독하실 학과를 등록해주세요");
        else
            msgTv.setText("공지사항을 구독하실 학과를 선택해주세요");

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listview.getItemAtPosition(i).toString().endsWith(")"))
                    Toast.makeText(NoticeMain.this, "미구현입니다..\n구현된 학과는 공과대_컴미공,인문대_중어중문학과 입니다.", Toast.LENGTH_SHORT).show();

                else {
                    Intent intent = new Intent(NoticeMain.this, NoticeList.class);
                    Log.d("title", listview.getItemAtPosition(i).toString());
                    intent.putExtra("info", listview.getItemAtPosition(i).toString());
                    startActivity(intent);
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                PopupMenu popup= new PopupMenu(NoticeMain.this, view);
                popup.getMenuInflater().inflate(R.menu.popup,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String res = "";
                        splitinfo = info.split("\n");
                        for(int a = 0 ; a < splitinfo.length ; a++) {
                            if(a == i) continue;
                            res += listview.getItemAtPosition(a).toString() + "\n";
                        }
                        info = res;
                        splitinfo = info.split("\n");
                        majorList.remove(i);
                        listview.setAdapter(adapter);

                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("info",info);
                        editor.apply();
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
        addmynotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeMain.this, NoticeOption.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("info",info);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("info",info);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1) {
            if(resultCode == 1122) {
                info += data.getStringExtra("str");
                splitinfo = info.split("\n");
                adapter.clear();
                majorList.clear();
                for(int i=0;i<splitinfo.length;i++) {
                    majorList.add(splitinfo[i]);
                }
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("info",info);
                editor.apply();
            }
        }
    }
    private class CustomAdapter extends ArrayAdapter<String> {
        Context context;
        public CustomAdapter(Context context) {
            super(context,R.layout.listitem,majorList);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem,null,true);
            TextView text1 = (TextView)rowView.findViewById(R.id.text1);
            text1.setText(majorList.get(position));

            return rowView;
        }
    }


}