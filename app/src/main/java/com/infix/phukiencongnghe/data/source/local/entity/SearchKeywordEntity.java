package com.infix.phukiencongnghe.data.source.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_keyword")
public class SearchKeywordEntity {

    @PrimaryKey
    @NonNull
    private String keyword;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public SearchKeywordEntity(
            @NonNull String keyword,
            long createdAt
    ) {
        this.keyword = keyword;
        this.createdAt = createdAt;
    }

    @NonNull
    public String getKeyword() {
        return keyword;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setKeyword(
            @NonNull String keyword
    ) {
        this.keyword = keyword;
    }

    public void setCreatedAt(
            long createdAt
    ) {
        this.createdAt = createdAt;
    }
}