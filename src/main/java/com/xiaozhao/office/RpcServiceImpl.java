package com.xiaozhao.office;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;

/**
 *
 * @author xiaozhao
 * @date 2019-07-2408:47
 */
public class RpcServiceImpl implements RpcService {

    @Suspendable
    @Override
    public String getName(int id) {
        return null;
    }

    @Override
    public String getAddress(String name) throws SuspendExecution {
        return null;
    }
}
