package com.example.skoal.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        lvItems = (ListView) findViewById(R.id.lvItems);
        populateListView();

        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                myDb.delete(String.valueOf(l) );
                populateListView();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View curr = adapterView.getChildAt((int) i);
                TextView c = (TextView) curr.findViewById(R.id.tv_taskName);

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class );
                intent.putExtra("editItem", c.getText().toString());
                intent.putExtra("index", l);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });


    }



    public void populateListView(){
        Cursor cursor = myDb.getAllData();
        String[] fromFieldNames = new String[] {"TaskName"};
        int[] toListViewIDs = new int[] {R.id.tv_taskName};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_layout,cursor,fromFieldNames,toListViewIDs,0);
        lvItems.setAdapter(myCursorAdapter);
    }



    public void onAddItem(View view) {

        boolean isInserted = myDb.insertData(etEditText.getText().toString() );
        if(isInserted == true) {
            //Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }

        etEditText.setText("");
        populateListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedItem = data.getExtras().getString("editItem");
            long index = data.getExtras().getLong("index", 0);
            myDb.update(String.valueOf(index), editedItem );
            populateListView();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }
}
