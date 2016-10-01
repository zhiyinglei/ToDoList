package com.example.skoal.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private final int REQUEST_CODE_INSERT = 21;
    private final int REQUEST_CODE_UPDATE = 20;
    int action;
    DatePicker dp;
    Button btn;
    Spinner sp;
    Spinner spStatus;

    EditText etEditText;
    long index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        btn = (Button) findViewById(R.id.btnEdit);
        sp = (Spinner)findViewById(R.id.spPriority);
        spStatus = (Spinner) findViewById(R.id.spStatus);

        action  = getIntent().getIntExtra("Action",0);

        // Setup priority Spinner
        List<String> list = new ArrayList<String>();
        list.add("high");
        list.add("medium");
        list.add("low");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        // Setup Status spinner
        List<String> listStatus = new ArrayList();
        listStatus.add("Todo");
        listStatus.add("Done");
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listStatus);
        dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(dataAdapterStatus);


        if(action == REQUEST_CODE_INSERT){
            dp = (DatePicker) findViewById(R.id.datePicker);
            btn.setText("SAVE");




        }
        if(action == REQUEST_CODE_UPDATE){
            btn.setText("UPDTAE");
            etEditText = (EditText) findViewById(R.id.etEditText);
            String editItem = getIntent().getStringExtra("editItem");
            etEditText.setText(editItem);
            index = getIntent().getLongExtra("index",0);
            String dueDate = getIntent().getStringExtra("dueDate");
            String[] mdy = dueDate.split("/");
            //Toast.makeText(this, dueDate, Toast.LENGTH_LONG).show();

            String priority = getIntent().getStringExtra("priority");
            int pos = list.indexOf(priority);
            sp.setSelection(pos);

            String status = getIntent().getStringExtra("status");
            pos = listStatus.indexOf(status);
            spStatus.setSelection(pos);

            dp = (DatePicker) findViewById(R.id.datePicker);


            dp.updateDate(Integer.valueOf(mdy[2]),Integer.valueOf(mdy[0])-1,Integer.valueOf(mdy[1]));
        }


    }

    public void onSaveItem(View view) {
        etEditText = (EditText) findViewById(R.id.etEditText);
        String editedItem = etEditText.getText().toString();


        if (editedItem.trim().matches("")) {
            Toast.makeText(this, "Please enter the Task!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra("editItem", editedItem);
        data.putExtra("index", index);

        int m =  dp.getMonth()+1;
        int y = dp.getYear();
        int d = dp.getDayOfMonth();
        String dueDate = m + "/" + d + "/" + y;


        data.putExtra("dueDate", dueDate);



        String priority = sp.getSelectedItem().toString();
        data.putExtra("priority", priority);
        //Toast.makeText(this, p, Toast.LENGTH_LONG).show();

        String status = spStatus.getSelectedItem().toString();
        data.putExtra("status", status);


        setResult(RESULT_OK, data);
        finish();
    }
}
