package com.example.sachin.mytestcase;

import android.support.test.filters.SmallTest;

import junit.framework.TestCase;

/**
 * Created by sachin on 14/04/17.
 */

public class LoginTestcases extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testchecklogin() throws Exception{
        final MainActivity mainActivity = new MainActivity();
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isLoginSuccess = mainActivity.checklogin();
                assertEquals(true,isLoginSuccess);
            }
        });
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
