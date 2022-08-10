package com.gptapptest.batchemailsender.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EmailList {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "listName")
    public String listName;

    @ColumnInfo(name = "list")
    public String list;
}
