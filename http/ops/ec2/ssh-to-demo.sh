#!/bin/sh

# If you get "Unprotected private key file!" you must do
# chmod 400 xxxxx.pem

ssh -i ~/work/code/amazon-ec2/ixcode-demo-apps.pem ubuntu@ec2-46-51-160-112.eu-west-1.compute.amazonaws.com


