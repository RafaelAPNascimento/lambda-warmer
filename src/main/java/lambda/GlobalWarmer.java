package lambda;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.lambda.*;
import com.amazonaws.services.lambda.model.*;
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

    private class AsyncLambdaHandler implements AsyncHandler<InvokeRequest, InvokeResult>
    {
        @Override
        public void onError(Exception e)
        {
            logger.log(String.format("========= Erro chama async concluida com erro: %s\n",e.getMessage()));
        }

        @Override
        public void onSuccess(InvokeRequest request, InvokeResult invokeResult)
        {
            logger.log(String.format("========= OK chama async concluida: %s\n",request.getFunctionName()));
        }
    }

    @Override
    public String handleRequest(final Void input, final Context context)
    {
        logger = context.getLogger();

        return handle01(context);
    }

    private String handle01(final Context context)
    {
        AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();

        ListFunctionsResult listResult = lambda.listFunctions();
        List<FunctionConfiguration> list = listResult.getFunctions();

        for (FunctionConfiguration f : list)
        {
            logger.log(String.format("\n=========== name: %s\n", f.getFunctionName()));
            logger.log(String.format("\n=========== arn: %s\n", f.getFunctionArn()));
        }
        logger.log("\n==");

        InvokeRequest req = new InvokeRequest()
                .withFunctionName("app-function01")
                .withQualifier("stg")
                //.withInvocationType(InvocationType.Event)
                .withPayload("{\"warmup\":\"true\", \"delay\":\"0\"}");

        Instant start = Instant.now();
        Future<InvokeResult> future_res = lambda.invokeAsync(req, new AsyncLambdaHandler());

        logger.log("\n========= Waiting for async callback\n");

        while (!future_res.isDone() && !future_res.isCancelled()) {
            // perform some other tasks...
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.log("\nThread.sleep() was interrupted!\n");
                System.exit(0);
            }
        }

        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        logger.log("======== DONE call");
        logger.log(String.format("\n============== elapsed: %s\n", elapsed));


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
