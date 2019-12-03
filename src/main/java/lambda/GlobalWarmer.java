package lambda;

import com.amazonaws.services.lambda.*;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.Warmup;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GlobalWarmer implements RequestHandler<Void, String>
{
    LambdaLogger logger;
    @Override
    public String handleRequest(final Void input, final Context context)
    {
        logger = context.getLogger();

        return handle01(context);
    }

    private String handle01(final Context context)
    {
        AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();

        InvokeRequest req = new InvokeRequest()
                .withFunctionName("app-function01")
                //.withInvocationType(InvocationType.Event)
                .withPayload("{\"warmup\":\"true\", \"delay\":\"0\"}");

        //for(int i = 0; i < 10; i++){}
        Instant start = Instant.now();
        Future<InvokeResult> future_res = lambda.invokeAsync(req);
        Instant finish = Instant.now();

        long elapsed = Duration.between(start, finish).toMillis();
        logger.log(String.format("\n============== elapsed: %s\n", elapsed));

        logger.log("\nWaiting for async callback\n");
        int i = 0;
        while (!future_res.isDone() && !future_res.isCancelled()) {
            // perform some other tasks...
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                logger.log("\nThread.sleep() was interrupted!\n");
                System.exit(0);
            }
            i++;
            if(i == 20)
                break;
        }

        if(i == 20)
            logger.log("\n=========== i == 20");
        else{
            try {
                InvokeResult res = future_res.get();
                if (res.getStatusCode() == 200) {
                    System.out.println("\nLambda function returned:");
                    ByteBuffer response_payload = res.getPayload();
                    logger.log(String.format("\n=========== resp: %s\n", new String(response_payload.array())));
                }
                else {
                    System.out.format("Received a non-OK response from AWS: %d\n",
                            res.getStatusCode());
                }
            }
            catch (InterruptedException | ExecutionException e) {
                logger.log(e.getMessage());
            }
        }

        return "Warmer";
    }

    private String handle02(final Context context)
    {
        AWSLambda lambda = AWSLambdaClientBuilder.defaultClient();
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("app-function01")
                .withPayload("{\"warmup\":\"true\"}");

        req.setInvocationType(InvocationType.Event);    //async
        InvokeResult res = lambda.invoke(req);

        ByteBuffer bf = res.getPayload();
        logger.log(String.format("\n============= result call: %s\n", new String(bf.array())));
        return "Warmer";
    }

    //force paralellism
    private String handle03(final Context context)
    {
        AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();

        InvokeRequest req = new InvokeRequest()
                .withFunctionName("app-function01")
                .withPayload("{\"warmup\":\"true\", \"delay\":\"100\"}");

        List<Future> futures = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Future<InvokeResult> future_res = lambda.invokeAsync(req);
            futures.add(future_res);
        }

        return "Warmer";
    }
}
