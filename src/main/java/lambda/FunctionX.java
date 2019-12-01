package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.Warmup;

public class FunctionX implements RequestHandler<Warmup, String>
{

    @Override
    public String handleRequest(Warmup input, Context context)
    {
        LambdaLogger logger = context.getLogger();
        return "Done!";
    }
}
