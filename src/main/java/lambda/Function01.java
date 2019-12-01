package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import model.ObjectInput;

public class Function01 extends AbstractFunction<ObjectInput, String>
{

    @Override
    public String handler(ObjectInput input, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log(String.format("\n==================== %s\n", this.getClass().getName()));
        logger.log(String.format("\n==================== input: %s\n", input));
        return "Done F1!";
    }
}
