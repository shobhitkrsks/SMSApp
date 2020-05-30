package com.example.smsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText recet,msget;
    Button sendbtn1,sendbtn2;
    int smspermid=0;
    String recphn,txtmsg;
    TextView encrypttv;
    CryptoUtil cryptoUtil=new CryptoUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recet=findViewById(R.id.recet);
        msget=findViewById(R.id.msget);
        sendbtn1=findViewById(R.id.sendbtn1);
        sendbtn2=findViewById(R.id.sendbtn2);
        encrypttv=findViewById(R.id.encrypttv);

        sendbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recphn=recet.getText().toString();
                txtmsg=msget.getText().toString();
                try {
                    txtmsg=cryptoUtil.encrypt(txtmsg);
                    encrypttv.setText(txtmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msget.clearFocus();
                checkPermission(Manifest.permission.SEND_SMS,smspermid);
            }
        });
        sendbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recphn=recet.getText().toString();
                txtmsg=msget.getText().toString();
                try {
                    txtmsg=cryptoUtil.encrypt(txtmsg);
                    encrypttv.setText(txtmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msget.clearFocus();
                sendSmsFunc2();
            }
        });
    }

    private void checkPermission(String sendSms, int smspermid) {
        if(ContextCompat.checkSelfPermission(MainActivity.this,sendSms)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{sendSms},smspermid);
        }
        else {
            sendSmsFunc1();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==smspermid)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                sendSmsFunc1();
            }
            else {
                Toast.makeText(MainActivity.this,"Permission not granted",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"Permission not granted",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSmsFunc1() {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(recphn,null,txtmsg,null,null);
        Toast.makeText(this,"Sending Message...",Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
    }

    private void sendSmsFunc2(){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("smsto:"));
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address",recphn);
        intent.putExtra("sms_body",txtmsg);


        startActivity(intent);
        Toast.makeText(this,"Opening Default SMS app",Toast.LENGTH_SHORT).show();
        finish();

    }

}
