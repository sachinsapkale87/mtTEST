package DBapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;

/**
 * Created by nitesh on 11/6/15.
 */
public class Createtables {

    private int callCount = 1; //total count of calls.
    private Context context;
    private INotifyCreateDBService iNotifyCreateDBService;
    private SharedPreferences sharedpreferences;

    public Createtables(Context context) {

        this.context = context;
        this.iNotifyCreateDBService = (INotifyCreateDBService) context;

        createTable("information");

    }


    private void createTable(final String value) {
        try {
            BuiltApplication builtApplication = Built.application(context, "bltbe7ff5cbe052b0b1");
            BuiltQuery builtQuery = builtApplication.classWithUid(value).query();
            builtQuery.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResult, BuiltError builtError) {

                    if (builtError == null) {

                        List<BuiltObject> builtObjectsList = queryResult.getResultObjects();
                        MyDbParam myDbParam = new MyDbParam(builtObjectsList, value);
                        new CreateDbAsync().execute(myDbParam);

                    } else {
                        updateStatus(value + " call failed ");
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void updateStatus(String name) {

        //to show the status of code
        --callCount;
        callsCompleted(name);
    }

    private void callsCompleted(String menuClassUid) {

        if (callCount == 0) {
            callCount = -1;

            //  AppSettings.setTimeZone(context, AppUtils.getCurrentTimeZone());
           // System.out.println("---All calls completed--");
            iNotifyCreateDBService.notifyCreateTable();
            //to test the querys after all the insertion is completed.

        } else {
          //  System.out.println("----||class||" + menuClassUid + "  " + callCount + " remaining");
        }
    }


    public interface INotifyCreateDBService {
        void notifyCreateTable();
    }


    public class CreateDbAsync extends AsyncTask<MyDbParam, Void, String> {


        @Override
        protected String doInBackground(MyDbParam... params) {
            List<BuiltObject> builtObjectsList = params[0].builtObjects;
            String controller = params[0].controller;

            switch (controller) {
                case "information": {
                    createInfoTaskTable(builtObjectsList, false);
                    break;
                }
            }

            return controller;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (s != null) {

                switch (s) {

                    case "information": {
                        sharedpreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("dt", getUTCTime());
                        shared.putPref("dt",getUTCTime(),context);
                        updateStatus("information class" + " call completed@--"+getUTCTime());
                        break;
                    }
                }
            }
        }
    }

    public class MyDbParam {

        private final List<BuiltObject> builtObjects;
        private final String controller;

        public MyDbParam(List<BuiltObject> builtObjects, String controller) {
            this.builtObjects = builtObjects;
            this.controller = controller;
        }

    }

    public void createInfoTaskTable(final List<BuiltObject> builtObjectsList, final boolean isFromUpdate) {
        final Dao<InfoActivity, Integer> teamSeasonAverageModelDao = ORMliteHelper.getOrMliteHelper(context).getInfoActivityDao();
        try {
            teamSeasonAverageModelDao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (BuiltObject builtObject : builtObjectsList) {
                        if (builtObject != null) {
                            teamSeasonAverageModelDao.createOrUpdate(new InfoActivity(builtObject.toJSON()));
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAll() {

        try {
            ORMliteHelper.getOrMliteHelper(context).getInfoActivityDao().deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<InfoActivity> getInfoActivity() {
        List<InfoActivity> playByPlayModels = null;
        try {
            String query = null;
            Dao<InfoActivity, Integer> playByPlayDao = ORMliteHelper.getOrMliteHelper(context).getInfoActivityDao();
            query = "select * from information";

            GenericRawResults<InfoActivity> genericRawResults = playByPlayDao.queryRaw(query.trim(), playByPlayDao.getRawRowMapper());
            playByPlayModels = genericRawResults.getResults();
            genericRawResults.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return playByPlayModels;
    }

    public static String getUTCTime() {

        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(utcTimeZone);
        sdf.format(calendar.getTime());
        return sdf.format(calendar.getTime());
    }

}

