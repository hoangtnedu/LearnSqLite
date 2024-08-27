package com.example.hocsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//overise
    EditText edtMaSV, edtTenSV, edtLophoc;
    Button btnThem, btnSua,btnXoa, btnXem;
    ListView listView;
    ArrayList<String> myList;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //Tham chieu id cho biến giao diện
        edtMaSV=findViewById(R.id.edtmaSV);
        edtTenSV=findViewById(R.id.edtTenSV);
        edtLophoc=findViewById(R.id.edtHoclop);
        btnSua=findViewById(R.id.btnSua);
        btnThem=findViewById(R.id.btnThem);
        btnXoa=findViewById(R.id.btnXoa);
        btnXem=findViewById(R.id.btnXem);
        //tao list view
        listView=findViewById(R.id.lsv);
        myList=new ArrayList<>();
        myAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,myList);
        listView.setAdapter(myAdapter);

        //database
        mydatabase =openOrCreateDatabase("qlsv.db",MODE_PRIVATE,null);
        // tạo table
        try {
        String Sql ="Create table tblSV(MaSV TEXT primary key,TenSV TEXT,Lop TEXT)";
        mydatabase.execSQL(Sql);
        }catch (Exception e){
            Log.e("Err","Table đã tồn tại.");
        }
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MaSV =edtMaSV.getText().toString();
                String TenSV= edtTenSV.getText().toString();
                String Lop =edtLophoc.getText().toString();
                ContentValues mycontentvalue =new ContentValues();
                mycontentvalue.put("MaSV",MaSV);
                mycontentvalue.put("TenSV",TenSV);
                mycontentvalue.put("Lop",Lop);
                String msg ="";
                if (mydatabase.insert("tblSV",null,mycontentvalue)==-1)
                {
                msg="Insert table fail";
                }
                else
                {
                    msg="Insert table sussessful";
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String MaSV= edtMaSV.getText().toString();
           String TenSV =edtTenSV.getText().toString();
           String Hoclop =edtLophoc.getText().toString();
           ContentValues values=new ContentValues();
           values.put("MaSV",MaSV);
           values.put("TenSV",TenSV);
           values.put("Lop",Hoclop);
           int i= mydatabase.update("tblSV",values,"MaSV=?", new String[]{MaSV});
           String msg="";

           if (i==0)
           {
               msg="Không có bản ghi nào được cập nhật";
           }
           else msg ="Có "+ i+" bản ghi được cập nhật.";
           Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }


        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MaSV=  edtMaSV.getText().toString();
                int i= mydatabase.delete("tblSV","MaSV =?",new String[]{MaSV});
                String msg="";
                if (i==0){
                    msg="Không có bản ghi nào được xóa!";
                }
                else {
                    msg="Có " + i +" bản ghi đã được xóa.";
                }
       Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          myList.clear();
                Cursor cursor=mydatabase.query("tblSv",null,null,null,null,null,null);
                cursor.moveToNext();
                while (!cursor.isAfterLast())
                {
                    String c=cursor.getString(0)+"-"+cursor.getString(1)+"-"+cursor.getString(2);
                    myList.add(c);
                    cursor.moveToNext();

                }
                cursor.close();
                myAdapter.notifyDataSetChanged();

            }
        });
    }
}