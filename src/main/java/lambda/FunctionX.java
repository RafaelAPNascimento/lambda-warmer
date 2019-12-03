package lambda;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.Warmup;

public class FunctionX implements RequestHandler<Warmup, String>
{
    LambdaLogger logger;

    @Override
    public String handleRequest(Warmup input, Context context)
    {
        logger = context.getLogger();
        return handler01(context);
    }

    private String handler01(Context context)
    {
        logger.log("\n============== metrics...\n");
        final AmazonCloudWatch cw =
                AmazonCloudWatchClientBuilder.defaultClient();

        ListMetricsRequest request = new ListMetricsRequest()
                .withMetricName("ConcurrentExecutions")
                .withNamespace("Lambda");

        boolean done = false;

        while(!done) {
            ListMetricsResult response = cw.listMetrics(request);

            for(Metric metric : response.getMetrics()) {
                logger.log(String.format(
                        "\n============ Retrieved metric %s\n", metric.getMetricName()));
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
        return "done";
    }
}
