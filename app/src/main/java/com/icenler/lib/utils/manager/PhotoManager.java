package com.icenler.lib.utils.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册图片获取
 */
public class PhotoManager {

    private Context mContext;
    private ContentResolver mContentResolver;
    private List<String> mBucketIds;

    public PhotoManager(Context context) {
        this.mContext = context;
        mContentResolver = mContext.getContentResolver();
        mBucketIds = new ArrayList<>();
    }

    public List<Album> getAlbum() {
        mBucketIds.clear();

        List<Album> data = new ArrayList<>();
        String projects[] = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projects
                , null
                , null
                , MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Album album = new Album();

                String buckedId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

                if (mBucketIds.contains(buckedId))
                    continue;

                mBucketIds.add(buckedId);

                String buckedName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String coverPath = getFrontCoverData(buckedId);

                album.setId(buckedId);
                album.setName(buckedName);
                album.setRealPath(coverPath);

                data.add(album);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return data;

    }

    public List<Photo> getPhoto(String buckedId) {
        List<Photo> photos = new ArrayList<>();

        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED}
                , MediaStore.Images.Media.BUCKET_ID + "=?"
                , new String[]{buckedId}
                , MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

                photos.add(new Photo(path, dataAdded, dataModified));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return photos;
    }


    private String getFrontCoverData(String bucketId) {
        String path = "empty";
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media.BUCKET_ID + "=?", new String[]{bucketId}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }


    public class Photo implements Parcelable {

        private String path;
        private long dataAdded;
        private long dataModified;

        public Photo(String path, long dataAdded, long dataModified) {
            this.path = path;
            this.dataAdded = dataAdded;
            this.dataModified = dataModified;
        }

        protected Photo(Parcel in) {
            path = in.readString();
            dataAdded = in.readLong();
            dataModified = in.readLong();
        }

        public Creator<Photo> CREATOR = new Creator<Photo>() {
            @Override
            public Photo createFromParcel(Parcel in) {
                return new Photo(in);
            }

            @Override
            public Photo[] newArray(int size) {
                return new Photo[size];
            }
        };

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getDataAdded() {
            return dataAdded;
        }

        public void setDataAdded(long dataAdded) {
            this.dataAdded = dataAdded;
        }

        public long getDataModified() {
            return dataModified;
        }

        public void setDataModified(long dataModified) {
            this.dataModified = dataModified;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(path);
            dest.writeLong(dataAdded);
            dest.writeLong(dataModified);
        }
    }

    public class Album {

        private String id;          // 相册ID
        private String name;        // 相册名称
        private String realPath;    // 路径

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRealPath() {
            return realPath;
        }

        public void setRealPath(String realPath) {
            this.realPath = realPath;
        }

    }

}
