package com.tao.hai.service.impl;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.Log;
import com.tao.hai.dao.LogDao;
import com.tao.hai.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogServiceImpl extends BaseServiceImpl<LogDao, Log> implements LogService {


    /**
     * 保存方法
     */
    public void save(Log log) {
        if (StringUtils.isEmpty(log.getLogId())) {
            log.setLogId(UUID.randomUUID().toString());
        } else {
            log.setNewRecord(false);
        }
        super.save(log);
    }
}
