package apimatic.core_lib.utilities;

import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_lib.CoreConfig;

public class MockCoreConfig  extends CompatibilityFactoryMock{


    @Rule
    public MockitoRule initRule = MockitoJUnit.rule(); 
    
    @Mock
    protected static CoreConfig.Builder mockCoreConfigBuilder;
    
    @Mock
    protected static CoreConfig mockCoreConfig;

  

    @Before
    public void setup() throws IOException {
        prepareMockCoreConfigBuilder();
        prepareMockCoreConfig();

    }

    public void prepareMockCoreConfigBuilder() {
        when(mockCoreConfigBuilder.baseUri(test -> getBaseUri(test))).thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.compatibilityFactory(compatibilityFactory)).thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.build()).thenReturn(mockCoreConfig);

    }

    private void prepareMockCoreConfig() {
        when(mockCoreConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
  
        when(mockCoreConfig.getGlobalHeaders()).thenReturn(httpHeaders);
        when(mockCoreConfig.getCompatibilityFactory()).thenReturn(compatibilityFactory);
    
        when(mockCoreConfig.getUserAgent()).thenReturn("APIMATIC3.0");
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }


}
