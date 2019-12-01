package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import model.ObjectInput;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Function03  extends AbstractFunction<ObjectInput, String>
{
    @Override
    public String handler(ObjectInput input, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log(String.format("\n==================== %s\n", this.getClass().getName()));
        logger.log(String.format("\n==================== input: %s\n", input));
        return "Done F3!";
    }
}
