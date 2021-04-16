package com.ernet.qrscanner.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ernet.qrscanner.R;
import com.ernet.qrscanner.utilities.Status;
import com.google.firebase.database.*;

public class TypeProduktNummer extends AppCompatActivity {
    private EditText orderId;
    private TextView aktStatus;
    private static String id;
    Status status = new Status();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_produkt_nummer);

        Button cancelButton = (Button) findViewById(R.id.cancel);
        Button searchBtn = (Button) findViewById(R.id.submitBtn);
        orderId = (EditText) findViewById(R.id.inputNr);

        reference = FirebaseDatabase.getInstance().getReference("auftraege");
        final AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setMessage("Auftrag mit dem eingegebenen ID konnte nicht gefunden werden!")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setId(orderId.getText().toString());
                final Query query = reference.orderByChild("auftragId").equalTo(getId());
                status.destroyStatusContext();

                if( TextUtils.isEmpty(orderId.getText())){
                    orderId.setError( "Bitte ID eingeben" );
                } else {
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String vorname = dataSnapshot.child(getId()).child("vorname").getValue(String.class);
                                String nachname = dataSnapshot.child(getId()).child("nachname").getValue(String.class);
                                String auftragStatus = dataSnapshot.child(getId()).child("auftragStatus").getValue(String.class);
                                String description = dataSnapshot.child(getId()).child("description").getValue(String.class);
                                String auftragId = dataSnapshot.child(getId()).child("auftragId").getValue(String.class);

                                status.setName(vorname + " " + nachname);
                                status.setId(auftragId);
                                status.setStatus(auftragStatus);
                                finish();
                                startActivity(new Intent(TypeProduktNummer.this, StatusWindow.class));
                            } else {
                                final AlertDialog alertDialog = alertDialogBuilder.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public EditText getOrderId() {
        return orderId;
    }

    public void setOrderId(EditText orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        TypeProduktNummer.id = id;
    }
}
