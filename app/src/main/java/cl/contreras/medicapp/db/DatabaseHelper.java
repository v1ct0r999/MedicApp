package cl.contreras.medicapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarma.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "alarmas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DOSIS = "dosis";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_FRECUENCIA = "frecuencia";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_DOSIS + " TEXT NOT NULL, " +
                COLUMN_STOCK + " TEXT NOT NULL, " +
                COLUMN_FRECUENCIA + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addAlarma(String nombre, String dosis, String stock, int frecuencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_DOSIS, dosis);
        values.put(COLUMN_STOCK, stock);
        values.put(COLUMN_FRECUENCIA, frecuencia);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllAlarmas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public int getLastInsertedId() {
        int lastId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        if (cursor != null) {
            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        return lastId;
    }


    public boolean eliminarAlarma(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public Alarmas detalleAlarma(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Alarmas alarma = null;
        Cursor cursorAlarmas;

        cursorAlarmas = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = " + id, null);

        if (cursorAlarmas.moveToFirst()) {
            alarma = new Alarmas();
            alarma.setId(cursorAlarmas.getInt(0));
            alarma.setNombre(cursorAlarmas.getString(1));
            alarma.setDosis(cursorAlarmas.getString(2));
            alarma.setStock(cursorAlarmas.getString(3));
            alarma.setFrecuencia(cursorAlarmas.getString(4));

        }

        cursorAlarmas.close();
        return alarma;
    }

    //como somos autistas tenemos que hacer 3 veces la funcion por ahora porque no tengo tiempo :)
    public boolean editarALarmaDosis(int id, String dosis) {

        boolean correcto = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE" + TABLE_NAME + " SET dosis = '" + dosis + "'");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();;
        }

        return correcto;

    }

    public boolean editarALarmaStock(int id, String stock) {

        boolean correcto = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE" + TABLE_NAME + " SET stock = '" + stock + "'");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();;
        }

        return correcto;

    }

    public boolean editarALarmaFrecuencia(int id, int frecuencia) {

        boolean correcto = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE" + TABLE_NAME + " SET frecuencia = '" + frecuencia + "' ");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();;
        }

        return correcto;

    }

}

