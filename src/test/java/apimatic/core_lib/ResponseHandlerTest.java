package apimatic.core_lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import apimatic.core_lib.utilities.MockCoreRequest;
import io.apimatic.core_interfaces.http.CoreHttpContext;
import io.apimatic.core_interfaces.http.CoreHttpMethod;
import io.apimatic.core_interfaces.http.HttpCallback;
import io.apimatic.core_interfaces.http.HttpClient;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.request.ResponseClassType;
import io.apimatic.core_interfaces.http.response.ApiResponseType;
import io.apimatic.core_interfaces.http.response.CoreHttpResponse;
import io.apimatic.core_interfaces.http.response.DynamicType;
import io.apimatic.core_lib.ApiCall;
import io.apimatic.core_lib.CoreResponseHandler;
import io.apimatic.core_lib.ErrorCase;
import io.apimatic.core_lib.types.ApiException;

public class ResponseHandlerTest extends MockCoreRequest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private ApiCall.Builder<?, ?> mockApiCallBuilder;

    @Mock
    private ApiCall<?, ?> mockApiCall;

    @Mock
    private HttpClient client;

    @Mock
    private CoreResponseHandler<?, ?> responseHandler;

    @Mock
    private CoreHttpResponse coreHttpResponse;

    @Mock
    private CoreHttpContext context;

    @Mock
    private HttpCallback httpCallback;

    @Mock
    private DynamicType dynamicType;

    @Mock
    private ApiResponseType<String> apiResponseType;

    @Before
    public void setup() throws IOException {
        setExpectations();
    }


    @Test
    public void testDeserializerMethod() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>()
                        .deserializer(string -> new String(string)).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig),
                "bodyValue");
    }

    @Test
    public void testDynamicResponseTypeMethod() throws IOException, ApiException {
        CoreResponseHandler<DynamicType, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<DynamicType, ApiException>()
                        .responseClassType(ResponseClassType.DYNAMIC_RESPONSE).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig),
                dynamicType);
    }


    @Test
    public void testApiResponseTypeMethod() throws IOException, ApiException {
        CoreResponseHandler<ApiResponseType<String>, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<ApiResponseType<String>, ApiException>()
                        .responseClassType(ResponseClassType.API_RESPONSE).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(201);
        when(coreHttpResponse.getHeaders()).thenReturn(httpHeaders);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig),
                apiResponseType);
    }

    @Test
    public void testDefaultTypeMethod() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>().build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(201);
        // verify
        assertNull(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig));
    }

    @Test
    public void testNullify404() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>().nullify404(true).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(404);

        // verify
        assertNull(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig));
    }

    @Test
    public void testNullify404False() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>().nullify404(false)
                        .globalErrorCase(getGlobalErrorCases()).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(404);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig);
        });

        String expectedMessage = "Not found";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testLocalException() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>()
                        .localErrorCase("403",
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new ApiException(reason, context)))
                        .build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(403);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig);
        });

        String expectedMessage = "Forbidden";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testDefaultException() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>()
                        .localErrorCase("403",
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new ApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(199);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig);
        });

        String expectedMessage = "Invalid response.";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testDefaultException1() throws IOException, ApiException {
        // when
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>()
                        .localErrorCase("403",
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new ApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();


        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(209);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig);
        });

        String expectedMessage = "Invalid response.";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    public void testGlobalException() throws IOException, ApiException {
        CoreResponseHandler<String, ApiException> coreResponseHandler =
                new CoreResponseHandler.Builder<String, ApiException>()
                        .globalErrorCase(getGlobalErrorCases()).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(400);


        ApiException apiException = assertThrows(ApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockCoreConfig);
        });

        String expectedMessage = "Bad Request";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    private Map<String, ErrorCase<ApiException>> getGlobalErrorCases() {

        Map<String, ErrorCase<ApiException>> globalErrorCase = new HashMap<>();
        globalErrorCase.put("400", ErrorCase.create("Bad Request",
                (reason, context) -> new ApiException(reason, context)));

        globalErrorCase.put("404", ErrorCase.create("Not found",
                (reason, context) -> new ApiException(reason, context)));

        globalErrorCase.put(ErrorCase.DEFAULT, ErrorCase.create("Invalid response.",
                (reason, context) -> new ApiException(reason, context)));

        return globalErrorCase;

    }

    private void prepareCoreConfigStub() throws IOException {
        when(mockCoreConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(mockCoreConfig.getGlobalHeaders()).thenReturn(httpHeaders);
        when(mockCoreConfig.getCompatibilityFactory()).thenReturn(compatibilityFactory);
        // when(mockCoreConfig.getHttpCallback()).thenReturn(httpCallback);
    }

    private void setExpectations() throws IOException {
        prepareCoreConfigStub();
        prepareCompatibilityStub();

        when(context.getResponse()).thenReturn(coreHttpResponse);
    }

    private void prepareCompatibilityStub() {
        when(compatibilityFactory.createHttpHeaders(anyMap())).thenReturn(httpHeaders);
        when(compatibilityFactory.createHttpRequest(any(CoreHttpMethod.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), any(Object.class)))
                        .thenReturn(coreHttpRequest);
        when(compatibilityFactory.createHttpRequest(any(CoreHttpMethod.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(coreHttpRequest);
        when(compatibilityFactory.createHttpContext(coreHttpRequest, coreHttpResponse))
                .thenReturn(context);

        when(compatibilityFactory.createDynamicResponse(coreHttpResponse)).thenReturn(dynamicType);

        when(compatibilityFactory.createAPiResponse(any(int.class), any(HttpHeaders.class),
                any(String.class))).thenReturn(apiResponseType);
    }


}

