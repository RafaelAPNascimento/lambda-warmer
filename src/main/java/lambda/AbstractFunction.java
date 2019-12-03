package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.Warmup;

public abstract class AbstractFunction<I,O> implements RequestHandler<I,O>
{
    @Override
    public O handleRequest(I input, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log(String.format("\n================ input: %s\n", input));

        if (input != null && Warmup.class.isInstance(input) && Warmup.class.cast(input).isWarmup())
        {
            Warmup warmup = (Warmup) input;
            logger.log(String.format("\n============= %s input: %s\n", this.getClass().getName(), input.toString()));

            if(warmup.getDelay() > 0)
            {
                logger.log(String.format("\n================ delay: %s\n", warmup.getDelay()));
                try {
                    Thread.sleep(warmup.getDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return (O) "Request feita pelo warmup";
        }
        return handler(input, context);
    }

    public abstract O handler(I input, Context context);
}
