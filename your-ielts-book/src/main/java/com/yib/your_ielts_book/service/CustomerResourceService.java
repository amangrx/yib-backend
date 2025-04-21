package com.yib.your_ielts_book.service;

public interface CustomerResourceService {
    boolean checkPayStatus(String jwt, int resourceId);
}
