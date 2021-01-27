package inspire2connect.inspire2connect.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ConstraintLayout signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private  String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;
    private static final int MY_REQUEST_CODE = 2399;
    ConstraintLayout[] ll_but = new ConstraintLayout[10];
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();



        ll_but[0] = findViewById(R.id.email_login_tile);
        ll_but[1] = findViewById(R.id.phone_login_tile);
        ll_but[2] = findViewById(R.id.google_login_tile);


        int[] btnToAdd = new int[]{0, 1, 2};

        for (int i = 0; i < btnToAdd.length; i++) {
            ll_but[btnToAdd[i]].setOnClickListener(this);
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            progressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, homeActivity.class));
        }
        progressDialog.dismiss();

        signInButton = findViewById(R.id.google_login_tile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1086619162320-gft5b3p1lckcshbrf14v6dih8rgu2r55.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    public void onClick(View view) {
        Intent i = null;

        switch(view.getId()){
            case R.id.google_login_tile:
                signIn();
                break;

            case R.id.email_login_tile:
                i = new Intent(LoginActivity.this, EmailLogin.class);
                startActivity(i);
                break;

            case R.id.phone_login_tile:
                i = new Intent(LoginActivity.this, PhoneLogin.class);
                startActivity(i);
                break;
        }

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, homeActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, homeActivity.class);
            startActivity(i);
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent i = new Intent(LoginActivity.this, homeActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }
}
