package com.spt.utils;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("apiUtilPattern")
public class APIUtilsPattern {

    public static Tuple2<String, String> getServiceAndDownStreamUrl(String path) {
        String[] splitPath = path.split("/"); ///payment-service/payments/api/v1.0/orders/1234
        String service = splitPath[1];//payment-service
        String downstreamUrl = path.replace("/" + service, "");
        return Tuple.of(service, downstreamUrl);
    }
/*

    @Cacheable(value = "apidefs", key = "{#method,#path}")
    public APIDef getMatchingApi(String path, HttpMethod method, List<APIDef> apiDefs) {
        Tuple2<String, String> servicePath = getServiceAndDownStreamUrl(path);
        APIDef apiDef = apiDefs
                .stream()
                .filter(
                        api -> api.getStatus().equals(Status.ACTIVE) &&
                                this.isMethodAndServiceMatch(api, method, servicePath._1) &&
                                isMatchingURIPath(servicePath._2, api))
                .findFirst().orElseThrow(() -> new AppException(AppErrorCode.API_NOT_MATCHING, HttpStatus.BAD_REQUEST));
        log.info("Matching the API request from access path:[{}] and method: [{}] for code,", path, method,apiDef.getCode());
        return apiDef;
    }

    private boolean isMethodAndServiceMatch(APIDef api, HttpMethod requestMethod,
                                            String backendContextService) {
        return api.getMethod().equals(requestMethod)
                && api.getService()
                .equalsIgnoreCase(backendContextService);
    }

    private boolean isMatchingURIPath(String downstreamUrl, APIDef apiDef) {
        log.info("Downstream URL:{}, API config:{}", downstreamUrl, apiDef);
        PathPattern pathInDB = PathPatternParser.defaultInstance.parse(apiDef.getPath());
        PathContainer pathContainer = PathContainer.parsePath(downstreamUrl);
        boolean patchMatch = pathInDB.matches(pathContainer);
        return patchMatch;
    }
*/


}
