package DBapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.sachin.mytestcase.MainActivity;
import com.example.sachin.mytestcase.R;

/**
 * Created by sachin on 21/04/17.
 */

public class SplashScreenActivity extends Activity  {

    private ProgressDialog progDailog;
    private Context mcontext;
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mcontext=this;
        IntentFilter intentFilter = new IntentFilter("INTENT_FILTER_CREATE_OR_DELTA_CALLS");
        registerReceiver(createTblBroadCastReceiver, intentFilter);

        relativeLayout = (RelativeLayout) findViewById(R.id.rel);
        relativeLayout.getBackground().setAlpha(150);
        imageView = (ImageView) findViewById(R.id.imgview);

        relativeLayout.setVisibility(View.GONE);
        Button buttonc = (Button) findViewById(R.id.btnc);
        Button buttonu = (Button) findViewById(R.id.btnup);
        buttonc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(mcontext, CreateDBintentService.class));
                relativeLayout.setVisibility(View.VISIBLE);
                showimage();
            }
        });
        buttonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                showimage();
                Intent msgIntent = new Intent(mcontext, LoadDeltaService.class);
                msgIntent.putExtra(LoadDeltaService.LOAD_DELTA_FROM_SPLASH, true);
                startService(msgIntent);
            }
        });

    }
    public void loadprogressbar() {
        progDailog = new ProgressDialog(mcontext);
        progDailog.setMessage("Loading, Please wait...");
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    public void stoploadprogress() {
        if (progDailog.isShowing() == true) {
            progDailog.dismiss();
        }
    }
    private BroadcastReceiver createTblBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    relativeLayout.setVisibility(View.GONE);
                    startActivity(new Intent(mcontext,Mainactivity.class));
                }
            },5000);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(createTblBroadCastReceiver);
    }

    public void showimage(){
        Glide.with(mcontext).load(R.raw.pigeon).into(imageView);
        imageView.setBackground(ContextCompat.getDrawable(mcontext,R.drawable.curve));
    }
}
