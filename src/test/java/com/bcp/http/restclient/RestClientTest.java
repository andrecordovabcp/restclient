package com.bcp.http.restclient;

import com.bcp.http.restclient.RestClient;
import com.bcp.http.restclient.request.RestRequest;
import com.bcp.http.restclient.request.body.BodyProcessors;
import com.bcp.http.restclient.response.RestResponse;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestClientTest extends AbstractRestClientTest {

  private final RestClient client = new RestClient(API_URL);
  private final String usuario_orig="andrecordovabcp@gmail.com";
  private final String password_orig="bcp2020.";
  private final String password_fail="bcp2020.2";
  
  @Test
  public void getUsuarioValido() {
	RestRequest request = RestRequest.builder("public/users/login")
	.parameter("password", password_orig)
	.parameter("username", usuario_orig)
	.POST()
    .build(); 	
    RestResponse<Usuario> response = client.execute(request, RESPONSE_HANDLER);
    getAsserts(response);
  }
  
  @Test
  public void getUsuarioInValido() {
	RestRequest request = RestRequest.builder("public/users/login")
	.parameter("password", password_fail)
	.parameter("username", usuario_orig)
	.POST()
    .build(); 	
    RestResponse<Usuario> response = client.execute(request, RESPONSE_HANDLER);
    getAssertsFail(response);
  }
  
  @Test
  public void getUsuarioValidoToken() {
	RestRequest request = RestRequest.builder("public/users/login")
	.parameter("password", password_orig)
	.parameter("username", usuario_orig)
	.POST()
    .build(); 	
    RestResponse<Usuario> response = client.execute(request, RESPONSE_HANDLER);
    getAssertsToken(response);
  }

  @Test
  public void getUsuarioInValidoToken() {
	RestRequest request = RestRequest.builder("public/users/login")
	.parameter("password", password_fail)
	.parameter("username", usuario_orig)
	.POST()
    .build(); 	
    RestResponse<Usuario> response = client.execute(request, RESPONSE_HANDLER);
    getAssertsFailToken(response);
  }
 
}
