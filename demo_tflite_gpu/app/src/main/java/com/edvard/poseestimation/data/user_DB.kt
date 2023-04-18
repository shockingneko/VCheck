package com.edvard.poseestimation.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.edvard.poseestimation.R
import java.text.SimpleDateFormat

class user_DB(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VER) {

    override fun onCreate(db: SQLiteDatabase?) {
        val user_table_query = ("CREATE TABLE " + user_table + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_PASSWORD + " TEXT,"
                + COL_NAME + " TEXT," + COL_IMAGE +" INTEGER," + COL_HEIGHT +" INTEGER," + COL_QUESTION + " TEXT," + COL_ANSWER + " TEXT)")
        db!!.execSQL(user_table_query)
        val history_table_query = ("CREATE TABLE if not exists $history_table(${COL_ID} INTEGER,${COL_PROJECT} TEXT,${COL_TIME} TEXT,${COL_RESULT_ID} INTEGER)")
        db.execSQL(history_table_query)
        val degree_result_table_query = ("CREATE TABLE if not exists $degree_table(${COL_RESULT_ID} INTEGER,${COL_EYE} INTEGER,${COL_DEGREE_RESULT} DOUBLE,${COL_RESULT} TEXT)")
        db.execSQL(degree_result_table_query)
        val colorblind_result_table_query = ("CREATE TABLE if not exists $colorblind_table(${COL_RESULT_ID} INTEGER,${COL_WEAK_GREEN} INTEGER,${COL_WEAK_RED} INTEGER,${COL_RESULT} TEXT)")
        db.execSQL(colorblind_result_table_query)
        val other_result_table_query = ("CREATE TABLE if not exists $other_result_table(${COL_PROJECT} TEXT,${COL_RESULT_ID} INTEGER,${COL_LEFT_EYE} INTEGER,${COL_RIGHT_EYE} INTEGER,${COL_RESULT} TEXT)")
        db.execSQL(other_result_table_query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    //create result data
    fun degree_result_data(){
        val db = this.writableDatabase
        var degree = 0.0

        for (i in 0..12){
            val value_right = ContentValues()
            val value_left = ContentValues()

            value_right.put(COL_RESULT_ID, i)
            value_left.put(COL_RESULT_ID, i + 13)

            value_right.put(COL_EYE, 0)
            value_left.put(COL_EYE, 1)

            value_right.put(COL_DEGREE_RESULT, degree)
            value_left.put(COL_DEGREE_RESULT, degree)

            if(degree == 0.0){
                value_right.put(COL_RESULT, "右眼 小於0.1")
                value_left.put(COL_RESULT, "左眼 小於0.1")
            }
            else if(degree == 1.2){
                value_right.put(COL_RESULT, "右眼 大於等於1.2")
                value_left.put(COL_RESULT, "左眼 大於等於1.2")
            }
            else{
                value_right.put(COL_RESULT, "右眼 $degree")
                value_left.put(COL_RESULT, "左眼 $degree")
            }

            val cursor_right = db.rawQuery("SELECT * FROM $degree_table WHERE result_id = $i", null)
            if(cursor_right.count <= 0){
                db.insert(degree_table,null, value_right)
            }
            val cursor_left = db.rawQuery("SELECT * FROM $degree_table WHERE result_id = ${i + 13}", null)
            if(cursor_left.count <= 0){
                db.insert(degree_table,null, value_left)
            }
            degree += 0.1
        }
        db.close()
    }

    fun colorblind_result_data(){
        val green_disease = arrayOf("","可能有綠色色弱","可能有綠色色盲")
        val red_disease = arrayOf("","可能有紅色色弱","可能有紅色色盲")
        var counter = 0

        val db = this.writableDatabase
        for(green_counter in 0..2){
            for(red_counter in 0..2){
                var outcome = "您"
                if(green_counter == 0 && red_counter == 0){
                    outcome += "辨色能力正常"
                }
                else if(green_counter == 0){
                    outcome += red_disease[red_counter] + " "
                }
                else{
                    outcome += green_disease[green_counter] + " " + red_disease[red_counter]
                }

                val values = ContentValues()
                values.put(COL_RESULT_ID, counter)
                values.put(COL_WEAK_GREEN, green_counter)
                values.put(COL_WEAK_RED, red_counter)
                values.put(COL_RESULT, outcome)

                if(db.rawQuery("SELECT * FROM $colorblind_table WHERE result_id = $counter", null).count <= 0)
                    db.insert(colorblind_table,null, values)

                counter ++
            }
        }
        db.close()
    }

    fun other_result_data(){
        val db = this.writableDatabase
        val project = arrayOf("散光", "對比度", "黃斑部病變", "老花眼")
        val asti_result = arrayOf("雙眼有尚未矯正的散光度數","右眼有尚未矯正的散光度數","左眼有尚未矯正的散光度數","雙眼沒有明顯尚未矯正的散光度數")
        val cont_result = arrayOf("正常", "異常")
        val macu_result = arrayOf("雙眼可能有黃斑部病變", "右眼可能有黃斑部病變", "左眼可能有黃斑部病變", "雙眼並沒有黃斑部病變")
        val pres_result = arrayOf("雙眼可能有老花眼", "右眼可能有老花眼", "左眼可能有老花眼", "雙眼並沒有老花眼")
        var asti_counter = 0
        var cont_counter = 0
        var macu_counter = 0
        var pres_counter = 0

        for(left in 0..1){
            for(right in 0..1){
                val values = ContentValues()
                values.put(COL_PROJECT, project[0])
                values.put(COL_RESULT_ID, asti_counter)
                values.put(COL_LEFT_EYE, left)
                values.put(COL_RIGHT_EYE, right)
                values.put(COL_RESULT, asti_result[left*2 + right])

                if(db.rawQuery("SELECT * FROM $other_result_table WHERE project = '${project[0]}' AND result_id = $asti_counter", null).count <= 0)
                    db.insert(other_result_table,null, values)

                asti_counter ++
            }
        }

        for(cont in 0..1){
            val values = ContentValues()
            values.put(COL_PROJECT, project[1])
            values.put(COL_RESULT_ID, cont_counter)
            values.put(COL_LEFT_EYE, cont)
            values.put(COL_RIGHT_EYE, cont)
            values.put(COL_RESULT, cont_result[cont])

            if(db.rawQuery("SELECT * FROM $other_result_table WHERE project = '${project[1]}' AND result_id = $asti_counter", null).count <= 0)
                db.insert(other_result_table,null, values)

            cont_counter ++
        }

        for(left in 0..1){
            for(right in 0..1){
                val values = ContentValues()
                values.put(COL_PROJECT, project[2])
                values.put(COL_RESULT_ID, macu_counter)
                values.put(COL_LEFT_EYE, left)
                values.put(COL_RIGHT_EYE, right)
                values.put(COL_RESULT, macu_result[left*2 + right])

                if(db.rawQuery("SELECT * FROM $other_result_table WHERE project = '${project[2]}' AND result_id = $asti_counter", null).count <= 0)
                    db.insert(other_result_table,null, values)

                macu_counter ++
            }
        }

        for(left in 0..1){
            for(right in 0..1){
                val values = ContentValues()
                values.put(COL_PROJECT, project[3])
                values.put(COL_RESULT_ID, pres_counter)
                values.put(COL_LEFT_EYE, left)
                values.put(COL_RIGHT_EYE, right)
                values.put(COL_RESULT, pres_result[left*2 + right])

                if(db.rawQuery("SELECT * FROM $other_result_table WHERE project = '${project[3]}' AND result_id = $asti_counter", null).count <= 0)
                    db.insert(other_result_table,null, values)

                pres_counter ++
            }
        }
    }

    fun check_result_data(): Boolean{
        val db = this.writableDatabase
        val cursor1 = db.rawQuery("SELECT * FROM $degree_table", null)
        val cursor2 = db.rawQuery("SELECT * FROM $colorblind_table", null)
        val cursor3 = db.rawQuery("SELECT * FROM $other_result_table", null)

        return cursor1.count > 0 && cursor2.count > 0 && cursor3.count > 0
    }

    fun calling_degree_result(result: search_degree_result): search_degree_result{
        val selectQuery = "SELECT * FROM $degree_table WHERE eye = ${result.eye} AND degree_result = ${result.degree_result}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.count > 0){
            cursor.moveToFirst()
            result.result_id = cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))
            result.result = cursor.getString(cursor.getColumnIndex(COL_RESULT))
        }
        return result
    }

    fun calling_colorblind_result(result:search_colorblind_result): search_colorblind_result{
        val selectQuery = "SELECT * FROM $colorblind_table WHERE weak_red = ${result.weak_red} AND weak_green = ${result.weak_green}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.count > 0){
            cursor.moveToFirst()
            result.result_id = cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))
            result.result = cursor.getString(cursor.getColumnIndex(COL_RESULT))
        }
        return result
    }

    fun calling_other_result(result: search_other_result): search_other_result{
        val selectQuery = "SELECT * FROM $other_result_table WHERE project = '${result.project}' AND left_eye = ${result.left_eye} AND right_eye = ${result.right_eye}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.count > 0){
            cursor.moveToFirst()
            result.result_id = cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))
            result.result = cursor.getString(cursor.getColumnIndex(COL_RESULT))
        }
        return result
    }

    //user
    val allpeoplename:List<personal>
        get(){
            val lstperson = ArrayList<personal>()
            val selectQuery = "SELECT * FROM $user_table"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            val null_person = personal(-1, "--------", R.drawable.idk, 0, "")
            lstperson.add(null_person)
            if (cursor.moveToFirst()){
                do {
                    val person = personal()
                    person.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    person.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    person.image = cursor.getInt(cursor.getColumnIndex(COL_IMAGE))
                    person.height = cursor.getInt(cursor.getColumnIndex(COL_HEIGHT))
                    person.password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))
                    lstperson.add(person)
                }while (cursor.moveToNext())
            }
            db.close()
            return lstperson
        }

    fun getpersonname(name: String): Boolean{
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $user_table WHERE name = '$name'", null)
        return cursor.count <= 0
    }

    fun getperson(id: Int): person_total_data {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $user_table WHERE id = $id", null)
        cursor.moveToFirst()
        val person = person_total_data()
        if(cursor.count > 0){
            person.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            person.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            person.image = cursor.getInt(cursor.getColumnIndex(COL_IMAGE))
            person.height = cursor.getInt(cursor.getColumnIndex(COL_HEIGHT))
            person.password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))
            person.forget_question = cursor.getString(cursor.getColumnIndex(COL_QUESTION))
            person.forget_answer = cursor.getString(cursor.getColumnIndex(COL_ANSWER))
        }
        return person
    }

    fun addperson(person: person_total_data){
        val db = this.writableDatabase
        val usersize = 10

        var id_num_array = IntArray(usersize)
        for(i in 1..usersize){
            id_num_array[i-1] = i
        }
        var idrandom_counter = 0
        for(j in 1..usersize){
            val cursor = db.rawQuery("SELECT $COL_ID FROM $user_table WHERE id = $j", null)
            if(cursor.count <= 0){
                var id_swap = id_num_array[j-1]
                id_num_array[j-1] = id_num_array[idrandom_counter]
                id_num_array[idrandom_counter] = id_swap
                idrandom_counter ++
            }
        }
        if(idrandom_counter > 0){
            var id_num = (Math.random()*idrandom_counter).toInt()

            val values = ContentValues()
            values.put(COL_ID, id_num_array[id_num])
            values.put(COL_NAME, person.name)
            values.put(COL_IMAGE, person.image)
            values.put(COL_HEIGHT, person.height)
            values.put(COL_PASSWORD, person.password)
            values.put(COL_QUESTION, person.forget_question)
            values.put(COL_ANSWER, person.forget_answer)

            db.insert(user_table,null, values)
            db.close()
        }
    }

    fun updateperson(person: personal):Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, person.name)
        values.put(COL_IMAGE, person.image)
        values.put(COL_HEIGHT, person.height)
        values.put(COL_PASSWORD, person.password)

        return db.update(user_table, values, "$COL_ID = ?", arrayOf(person.id.toString()))
    }

    //history
    fun all_data(id: Int):List<history>{
        val person = ArrayList<history>()
        val selectQuery = "SELECT * FROM $history_table WHERE id = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToLast()){
            do {
                val history = history()
                history.project = cursor.getString(cursor.getColumnIndex(COL_PROJECT))
                history.time = cursor.getString(cursor.getColumnIndex(COL_TIME))
                if(history.project == "度數"){
                    val deg_cursor = db.rawQuery("SELECT $COL_RESULT FROM $degree_table WHERE result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                    if(deg_cursor.count > 0)
                        deg_cursor.moveToFirst()
                        history.result = deg_cursor.getString(deg_cursor.getColumnIndex(COL_RESULT))
                }
                else if(history.project == "色弱"){
                    val col_cursor = db.rawQuery("SELECT $COL_RESULT FROM $colorblind_table WHERE result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                    if(col_cursor.count > 0)
                        col_cursor.moveToFirst()
                        history.result = col_cursor.getString(col_cursor.getColumnIndex(COL_RESULT))
                }
                else{
                    val other_cursor = db.rawQuery("SELECT $COL_RESULT FROM $other_result_table WHERE project = '${history.project}' AND result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                    if(other_cursor.count > 0)
                        other_cursor.moveToFirst()
                        history.result = other_cursor.getString(other_cursor.getColumnIndex(COL_RESULT))
                }
                //history.result = cursor.getString(cursor.getColumnIndex(COL_RESULT))

                person.add(history)
            }while (cursor.moveToPrevious())
        }
        db.close()
        return person
    }

    fun all_project_data(project_name: String, id: Int): ArrayList<history> {
        val person = ArrayList<history>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $history_table WHERE id = $id AND project = '$project_name'", null)
        if(cursor.count > 0){
            if (cursor.moveToLast()){
                do {
                    val history = history()
                    history.project = cursor.getString(cursor.getColumnIndex(COL_PROJECT))
                    history.time = cursor.getString(cursor.getColumnIndex(COL_TIME))
                    if(history.project == "度數"){
                        val deg_cursor = db.rawQuery("SELECT $COL_RESULT FROM $degree_table WHERE result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                        if(deg_cursor.count > 0)
                            deg_cursor.moveToFirst()
                            history.result = deg_cursor.getString(deg_cursor.getColumnIndex(COL_RESULT))
                    }
                    else if(history.project == "色弱"){
                        val col_cursor = db.rawQuery("SELECT $COL_RESULT FROM $colorblind_table WHERE result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                        if(col_cursor.count > 0)
                            col_cursor.moveToFirst()
                            history.result = col_cursor.getString(col_cursor.getColumnIndex(COL_RESULT))
                    }
                    else{
                        val other_cursor = db.rawQuery("SELECT $COL_RESULT FROM $other_result_table WHERE project = '${history.project}' AND result_id = ${cursor.getInt(cursor.getColumnIndex(COL_RESULT_ID))}", null)
                        if(other_cursor.count > 0)
                            other_cursor.moveToFirst()
                            history.result = other_cursor.getString(other_cursor.getColumnIndex(COL_RESULT))
                    }

                    person.add(history)
                }while (cursor.moveToPrevious())
            }
            db.close()
        }
        return person
    }

    fun add_data(history: history){
        val db = this.writableDatabase
        val values = ContentValues()

        var nowtimeformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var nowtime = nowtimeformat.format(System.currentTimeMillis())

        values.put(COL_ID, history.id)
        values.put(COL_PROJECT, history.project)
        values.put(COL_TIME, nowtime)
        values.put(COL_RESULT_ID, history.result_id)

        db.insert(history_table,null, values)
        db.close()
    }

    /*fun deletedata(id: String, time: String){
        val db = this.writableDatabase
        set_table_name(id)
        db.delete(table_name,"${COL_TIME} = ?", arrayOf(time))
        db.close()
    }*/

    fun delete_account(id: Int){
        //val db = this.writableDatabase
        //val cursor = db.rawQuery("SELECT * FROM $history_table WHERE id = $id", null)
        val db = this.writableDatabase

        db.delete(history_table,"$COL_ID = ?", arrayOf(id.toString()))
        db.delete(user_table,"$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        /*val db = this.writableDatabase
        var DROP_TABLE = "DROP TABLE IF EXISTS " + table_name;
        db.execSQL(DROP_TABLE);*/

    }

    companion object{
        private val DATABASE_NAME = "user.db"
        private val DATABASE_VER = 1

        private val user_table = "personal"
        private val COL_ID = "id"
        private val COL_PASSWORD = "password"
        private val COL_NAME = "name"
        private val COL_IMAGE = "image"
        private val COL_HEIGHT = "height"
        private val COL_QUESTION = "question"
        private val COL_ANSWER = "answer"

        private val history_table = "history"
        private val COL_PROJECT = "project"
        private val COL_TIME = "time"
        private val COL_RESULT_ID = "result_id"

        private val degree_table = "度數"
        //result id
        private val COL_EYE = "eye"
        private val COL_DEGREE_RESULT = "degree_result"
        private val COL_RESULT = "result"

        private val colorblind_table = "色弱"
        //result id
        private val COL_WEAK_RED = "weak_red"
        private val COL_WEAK_GREEN = "weak_green"
        //result

        private val other_result_table = "其他"
        //project
        //result id
        private val COL_LEFT_EYE = "left_eye"
        private val COL_RIGHT_EYE = "right_eye"
        //result
    }
}