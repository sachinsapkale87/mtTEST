package com.example.sachin.mytestcase;

/**
 * Created by sachin on 12/04/17.
 */

public interface LoginView {

    String getUsername();

    void showUsernameError(int resId);

    String getPassword();

    void showpasswordError(int preId);

    void showToastMsg(String rest);
}
