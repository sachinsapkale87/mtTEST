package DBapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.sachin.mytestcase.R;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.util.List;

/**
 * Created by sachin on 21/04/17.
 */

public class Mainactivity extends Activity {

    private ProgressDialog progDailog;
    private Context mcontext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mcontext=this;

        List<InfoActivity> infoActivities = getInfoActivity();
        if(infoActivities!=null && infoActivities.size()>0){
            for(int i=0;i<infoActivities.size();i++){
                InfoActivity infoActivity = infoActivities.get(i);
                System.out.println("------"+infoActivity.name+"-|-"+infoActivity.phone);
            }
        }
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

    public List<InfoActivity> getInfoActivity() {
        List<DBapp.InfoActivity> playByPlayModels = null;
        try {
            String query = null;
            Dao<DBapp.InfoActivity, Integer> playByPlayDao = ORMliteHelper.getOrMliteHelper(mcontext).getInfoActivityDao();
            query = "select * from information";

            GenericRawResults<DBapp.InfoActivity> genericRawResults = playByPlayDao.queryRaw(query.trim(), playByPlayDao.getRawRowMapper());
            playByPlayModels = genericRawResults.getResults();
            genericRawResults.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return playByPlayModels;
    }
}
