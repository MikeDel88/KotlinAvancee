package androidkotlin.formation.kotlinavancee

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DataBase(context: Context): SQLiteOpenHelper(context, "test.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //
    }

    fun createUser(user: UserDB) {
        val values = ContentValues()
        values.put("name", user.name)
        values.put("age", user.age)
        values.put("email", user.email)

        writableDatabase.insert("users", null, values)
    }

    fun getUsersCount(): Int = DatabaseUtils.queryNumEntries(readableDatabase, "users", null ).toInt()
    fun getAllUsers(): MutableList<UserDB>  {
        val users = mutableListOf<UserDB>()

       readableDatabase.rawQuery("SELECT * FROM users", null).use { cursor ->
           while(cursor.moveToNext()) {
               val user = UserDB(
                   cursor.getString(cursor.getColumnIndexOrThrow("name")),
                   cursor.getInt(cursor.getColumnIndexOrThrow("age")),
                   cursor.getString(cursor.getColumnIndexOrThrow("email")),
               )
               users.add(user)
           }
        }

        return users
    }
}