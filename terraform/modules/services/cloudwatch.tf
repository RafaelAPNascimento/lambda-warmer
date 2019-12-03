resource "aws_cloudwatch_event_rule" "warmup-rule" {
  name = "warmup-rule"
  description = "Regra de warmup para os lambdas"
  schedule_expression = "rate(1 minute)"
  is_enabled = true
}

resource "aws_cloudwatch_log_group" "lambda-cloudwatch-logs" {
  for_each = aws_lambda_function.lambda-function
  name = "/aws/lambda/${each.value.function_name}"
  retention_in_days = 1
}

resource "aws_cloudwatch_event_target" "lambda-warmer" {
  rule = aws_cloudwatch_event_rule.warmup-rule.name
  target_id = "global-warmer"
  arn = aws_lambda_function.lambda-function["global-warmer"].arn
}

//resource "aws_sns_topic_policy" "sns-policy" {
//  count  = 1
//  arn    = aws_sns_topic.warmup-topic.arn
//  policy = "${data.aws_iam_policy_document.sns_topic_policy.0.json}"
//}
