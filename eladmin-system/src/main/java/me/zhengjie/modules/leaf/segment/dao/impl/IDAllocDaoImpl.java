package me.zhengjie.modules.leaf.segment.dao.impl;


import me.zhengjie.modules.leaf.segment.IDAllocRepository;
import me.zhengjie.modules.leaf.segment.dao.IDAllocDao;
import me.zhengjie.modules.leaf.segment.model.LeafAlloc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IDAllocDaoImpl implements IDAllocDao {
    @Autowired
    private IDAllocRepository allocRepository;

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        List<LeafAlloc> list = allocRepository.getAllLeafAllocs();
        return list;
    }

    @Override
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        allocRepository.updateMaxId(tag);
        LeafAlloc leafAlloc = allocRepository.getLeafAlloc(tag);
        return leafAlloc;
    }

    @Override
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        allocRepository.updateMaxIdByCustomStep(leafAlloc);
        LeafAlloc ac = allocRepository.getLeafAlloc(leafAlloc.getKey());
        return ac;
    }


    @Override
    public List<String> getAllTags() {
        return allocRepository.getAllTags();
    }
}
