resource "aws_s3_bucket" "lambda-warmer" {
  bucket = "${var.app-name}-88909188022e"
  region = var.region
  acl = "private"
  tags = {
    Name = "SNS Test - S3 Bucket - ${var.app-name}"
  }
}

resource "aws_s3_bucket_object" "file_upload" {
  depends_on = [
    aws_s3_bucket.lambda-warmer
  ]

  bucket = aws_s3_bucket.lambda-warmer.id
  key = var.package-file
  source = "${path.module}/../../../target/${var.package-file}"
  server_side_encryption = "AES256"
  etag = filebase64sha256("${path.module}/../../../target/${var.package-file}")
}