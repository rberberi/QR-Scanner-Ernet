package com.ernet.qrscanner.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.ernet.qrscanner.MainActivity;
import com.ernet.qrscanner.R;
import com.ernet.qrscanner.utilities.Status;
import com.google.firebase.database.*;
import com.google.zxing.Result;

public class ScannerQR extends AppCompatActivity {
    private static String result_variable;
    Status status = new Status();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);
        status.destroyStatusContext();

        // the code below appears in onCreate() method
        if (ContextCompat.checkSelfPermission(ScannerQR.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(ScannerQR.this, new String[] {Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private CodeScanner mCodeScanner;

    private void startScanning() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        reference = FirebaseDatabase.getInstance().getReference("auftraege");

        final AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setMessage("Auftrag mit dem gescannten ID konnte nicht gefunden werden!")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                startActivity(new Intent(ScannerQR.this, MainActivity.class));
                            }
                        });

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScannerQR.this, result.getText(), Toast.LENGTH_SHORT).show();
                        setResult_variable(result.toString());

                        final Query query = reference.orderByChild("auftragId").equalTo(getResult_variable());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String vorname = dataSnapshot.child(getResult_variable()).child("vorname").getValue(String.class);
                                    String nachname = dataSnapshot.child(getResult_variable()).child("nachname").getValue(String.class);
                                    String auftragStatus = dataSnapshot.child(getResult_variable()).child("auftragStatus").getValue(String.class);
                                    String description = dataSnapshot.child(getResult_variable()).child("description").getValue(String.class);
                                    String auftragId = dataSnapshot.child(getResult_variable()).child("auftragId").getValue(String.class);

                                    status.setName(vorname + " " + nachname);
                                    status.setId(auftragId);
                                    status.setStatus(auftragStatus);
                                    finish();
                                    startActivity(new Intent(ScannerQR.this, StatusWindow.class));
                                }
                                else {
                                    final AlertDialog alertDialog = alertDialogBuilder.show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    public String getResult_variable() {
        return result_variable;
    }

    private void setResult_variable(String result_variable) {
        ScannerQR.result_variable = result_variable;
    }

}
