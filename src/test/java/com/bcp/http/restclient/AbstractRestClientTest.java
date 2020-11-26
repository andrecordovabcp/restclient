package com.bcp.http.restclient;

import static org.junit.Assert.*;

import com.google.gson.reflect.TypeToken;
import com.bcp.http.restclient.response.RestResponse;
import com.bcp.http.restclient.response.handler.ResponseHandler;
import com.bcp.http.restclient.response.handler.ResponseHandlers;
import com.bcp.http.restclient.util.ObjectListParser;
import com.bcp.http.restclient.util.ObjectParser;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public abstract class AbstractRestClientTest {

  static final String API_URL = "https://api.octoperf.com/";
  static final int TIMEOUT = 8;
  static final Gson GSON = new Gson();
  private static final ObjectParser JSON_PARSER = new ObjectParser() {
    @Override
    public <T> T parse(Class<T> clazz, String data) {
      return GSON.fromJson(data, clazz);
    }
  };
  static final ResponseHandler<Usuario> RESPONSE_HANDLER =
    ResponseHandlers.object(Usuario.class, JSON_PARSER);

  

  void getAsserts(RestResponse<Usuario> response) {
    assertTrue("exito", response.isSuccessful());
    assertFalse("error", response.isErrorResponse());
  }
  
  void getAssertsFail(RestResponse<Usuario> response) {
    assertFalse("exito", response.isSuccessful());
    assertTrue("error", response.isErrorResponse());
  }

  void getAssertsToken(RestResponse<Usuario> response) {
    assertTrue("exito", response.isSuccessful());
    assertFalse("error", response.isErrorResponse());
    System.out.println(response.getData());
    assertNotNull("token",response.getData().getToken());
  }
  void getAssertsFailToken(RestResponse<Usuario> response) {
    assertFalse("exito", response.isSuccessful());
    assertTrue("error", response.isErrorResponse());
    assertNull(response.getData());
  }
    

  protected static class Usuario {
	  private String token;
	  public Usuario(String token) {
		  this.token=token;
	  }
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Usuario post = (Usuario) o;
      return 
        Objects.equals(token, post.token);
    }

    @Override
    public int hashCode() {
      return 1;
    }

    @Override
    public String toString() {
      return "Datas{" +
        "token='" + token + '\'' +
        '}';
    }
	  
  }
}
