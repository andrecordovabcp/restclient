package com.bcp.http.restclient;

import com.bcp.http.restclient.request.RestRequest;
import com.bcp.http.restclient.response.RestResponse;
import com.bcp.http.restclient.response.handler.ResponseHandler;
import com.bcp.http.restclient.response.handler.ResponseHandlers;

/**
 * Class that sends synchronous REST requests
 */
public class RestClient extends AbstractRestClient {

  /**
   *
   * @param baseUrl the base url of the rest api
   */
  public RestClient(String baseUrl) {
    super(baseUrl);
  }

  /**
   * Execute an http request
   * @param request the request to execute
   * @return the response of the server
   */
  public RestResponse<Void> execute(RestRequest request) {
    return execute(request, ResponseHandlers.noResponse());
  }

  /**
   * Execute an http request
   * @param request the request to execute
   * @param responseHandler the response handler
   * @return the response of the server
   */
  public <T> RestResponse<T> execute(RestRequest request, ResponseHandler<T> responseHandler) {
    return doExecute(request, responseHandler);
  }

}
