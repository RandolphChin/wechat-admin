package me.zhengjie.modules.leaf.segment;

import me.zhengjie.modules.leaf.segment.model.LeafAlloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDAllocRepository  extends JpaRepository<LeafAlloc,String>, JpaSpecificationExecutor<LeafAlloc> {

    @Query(value = "select u from LeafAlloc u")
    List<LeafAlloc> getAllLeafAllocs();

    @Query(value = "select u from LeafAlloc u where u.key = ?1")
    LeafAlloc getLeafAlloc(String tag);

    @Transactional
    @Modifying
    @Query(value = "UPDATE LeafAlloc SET maxId = maxId + step WHERE key = ?1")
    void updateMaxId(String tag);

    @Transactional
    @Modifying
    @Query(value = "UPDATE leaf_alloc SET max_id = max_id + #{leafAlloc.stop} WHERE biz_tag = #{leafAlloc.key}" ,nativeQuery = true)
    void updateMaxIdByCustomStep(LeafAlloc leafAlloc);

    @Query(value = "SELECT biz_tag FROM leaf_alloc",nativeQuery = true)
    List<String> getAllTags();
}
