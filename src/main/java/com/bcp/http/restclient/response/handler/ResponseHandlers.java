package com.bcp.http.restclient.response.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.bcp.http.restclient.util.BytesContainer;
import com.bcp.http.restclient.util.IOUtils;
import com.bcp.http.restclient.util.ObjectListParser;
import com.bcp.http.restclient.util.ObjectParser;
import com.bcp.http.restclient.util.ObjectSetParser;

/**
 * Util class implementing different {@link ResponseHandler}
 */
public final class ResponseHandlers {

  private static final ResponseHandler<String> STRING_HANDLER =
      new ResponseHandler<String>() {
        @Override
        public String convert(InputStream inputStream) throws IOException {
          return IOUtils.toString(inputStream);
        }
  };

  private static final ResponseHandler<BytesContainer> BYTES_HANDLER =
      new ResponseHandler<BytesContainer>() {
        @Override
        public BytesContainer convert(InputStream inputStream) throws IOException {
          return IOUtils.toByteArray(inputStream);
        }
  };

  private static final ResponseHandler<Integer> INT_HANDLER =
          new ResponseHandler<Integer>() {
            @Override
            public Integer convert(InputStream inputStream) throws IOException {
              return Integer.parseInt(STRING_HANDLER.convert(inputStream));
            }
  };

  private static final ResponseHandler<Void> NO_RESPONSE = new ResponseHandler<Void>() {
    @Override
    public Void convert(InputStream inputStream) {
      return null;
    }
  };

  private ResponseHandlers() {}

  public static ResponseHandler<String> string() {
    return STRING_HANDLER;
  }

  public static ResponseHandler<Integer> integer() {
    return INT_HANDLER;
  }

  public static <T extends Enum<T>> ResponseHandler<T> enumeration(final Class<T> clazz) {
    return new ResponseHandler<T>() {
      @Override
      public T convert(InputStream inputStream) throws IOException {
        String name = STRING_HANDLER.convert(inputStream)
          .replace("\"", ""); //in case it is a string representation
        return Enum.valueOf(clazz, name);
      }
    };
  }

  /**
   * Response handler returning raw response into a byte array
   * @return response handler converting to byte array
   */
  public static ResponseHandler<BytesContainer> bytes() {
    return BYTES_HANDLER;
  }

  /**
   * Response handler returning multipart response into a byte array
   * @return response handler converting multipart response to byte array
   */
  public static ResponseHandler<BytesContainer> multipartBytes() {
    return bytes();
  }

  /**
   * handler writing response into a file
   * @param file the file to write the response content to
   * @return response handler writing in given file
   */
  public static ResponseHandler<File> multipartFile(File file) {
    return multipartFile(file, IOUtils.DEFAULT_BUFFER_SIZE);
  }

  /**
   * handler writing response into a file
   * @param file the file to write the response content to
   * @param bufferSize the buffer size
   * @return response handler writing in given file
   */
  public static ResponseHandler<File> multipartFile(final File file,
                                                    final int bufferSize) {
    return new ResponseHandler<File>() {
      @Override
      public File convert(InputStream inputStream) throws IOException {
        if (!file.exists() && !file.createNewFile()) {
          throw new IOException("Couldn't create new file");
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
          IOUtils.copy(inputStream, fos, bufferSize);
        }
        return file;
      }
    };
  }

  /**
   * handler writing response into a file
   * @param filePath the path of the file to write the response content to
   * @return response handler writing in given file
   */
  public static ResponseHandler<File> multipartFile(String filePath) {
    return multipartFile(filePath, IOUtils.DEFAULT_BUFFER_SIZE);
  }

  /**
   * handler writing response into a file
   * @param filePath the path of the file to write the response content to
   * @param bufferSize the buffer size
   * @return response handler writing in given file
   */
  public static ResponseHandler<File> multipartFile(final String filePath,
                                                    final int bufferSize) {
    return new ResponseHandler<File>() {
      @Override
      public File convert(InputStream inputStream) throws IOException {
        File file = new File(filePath);
        if (!file.exists() && !file.createNewFile()) {
          return null;
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
          IOUtils.copy(inputStream, fos, bufferSize);
        }
        return file;
      }
    };
  }

  /**
   * handler converting response into an object
   * @param tClass the class o the object
   * @param parser the parser
   * @param <T> the type of the object
   * @return handler converting the response into an object
   */
  public static <T> ResponseHandler<T> object(final Class<T> tClass,
                                              final ObjectParser parser) {
    return new ResponseHandler<T>() {
      @Override
      public T convert(InputStream inputStream) throws IOException {
        return parser.parse(tClass, STRING_HANDLER.convert(inputStream));
      }
    };
  }

  /**
   * handler converting response into a list of objects
   * @param tClass the class of the objects
   * @param parser the parser
   * @param <T> the type of the object
   * @return handler converting the response into a list of objects
   */
  public static <T> ResponseHandler<List<T>> objectList(final Class<T> tClass,
                                                        final ObjectListParser parser) {
    return new ResponseHandler<List<T>>() {
      @Override
      public List<T> convert(InputStream inputStream) throws IOException {
        return parser.parse(tClass, STRING_HANDLER.convert(inputStream));
      }
    };
  }

  /**
   * handler converting response into a set of objects
   * @param tClass the class of the objects
   * @param parser the parser
   * @param <T> the type of the object
   * @return handler converting the response into a set of objects
   */
  public static <T> ResponseHandler<Set<T>> objectSetHandler(final Class<T> tClass,
      final ObjectSetParser parser) {
    return new ResponseHandler<Set<T>>() {
      @Override
      public Set<T> convert(InputStream inputStream) throws IOException {
        return parser.parse(tClass, STRING_HANDLER.convert(inputStream));
      }
    };
  }

  /**
   * handler for ignoring the response
   * @return a handler ignoring the response
   */
  public static ResponseHandler<Void> noResponse() {
    return NO_RESPONSE;
  }
}
