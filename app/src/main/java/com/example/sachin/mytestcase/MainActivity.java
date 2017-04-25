package com.example.sachin.mytestcase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sachin on 12/04/17.
 */

public class MainActivity extends Activity{

    EditText ed_username,ed_password;
    Button btn;
    private Context mcontext;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mcontext = this;
        ed_username=(EditText) findViewById(R.id.u1);
        ed_password=(EditText) findViewById(R.id.p1);
        btn=(Button) findViewById(R.id.btn1);
       // presenter =new LoginPresenter(MainActivity.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        presenter.onloginclicked();
                int count=0;
                if(!TextUtils.isEmpty(ed_username.getText().toString())){
                   count=count+1;
                }else{
                    ed_username.setError("Username cannot be empty!");
                }

                if(!TextUtils.isEmpty(ed_password.getText().toString())){
                    count=count+1;
                }else{
                    ed_password.setError("Password cannot be empty!");
                }

                if(count==2){
                    if(ed_username.getText().toString().trim().equals("sachin") && ed_password.getText().toString().trim().equals("sachin")){
                        Toast.makeText(mcontext,"Login Success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mcontext,"Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public static int addNumber(int a,int b){
        return a+b;
    }

//    @Override
//    public String getUsername() {
//        return ed_username.getText().toString();
//    }
//
//    @Override
//    public void showUsernameError(int resId) {
//        ed_username.setError(getString(resId));
//    }
//
//    @Override
//    public String getPassword() {
//        return ed_password.getText().toString();
//
//    }
//
//    @Override
//    public void showpasswordError(int preId) {
//        ed_password.setError(getString(preId));
//    }
//
//    @Override
//    public void showToastMsg(String resid) {
//        Toast.makeText(mcontext,resid, Toast.LENGTH_SHORT).show();
//    }


    public boolean checklogin() {
        if(ed_username.getText().toString().trim().equals("sachin") && ed_password.getText().toString().trim().equals("sachin")){
          return true;
        }else{
            return false;
        }


    }
}
