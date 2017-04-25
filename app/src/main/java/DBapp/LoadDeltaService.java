package DBapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltFetchSchemaCallback;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;

/**
 * Created by dipak on 7/4/16.
 */
public class LoadDeltaService extends IntentService {

    private static final String TAG = LoadDeltaService.class.getName();
    private int count;
    private HashMap<String, String> hashMapdeltaCall;
    private Context context;
    private ORMliteHelper ormliteHelper;
    private Handler handler = new Handler();
    public static final String LOAD_DELTA_FROM_SPLASH = "FROM_SPLASH";
    private boolean isFromSplash = false;
    private SharedPreferences sharedpreferences;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public LoadDeltaService() {
        super(LoadDeltaService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            isFromSplash = intent.getBooleanExtra(LOAD_DELTA_FROM_SPLASH, false);

            context = this;
            //make call for getting all schema of classes.
            hashMapdeltaCall = new HashMap<>();
            builtCallForAllClassesSchema();


    }


    private void builtCallForAllClassesSchema() {
        BuiltApplication builtApplication = null;
        try {
            builtApplication = Built.application(context, "bltbe7ff5cbe052b0b1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        builtApplication.fetchLastActivities(new BuiltFetchSchemaCallback() {
            @Override
            public void onCompletion(BuiltConstant.BuiltResponseType responseType, Object o, BuiltError builtError) {

                if (builtError == null) {
                    try {

                        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) o;
                        //o = null;

                        if (stringObjectHashMap != null && stringObjectHashMap.size() > 0) {
                            //System.out.println("--------key|"+stringObjectHashMap);


                            for (String key : stringObjectHashMap.keySet()) {
                                //System.out.println("-------key|"+key+"|---|"+stringObjectHashMap.get(key));
                                if(stringObjectHashMap.get(key) != null && !stringObjectHashMap.get(key).toString().equals("null"))
                                    setTimeForDeltaCalls(key, (String) stringObjectHashMap.get(key));
                            }

                            stringObjectHashMap.clear();

                            count = hashMapdeltaCall.size();

                            if (count > 0) {

                                ormliteHelper = ORMliteHelper.getOrMliteHelper(context);
                              //  modelParser = ModelParser.getInstance(context);
                                startDeltaCalls(hashMapdeltaCall);

                            } else {

                                count = 1;
                                sendBroadCastInfo("#No delta found in class");
                            }
                        }

                    } catch (Exception e) {
                        count = 1;
                        sendBroadCastInfo("Exception:" + e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    count = 1;
                    sendBroadCastInfo("Error:" + builtError.getErrorMessage());
                }
            }

        });
    }

    private void sendBroadCastInfo(String val) {

        //System.out.println("-------------sendBroadCastInfo|"+val);

        if (count == 1) {
            Intent localIntent = new Intent("INTENT_FILTER_CREATE_OR_DELTA_CALLS");
            localIntent.putExtra(LOAD_DELTA_FROM_SPLASH, isFromSplash);
            hashMapdeltaCall.clear();
            sendBroadcast(localIntent);
        } else {
            count--;
        }
    }

    private void startDeltaCalls(final HashMap<String, String> uidHash) {

        for (String uid : uidHash.keySet()) {
            try {

                deltaCall(uidHash.get(uid).toString(), uid);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deltaCall(final String last_updated_time, final String classUID) {
        BuiltApplication builtApplication=null;
        try {
             builtApplication = Built.application(context, "bltbe7ff5cbe052b0b1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BuiltQuery builtQuery = builtApplication.classWithUid(classUID).query();
        String delta = null;

//        System.out.println("-------deltaCall|"+classUID);


        switch (classUID) {
            case "information": {
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                String date =prefs.getString("dt","");
                delta = shared.getPref("dt",context);//delta time
            //    System.out.println("-----delta previous date----"+delta);
                break;
            }
        }
        builtQuery.allDeltaAt(delta);
        if (delta != null) {
            builtQuery.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResult, BuiltError builtError) {

                    if (builtError == null) {

                        MyDbParam myDbParam = new MyDbParam(queryResult, classUID);
                        new CreateDbAsync().execute(myDbParam);

                        switch (classUID) {
                            case "information": {
                                 shared.putPref("dt",last_updated_time,context);
               //                  System.out.println("-----saving last updated time----"+last_updated_time);
                                break;
                            }
                        }

                    } else {
                        sendBroadCastInfo(classUID + " call Failed");
                    }
                }

            });
        }

    }


    public class CreateDbAsync extends AsyncTask<MyDbParam, Void, String> {


        @Override
        protected String doInBackground(MyDbParam... params) {
            BuiltQueryResult queryResult = params[0].queryResult;
            String controller = params[0].controller;

            List<BuiltObject> updatedAtList = queryResult.updatedAt();
            List<BuiltObject> deletedAtList = queryResult.deletedAt();
            //List<BuiltObject> createAtList = queryResult.createdAt();


            switch (controller) {
                case "information": {

                    if (deletedAtList != null && deletedAtList.size() > 0) {
                        for (BuiltObject bo : isSafe(deletedAtList)) {
                            deleteAll((bo.toJSON().optString("uid")));
                        }
                        deletedAtList.clear();
                    }

                    if (updatedAtList != null && updatedAtList.size() > 0) {
                        createInfoTaskTable(updatedAtList,true);
                        updatedAtList.clear();
                    }

                   /* if (createAtList != null && createAtList.size() > 0) {
                        modelParser.fillAboutTable(createAtList, true);
                        createAtList.clear();
                    }*/
                    break;
                }

            }

            return controller;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                sendBroadCastInfo(s + " call is completed");
            }

        }
    }

    public class MyDbParam {

        private final BuiltQueryResult queryResult;
        private final String controller;

        public MyDbParam(BuiltQueryResult queryResult, String controller) {
            this.queryResult = queryResult;
            this.controller = controller;
        }

    }


    private void setTimeForDeltaCalls(String uid, String last_activity) {
        try {

            // System.out.println("-------uid|"+uid+"|---|"+last_activity);

            Date calendar = parse(last_activity);



            if (calendar != null) {


                if (uid.equals("information")) {
                    String date= shared.getPref("dt",context);
                 //   System.out.println("-----last-saving--"+date+"-|-lastsavingbuilt--"+last_activity);
                    if (calendar.after(parse(date))) {
                        hashMapdeltaCall.put(uid, last_activity);
                    }

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Date parse(String date) throws ParseException {
        Date s = null;
        SimpleDateFormat old = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            //s = old.parse(date);
            s = old.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;

    }
    public void  deleteAll(String u_id) {


        try {
            DeleteBuilder<InfoActivity, Integer> deleteBuilder = ORMliteHelper.getOrMliteHelper(context).getInfoActivityDao().deleteBuilder();
            deleteBuilder.where().eq(InfoActivity.InFo.UID.getValue(), u_id);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public void createInfoTaskTable(final List<BuiltObject> builtObjectsList, final boolean isFromUpdate) {
        if (isFromUpdate){
            for (BuiltObject bo : isSafe(builtObjectsList)) {
                deleteAll((bo.toJSON().optString("uid")));
            }

        }
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
    public static <T> Iterable<T> isSafe(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

}
