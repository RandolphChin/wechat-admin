package me.zhengjie.modules.leaf.segment.dao;


import me.zhengjie.modules.leaf.segment.model.LeafAlloc;
import java.util.List;

public interface IDAllocDao {
     List<LeafAlloc> getAllLeafAllocs();

     // LeafAlloc getLeafAlloc(String tag);

     LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

     LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

     List<String> getAllTags();
}
