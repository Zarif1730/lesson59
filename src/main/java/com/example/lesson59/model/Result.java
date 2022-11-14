package com.example.lesson59.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    String message;
    boolean status;
    Object data;
    String exception;

    public  Result success(Object data){
        return new Result("Success",true,data,null);
    }

    public static   Result successStatic(Object data){
        return new Result("Success",true,data,null);
    }

    public Result failed(String exception){
        return new Result("Failed",false,null,exception);
    }
    public static Result failedStatic(Exception e){
        return new Result("Failed",false,null,e.getMessage());
    }

}
