package com.jcondotta.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum CacheAction {

    PUT("put"),
    PUT_IF_ABSENT("putIfAbsent");

    private final String display;

}
