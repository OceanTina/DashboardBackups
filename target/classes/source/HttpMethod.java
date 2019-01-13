package source;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpMethod {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String HEAD = "HEAD";
    String OPTIONS = "OPTIONS";
    String PATCH = "PATCH";

    String value();
}
