package com.sdc.threadservicedemo;

/**
 * Created by intern on 2018/3/2.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
