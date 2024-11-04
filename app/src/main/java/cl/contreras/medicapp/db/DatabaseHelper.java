package cl.contreras.medicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarma.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_NAME = "alarmas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DOSIS = "dosis";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_FRECUENCIA = "frecuencia";
    private static final String COLUMN_HORA_INICIAL = "hora_inicial";

    private static final String TABLE_CONTACTOS = "contactos";
    public static final String COLUMN_ID_CONTACTO = "id"; // ID para contactos
    public static final String COLUMN_NOMBRE_CONTACTO = "nombre"; // Nombre del contacto
    public static final String COLUMN_TELEFONO = "telefono"; // Teléfono del contacto

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_DOSIS + " TEXT, " +
                COLUMN_STOCK + " TEXT, " +
                COLUMN_FRECUENCIA + " INTEGER, " +
                COLUMN_HORA_INICIAL + " TEXT)";
        db.execSQL(createTable);

        String createContactosTable = "CREATE TABLE " + TABLE_CONTACTOS + " (" +
                COLUMN_ID_CONTACTO + " INTEGER PRIMARY KEY, " +
                COLUMN_NOMBRE_CONTACTO + " TEXT, " +
                COLUMN_TELEFONO + " TEXT)";
        db.execSQL(createContactosTable);
        Log.d("DatabaseHelper", "Tabla de contactos creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public boolean addAlarma(String nombre, String dosis, String stock, int frecuencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_DOSIS, dosis);
        values.put(COLUMN_STOCK, stock);
        values.put(COLUMN_FRECUENCIA, frecuencia);

        // Registrar la hora actual como hora inicial
        String horaInicial = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        values.put(COLUMN_HORA_INICIAL, horaInicial);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean addContacto(String nombre, String telefono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_CONTACTO, nombre);
        values.put(COLUMN_TELEFONO, telefono);

        long result = db.insert(TABLE_CONTACTOS, null, values);
        return result != -1;
    }

    public Cursor getAllAlarmas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getAllContactos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CONTACTOS, null, null, null, null, null, null);
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

    public boolean eliminarContacto(int id) { // Método para eliminar contacto
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DatabaseHelper", "Eliminando contacto con ID: " + id);
        return db.delete(TABLE_CONTACTOS, COLUMN_ID_CONTACTO + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

}

