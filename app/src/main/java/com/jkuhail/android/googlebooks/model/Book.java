package com.jkuhail.android.googlebooks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String bookTitle;
    private String authorName;
    private String publishedDate;
    private String description;
    private int pageCount;
    private String categories;
    private String image;
    private String url;
    private Book book;



    public Book(String bookTitle, String authorName, String publishedDate, String description, int pageCount, String categories, String image, String url) {
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.categories = categories;
        this.image = image;
        this.url = url;
    }

    public Book getBook(){return book;}

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookTitle);
        dest.writeString(this.authorName);
        dest.writeString(this.publishedDate);
        dest.writeString(this.description);
        dest.writeInt(this.pageCount);
        dest.writeString(this.categories);
        dest.writeString(this.image);
        dest.writeString(this.url);
    }

    public Book(Parcel in) {
        this.bookTitle = in.readString();
        this.authorName = in.readString();
        this.publishedDate = in.readString();
        this.description = in.readString();
        this.pageCount = in.readInt();
        this.categories = in.readString();
        this.image = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
