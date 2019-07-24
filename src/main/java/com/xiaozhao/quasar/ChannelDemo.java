package com.xiaozhao.quasar;

import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

/**
 * @author xiaozhao
 * @date 2019-07-2413:38
 */
public class ChannelDemo {
    public static void main(String[] args) {
        Channel channel = Channels.newChannel(10);
    }
}
