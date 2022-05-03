package chul.knu_sotong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class NoticeOption extends AppCompatActivity {

    int code1,code2;
    Button finish;
    String str="";
    Intent intent = new Intent();
    public static final String PREFS_NAME = "MyPrefsFile";
    String innerdb;
    String splitdb[];
    boolean flag;
    ArrayAdapter<CharSequence> adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticeoption);
        splitdb = new String[100];
        flag = true;


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        innerdb = sharedPreferences.getString("info","");
        splitdb = innerdb.split("\n");

        finish = (Button)findViewById(R.id.finish);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                code1=i;
                code1++;
                switch (code1) {

                    case 1:
                        adapter2 = ArrayAdapter.createFromResource(NoticeOption.this,R.array.major_array1,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                code2 = i;
                                code2 ++;
                                str="";
                                str+=adapter.getItem(code1-1);
                                str+=adapter2.getItem(code2-1)+"\n";
                                for(int a = 0 ; a < splitdb.length ; a++) {
                                    if(splitdb[a].equals(adapter.getItem(code1 - 1).toString() + adapter2.getItem(code2 - 1).toString())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                intent.putExtra("str", str);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                        break;
                    case 2:
                        adapter2 = ArrayAdapter.createFromResource(NoticeOption.this,R.array.major_array2,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                code2 = i;
                                code2 ++;
                                str="";
                                str+=adapter.getItem(code1-1);
                                str+=adapter2.getItem(code2-1)+"\n";
                                for(int a = 0 ; a < splitdb.length ; a++) {
                                    if(splitdb[a].equals(adapter.getItem(code1 - 1).toString() + adapter2.getItem(code2 - 1).toString())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                intent.putExtra("str", str);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                        break;
                    case 3:
                        adapter2 = ArrayAdapter.createFromResource(NoticeOption.this,R.array.major_array3,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                code2 = i;
                                code2 ++;
                                str="";
                                str+=adapter.getItem(code1-1);
                                str+=adapter2.getItem(code2-1)+"\n";
                                for(int a = 0 ; a < splitdb.length ; a++) {
                                    if(splitdb[a].equals(adapter.getItem(code1 - 1).toString() + adapter2.getItem(code2 - 1).toString())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                intent.putExtra("str", str);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = adapter.getItem(code1-1).toString()+adapter2.getItem(code2-1).toString();
                for(int i = 0 ; i < splitdb.length ; i++) {
                    if(splitdb[i].equals(str)) {
                        flag = false;
                        break;
                    }
                    else
                        flag = true;
                }

                if(!flag) {
                    Toast.makeText(NoticeOption.this, "이미 등록된 학과입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setResult(1122, intent);
                    Toast.makeText(NoticeOption.this, "학과가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
