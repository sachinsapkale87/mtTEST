package DBapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.util.List;

/**
 * Created by sachin on 21/04/17.
 */

public class CreateDBintentService extends IntentService implements Createtables.INotifyCreateDBService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private final static String TAG = CreateDBintentService.class.getName();
    public CreateDBintentService(String name) {
        super(name);
    }
    public CreateDBintentService() {
        super(TAG);
    }

    @Override
    public void notifyCreateTable() {
        if(isTableCreated(this)){
            sendBroadcast(new Intent("INTENT_FILTER_CREATE_OR_DELTA_CALLS"));
        }else{
            showAlert();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Createtables(this);
    }

    public boolean isTableCreated(Context context) {

        List<InfoActivity> resultNames = null;
        try {
            Dao<InfoActivity, Integer> modelDao = ORMliteHelper.getOrMliteHelper(context).getInfoActivityDao();
            String rawQuery = " SELECT * FROM information";
            GenericRawResults<InfoActivity> genericRawResults = modelDao.queryRaw(rawQuery, modelDao.getRawRowMapper());
            resultNames = genericRawResults.getResults();
            genericRawResults.close();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return resultNames != null && resultNames.size() > 0;


    }
    private void showAlert() {

        Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
    }
}
