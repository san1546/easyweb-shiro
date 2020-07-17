package com.hc.hyh.system.service;

import com.hc.hyh.common.PageResult;
import com.hc.hyh.system.model.LoginRecord;

public interface LoginRecordService {

    boolean add(LoginRecord loginRecord);

    PageResult<LoginRecord> list(int pageNum, int pageSize, String startDate, String endDate, String account);
}
