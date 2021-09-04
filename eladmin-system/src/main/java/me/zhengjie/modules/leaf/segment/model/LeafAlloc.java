package me.zhengjie.modules.leaf.segment.model;

import javax.persistence.*;

@Entity
@Table(name="leaf_alloc")
public class LeafAlloc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biz_tag")
    private String key;

    @Column(name = "max_id")
    private long maxId;

    @Column(name = "step")
    private int step;

    @Column(name = "update_time")
    private String updateTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
