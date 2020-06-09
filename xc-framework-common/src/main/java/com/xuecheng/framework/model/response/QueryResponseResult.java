package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult<T> queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

    public static QueryResponseResult SUCCESS(QueryResult queryResult) {
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }


    public static void main(String[] args) {

    }
}
