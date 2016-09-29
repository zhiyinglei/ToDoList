package com.example.skoal.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditText;
    long index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditText = (EditText) findViewById(R.id.etEditText);
        String editItem = getIntent().getStringExtra("editItem");
        etEditText.setText(editItem);
        index = getIntent().getLongExtra("index",0);
        //Toast.makeText(this, String.valueOf(index), Toast.LENGTH_LONG).show();

    }

    public void onSaveItem(View view) {
        etEditText = (EditText) findViewById(R.id.etEditText);
        String editedItem = etEditText.getText().toString();
        Intent data = new Intent();
        data.putExtra("editItem", editedItem);
        data.putExtra("index", index);
        setResult(RESULT_OK, data);

        finish();
    }
}
