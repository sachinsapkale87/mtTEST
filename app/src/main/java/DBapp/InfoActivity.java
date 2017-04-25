package DBapp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

/**
 * Created by sachin on 21/04/17.
 */
@DatabaseTable(tableName = "information")
public class InfoActivity {
    @DatabaseField(generatedId = true)
    public int _id;

    @DatabaseField
    public String uid;

    @DatabaseField
    public String name;

    @DatabaseField
    public int phone;

    public InfoActivity() {
    }

    public InfoActivity(JSONObject jsonObject) {
        uid = jsonObject.optString("uid");
        name = jsonObject.optString("name");
        phone = jsonObject.optInt("phone");
    }

    public enum InFo {
        UID("uid"),
        NAME("name"),
        PHONE("phone");

        private String name;

        InFo(final String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }
}
