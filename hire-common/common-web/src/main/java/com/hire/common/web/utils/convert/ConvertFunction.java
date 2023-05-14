package com.hire.common.web.utils.convert;

public interface ConvertFunction<T, R, U> {
    U apply(T t, R r);
}
