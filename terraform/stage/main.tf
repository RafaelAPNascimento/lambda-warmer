
module "lambda-warmer-module" {
  source = "../modules/services"
  region = "us-east-1"
  app-name = "app"
  lambda-role = "arn:aws:iam::205303771310:role/LambdaRoleTest"
  package-file = "lambda-warmer.jar"
}