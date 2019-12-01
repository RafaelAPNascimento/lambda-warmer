locals {
  api-data = yamldecode(file("${path.module}/template/lambda-warmer.yaml"))
  lambda_functions = local.api-data.functions
  lambda-sns-trigger = "lambda-sns-trigger"
}

resource "aws_lambda_function" "lambda-function" {
  for_each = local.lambda_functions
  function_name = "${var.app-name}-${each.key}"
  handler = each.value.handler
  role = var.lambda-role
  runtime = "java8"

  s3_bucket = aws_s3_bucket.lambda-warmer.id
  s3_key = aws_s3_bucket_object.file_upload.id

  memory_size = 256
  timeout = 30
  reserved_concurrent_executions = 5

  tags = each.value.tags

  source_code_hash = filebase64sha256("${path.module}/../../../target/${var.package-file}")
  publish = true
}

resource "aws_lambda_permission" "allow-cloudwatch-to-lambda" {

  function_name = aws_lambda_function.lambda-function["global-warmer"].function_name
  action = "lambda:InvokeFunction"
  principal = "events.amazonaws.com"
  source_arn = aws_cloudwatch_event_rule.warmup-rule.arn
  statement_id = "AllowExecutionFromCloudWatch"
}

//resource "aws_lambda_permission" "allow-sns-to-lambda" {
//  for_each = aws_lambda_function.lambda-function
//  function_name = each.value.function_name
//  action = "lambda:InvokeFunction"
//  principal = "sns.amazonaws.com"
//  source_arn = aws_sns_topic.warmup-topic.arn
//  statement_id = "AllowExecutionFromSNS"
//}
