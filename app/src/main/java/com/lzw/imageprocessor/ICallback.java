package com.lzw.imageprocessor;

/**
 * 回调接口
 */
public interface ICallback {
    void onSuccess(String result);
    void onFailed(String error);
}
