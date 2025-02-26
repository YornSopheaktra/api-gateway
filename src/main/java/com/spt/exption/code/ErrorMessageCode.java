package com.spt.exption.code;

public interface ErrorMessageCode {

     String getCode();
     String getMessage();
     default String prefix(){
          return "GW-";
     }

}
