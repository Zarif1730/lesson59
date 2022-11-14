package com.example.lesson59.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{
    private String resourceName;  // User
    private String fieldName;  // id
    private Object fieldValue;  // 1

    public ResourceNotFound(String resourceName,String fieldName, Object fieldValue) {  // User not found id : 1
        super(String.format("%s not found %s : %s",resourceName,fieldName,fieldValue));
    }
}
