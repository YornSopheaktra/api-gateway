package com.spt.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtilsRequest {

    public static boolean isIgnoreResources(String resource, String path) {
        log.trace("Ignore resources config:{}", resource);
        if (resource.startsWith(URLPattern.ALLOW_ANY_START_WITH) ||
                resource.endsWith(URLPattern.ALLOW_ANY_END_WITH)) {
            String resourcesRePlace = resource.replace("**", "");
            if (resource.startsWith(URLPattern.ALLOW_ANY_START_WITH)) {
                return path.endsWith(resourcesRePlace);
            } else {
                return path.startsWith(resourcesRePlace);
            }

        } else if (resource.equals(path)) {
            log.trace("Matched with with exactly path: {}", path);
            return true;
        }
        log.trace("Ignore resource false!");
        return false;
    }

    public static boolean verifyPermissionFromRequestPath(String permission, String path) {
        if (permission.endsWith(URLPattern.ALLOW_ANY_START_WITH)) {
            String prefixUrl = permission.replace("**", "");
            if (path.startsWith(prefixUrl)) {
                log.trace("Matched with allow:{}", URLPattern.ALLOW_ANY_START_WITH);
                return true;
            }
        } else if (permission.equals(URLPattern.ALLOW_ALL)) {
            log.trace("Matched with allow all:{}", URLPattern.ALLOW_ALL);
            return true;
        } else if (permission.equals(path)) {
            log.trace("Matched with with exactly path with path: {}", path);
            return true;
        }
        return false;
    }
}
