package com.example.demovideocalling.lib;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.example.demovideocalling.WebSocketTransport;

import org.json.JSONObject;
import org.mediasoup.droid.Logger;


import io.reactivex.Observable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Protoo extends PeerProto {

  private static final String TAG = "Protoo";

  public interface RequestGenerator {
    void request(JSONObject req);
  }

  public Protoo(@NonNull WebSocketTransport transport, @NonNull Listener listener) {
    super(transport, listener);
  }

  public Observable<String> request(String method) {
    return request(method, new JSONObject());
  }

  public Observable<String> request(String method, @NonNull RequestGenerator generator) {
    JSONObject req = new JSONObject();
    generator.request(req);
    return request(method, req);
  }

  public Observable<String> request(String method, @NonNull JSONObject data) {
    Logger.d(TAG, "request(), method: " + method);
    return Observable.create(
        emitter ->
            request(
                method,
                data,
                new ClientRequestHandler() {
                  @Override
                  public void resolve(String data) {
                    if (!emitter.isDisposed()) {
                      emitter.onNext(data);
                    }
                  }

                  @Override
                  public void reject(long error, String errorReason) {
                    if (!emitter.isDisposed()) {
                      emitter.onError(new ProtooException(error, errorReason));
                    }
                  }
                }));
  }

  @WorkerThread
  public String syncRequest(String method) throws ProtooException {
    return syncRequest(method, new JSONObject());
  }

  @WorkerThread
  public String syncRequest(String method, @NonNull RequestGenerator generator)
      throws ProtooException {
    JSONObject req = new JSONObject();
    generator.request(req);
    return syncRequest(method, req);
  }

  @WorkerThread
  public String syncRequest(String method, @NonNull JSONObject data) throws ProtooException {
    Logger.d(TAG, "syncRequest(), method: " + method);

    try {
      return request(method, data).blockingFirst();
    } catch (Throwable throwable) {
      throw new ProtooException(-1, throwable.getMessage());
    }
  }
}
