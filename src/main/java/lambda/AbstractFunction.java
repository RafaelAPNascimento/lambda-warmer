package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.Warmup;

public abstract class AbstractFunction<I,O> implements RequestHandler<I,O>
{
    @Override
    public O handleRequest(I input, Context context)
    {
        if (input != null && Warmup.class.isInstance(input) && Warmup.class.cast(input).isWarmup())
        {
            return (O) "Request feita pelo warmup";
        }
        return handler(input, context);
    }

    public abstract O handler(I input, Context context);
}
