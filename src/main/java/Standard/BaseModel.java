package Standard;

import java.util.HashMap;
import java.util.Map;

import Connection.SQLGenerator;

public abstract class BaseModel {
    protected String table;
    protected int id;

    public BaseModel(String table) {
        this.table = table;
    }

    public BaseModel(String table, int id) {
        this.table = table;
        this.id = id;
        read();
    }

    public int create() {
        int generatedId = SQLGenerator.insertSQL(table, toMap());
        this.id = generatedId;
        return generatedId;
    }

    public void read() {
        if (this.id <= 0) {
            return;
        }

        Map<String, String> filters = new HashMap<>();
        String idTable = "id_" + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase();
        filters.put(idTable, String.valueOf(this.id));

        String[][] result = SQLGenerator.selectSQL(table, null, filters);

        if (result.length < 2) {
            return;
        }

        String[] columns = result[0];
        String[] values = result[1];

        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            data.put(columns[i], values[i]);
        }

        populate(data);
    }

    public void update() {
        SQLGenerator.updateSQL(table, this.id, toMap());
    }

    public void delete() {
        SQLGenerator.deleteSQL(table, this.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract Map<String, String> toMap();

    public abstract void populate(Map<String, String> data);
}
