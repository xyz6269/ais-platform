package com.example.chatservice.exceptions;

public class GrpcServiceException extends RuntimeException {
  public GrpcServiceException(String message) {
    super(message);
  }
}
