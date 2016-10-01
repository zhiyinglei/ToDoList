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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;

    private final int REQUEST_CODE_UPDATE = 20;
    private final int REQUEST_CODE_INSERT = 21;

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
                TextView dueDate = (TextView) curr.findViewById(R.id.tv_dueDate);

                String priority = ((TextView) curr.findViewById(R.id.tv_priority)).getText().toString();
                String status = ((TextView) curr.findViewById(R.id.tv_status)).getText().toString();

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class );
                intent.putExtra("editItem", c.getText().toString());
                intent.putExtra("index", l);
                intent.putExtra("dueDate", dueDate.getText().toString());
                intent.putExtra("priority", priority);
                intent.putExtra("status", status);
                intent.putExtra("Action", REQUEST_CODE_UPDATE);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);

            }
        });


    }



    public void populateListView(){
        Cursor cursor = myDb.getAllData();
        String[] fromFieldNames = new String[] {"TaskName", "DueDate","Priority","Status"};
        int[] toListViewIDs = new int[] {R.id.tv_taskName, R.id.tv_dueDate,R.id.tv_priority, R.id.tv_status};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_layout,cursor,fromFieldNames,toListViewIDs,0);
        lvItems.setAdapter(myCursorAdapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATE) {
            String editedItem = data.getExtras().getString("editItem");
            long index = data.getExtras().getLong("index", 0);
            String dueDate = data.getExtras().getString("dueDate");
            String priority = data.getExtras().getString("priority");
            String status = data.getExtras().getString("status");
            myDb.update(String.valueOf(index), editedItem, dueDate,priority,status );



//            Toast.makeText(MainActivity.this, dueDate, Toast.LENGTH_LONG).show();

        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_INSERT){
            String editedItem = data.getExtras().getString("editItem");
            //long index = data.getExtras().getLong("index", 0);
            String dueDate = data.getExtras().getString("dueDate");
            String priority = data.getExtras().getString("priority");
            String status = data.getExtras().getString("status");
            //Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();
            myDb.insertData(editedItem, dueDate, priority, status );
        }

        populateListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    public void onAddTask(View view) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class );
        intent.putExtra("Action", REQUEST_CODE_INSERT);
        startActivityForResult(intent, REQUEST_CODE_INSERT);
    }
}
