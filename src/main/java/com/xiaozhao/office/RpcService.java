package com.xiaozhao.office;

import co.paralleluniverse.fibers.SuspendExecution;

/**
 * @author xiaozhao
 * @date 2019-07-2408:46
 */
public interface RpcService {
    String getName(int id);

    String getAddress(String name) throws SuspendExecution;
}
