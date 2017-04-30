package jp.yuta.kohashi.esc.network.api;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 30 / 04 / 2017
 */
public interface EscApiCallback<T> {
    void callback(T response);
}
