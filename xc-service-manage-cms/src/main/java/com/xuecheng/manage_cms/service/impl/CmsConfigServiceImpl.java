package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsConfigServiceImpl implements CmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Override
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> option = cmsConfigRepository.findById(id);
        if(option.isPresent()){
            return option.get();
        }
        return null;
    }
}
