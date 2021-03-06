package com.bcp.http.restclient;

import com.bcp.http.restclient.request.RestRequest;
import com.bcp.http.restclient.response.RestResponse;
import com.bcp.http.restclient.response.handler.ResponseHandler;
import com.bcp.http.restclient.response.handler.ResponseHandlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that sends asynchronous REST requests
 */
public class AsyncRestClient extends AbstractRestClient {

  private final ExecutorService executor;

  public AsyncRestClient(String baseUrl) {
    this(baseUrl, 1);
  }

  /**
   *
   * @param baseUrl the base url of the rest api
   * @param nbThreads the number of possible simultaneous requests
   */
  public AsyncRestClient(String baseUrl, int nbThreads) {
    this(baseUrl, Executors.newFixedThreadPool(nbThreads));
  }

  /**
   *
   * @param baseUrl the base url of the rest api
   * @param executor the executor that will execute the requests
   */
  public AsyncRestClient(String baseUrl, ExecutorService executor) {
    super(baseUrl);
    this.executor = executor;
  }

  /**
   * Execute asynchronously the given request
   * @param request the request
   */
  public void execute(final RestRequest request) {
    execute(request, ResponseHandlers.noResponse(), null);
  }

  public <T> void execute(final RestRequest request,
                                              final ResponseHandler<T> successResponseHandler,
                                              final Callback<T> callback) {
    executor.submit(new Runnable() {
      @Override
      public void run() {
        RestResponse<T> response = doExecute(request, successResponseHandler);
        if (callback != null) callback.call(response);
      }
    });
  }

  public void shutDown() {
    if (executor != null) {
      executor.shutdown();
    }
  }

  public interface Callback<T> {
    void call(RestResponse<T> response);
  }
}
