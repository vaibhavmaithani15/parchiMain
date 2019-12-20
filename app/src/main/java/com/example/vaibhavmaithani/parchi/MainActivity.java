package com.example.vaibhavmaithani.parchi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    EditText mobileEidtText;
    TextInputLayout mobilenoLayout;
    Button loginBtn;
    Button googleBtn;
    String loginmobileNumber;
    CountryCodePicker cpp1;
    static final String TAG="MainActivity";
    static final int RC_SIGN_IN=0;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    private String phoneNumber;
    private String personName;
    private String personEmail;
    Uri personPhoto;
    String personId;
    Intent Firstintent;

    CircleImageView navigationImage;
    TextView navigationName;
    TextView navigationEmail;
    String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loginBtn = (Button) findViewById(R.id.loginbtn_id);
        googleBtn = (Button) findViewById(R.id.google_btn);
        mobileEidtText= (EditText)findViewById(R.id.mobileno_id);
        mobilenoLayout = (TextInputLayout) findViewById(R.id.mobileNo_TextInputLayout);
        loginmobileNumber=mobileEidtText.getText().toString();
        cpp1= (CountryCodePicker) findViewById(R.id.cpp1);
        mAuth= FirebaseAuth.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        navigationImage= (CircleImageView) findViewById(R.id.navigationimage_id);
        navigationName= (TextView) findViewById(R.id.navigationname_id);
        navigationEmail= (TextView) findViewById(R.id.navigationemail_id);




        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()!=null)
               {
                  Firstintent =new Intent(MainActivity.this,firstActivity.class);
                   startActivity(Firstintent);
               }
            }
        };
        mobileEidtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobileEidtText.getText().toString().isEmpty()) {
                    mobilenoLayout.setErrorEnabled(true);
                    mobilenoLayout.setError("Please Enter Your Mobile");
                } else {
                    mobilenoLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginmobileNumber = mobileEidtText.getText().toString();
                if (mobileEidtText.getText().toString().isEmpty()) {
                    mobilenoLayout.setErrorEnabled(true);
                    mobilenoLayout.setError("Please Enter Your Mobile");
                } else if(isValidPhone(loginmobileNumber)) {
                    phoneNumber = mobileEidtText.getText().toString().trim();
                    Intent intent=new Intent(MainActivity.this, Registration.class);
                    intent.putExtra("number",phoneNumber);
                    startActivity(intent);
                }else{
                    mobilenoLayout.setErrorEnabled(true);
                    mobilenoLayout.setError("Number Not Valid");
                }
                }

        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn()
    {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else
            {
                Toast.makeText(this, "Login Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user=mAuth.getCurrentUser();
                    updateUI(user);
                }
                if(!task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            personName = acct.getDisplayName();
            personPhoto= acct.getPhotoUrl();
            personEmail= acct.getEmail();
            personId= acct.getId();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            navigationName.setText(personName);


            Toast.makeText(this,"person name "+personName+ "person Name "+personGivenName +"perosn Family"+personFamilyName +"person Email "+personEmail+"person Id "+personId  , Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener !=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 10)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        super.onBackPressed();
    }
}
