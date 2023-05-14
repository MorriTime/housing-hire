package com.hire.common.web.utils.convert;

public interface MultiConverter<T, R, U> {

    U convert(ConvertFunction<T, R, U> f);

}
