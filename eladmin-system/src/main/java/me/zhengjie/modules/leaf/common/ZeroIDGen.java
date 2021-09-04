package me.zhengjie.modules.leaf.common;


import me.zhengjie.modules.leaf.IDGen;

public class ZeroIDGen implements IDGen {
    @Override
    public Result get(String key) {
        return new Result(0, Status.SUCCESS);
    }
}
