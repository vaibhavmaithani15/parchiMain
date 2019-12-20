package com.example.vaibhavmaithani.parchi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {

    public EditText mobileNumberEditText;
    public EditText otpNumber;
    public Button  signInButton;
    public ProgressBar pb;
    private String verficationId;
    private String mobileNumber;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mobileNumberEditText= (EditText) findViewById(R.id.mobileNumber_id);
        otpNumber= (EditText) findViewById(R.id.otp_id);
        signInButton= (Button) findViewById(R.id.restigerbtn_id);
        pb= (ProgressBar) findViewById(R.id.progressbar);
        String phone=getIntent().getStringExtra("number");
        mobileNumberEditText.setFocusable(false);
        mobileNumberEditText.setEnabled(false);
        mobileNumberEditText.setCursorVisible(false);
        mobileNumberEditText.setKeyListener(null);
        mobileNumberEditText.setBackgroundColor(Color.TRANSPARENT);
        Bundle bundle=getIntent().getExtras();
        mobileNumber=bundle.getString("number");
        mobileNumberEditText.setText("+91"+mobileNumber);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sendVerificationCode(phone);
        mAuth=FirebaseAuth.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otpNumber.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    otpNumber.setError("Enter code...");
                    otpNumber.requestFocus();
                    return;
                }

                verifyCode(code);

            }
        });
    };



    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //start the profile activity
                    Intent intent=new Intent(Registration.this,firstActivity.class);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verficationId,code);
        signInWithCredential(credential);
    }
    //OTP Send Code
    private void sendVerificationCode(String number){
        pb.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verficationId=s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
          String  code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                otpNumber.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }};

}