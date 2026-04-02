import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Example test using testing dependencies from maven_testing.
 */
public class ExampleTest {
    @Mock
    private CoreLib mockCoreLib;

    @Test
    public void testCoreLibInitialization() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(mockCoreLib);
        verify(mockCoreLib, never()).process(anyString());
    }
}