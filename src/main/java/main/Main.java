package main;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.ListFunctionsResult;

import java.util.List;

public class Main {

    public static void main(String[] args)
    {
        AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();

        ListFunctionsResult listResult = lambda.listFunctions();
        List<FunctionConfiguration> list = listResult.getFunctions();

        for (FunctionConfiguration f : list)
        {
            System.out.println(f.getFunctionName());
            System.out.println(f.getFunctionArn());
        }
    }
}
