package com.ernet.qrscanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.ernet.qrscanner.MainActivity;
import com.ernet.qrscanner.R;
import com.ernet.qrscanner.utilities.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StatusWindow extends AppCompatActivity {
    String _ORDERSTATUS;
    private static String id;
    private TextView auftrId;
    private String status;
    private String lastStatus;
    private String lastStatusChangeTime;
    private TextView aktStatus;
    private TextView textView;
    DatabaseReference reference;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Status statusObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_window);

        // Get database reference, to update data
        reference = FirebaseDatabase.getInstance().getReference("auftraege");
        auftrId = (TextView) findViewById(R.id.auftrId);
        Spinner spinner = findViewById(R.id.spinner);
        aktStatus = (TextView) findViewById(R.id.aktStatus);
        TextView kundName = (TextView) findViewById(R.id.kundName);
        setLastStatusChangeTime(sdf.format(timestamp).toString());
        Button save = (Button) findViewById(R.id.save);
        Button neuScann = (Button) findViewById(R.id.neuScann);
        Button backToHome = (Button) findViewById(R.id.backToHome);
        statusObject = new Status();

        setId(statusObject.getId());
        kundName.setText(statusObject.getName());
        aktStatus.setText(statusObject.getStatus());


        TypeProduktNummer typePN = new TypeProduktNummer();
        showOrderId();
        final List<String> statusOption = new ArrayList<String>();
        statusOption.add("Angenommen");
        statusOption.add("In Bearbeitung");
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

        // TODO: Remove klicklistener, after debug
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aktStatus.setText(status);
                update(v);
                toastMessage();
            }
        });

        neuScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StatusWindow.this, ScannerQR.class));
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StatusWindow.this, MainActivity.class));
            }
        });

        // Temporary save current status
        _ORDERSTATUS = statusObject.getStatus();
    }


    // Check if given Object is null
    private boolean isNull(Object obj) {
        return obj == null;
    }

    // Handle update status in utilities
    public void update(View view) {
        isStatusChanged();
//        if (isStatusChanged()) {
//            Toast.makeText(this, "Status wurde aktualisiert", Toast.LENGTH_LONG).show();
//        }
    }

    // Check whether status has been changed
    public boolean isStatusChanged() {
        if (isNull(_ORDERSTATUS)) {
            System.out.println("Error: _ORDERSTATUS is " + _ORDERSTATUS);
            return false;
        }

        if(!_ORDERSTATUS.equals(aktStatus.getText().toString())) {
            // Set new status and update utilities
            reference.child(getId()).child("lastStatus").setValue(_ORDERSTATUS);
            reference.child(getId()).child("lastStatusChangeTime").setValue(sdf.format(timestamp).toString());
            reference.child(getId()).child("auftragStatus").setValue(aktStatus.getText().toString());
            statusObject.destroyStatusContext();
            finish();
            startActivity(new Intent(StatusWindow.this, MainActivity.class));
            return true;
        } else {
            return false;
        }
    }


    private void showOrderId() {
        this.auftrId.setText(this.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        StatusWindow.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getLastStatusChangeTime() {
        return lastStatusChangeTime;
    }

    public void setLastStatusChangeTime(String lastStatusChangeTime) {
        this.lastStatusChangeTime = lastStatusChangeTime;
    }

    /**
     * customizable toast
     */
    private void toastMessage() {
        Toast.makeText(this, "Status erfolgreich aktualisiert!", Toast.LENGTH_SHORT).show();
    }
}
