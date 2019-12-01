package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public class Function01 extends AbstractFunction<String, String>
{

    @Override
    public String handler(String input, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log(String.format("\n==================== %s\n", this.getClass().getName()));
        logger.log(String.format("\n==================== input: %s\n", input));
        return "Done F1!";
    }
}
