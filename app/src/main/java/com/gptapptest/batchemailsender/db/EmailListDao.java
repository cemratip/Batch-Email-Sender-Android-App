package com.gptapptest.batchemailsender.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmailListDao {
    @Query("SELECT * FROM EmailList")
    List<EmailList> getAllEmailLists();

    @Insert
    void insertEmailList(EmailList... emailList);

    @Delete
    void delete(EmailList emailList);
}
