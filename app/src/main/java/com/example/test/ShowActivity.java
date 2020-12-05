package com.example.test;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowActivity extends AppCompatActivity{
    SharedPreferences pf;
    private Button mBtnAdd;
    private ListView lv_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.show_activity);
        init();
    }
    public void init(){

        mBtnAdd = findViewById(R.id.btn_add);
        lv_note = findViewById(R.id.lv_note);
        List<String> valuesList = new ArrayList<>();
        //创建Values类型的list保存数据库中的数据
        final SharedPreferences sharedPreferences = getSharedPreferences("concern",MODE_PRIVATE);
       final Map map=sharedPreferences.getAll();
        java.util.Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
            String key= entry.getKey().toString();      //返回对应的键
            String code=entry.getValue().toString();   //返回对应的值
            valuesList.add(key);
        }
        final MyBaseAdapter myBaseAdapter = new MyBaseAdapter(valuesList,this,R.layout.note_item);
        lv_note.setAdapter(myBaseAdapter);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        lv_note.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowActivity.this,PickActivity.class);
                String key = (String) lv_note.getItemAtPosition(position);
                intent.putExtra("key",key);
                intent.putExtra("value",map.get(key).toString());
                startActivity(intent);
            }
        });
        lv_note.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String city = (String) lv_note.getItemAtPosition(position);
                new AlertDialog.Builder(ShowActivity.this)
                        .setTitle("提示")
                        .setMessage("是否删除?")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove(city);
                                        editor.commit();
                                        myBaseAdapter.removeItem(position);
                                        lv_note.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                myBaseAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        //MainActivity.this.onResume();
                                    }
                                })
                        .setNegativeButton("no",null).show();
                return true;
            }
        });
    }

    class MyBaseAdapter extends BaseAdapter{

        private List<String> valuesList;
        private Context context;
        private int layoutId;

        public MyBaseAdapter(List<String> valuesList, Context context, int layoutId) {
            this.valuesList = valuesList;
            this.context = context;
            this.layoutId = layoutId;
        }

        @Override
        public int getCount() {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.note_item, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.city = (TextView) convertView.findViewById(R.id.city);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String city = valuesList.get(position);
            viewHolder.city.setText(city);
            return convertView;
        }
        public void removeItem(int position){
            this.valuesList.remove(position);
        }

    }
    class ViewHolder{
        TextView city;
    }
}



