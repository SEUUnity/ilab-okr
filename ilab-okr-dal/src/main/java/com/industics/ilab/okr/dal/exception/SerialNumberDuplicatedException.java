package com.industics.ilab.okr.dal.exception;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.isword.common.exception.ApiErrorException;

/**
 * @author huanglanxin
 * @date 2018/4/17 17:44
 */
public class SerialNumberDuplicatedException extends ApiErrorException {

    private static final long serialVersionUID = -4799145874838644282L;

    public SerialNumberDuplicatedException(String snType) {
        super(ErrorTypes.SERIAL_NUMBER_DUPLICATED, snType);
    }
}
