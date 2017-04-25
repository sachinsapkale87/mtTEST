package com.example.sachin.mytestcase;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sachin on 12/04/17.
 */
public class LoginTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public LoginTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testElements() throws Exception{
        EditText editTextu1 = (EditText) getActivity().findViewById(R.id.u1);
        EditText editTextp1 = (EditText) getActivity().findViewById(R.id.p1);
        Button btn = (Button) getActivity().findViewById(R.id.btn1);

        assertNotNull(editTextu1);
        assertNotNull(editTextp1);
        assertNotNull(btn);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}