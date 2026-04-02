import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example core library using production dependencies from maven_core.
 */
public class CoreLib {
    private static final Logger logger = LoggerFactory.getLogger(CoreLib.class);

    public void process(String input) {
        var items = Lists.newArrayList(input.split(","));
        logger.info("Processing {} items", items.size());
    }
}