package appcentralpet.com.newcentralpet.BancoMeusPets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;

/**
 * Created by L Moraes on 28/08/2017.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements Serializable {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String sexo, String raca, String tipo, String idade, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PET VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, sexo);
        statement.bindString(3, raca);
        statement.bindString(4, tipo);
        statement.bindString(5, idade);
        statement.bindBlob(6, image);

        statement.executeInsert();
    }

    public void updateData(String name, String sexo, String raca, String tipo, String idade, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE PET SET name = ?, sexo = ?, raca = ?, tipo = ?, idade = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, sexo);
        statement.bindString(3, raca);
        statement.bindString(4, tipo);
        statement.bindString(5, idade);
        statement.bindBlob(6, image);
        statement.bindDouble(7, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM PET WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
