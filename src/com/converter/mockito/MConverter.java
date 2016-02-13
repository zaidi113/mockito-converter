package com.converter.mockito;

import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by muhammadraza on 02/02/2016.
 */
public interface MConverter {

    Optional<ConversionResult> convert(String jmockCode);
}
