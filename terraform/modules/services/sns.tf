//resource "aws_sns_topic" "warmup-topic" {
//  name = "warmup-topic"
//}
//
//resource "aws_sns_topic_subscription" "sns-lambda-subscritption" {
//  topic_arn = aws_sns_topic.warmup-topic.arn
//  protocol = "lambda"
//  for_each = aws_lambda_function.lambda-function
//  endpoint = each.value.arn
//}
//
//resource "aws_sns_topic_policy" "default" {
//  count  = 1
//  arn    = aws_sns_topic.warmup-topic.arn
//  policy = "${data.aws_iam_policy_document.sns_topic_policy.0.json}"
//}
//
//data "aws_iam_policy_document" "sns_topic_policy" {
//  count     = "1"
//  statement {
//    sid       = "Allow CloudwatchEvents"
//    actions   = ["sns:Publish"]
//    resources = [aws_sns_topic.warmup-topic.arn]
//
//    principals {
//      type        = "Service"
//      identifiers = ["events.amazonaws.com"]
//    }
//  }
//}