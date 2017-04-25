package DBapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by sachin on 21/04/17.
 */

public class ORMliteHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "sachintest.db";
    private final Context mcontext;
    private final String DB_PATH;
    private static ORMliteHelper ormLiteHelper  = null;
    private Dao<InfoActivity, Integer> informationDAO;


    public static ORMliteHelper getOrMliteHelper(Context context){
        if(ormLiteHelper==null){
            ormLiteHelper = new ORMliteHelper(context);
        }
        return ormLiteHelper;
    }

    public ORMliteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mcontext = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
//        System.out.println("---------db_path----"+DB_PATH);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try{
            TableUtils.createTableIfNotExists(connectionSource,InfoActivity.class);
        }catch (Exception e){
            e.printStackTrace();
       //     System.out.println("----ormlitehelper---oncreate---exception--"+e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try{

            TableUtils.dropTable(connectionSource,InfoActivity.class,true);
        }catch (Exception e){
            e.printStackTrace();
      //      System.out.println("----ormlitehelper---onUpgrade---exception--"+e.getMessage());
        }
    }


    public Dao<InfoActivity, Integer> getInfoActivityDao(){

        if (informationDAO == null){
            try {
                informationDAO = getDao(InfoActivity.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return informationDAO;
    }
    public void createDataBase() {

        int version = 1;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
//
        if (version == myDataBase.getVersion()) {
      //      System.out.println("------newDB is up to date----");
        } else {
//
//              //  mcontext.startService(new Intent(mcontext, CreateDBintentService.class));
                mcontext.deleteDatabase(DATABASE_NAME);
                mcontext.startService(new Intent(mcontext, CreateDBintentService.class));
        }
    }

}
