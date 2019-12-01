variable "region" {
  description = "aws region to use"
}

variable "app-name" {
  default = "app-test"
}

variable "lambda-role" {
  description = "lambda role"
}

variable "package-file" {
  description = "app jar file"
}