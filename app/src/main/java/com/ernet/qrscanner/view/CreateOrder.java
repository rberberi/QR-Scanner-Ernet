package com.ernet.qrscanner.view;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ernet.qrscanner.MainActivity;
import com.ernet.qrscanner.R;
import com.ernet.qrscanner.utilities.Auftrag;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateOrder extends AppCompatActivity {
    private String status;
    private String id;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        Spinner spinner = findViewById(R.id.createOrderSpinner);
        Button createOrderBtn = findViewById(R.id.createOrderBtn);
        Button cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
        final EditText vname = findViewById(R.id.KundeVorname);
        final EditText nname = findViewById(R.id.KundeNachname);
        final EditText orderDescription = findViewById(R.id.orderDescription);

        final List<String> statusOption = new ArrayList<String>();
        statusOption.add("In Bearbeitung");
        statusOption.add("Angenommen");
        statusOption.add("Lieferbar");
        statusOption.add("Geliefert");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusOption);
        spinner.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = statusOption.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateOrder.this, MainActivity.class));
            }
        });

        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setId(sdf.format(timestamp).toString());

                // Create new order
                Auftrag auftrag = new Auftrag(getId(), vname.getText().toString(), nname.getText().toString(), orderDescription.getText().toString(), getStatus(), "null", "null");

                // Get utilities instance
                rootNode = FirebaseDatabase.getInstance();

                reference = rootNode.getReference("auftraege");
                // reference.setValue("First Data stored...");
                if (vname.length() > 0 && nname.length() > 0 && orderDescription.length() > 0 && status.length() > 0) {
                    reference.child(getId()).setValue(auftrag);
                    startActivity(new Intent(CreateOrder.this, MainActivity.class));
                    Toast.makeText(CreateOrder.this, "Auftrag erfolgreich erstellt", Toast.LENGTH_LONG).show();
                    vname.setText("");
                    nname.setText("");
                    orderDescription.setText("");
                    status = "";
                }
            }
        });
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
