package service.impl;

import service.api.UpperCaseService;

public class UpperCaseImpl implements UpperCaseService {

    @Override
    public String toUpper(String msg) {
        return msg.toUpperCase();
    }
}
