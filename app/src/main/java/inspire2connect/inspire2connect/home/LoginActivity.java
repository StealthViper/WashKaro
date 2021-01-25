package inspire2connect.inspire2connect.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final int MY_REQUEST_CODE = 2399;
    ConstraintLayout[] ll_but = new ConstraintLayout[10];

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

        ll_but[0] = findViewById(R.id.google_login_tile);
        ll_but[1] = findViewById(R.id.email_login_tile);
        ll_but[2] = findViewById(R.id.phone_login_tile);


        int[] btnToAdd = new int[]{0, 1, 2};

        for (int i = 0; i < btnToAdd.length; i++) {
            ll_but[btnToAdd[i]].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Intent i = null;

        switch(view.getId()){
            case R.id.google_login_tile:
                i = new Intent(LoginActivity.this, GoogleLogin.class);
                startActivity(i);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Loge("UPDATE_STATUS", "Update flow failed! Result code: " + resultCode);
            }
        }
    }
}
