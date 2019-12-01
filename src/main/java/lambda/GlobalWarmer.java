package lambda;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.Duration;
import java.time.Instant;

public class GlobalWarmer implements RequestHandler<Void, String>
{
    @Override
    public String handleRequest(final Void input, final Context context)
    {
        final AWSLambda lambda = AWSLambdaClientBuilder.defaultClient();
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("app-test-function01")
                .withInvocationType(InvocationType.Event)   //async
                .withPayload("{\"warmup\":\"true\"}");

        Instant start = Instant.now();

        lambda.invoke(req);

        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        return "Aquecimento dos Lambdas";
    }
}
